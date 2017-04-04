package com.app.ambruster.gestiondepedidos.data.service.task;


import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.app.ambruster.gestiondepedidos.GPApplication;
import com.app.ambruster.gestiondepedidos.data.model.adArticle;
import com.app.ambruster.gestiondepedidos.data.model.adFamily;
import com.app.ambruster.gestiondepedidos.data.model.adUser;
import com.app.ambruster.gestiondepedidos.data.service.ServiceManager;
import com.app.ambruster.gestiondepedidos.log.LogManager;
import com.app.ambruster.gestiondepedidos.view.fragment.FamilyListFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FamilyServiceTask {

    FamilyServiceTaskCallBack callBack;

    public FamilyServiceTask(FamilyServiceTaskCallBack callBack) {
        this.callBack = callBack;
    }

    public void CallService(final adUser user, final int page, final String search) {

        String tag_string_req = "get_family";
        final ArrayList<adFamily> list = new ArrayList<>();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                ServiceManager.getInstance().getURL(7).toString(), new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jObj = new JSONObject(response);
                    LogManager.getInstance().info(getClass().getCanonicalName(), jObj.getString("success"));

                    if (jObj.getString("success").equals("true")) {

                        JSONArray ja = jObj.getJSONArray("family");
                        adFamily family;

                        if (ja.length() > 0) {
                            for (int i = 0; i < ja.length(); i++) {

                                family = new adFamily();
                                family.setId(Integer.parseInt(ja.getJSONObject(i).getString("idfamilia")));
                                family.setName(ja.getJSONObject(i).getString("famnom"));
                                family.setCode(ja.getJSONObject(i).getString("famcod"));
                                family.setUrl(ja.getJSONObject(i).getString("famimagen"));

                                list.add(family);
                            }

                            callBack.onFamilyServiceTaskCallBack(list);

                            if (ja.length() < ServiceManager.LIMIT) {
                                FamilyListFragment.loadmoreFAM = false;
                            }
                        } else {
                            FamilyListFragment.loadmoreFAM = false;
                            callBack.onFamilyServiceTaskCallBack(new ArrayList<adFamily>());
                        }

                    } else {
                        callBack.onFamilyServiceTaskCallBack(new ArrayList<adFamily>());
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

    public interface FamilyServiceTaskCallBack {
        void onFamilyServiceTaskCallBack(ArrayList<adFamily> families);

        void onError(String message);
    }

}
