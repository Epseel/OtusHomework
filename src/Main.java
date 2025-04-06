import animals.Animal;
import data.AnimalTypeData;
import data.CommandsData;
import data.ColorData;
import factory.AnimalFactory;
import tools.NameValidatorTool;
import tools.NumberTools;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    static Scanner scanner = new Scanner(System.in);
    static NumberTools numberTools = new NumberTools();

    public static void main(String[] args) {
        List<Animal> animals = new ArrayList<>();


        List<String> nameString = new ArrayList<>();
        for (CommandsData commandsData : CommandsData.values()) {
            nameString.add(commandsData.name().toLowerCase());
        }

        while (true) {
            System.out.println(String.format("Введите команду: %s", String.join("|", nameString)));

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
                    listAnimals(animals);
                    break;
                case EXIT:
                    System.out.println("До новых встреч!");
                    System.exit(0);

            }
        }
    }


    private static void addAnimal(List<Animal> animals) {

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


        animal.say();
    }


    private static void listAnimals(List<Animal> animals) {
        if (animals.isEmpty()) {
            System.out.println("Список животных пуст");
            return;
        }

        for (Animal animal : animals) {
            System.out.println(animal.toString());
        }
    }
}