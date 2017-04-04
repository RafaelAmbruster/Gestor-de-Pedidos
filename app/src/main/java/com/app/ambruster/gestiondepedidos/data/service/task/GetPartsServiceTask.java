package com.app.ambruster.gestiondepedidos.data.service.task;


import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.app.ambruster.gestiondepedidos.GPApplication;
import com.app.ambruster.gestiondepedidos.data.model.adArticle;
import com.app.ambruster.gestiondepedidos.data.model.adArticleAmount;
import com.app.ambruster.gestiondepedidos.data.model.adClient;
import com.app.ambruster.gestiondepedidos.data.model.adObra;
import com.app.ambruster.gestiondepedidos.data.model.adPartObra;
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

public class GetPartsServiceTask {

    GetPartsServiceTaskCallBack callBack;

    public GetPartsServiceTask(GetPartsServiceTaskCallBack callBack) {
        this.callBack = callBack;
    }

    public void CallService(final adUser user, final adPartObra part) {

        String tag_string_req = "get_part";
        String url = ServiceManager.getInstance().getURL(21).toString();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jObj = new JSONObject(response);
                    LogManager.getInstance().info(getClass().getCanonicalName(), jObj.getString("success"));
                    adPartObra partObra = new adPartObra();
                    JSONArray ja;
                    adArticle article;
                    adArticleAmount amount;
                    List<adArticleAmount> list;

                    if (jObj.getString("success").equals("true")) {

                        JSONObject jPART = jObj.getJSONObject("part");
                        partObra.setId(Integer.parseInt(jPART.getString("idParte")));

                        adClient cl = new adClient();
                        cl.setId(Integer.parseInt(jPART.getString("idCliente")));
                        cl.setClicode(jPART.getString("ParCliCod"));
                        cl.setCliname(jPART.getString("ParCliNom"));
                        partObra.setClient(cl);

                        adUser us = new adUser();
                        us.setId(Integer.parseInt(jPART.getString("idComercial")));
                        us.setComcode(jPART.getString("ParComCod"));
                        partObra.setCommercial(us);

                        adObra obra = new adObra();
                        obra.setObraName(jPART.getString("ParObraNom"));
                        obra.setObraCod(jPART.getString("ParObraCod"));
                        partObra.setObra(obra);

                        switch (Integer.parseInt(jPART.getString("ParteEstado"))) {
                            case 1:
                                partObra.setState("Pendiente");
                                break;

                            case 2:
                                partObra.setState("enviado");
                                break;

                            case 3:
                                partObra.setState("recogido");
                                break;
                        }

                        partObra.setPartcode(jPART.getString("ParCod"));
                        partObra.setPartserie(jPART.getString("ParSer"));
                        partObra.setPartdate(jPART.getString("ParFec"));
                        partObra.setPartlanguaje(jPART.getString("ParIdioma"));
                        partObra.setPartsign(jPART.getString("ImagenFirma"));
                        partObra.setPartsignclient(jPART.getString("ImagenFirmacliente"));
                        LogManager.getInstance().info("FIRMA", jPART.getString("ImagenFirma"));
                        partObra.setDescription(jPART.getString("Descripcion"));

                        ja = jPART.getJSONArray("mEmpleados");
                        list = new ArrayList<>();

                        if (ja.length() > 0) {
                            for (int i = 0; i < ja.length(); i++) {
                                article = new adArticle();
                                article.setId(Integer.parseInt(ja.getJSONObject(i).getString("idArticulo")));
                                article.setArtname(ja.getJSONObject(i).getString("artnom"));
                                article.setArtcode(ja.getJSONObject(i).getString("artcod"));
                                article.setArtprecio(ja.getJSONObject(i).getString("precio"));
                                amount = new adArticleAmount();
                                amount.setArticle(article);
                                amount.setAmount(ja.getJSONObject(i).getString("cantidad"));

                                list.add(amount);
                            }
                        }
                        partObra.setMaetrialamountlist((ArrayList<adArticleAmount>) list);

                        ja = jPART.getJSONArray("operarios");
                        list = new ArrayList<>();

                        if (ja.length() > 0) {
                            for (int i = 0; i < ja.length(); i++) {
                                article = new adArticle();
                                article.setId(Integer.parseInt(ja.getJSONObject(i).getString("idArticulo")));
                                article.setArtname(ja.getJSONObject(i).getString("artnom"));
                                article.setArtcode(ja.getJSONObject(i).getString("artcod"));
                                article.setArtprecio(ja.getJSONObject(i).getString("precio"));
                                amount = new adArticleAmount();
                                amount.setArticle(article);
                                amount.setAmount(ja.getJSONObject(i).getString("cantidad"));
                                amount.setIdcomercial(ja.getJSONObject(i).getString("idvendedor"));
                                list.add(amount);
                            }
                        }
                        partObra.setOperamountlist((ArrayList<adArticleAmount>) list);
                        callBack.onGetPartsServiceTaskCallBack(partObra);

                    } else {
                        partObra = null;
                        callBack.onGetPartsServiceTaskCallBack(partObra);
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
                params.put("idParte", String.valueOf(part.getId()));
                return params;
            }
        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 5,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        GPApplication.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    public interface GetPartsServiceTaskCallBack {
        void onGetPartsServiceTaskCallBack(adPartObra part);

        void onError(String message);
    }

}
