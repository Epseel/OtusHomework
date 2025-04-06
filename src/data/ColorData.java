package data;

public enum ColorData {

    BROWN("Коричневый"),
    WHITE("Белый"),
    BLACK("Черный"),
    GREY("Серый");

    private String name;


    ColorData(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
