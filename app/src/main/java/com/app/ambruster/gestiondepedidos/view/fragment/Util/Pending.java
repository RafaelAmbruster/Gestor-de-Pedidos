package com.app.ambruster.gestiondepedidos.view.fragment.Util;


public class Pending {
    int type;
    String value;

    public Pending(int type, String value) {
        this.type = type;
        this.value = value;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
