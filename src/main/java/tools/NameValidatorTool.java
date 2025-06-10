package tools;

public class NameValidatorTool {

    private static final  String NAME_PATTERN = "^[a-zA-Za-яА-Я]+$";

    public static boolean isNameValid(String name) {
        return name != null && !name.trim().isEmpty() && name.matches(NAME_PATTERN);
    }
}
