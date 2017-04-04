package com.app.ambruster.gestiondepedidos.data.exception;


public class ArticleException extends RuntimeException {

    private String message;

    public ArticleException(String detailMessage) {
        super(detailMessage);
        this.message = detailMessage;
    }

    public ArticleException(Exception exception) {
        super(exception);
        this.setStackTrace(exception.getStackTrace());
        this.message = exception.getMessage();
    }

    @Override
    public String getMessage() {
        return getMessage();
    }
}
