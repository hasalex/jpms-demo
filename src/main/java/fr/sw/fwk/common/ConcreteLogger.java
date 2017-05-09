package fr.sw.fwk.common;

public class ConcreteLogger {

    private final String name;

    public ConcreteLogger(String name) {
        this.name = name;
    }

    public void error(String message, Throwable e) {
        error("ERR: " + format(message, e));
        e.printStackTrace();
    }

    public void error(String message) {
        System.err.println("ERR: " + format(message));
    }

    public void info(String message) {
        System.out.println("LOG: " + format(message));
    }

    private String format(String text) {
        return "" + name + " - " + text;
    }

    private String format(String message, Throwable e) {
        return format(message) + " -- " + e.getMessage();
    }
}
