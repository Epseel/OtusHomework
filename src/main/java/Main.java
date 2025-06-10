import animals.Animal;
import data.AnimalTypeData;
import data.CommandsData;
import data.ColorData;
import factory.AnimalFactory;
import tables.AnimalTable;
import tools.NameValidatorTool;
import tools.NumberTools;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final NumberTools numberTools = new NumberTools();
    private static AnimalTable animalTable;

    public static void main(String[] args) {
        try {
            animalTable = new AnimalTable();
            animalTable.createTable();

            List<Animal> animals = animalTable.getAllAnimals();

            List<String> nameString = new ArrayList<>();
            for (CommandsData commandsData : CommandsData.values()) {
                nameString.add(commandsData.name().toLowerCase());
            }

            while (true) {
                System.out.printf("Введите команду: %s%n", String.join("|", nameString));

                String userCommand = scanner.next().trim();
                String userCommandUpperCase = userCommand.toUpperCase();

                try {
                    CommandsData.valueOf(userCommandUpperCase);
                } catch (IllegalArgumentException e) {
                    System.out.printf("Введенная вами команда %s не поддерживается\n", userCommand);
                    continue;
                }

                switch (CommandsData.valueOf(userCommandUpperCase)) {
                    case ADD:
                        addAnimal(animals);
                        break;
                    case LIST:
                        listAnimals();
                        break;
                    case EDIT:
                        editAnimal();
                        break;
                    case FILTER:
                        filterAnimalsByType();
                        break;
                    case EXIT:
                        System.out.println("До новых встреч!");
                        animalTable.close();
                        System.exit(0);
                }
            }
        } catch (SQLException | IOException e) {
            System.out.println("Ошибка работы с базой данных: " + e.getMessage());
        }
    }

    private static void addAnimal(List<Animal> animals) throws SQLException, IOException {
        AnimalTypeData animalType = null;
        while (animalType == null) {
            System.out.println("Укажите животное, которое хотите добавить (cat|dog|duck)?");
            String animalTypeStr = scanner.next().trim().toUpperCase();

            try {
                animalType = AnimalTypeData.valueOf(animalTypeStr);
            } catch (IllegalArgumentException e) {
                System.out.println("Неверный тип животного: " + animalTypeStr.toLowerCase());
            }
        }

        System.out.println("Введите имя (имя должно состоять только из букв латиницы или кириллицы):");

        if (scanner.hasNextLine()) {
            scanner.nextLine();
        }

        String name;
        while (true) {
            name = scanner.nextLine().trim();

            if (name.isEmpty()) {
                System.out.println("Имя не должно быть пустым. Пожалуйста, введите имя:");
                continue;
            }

            if (!NameValidatorTool.isNameValid(name)) {
                System.out.println("Некорректное имя '" + name + "'. Используйте только буквы латиницы или кириллицы:");
                continue;
            }

            break;
        }

        System.out.println("Введите возраст (целое число от 1 до 99):");
        int age = 0;
        while (age <= 0) {
            String ageAnimal = scanner.nextLine().trim();
            if (numberTools.isNumber(ageAnimal)) {
                age = Integer.parseInt(ageAnimal);
            } else {
                System.out.println("Некорректный возраст '" + ageAnimal + "'. Введите целое число от 1 до 99:");
            }
        }

        System.out.println("Введите вес (целое число от 1 до 99):");
        int weight = 0;
        while (weight <= 0) {
            String weightAnimal = scanner.nextLine().trim();
            if (numberTools.isNumber(weightAnimal)) {
                weight = Integer.parseInt(weightAnimal);
            } else {
                System.out.println("Некорректный вес '" + weightAnimal + "'. Введите целое число от 1 до 99:");
            }
        }

        System.out.println("Введите цвет: Brown|White|Black|Grey");
        ColorData color = null;
        while (color == null) {
            String colorAnimal = scanner.next().trim().toUpperCase();
            try {
                color = ColorData.valueOf(colorAnimal);
            } catch (IllegalArgumentException e) {
                System.out.println("Некорректный цвет '" + colorAnimal.toLowerCase() + "'. Введите один из доступных: Brown|White|Black|Grey");
            }
        }

        AnimalFactory factory = new AnimalFactory(name, age, weight, color);
        Animal animal = factory.create(animalType);
        animals.add(animal);
        animalTable.saveAnimal(animal, animalType);

        animal.say();
    }

    private static void listAnimals() throws SQLException, IOException {
        List<Animal> animals = animalTable.getAllAnimals();
        if (animals.isEmpty()) {
            System.out.println("Список животных пуст");
            return;
        }

        for (Animal animal : animals) {
            System.out.println(animal.toString());
        }
    }

    private static void editAnimal() throws SQLException, IOException {
        System.out.println("Введите имя животного для редактирования (или 'cancel' для отмены):");
        scanner.nextLine();
        String oldName = scanner.nextLine().trim();

        if (oldName.equalsIgnoreCase("cancel")) {
            System.out.println("Редактирование отменено.");
            return;
        }

        if (!animalTable.isAnimalExists(oldName)) {
            System.out.println("Животное с именем '" + oldName + "' не найдено!");
            return;
        }

        String newName = getValidName("Введите новое имя (имя должно состоять только из букв латиницы или кириллицы, или введите 'cancel' для отмены):", oldName);
        if (newName == null) return;

        int age = getValidNumber("Введите новый возраст (целое число от 1 до 99, или 'cancel' для отмены):");
        if (age == -1) return;

        int weight = getValidNumber("Введите новый вес (целое число от 1 до 99, или 'cancel' для отмены):");
        if (weight == -1) return;

        ColorData color = getValidColor("Введите новый цвет: Brown|White|Black|Grey (или 'cancel' для отмены):");
        if (color == null) return;

        AnimalTypeData type = getValidAnimalType("Введите новый тип животного (cat|dog|duck, или 'cancel' для отмены):");
        if (type == null) return;

        AnimalFactory factory = new AnimalFactory(newName, age, weight, color);
        Animal updatedAnimal = factory.create(type);
        animalTable.updateAnimal(oldName, updatedAnimal, type);

        System.out.println("Животное успешно обновлено!");
    }

    private static String getValidName(String message, String oldName) {
        while (true) {
            System.out.println(message);
            String name = scanner.nextLine().trim();

            if (name.equalsIgnoreCase("cancel")) {
                System.out.println("Редактирование отменено.");
                return null;
            }

            if (name.isEmpty()) {
                System.out.println("Имя не должно быть пустым.");
                continue;
            }

            if (!NameValidatorTool.isNameValid(name)) {
                System.out.println("Некорректное имя '" + name + "'. Используйте только буквы латиницы или кириллицы:");
                continue;
            }

            if (!name.equalsIgnoreCase(oldName) && animalTable.isAnimalExists(name)) {
                System.out.println("Животное с именем '" + name + "' уже существует!");
                continue;
            }

            return name;
        }
    }

    private static int getValidNumber(String message) {
        while (true) {
            System.out.println(message);
            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("cancel")) {
                System.out.println("Редактирование отменено.");
                return -1;
            }

            if (numberTools.isNumber(input)) {
                return Integer.parseInt(input);
            } else {
                System.out.println("Некорректное значение '" + input + "'. Введите целое число от 1 до 99:");
            }
        }
    }

    private static ColorData getValidColor(String message) {
        while (true) {
            System.out.println(message);
            String input = scanner.nextLine().trim().toUpperCase();

            if (input.equalsIgnoreCase("CANCEL")) {
                System.out.println("Редактирование отменено.");
                return null;
            }

            try {
                return ColorData.valueOf(input);
            } catch (IllegalArgumentException e) {
                System.out.println("Некорректный цвет '" + input.toLowerCase() + "'. Введите один из доступных: Brown|White|Black|Grey");
            }
        }
    }

    private static AnimalTypeData getValidAnimalType(String message) {
        while (true) {
            System.out.println(message);
            String input = scanner.nextLine().trim().toUpperCase();

            if (input.equalsIgnoreCase("CANCEL")) {
                System.out.println("Редактирование отменено.");
                return null;
            }

            try {
                return AnimalTypeData.valueOf(input);
            } catch (IllegalArgumentException e) {
                System.out.println("Неверный тип животного: " + input.toLowerCase());
            }
        }
    }

    private static void filterAnimalsByType() throws SQLException, IOException {
        System.out.println("Введите тип животного для фильтрации (cat|dog|duck):");
        String typeStr = scanner.next().trim().toUpperCase();

        try {
            AnimalTypeData type = AnimalTypeData.valueOf(typeStr);
            List<Animal> filteredAnimals = animalTable.getAnimalsByType(type);

            if (filteredAnimals.isEmpty()) {
                System.out.println("Животных типа " + type.name().toLowerCase() + " не найдено");
            } else {
                System.out.println("Список животных типа " + type.name().toLowerCase() + ":");
                for (Animal animal : filteredAnimals) {
                    System.out.println(animal.toString());
                }
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Неверный тип животного: " + typeStr.toLowerCase());
        }
    }
}