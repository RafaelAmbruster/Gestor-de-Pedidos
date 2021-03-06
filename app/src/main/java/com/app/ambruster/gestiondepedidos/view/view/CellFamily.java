package com.app.ambruster.gestiondepedidos.view.view;

import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.ambruster.gestiondepedidos.R;
import com.app.ambruster.gestiondepedidos.data.model.adFamily;
import com.squareup.picasso.Picasso;

public class CellFamily extends ViewCell<adFamily> {

    public ImageView image;
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

            String url = getObject()
                    .getUrl();
            if (url != null) {
                if (url.getBytes().length > 5) {
                    Picasso.with(context).load(url).into(image);
                }
            }

            if (getObject().getCode() != null)
                this.code.setText(getObject().getCode());
            name.setText(getObject().getName());

            RunLoading();
        }
    }

    protected void RunLoading() {
        int previousDegrees = 0;
        int degrees = 360;
        RotateAnimation animation = new RotateAnimation(previousDegrees, degrees,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setFillEnabled(true);
        animation.setFillAfter(true);
        animation.setRepeatMode(Animation.INFINITE);
        animation.setRepeatCount(3);
        animation.setDuration(2000);
        getView().findViewById(R.id.placeholder_image).startAnimation(animation);
    }

    @Override
    public void addListeners() {
    }

    @Override
    public void onScroll(float yOffset) {
    }

}
