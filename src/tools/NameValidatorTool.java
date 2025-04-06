package tools;

public class NameValidatorTool {

    private static String namePattern = "^[a-zA-Za-яА-Я]+$";

    public static boolean isNameValid(String name) {
        return name != null && !name.trim().isEmpty() && name.matches(namePattern);
    }
}
