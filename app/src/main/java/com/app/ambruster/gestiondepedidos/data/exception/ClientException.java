package com.app.ambruster.gestiondepedidos.data.exception;


public class ClientException extends RuntimeException {

    private String message;

    public ClientException(String detailMessage) {
        super(detailMessage);
        this.message = detailMessage;
    }

    public ClientException(Exception exception) {
        super(exception);
        this.setStackTrace(exception.getStackTrace());
        this.message = exception.getMessage();
    }

    @Override
    public String getMessage() {
        return getMessage();
    }
}
