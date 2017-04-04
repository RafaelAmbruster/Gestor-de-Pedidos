package com.app.ambruster.gestiondepedidos.view.view;

import android.widget.TextView;

import com.app.ambruster.gestiondepedidos.R;
import com.app.ambruster.gestiondepedidos.data.model.adRequest;


public class CellRequest extends ViewCell<adRequest> {
    TextView request;
    TextView date;
    TextView client;

    @Override
    public void animate() {
    }

    @Override
    public void build() {
    }

    @Override
    public void load() {
        this.request = (TextView) getView().findViewById(R.id.request);
        this.date = (TextView) getView().findViewById(R.id.date);
        this.client = (TextView) getView().findViewById(R.id.client);
    }

    @Override
    public void fill() {
        if (getObject() != null) {
            request.setText(String.valueOf(getObject().getId()));
            date.setText(getObject().getServicedate());
            client.setText(getObject().getClient().getCliname());
        }
    }

    @Override
    public void addListeners() {
    }

    @Override
    public void onScroll(float yOffset) {
    }

}
