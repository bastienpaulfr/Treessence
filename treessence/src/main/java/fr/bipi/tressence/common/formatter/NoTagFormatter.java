package fr.bipi.tressence.common.formatter;

public class NoTagFormatter implements Formatter {

    public static final NoTagFormatter INSTANCE = new NoTagFormatter();

    private NoTagFormatter() {
    }

    @Override
    public String format(int priority, String tag, String message) {
        return message + "\n";
    }
}
