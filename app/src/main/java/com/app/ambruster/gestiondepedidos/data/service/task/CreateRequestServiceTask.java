package com.app.ambruster.gestiondepedidos.data.service.task;


import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.app.ambruster.gestiondepedidos.GPApplication;
import com.app.ambruster.gestiondepedidos.data.model.adArticleAmount;
import com.app.ambruster.gestiondepedidos.data.model.adRequest;
import com.app.ambruster.gestiondepedidos.data.model.adUser;
import com.app.ambruster.gestiondepedidos.data.service.ServiceManager;
import com.app.ambruster.gestiondepedidos.log.LogManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CreateRequestServiceTask {

    CreateRequestServiceTaskCallBack callBack;
    String articles_amount;

    public CreateRequestServiceTask(CreateRequestServiceTaskCallBack callBack) {
        this.callBack = callBack;
    }

    public void CallService(final adUser user, final adRequest request, final int status) {

        String tag_string_req = "cre_request";
        String url = ServiceManager.getInstance().getURL(15).toString();

        StringBuilder result = new StringBuilder();

        for (adArticleAmount item : request.getAmountlist()) {
            result.append(item.getArticle().getId() + "#" + item.getAmount());
            result.append(",");
        }

        articles_amount = result.length() > 0 ? result.substring(0, result.length() - 1) : "";

        StringRequest strReq;
        strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jObj = new JSONObject(response);
                    LogManager.getInstance().info(getClass().getCanonicalName(), jObj.getString("success"));

                    if (jObj.getString("success").equals("true")) {
                        callBack.onCreateRequestServiceTaskCallBack(true, Integer.parseInt(jObj.getString("idPedido")));
                    } else {
                        callBack.onCreateRequestServiceTaskCallBack(false, 0);
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
                params.put("username", user.getComcode());
                Log.i("username", user.getComcode());
                params.put("token", user.getToken());
                Log.i("token", user.getToken());
                params.put("idCliente", String.valueOf(request.getClient().getId()));
                Log.i("idCliente", String.valueOf(request.getClient().getId()));
                params.put("clicod", request.getClient().getClicode());
                Log.i("clicod", request.getClient().getClicode());
                params.put("articles", articles_amount);
                Log.i("articles", articles_amount);
                params.put("idComercial", String.valueOf(user.getId()));
                Log.i("idComercial", String.valueOf(user.getId()));
                params.put("status", String.valueOf(status));
                Log.i("status", String.valueOf(status));

                if (request.getId() != 999999)
                    params.put("idRequest", String.valueOf(request.getId()));
                Log.i("idRequest", String.valueOf(request.getId()));

                return params;
            }
        };

        GPApplication.getInstance().addToRequestQueue(strReq, tag_string_req);

        strReq.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 5,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    public interface CreateRequestServiceTaskCallBack {
        void onCreateRequestServiceTaskCallBack(Boolean status, int id);

        void onError(String message);
    }

}
