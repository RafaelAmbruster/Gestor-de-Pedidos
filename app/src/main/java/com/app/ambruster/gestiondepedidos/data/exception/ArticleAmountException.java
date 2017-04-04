package com.app.ambruster.gestiondepedidos.data.exception;


public class ArticleAmountException extends RuntimeException {

    private String message;

    public ArticleAmountException(String detailMessage) {
        super(detailMessage);
        this.message = detailMessage;
    }

    public ArticleAmountException(Exception exception) {
        super(exception);
        this.setStackTrace(exception.getStackTrace());
        this.message = exception.getMessage();
    }

    @Override
    public String getMessage() {
        return getMessage();
    }
}
