package fr.sw.fwk.dao;

import fr.sw.fwk.common.SwException;

public class DaoException extends SwException {

    public enum Reason {ALREADY_EXISTS;};

    private Reason reason;
    public DaoException(String message) {
        super(message);
    }

    public DaoException(Reason reason, String message) {
        super(message);
        this.reason = reason;
    }

    public DaoException(String message, Throwable cause) {
        super(message, cause);
    }

    public DaoException(Throwable e) {
        super(e);
    }

    public Reason getReason() {
        return reason;
    }
}
