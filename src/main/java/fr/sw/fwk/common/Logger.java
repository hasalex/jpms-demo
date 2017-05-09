package fr.sw.fwk.common;

public class Logger {

    private final ConcreteLogger concreteLogger;

    public Logger(Class<?> clazz) {
        concreteLogger = new ConcreteLogger(clazz.getName());
    }

    public void error(String message, Throwable e) {
        concreteLogger.error(message, e);
    }

    public void error(Throwable e) {
        concreteLogger.error(e.getMessage(), e);
    }

    public void error(String message) {
        concreteLogger.error(message);
    }

    public void log(String message) {
        concreteLogger.info(message);
    }
}
