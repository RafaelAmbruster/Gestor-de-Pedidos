package com.app.ambruster.gestiondepedidos.view.adapter;

public class UtilTextValuePair {

	private String text;
    private int item;
    
    public UtilTextValuePair(String text, int item) {
            this.text = text;
            this.item = item;
    }
    public String getText() {
        return text;
    }

    public int getItem() {
        return item;
    }
    
    @Override
    public String toString() {
        return getText();
    }
	
}
