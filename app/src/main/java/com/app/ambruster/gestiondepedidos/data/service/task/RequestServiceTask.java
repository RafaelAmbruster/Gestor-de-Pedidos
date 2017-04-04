package com.app.ambruster.gestiondepedidos.data.service.task;


import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.app.ambruster.gestiondepedidos.GPApplication;
import com.app.ambruster.gestiondepedidos.data.model.adClient;
import com.app.ambruster.gestiondepedidos.data.model.adRequest;
import com.app.ambruster.gestiondepedidos.data.model.adUser;
import com.app.ambruster.gestiondepedidos.data.service.ServiceManager;
import com.app.ambruster.gestiondepedidos.log.LogManager;
import com.app.ambruster.gestiondepedidos.util.CustomDateFormat;
import com.app.ambruster.gestiondepedidos.view.fragment.RequestListFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RequestServiceTask {

    RequestServiceTaskCallBack callBack;

    public RequestServiceTask(RequestServiceTaskCallBack callBack) {
        this.callBack = callBack;
    }

    public void CallService(final adUser user, final int page, final int status, final int total) {

        String tag_string_req = "get_request";
        final ArrayList<adRequest> list = new ArrayList<>();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                ServiceManager.getInstance().getURL(10).toString(), new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jObj = new JSONObject(response);
                    LogManager.getInstance().info(getClass().getCanonicalName(), jObj.getString("success"));

                    if (jObj.getString("success").equals("true")) {

                        JSONArray ja = jObj.getJSONArray("requests");
                        adRequest request;

                        if (ja.length() > 0) {
                            for (int i = 0; i < ja.length(); i++) {

                                request = new adRequest();
                                if (ja.getJSONObject(i).getString("idPedido") != null) {
                                    request.setId(Integer.parseInt(ja.getJSONObject(i).getString("idPedido")));
                                    request.setRequestdate(CustomDateFormat.getCurrentDate(ja.getJSONObject(i).getString("pedfec")));
                                    adClient cl = new adClient();
                                    cl.setCliname(ja.getJSONObject(i).getString("cliente"));
                                    request.setClient(cl);
                                    request.setServicedate(ja.getJSONObject(i).getString("pedfec"));
                                    request.setStatus(Integer.parseInt(ja.getJSONObject(i).getString("status")));
                                    list.add(request);
                                    callBack.onRequestServiceTaskCallBack(list);
                                } else {
                                    callBack.onRequestServiceTaskCallBack(new ArrayList<adRequest>());
                                }
                            }
                            if (ja.length() < ServiceManager.LIMIT) {
                                RequestListFragment.loadmore = false;
                            }
                        } else {
                            RequestListFragment.loadmore = false;
                            callBack.onRequestServiceTaskCallBack(new ArrayList<adRequest>());
                        }

                    } else {
                        callBack.onRequestServiceTaskCallBack(new ArrayList<adRequest>());
                        callBack.onError(jObj.getString("message"));
                    }
                } catch (JSONException e) {
                    LogManager.getInstance().info(getClass().getCanonicalName(), e.getMessage());
                    callBack.onError(e.getMessage());
                } catch (ParseException e) {
                    e.printStackTrace();
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
                params.put("limit", total + "");
                params.put("comcod", "");
                params.put("status", status + "");

                return params;
            }
        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 5,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        GPApplication.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    public interface RequestServiceTaskCallBack {
        void onRequestServiceTaskCallBack(ArrayList<adRequest> requests);

        void onError(String message);
    }

}
