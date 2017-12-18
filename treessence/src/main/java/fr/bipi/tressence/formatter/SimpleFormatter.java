package fr.bipi.tressence.formatter;

public class SimpleFormatter implements Formatter {
    @Override
    public String format(int priority, String tag, String message) {
        return tag + " : " + message;
    }
}
