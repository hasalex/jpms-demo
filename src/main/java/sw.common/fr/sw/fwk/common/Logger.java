package fr.sw.fwk.common;

import org.slf4j.LoggerFactory;

public class Logger {

    private final String name;
    private final org.slf4j.Logger slfLogger;

    public Logger(Class<?> clazz) {
        super();
        this.name = clazz.getName();
        slfLogger = LoggerFactory.getLogger(clazz);
    }

    public void error(String message, Exception e) {
        slfLogger.error(message, e);
    }

    public void error(Throwable e) {
        slfLogger.error("", e);
    }

    public void error(String message) {
        slfLogger.error(message);

    }

    public void log(String message) {
        slfLogger.info(message);
    }

    private String format(String text) {
        return "" + name + " - " + text;
    }

    private String format(String message, Exception e) {
        return format(message) + " -- " + e.getMessage();
    }
}
