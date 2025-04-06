package factory;

import animals.Animal;
import animals.birds.Duck;
import animals.pets.Cat;
import animals.pets.Dog;
import data.AnimalTypeData;
import data.ColorData;

public class AnimalFactory {

    private String name;
    private int age;
    private int weight;
    private ColorData colorData;

    public AnimalFactory(String name, int age, int weight, ColorData colorData) {
        this.name = name;
        this.age = age;
        this.weight = weight;
        this.colorData = colorData;
    }

    public Animal create(AnimalTypeData animalTypeData) {
        switch (animalTypeData) {
            case CAT: {
                return new Cat(name, age, weight, colorData);
            }
            case DOG: {
                return new Dog(name, age, weight, colorData);
            }
            case DUCK: {
                return new Duck(name, age, weight, colorData);
            }
        }

        return null;
    }
}
