package com.app.ambruster.gestiondepedidos.data.exception;


public class PartObraException extends RuntimeException {

    private String message;

    public PartObraException(String detailMessage) {
        super(detailMessage);
        this.message = detailMessage;
    }

    public PartObraException(Exception exception) {
        super(exception);
        this.setStackTrace(exception.getStackTrace());
        this.message = exception.getMessage();
    }

    @Override
    public String getMessage() {
        return getMessage();
    }
}
