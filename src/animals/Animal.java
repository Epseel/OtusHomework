package animals;

import data.ColorData;

public abstract class Animal {

    private String name;
    private int age;
    private int weight;
    private ColorData color;

    public Animal(String name, int age, int weight, ColorData color) {
        this.name = name;
        this.age = age;
        this.weight = weight;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public int getWeight() {
        return weight;
    }

    public ColorData getColor() {
        return color;
    }


    public void say() {
        System.out.println("Я говорю");
    }

    public void go() {
        System.out.println("Я иду");
    }

    public void drink() {
        System.out.println("Я пью");
    }

    public void eat() {
        System.out.println("Я ем");
    }

    @Override
    public String toString() {
        return String.format("Привет! Меня зовут %s, мне %d %s, я вешу - %d кг, мой цвет - %s", name, age, getYearCase(), weight, color.getName());
    }

    private String getYearCase() {
        if (age >= 11 && age <= 14) {
            return "лет";
        }

        int reminder = age % 10;
        if (reminder == 1) {
            return "год";
        }

        if (reminder >= 2 && reminder <= 4) {
            return "года";
        }

        return "лет";

    }


}
