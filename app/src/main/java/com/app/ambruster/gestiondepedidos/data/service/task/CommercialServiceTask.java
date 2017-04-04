package com.app.ambruster.gestiondepedidos.data.service.task;


import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.app.ambruster.gestiondepedidos.GPApplication;
import com.app.ambruster.gestiondepedidos.data.model.adClient;
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

public class CommercialServiceTask {

    CommercialServiceTaskCallBack callBack;

    public CommercialServiceTask(CommercialServiceTaskCallBack callBack) {
        this.callBack = callBack;
    }

    public void CallService(final adUser user, final int page, final int total) {

        String tag_string_req = "get_commecial";
        final ArrayList<adUser> list = new ArrayList<>();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                ServiceManager.getInstance().getURL(24).toString(), new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jObj = new JSONObject(response);
                    LogManager.getInstance().info(getClass().getCanonicalName(), jObj.getString("success"));

                    if (jObj.getString("success").equals("true")) {

                        JSONArray ja = jObj.getJSONArray("comercials");
                        adUser commercial;

                        if (ja.length() > 0) {
                            for (int i = 0; i < ja.length(); i++) {

                                commercial = new adUser();
                                commercial.setId(Integer.parseInt(ja.getJSONObject(i).getString("idcomercial")));
                                commercial.setComname(ja.getJSONObject(i).getString("comnom"));
                                commercial.setComcode(ja.getJSONObject(i).getString("comcod"));
                                list.add(commercial);
                            }

                            if (list.size() > 0)
                                callBack.onCommercialServiceTaskCallBack(list);
                            else
                                callBack.onCommercialServiceTaskCallBack(new ArrayList<adUser>());

                            if (ja.length() < ServiceManager.LIMIT) {
                                RequestListFragment.loadmore = false;
                            }
                        } else {
                            RequestListFragment.loadmore = false;
                            callBack.onCommercialServiceTaskCallBack(new ArrayList<adUser>());
                        }

                    } else {
                        callBack.onCommercialServiceTaskCallBack(new ArrayList<adUser>());
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
                params.put("page", page + "");
                params.put("limit", total + "");
                return params;
            }
        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 5,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        GPApplication.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    public interface CommercialServiceTaskCallBack {
        void onCommercialServiceTaskCallBack(ArrayList<adUser> commercials);

        void onError(String message);
    }

}
