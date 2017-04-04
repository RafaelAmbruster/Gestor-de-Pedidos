package com.app.ambruster.gestiondepedidos.data.service.task;


import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.app.ambruster.gestiondepedidos.GPApplication;
import com.app.ambruster.gestiondepedidos.data.model.adArticle;
import com.app.ambruster.gestiondepedidos.data.model.adArticleAmount;
import com.app.ambruster.gestiondepedidos.data.model.adClient;
import com.app.ambruster.gestiondepedidos.data.model.adRequest;
import com.app.ambruster.gestiondepedidos.data.model.adUser;
import com.app.ambruster.gestiondepedidos.data.service.ServiceManager;
import com.app.ambruster.gestiondepedidos.log.LogManager;
import com.app.ambruster.gestiondepedidos.util.CustomDateFormat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetRequestServiceTask {

    GetRequestServiceTaskCallBack callBack;

    public GetRequestServiceTask(GetRequestServiceTaskCallBack callBack) {
        this.callBack = callBack;
    }

    public void CallService(final adUser user, final adRequest request) {

        String tag_string_req = "get_request";
        String url = ServiceManager.getInstance().getURL(16).toString();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jObj = new JSONObject(response);
                    LogManager.getInstance().info(getClass().getCanonicalName(), jObj.getString("success"));
                    adRequest request = new adRequest();

                    if (jObj.getString("success").equals("true")) {
                        try {

                            JSONObject jREQ = jObj.getJSONObject("request");
                            request.setId(Integer.parseInt(jREQ.getString("idPedido")));
                            android.util.Log.d("Pedido", jREQ.getString("idPedido"));

                            request.setStatus(Integer.parseInt(jREQ.getString("status")));
                            android.util.Log.d("Status", jREQ.getString("status"));

                            request.setRequestdate(CustomDateFormat.getCurrentDateAUX(jREQ.getString("pedfec")));
                            request.setUser(user);

                            adClient client = new adClient();
                            client.setCliname(jREQ.getString("clinom"));
                            android.util.Log.d("clinom", jREQ.getString("clinom"));
                            client.setId(Integer.parseInt(jREQ.getString("idCliente")));
                            android.util.Log.d("idCliente", jREQ.getString("idCliente"));

                            client.setClicode(jREQ.getString("pedclicod"));
                            android.util.Log.d("pedclicod", jREQ.getString("pedclicod"));
                            request.setClient(client);

                            JSONArray ja = jREQ.getJSONArray("articles");
                            adArticle article;
                            adArticleAmount amount;
                            List<adArticleAmount> list = new ArrayList<>();

                            if (ja.length() > 0) {
                                for (int i = 0; i < ja.length(); i++) {

                                    article = new adArticle();
                                    article.setId(Integer.parseInt(ja.getJSONObject(i).getString("idArticulo")));
                                    article.setArtname(ja.getJSONObject(i).getString("artnom"));
                                    article.setArtcode(ja.getJSONObject(i).getString("artcod"));

                                    amount = new adArticleAmount();
                                    amount.setArticle(article);
                                    amount.setAmount(ja.getJSONObject(i).getString("cantidad"));

                                    list.add(amount);
                                }
                            }

                            android.util.Log.d("list size", list.size() + "");
                            request.setAmountlist((ArrayList<adArticleAmount>) list);

                        } catch (ParseException e) {
                            callBack.onError(e.getMessage());
                        }
                        callBack.onGetRequestServiceTaskCallBack(request);

                    } else {
                        request = null;
                        callBack.onGetRequestServiceTaskCallBack(request);
                        callBack.onError(jObj.getString("message"));
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
                params.put("idRequest", String.valueOf(request.getId()));
                return params;
            }
        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 5,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        GPApplication.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    public interface GetRequestServiceTaskCallBack {
        void onGetRequestServiceTaskCallBack(adRequest request);

        void onError(String message);
    }

}
