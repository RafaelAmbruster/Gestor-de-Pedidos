package com.app.ambruster.gestiondepedidos;

import android.app.Application;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.TextUtils;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.app.ambruster.gestiondepedidos.data.ApplicationDatabaseManager;
import com.app.ambruster.gestiondepedidos.log.LogManager;
import com.crashlytics.android.Crashlytics;
import com.mikepenz.materialdrawer.util.DrawerImageLoader;
import com.orhanobut.logger.Logger;
import com.squareup.picasso.Picasso;

import io.fabric.sdk.android.Fabric;

public class GPApplication extends Application {

    private static GPApplication instance;
    public static String TAG = "GP";
    private RequestQueue mRequestQueue;

    public GPApplication() {
        instance = this;
    }

    public static synchronized GPApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        ApplicationDatabaseManager.init(getApplicationContext());
        Fabric.with(this, new Crashlytics());
        Logger.init("Gestión").hideThreadInfo().setMethodCount(3).setMethodOffset(2);
        LogManager.init();

        DrawerImageLoader.init(new DrawerImageLoader.IDrawerImageLoader() {
            @Override
            public void set(ImageView imageView, Uri uri, Drawable placeholder) {
                Picasso.with(imageView.getContext()).load(uri).placeholder(placeholder).into(imageView);
            }

            @Override
            public void cancel(ImageView imageView) {
                Picasso.with(imageView.getContext()).cancelRequest(imageView);
            }

            @Override
            public Drawable placeholder(Context ctx) {
                return null;
            }
        });
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }



}
