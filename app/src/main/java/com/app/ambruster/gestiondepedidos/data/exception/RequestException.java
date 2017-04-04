package com.app.ambruster.gestiondepedidos.data.exception;


public class RequestException extends RuntimeException {

    private String message;

    public RequestException(String detailMessage) {
        super(detailMessage);
        this.message = detailMessage;
    }

    public RequestException(Exception exception) {
        super(exception);
        this.setStackTrace(exception.getStackTrace());
        this.message = exception.getMessage();
    }

    @Override
    public String getMessage() {
        return getMessage();
    }
}
