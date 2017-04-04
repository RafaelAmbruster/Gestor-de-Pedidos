package com.app.ambruster.gestiondepedidos.util.background;

public abstract interface BackgroundProcessEvent {

    public abstract void postProcess();

    public abstract void process();
}

