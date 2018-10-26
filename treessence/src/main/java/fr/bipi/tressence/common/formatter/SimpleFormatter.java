package fr.bipi.tressence.common.formatter;

public class SimpleFormatter implements Formatter {

    public static final SimpleFormatter INSTANCE = new SimpleFormatter();

    private SimpleFormatter() {
    }

    @Override
    public String format(int priority, String tag, String message) {
        return tag + " : " + message + "\n";
    }
}
