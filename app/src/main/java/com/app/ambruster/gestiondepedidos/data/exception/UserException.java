package com.app.ambruster.gestiondepedidos.data.exception;


public class UserException extends RuntimeException {

    private String message;

    public UserException(String detailMessage) {
        super(detailMessage);
        this.message = detailMessage;
    }

    public UserException(Exception exception) {
        super(exception);
        this.setStackTrace(exception.getStackTrace());
        this.message = exception.getMessage();
    }

    @Override
    public String getMessage() {
        return getMessage();
    }
}
