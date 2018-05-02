package fr.bipi.tressence.formatter;

public class NoTagFormatter implements Formatter {

    public static final NoTagFormatter INSTANCE = new NoTagFormatter();

    private NoTagFormatter() {
    }

    @Override
    public String format(int priority, String tag, String message) {
        return message;
    }
}
