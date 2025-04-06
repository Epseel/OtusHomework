package animals.birds;

import animals.Animal;
import data.ColorData;

public class Duck extends Animal implements IFlying {
    public Duck(String name, int age, int weight, ColorData color) {
        super(name, age, weight, color);
    }

    @Override
    public void say() {
        System.out.println("Кря");
    }
}
