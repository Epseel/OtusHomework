package animals.pets;

import animals.Animal;
import data.ColorData;

public class Dog extends Animal {

    public Dog(String name, int age, int weight, ColorData color) {
        super(name, age, weight, color);
    }

    @Override
    public void say() {
        System.out.println("Гав");
    }
}
