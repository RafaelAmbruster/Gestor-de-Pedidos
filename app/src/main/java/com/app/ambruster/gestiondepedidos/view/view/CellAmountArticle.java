package com.app.ambruster.gestiondepedidos.view.view;

import android.widget.TextView;

import com.app.ambruster.gestiondepedidos.R;
import com.app.ambruster.gestiondepedidos.data.model.adArticleAmount;
import com.app.ambruster.gestiondepedidos.data.model.adRequest;


public class CellAmountArticle extends ViewCell<adArticleAmount> {
    TextView amount;
    TextView code;
    TextView article;

    @Override
    public void animate() {
    }

    @Override
    public void build() {
    }

    @Override
    public void load() {
        this.amount = (TextView) getView().findViewById(R.id.amount);
        this.code = (TextView) getView().findViewById(R.id.code);
        this.article = (TextView) getView().findViewById(R.id.article);
    }

    @Override
    public void fill() {
        if (getObject() != null) {
            amount.setText(String.valueOf(getObject().getAmount()));
            code.setText(getObject().getArticle().getArtcode());
            article.setText(getObject().getArticle().getArtname());
        }
    }

    @Override
    public void addListeners() {
    }

    @Override
    public void onScroll(float yOffset) {
    }

}
