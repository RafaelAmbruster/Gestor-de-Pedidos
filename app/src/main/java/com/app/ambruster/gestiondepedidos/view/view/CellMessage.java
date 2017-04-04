package com.app.ambruster.gestiondepedidos.view.view;

import android.widget.ImageView;
import android.widget.TextView;

import com.app.ambruster.gestiondepedidos.R;
import com.app.ambruster.gestiondepedidos.data.model.adMessage;


public class CellMessage extends ViewCell<adMessage> {

    public ImageView image, status;
    TextView code;
    TextView name;

    @Override
    public void animate() {
    }

    @Override
    public void build() {
    }

    @Override
    public void load() {
        this.image = (ImageView) getView().findViewById(R.id.image);
        this.code = (TextView) getView().findViewById(R.id.Code);
        this.name = (TextView) getView().findViewById(R.id.Name);
    }

    @Override
    public void fill() {
        if (getObject() != null) {

            if ((getObject().getRequestid() + "") != null) {
                this.code.setText("PEDIDO: " + getObject().getRequestid());
                name.setText("CLIENTE: " + getObject().getClientname());
            }
        }
    }


    @Override
    public void addListeners() {
    }

    @Override
    public void onScroll(float yOffset) {
    }

}
