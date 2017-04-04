package com.app.ambruster.gestiondepedidos.data.exception;


public class VersionException extends RuntimeException {

    private String message;

    public VersionException(String detailMessage) {
        super(detailMessage);
        this.message = detailMessage;
    }

    public VersionException(Exception exception) {
        super(exception);
        this.setStackTrace(exception.getStackTrace());
        this.message = exception.getMessage();
    }

    @Override
    public String getMessage() {
        return getMessage();
    }
}
