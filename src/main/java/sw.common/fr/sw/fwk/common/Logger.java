package fr.sw.fwk.common;

public class Logger {

    private final String name;

    public Logger(Class<?> clazz) {
        super();
        this.name = clazz.getName();
    }

    public void error(String message, Exception e) {
        error("ERR: " + format(message, e));
        error(e);
    }

    public void error(Throwable e) {
        error(e.getMessage());
        e.printStackTrace();
    }

    public void error(String message) {
        System.err.println("ERR: " + format(message));
    }

    public void log(String message) {
        System.out.println("LOG: " + format(message));
    }

    private String format(String text) {
        return "" + name + " - " + text;
    }

    private String format(String message, Exception e) {
        return format(message) + " -- " + e.getMessage();
    }
}
