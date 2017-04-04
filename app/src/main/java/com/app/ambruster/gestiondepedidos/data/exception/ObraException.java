package com.app.ambruster.gestiondepedidos.data.exception;


public class ObraException extends RuntimeException {

    private String message;

    public ObraException(String detailMessage) {
        super(detailMessage);
        this.message = detailMessage;
    }

    public ObraException(Exception exception) {
        super(exception);
        this.setStackTrace(exception.getStackTrace());
        this.message = exception.getMessage();
    }

    @Override
    public String getMessage() {
        return getMessage();
    }
}
