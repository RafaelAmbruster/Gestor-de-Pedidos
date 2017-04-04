package com.app.ambruster.gestiondepedidos.data.exception;


public class MessageException extends RuntimeException {

    private String message;

    public MessageException(String detailMessage) {
        super(detailMessage);
        this.message = detailMessage;
    }

    public MessageException(Exception exception) {
        super(exception);
        this.setStackTrace(exception.getStackTrace());
        this.message = exception.getMessage();
    }

    @Override
    public String getMessage() {
        return getMessage();
    }
}
