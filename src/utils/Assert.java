package utils;

public class Assert {
    public static String assertStringNotEmpty(String string, String message) throws IllegalArgumentException {
        if (string == null || string.equals("")) {
            throw new IllegalArgumentException(message);
        }
        return string;
    }
}
