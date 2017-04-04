package com.app.ambruster.gestiondepedidos.data.exception;


public class RoleException extends RuntimeException {

    private String message;

    public RoleException(String detailMessage) {
        super(detailMessage);
        this.message = detailMessage;
    }

    public RoleException(Exception exception) {
        super(exception);
        this.setStackTrace(exception.getStackTrace());
        this.message = exception.getMessage();
    }

    @Override
    public String getMessage() {
        return getMessage();
    }
}
