package com.app.ambruster.gestiondepedidos.data.service.task;


import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.app.ambruster.gestiondepedidos.GPApplication;
import com.app.ambruster.gestiondepedidos.data.model.adClient;
import com.app.ambruster.gestiondepedidos.data.model.adObra;
import com.app.ambruster.gestiondepedidos.data.model.adPartObra;
import com.app.ambruster.gestiondepedidos.data.model.adUser;
import com.app.ambruster.gestiondepedidos.data.service.ServiceManager;
import com.app.ambruster.gestiondepedidos.log.LogManager;
import com.app.ambruster.gestiondepedidos.view.fragment.ArticleListFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PartsServiceTask {

    PartsServiceTaskCallBack callBack;

    public PartsServiceTask(PartsServiceTaskCallBack callBack) {
        this.callBack = callBack;
    }

    public void CallService(final adUser user, final int page, final int status, final int total){

        String tag_string_req = "get_parts";
        final ArrayList<adPartObra> list = new ArrayList<>();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                ServiceManager.getInstance().getURL(17).toString(), new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {

                    LogManager.getInstance().info(getClass().getCanonicalName(), response);
                    JSONObject jObj = new JSONObject(response);
                    if (jObj.getString("success").equals("true")) {

                        JSONArray ja = jObj.getJSONArray("parts");
                        adPartObra parts;
                        LogManager.getInstance().info(getClass().getCanonicalName(), ja.length() + " ");
                        if (ja.length() > 0) {
                            for (int i = 0; i < ja.length(); i++) {

                                parts = new adPartObra();
                                parts.setId(Integer.parseInt(ja.getJSONObject(i).getString("idParte")));

                                adClient cl = new adClient();
                                cl.setId(Integer.parseInt(ja.getJSONObject(i).getString("idCliente")));
                                cl.setClicode(ja.getJSONObject(i).getString("ParCliCod"));
                                cl.setCliname(ja.getJSONObject(i).getString("ParCliNom"));
                                parts.setClient(cl);

                                adUser us = new adUser();
                                us.setId(Integer.parseInt(ja.getJSONObject(i).getString("idComercial")));
                                us.setComcode(ja.getJSONObject(i).getString("ParComCod"));
                                parts.setCommercial(us);

                                adObra obra = new adObra();
                                obra.setObraName(ja.getJSONObject(i).getString("ParObraNom"));
                                obra.setObraCod(ja.getJSONObject(i).getString("ParObraCod"));
                                parts.setObra(obra);

                                switch (Integer.parseInt(ja.getJSONObject(i).getString("ParteEstado")))
                                {
                                    case 1:
                                        parts.setState("Pendiente");
                                        break;

                                    case 2:
                                        parts.setState("enviado");
                                        break;

                                    case 3:
                                        parts.setState("recogido");
                                        break;
                                }

                                parts.setPartcode(ja.getJSONObject(i).getString("ParCod"));
                                parts.setPartserie(ja.getJSONObject(i).getString("ParSer"));
                                parts.setPartdate(ja.getJSONObject(i).getString("ParFec"));
                                parts.setPartlanguaje(ja.getJSONObject(i).getString("ParIdioma"));
                                parts.setPartdaterec(ja.getJSONObject(i).getString("ParFechaRecogido"));
                                parts.setPartsign(ja.getJSONObject(i).getString("ImagenFirma"));
                                parts.setDescription(ja.getJSONObject(i).getString("Descripcion"));

                                list.add(parts);
                            }

                            callBack.onPartsServiceTaskCallBack(list);

                        } else {
                            callBack.onPartsServiceTaskCallBack(new ArrayList<adPartObra>());
                            ArticleListFragment.loadmore = false;
                        }
                    } else {
                        callBack.onPartsServiceTaskCallBack(new ArrayList<adPartObra>());
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
                params.put("status", status + "");

                return params;
            }
        };

        strReq.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 5,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        GPApplication.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    public interface PartsServiceTaskCallBack {
        void onPartsServiceTaskCallBack(ArrayList<adPartObra> articles);

        void onError(String message);
    }

}
