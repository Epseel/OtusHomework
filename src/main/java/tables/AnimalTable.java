package tables;

import animals.Animal;
import data.AnimalTypeData;
import data.ColorData;
import database.IDatabase;
import database.MySqlConnectionDatabase;
import factory.AnimalFactory;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AnimalTable extends AbsTable {

    private IDatabase iDatabase = null;

    public AnimalTable() {
        super("animal");
        iDatabase = new MySqlConnectionDatabase();
    }

    private boolean isTableExist() throws SQLException, IOException {
        String sqlReq = "show tables";
        ResultSet tables = iDatabase.requestExecuteWithReturn(sqlReq);

        while (tables.next()) {
            String tableName = tables.getString(1);
            if (tableName.equals(name)) {
                return true;
            }
        }
        return false;
    }

    public void createTable() throws SQLException, IOException {
        if (!isTableExist()) {
            String sqlRequest = String.format("create table %s (id int auto_increment primary key, name VARCHAR(20), age int, weight int, color VARCHAR(20), type VARCHAR(10))", name);
            iDatabase.requestExecute(sqlRequest);
        }
    }

    public void saveAnimal(Animal animal, AnimalTypeData type) throws SQLException, IOException {
        String sqlRequest = String.format("insert into %s (name, age, weight, color, type) values ('%s', %d, %d, '%s', '%s')",
                name,
                animal.getName(),
                animal.getAge(),
                animal.getWeight(),
                animal.getColor().name(),
                type.name());
        iDatabase.requestExecute(sqlRequest);
    }

    public List<Animal> getAllAnimals() throws SQLException, IOException {
        List<Animal> animals = new ArrayList<>();
        String sqlRequest = String.format("select * from %s", name);
        ResultSet resultSet = iDatabase.requestExecuteWithReturn(sqlRequest);

        while (resultSet.next()) {
            String name = resultSet.getString("name");
            int age = resultSet.getInt("age");
            int weight = resultSet.getInt("weight");
            ColorData color = ColorData.valueOf(resultSet.getString("color"));
            AnimalTypeData type = AnimalTypeData.valueOf(resultSet.getString("type"));

            AnimalFactory factory = new AnimalFactory(name, age, weight, color);
            Animal animal = factory.create(type);
            animals.add(animal);
        }

        return animals;
    }

    public List<Animal> getAnimalsByType(AnimalTypeData type) throws SQLException, IOException {
        List<Animal> animals = new ArrayList<>();
        String sqlRequest = String.format("select * from %s where type = '%s'", name, type.name());
        ResultSet resultSet = iDatabase.requestExecuteWithReturn(sqlRequest);

        while (resultSet.next()) {
            String name = resultSet.getString("name");
            int age = resultSet.getInt("age");
            int weight = resultSet.getInt("weight");
            ColorData color = ColorData.valueOf(resultSet.getString("color"));

            AnimalFactory factory = new AnimalFactory(name, age, weight, color);
            Animal animal = factory.create(type);
            animals.add(animal);
        }

        return animals;
    }

    public boolean isAnimalExists(String animalName) {
        try {
            String sqlRequest = String.format("SELECT COUNT(*) AS count FROM %s WHERE name = '%s'", this.name, animalName);
            ResultSet resultSet = iDatabase.requestExecuteWithReturn(sqlRequest);
            return resultSet.next() && resultSet.getInt("count") > 0;
        } catch (SQLException | IOException e) {
            System.out.println("Ошибка при проверке существования животного: " + e.getMessage());
            return false;
        }
    }

    public void updateAnimal(String oldName, Animal updatedAnimal, AnimalTypeData type) throws SQLException, IOException {
        String sqlRequest = String.format("update %s set name = '%s', age = %d, weight = %d, color = '%s', type = '%s' where name = '%s'",
                name,
                updatedAnimal.getName(),
                updatedAnimal.getAge(),
                updatedAnimal.getWeight(),
                updatedAnimal.getColor().name(),
                type.name(),
                oldName);
        iDatabase.requestExecute(sqlRequest);
    }

    public void close() throws SQLException {
        iDatabase.close();
    }
}