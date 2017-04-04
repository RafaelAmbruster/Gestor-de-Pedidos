package com.app.ambruster.gestiondepedidos.view.view;

import android.widget.TextView;

import com.app.ambruster.gestiondepedidos.R;
import com.app.ambruster.gestiondepedidos.data.model.adArticleAmount;


public class CellAmountArticlePart extends ViewCell<adArticleAmount> {
    TextView cant;
    TextView code;
    TextView description;

    @Override
    public void animate() {
    }

    @Override
    public void build() {
    }

    @Override
    public void load() {
        this.description = (TextView) getView().findViewById(R.id.description);
        this.code = (TextView) getView().findViewById(R.id.code);
        this.cant = (TextView) getView().findViewById(R.id.cant);
    }

    @Override
    public void fill() {
        if (getObject() != null) {
            cant.setText(String.valueOf(getObject().getAmount()));
            code.setText(getObject().getArticle().getArtcode());
            description.setText(getObject().getArticle().getArtname());
        }
    }

    @Override
    public void addListeners() {
    }

    @Override
    public void onScroll(float yOffset) {
    }

}
