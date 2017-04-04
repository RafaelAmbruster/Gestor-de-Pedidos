package com.app.ambruster.gestiondepedidos.data.service.task;


import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.app.ambruster.gestiondepedidos.GPApplication;
import com.app.ambruster.gestiondepedidos.data.model.adArticle;
import com.app.ambruster.gestiondepedidos.data.model.adClient;
import com.app.ambruster.gestiondepedidos.data.model.adUser;
import com.app.ambruster.gestiondepedidos.data.service.ServiceManager;
import com.app.ambruster.gestiondepedidos.log.LogManager;
import com.app.ambruster.gestiondepedidos.view.fragment.ArticleListFragment;
import com.app.ambruster.gestiondepedidos.view.fragment.ClientListFragment;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ClientServiceTask {

    ClientServiceTaskCallBack callBack;

    public ClientServiceTask(ClientServiceTaskCallBack callBack) {
        this.callBack = callBack;
    }

    public void CallService(final adUser user, final int page, final String search) {

        String tag_string_req = "get_client";
        final ArrayList<adClient> list = new ArrayList<>();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                ServiceManager.getInstance().getURL(9).toString(), new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jObj = new JSONObject(response);
                    LogManager.getInstance().info(getClass().getCanonicalName(), jObj.getString("success"));

                    if (jObj.getString("success").equals("true")) {

                        JSONArray ja = jObj.getJSONArray("clients");
                        adClient client;

                        if (ja.length() > 0) {
                            for (int i = 0; i < ja.length(); i++) {

                                client = new adClient();
                                client.setId(Integer.parseInt(ja.getJSONObject(i).getString("idcliente")));
                                client.setCliname(ja.getJSONObject(i).getString("clinom"));
                                client.setClicode(ja.getJSONObject(i).getString("clicod"));

                                list.add(client);
                            }

                            callBack.onClientServiceTaskCallBack(list);

                            if (ja.length() < ServiceManager.LIMIT) {
                                ClientListFragment.loadmore = false;
                            }
                        } else {
                            callBack.onClientServiceTaskCallBack(new ArrayList<adClient>());
                            ClientListFragment.loadmore = false;
                        }
                    } else {
                        callBack.onError(jObj.getString("message"));
                        callBack.onClientServiceTaskCallBack(new ArrayList<adClient>());
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
                params.put("token", user.getToken());
                params.put("page", page + "");
                params.put("limit", ServiceManager.LIMIT + "");
                params.put("search", search);
                return params;
            }
        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 5,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        GPApplication.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    public interface ClientServiceTaskCallBack {
        void onClientServiceTaskCallBack(ArrayList<adClient> clients);

        void onError(String message);
    }

}
