package com.app.ambruster.gestiondepedidos.data.service.task;


import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.app.ambruster.gestiondepedidos.GPApplication;
import com.app.ambruster.gestiondepedidos.data.model.adPartObra;
import com.app.ambruster.gestiondepedidos.data.model.adRequest;
import com.app.ambruster.gestiondepedidos.data.model.adUser;
import com.app.ambruster.gestiondepedidos.data.service.ServiceManager;
import com.app.ambruster.gestiondepedidos.log.LogManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DeletePartServiceTask {

    DeletePartServiceTaskTaskCallBack callBack;

    public DeletePartServiceTask(DeletePartServiceTaskTaskCallBack callBack) {
        this.callBack = callBack;
    }

    public void CallService(final adUser user, final adPartObra partObra) {

        String tag_string_req = "del_part";
        String url = ServiceManager.getInstance().getURL(22).toString();

        url = url.replace("$username", user.getName());
        url = url.replace("$token", user.getToken());
        url = url.replace("$idParte", String.valueOf(partObra.getId()));

        Log.d("URL", url);

        StringRequest strReq = new StringRequest(Request.Method.DELETE,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jObj = new JSONObject(response);
                    LogManager.getInstance().info(getClass().getCanonicalName(), jObj.getString("success"));

                    if (jObj.getString("success").equals("true")) {
                        callBack.onDeletePartServiceTaskTaskCallBack(true);

                    } else {
                        callBack.onDeletePartServiceTaskTaskCallBack(false);
                    }
                } catch (JSONException e) {
                    LogManager.getInstance().info(getClass().getCanonicalName(), e.getMessage());
                    callBack.onError(e.getMessage());
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callBack.onError(error.getMessage());
            }
        }) {

            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded";
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                return params;
            }
        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 5,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        GPApplication.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    public interface DeletePartServiceTaskTaskCallBack {
        void onDeletePartServiceTaskTaskCallBack(Boolean status);

        void onError(String message);
    }

}
