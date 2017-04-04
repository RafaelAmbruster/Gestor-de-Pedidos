package com.app.ambruster.gestiondepedidos.data.exception;


public class CategoryException extends RuntimeException {

    private String message;

    public CategoryException(String detailMessage) {
        super(detailMessage);
        this.message = detailMessage;
    }

    public CategoryException(Exception exception) {
        super(exception);
        this.setStackTrace(exception.getStackTrace());
        this.message = exception.getMessage();
    }

    @Override
    public String getMessage() {
        return getMessage();
    }
}
