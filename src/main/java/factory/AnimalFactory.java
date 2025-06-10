package factory;

import animals.Animal;
import animals.birds.Duck;
import animals.pets.Cat;
import animals.pets.Dog;
import data.AnimalTypeData;
import data.ColorData;

public class AnimalFactory {

    private final String name;
    private final int age;
    private final int weight;
    private final ColorData colorData;

    public AnimalFactory(String name, int age, int weight, ColorData colorData) {
        this.name = name;
        this.age = age;
        this.weight = weight;
        this.colorData = colorData;
    }

    public Animal create(AnimalTypeData animalTypeData) {
        return switch (animalTypeData) {
            case CAT -> new Cat(name, age, weight, colorData);
            case DOG -> new Dog(name, age, weight, colorData);
            case DUCK -> new Duck(name, age, weight, colorData);
        };

    }
}
