package fr.bipi.tressence.formatter;

public class NoTagFormatter implements Formatter {
    @Override
    public String format(int priority, String tag, String message) {
        return message;
    }
}
