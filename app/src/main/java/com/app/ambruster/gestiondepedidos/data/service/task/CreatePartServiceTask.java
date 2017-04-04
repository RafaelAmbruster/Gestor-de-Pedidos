package com.app.ambruster.gestiondepedidos.data.service.task;


import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.app.ambruster.gestiondepedidos.GPApplication;
import com.app.ambruster.gestiondepedidos.data.model.adArticleAmount;
import com.app.ambruster.gestiondepedidos.data.model.adPartObra;
import com.app.ambruster.gestiondepedidos.data.model.adUser;
import com.app.ambruster.gestiondepedidos.data.service.ServiceManager;
import com.app.ambruster.gestiondepedidos.log.LogManager;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class CreatePartServiceTask {

    CreatePartServiceTaskCallBack callBack;
    String articles_amount_materials, articles_amount_opers;
    StringBuilder result;

    public CreatePartServiceTask(CreatePartServiceTaskCallBack callBack) {
        this.callBack = callBack;
    }

    public void CallService(final adUser user, final adPartObra parts, final int status) {

        String tag_string_req = "cre_part";
        String url = ServiceManager.getInstance().getURL(20).toString();

        result = new StringBuilder();
        if (parts.getMaetrialamountlist() != null) {
            for (adArticleAmount item : parts.getMaetrialamountlist()) {
                result.append(item.getArticle().getId() + "#" + item.getAmount());
                result.append("|");
            }
            articles_amount_materials = result.length() > 0 ? result.substring(0, result.length() - 1) : "";
        }

        if (parts.getOperamountlist() != null) {
            result = new StringBuilder();
            for (adArticleAmount item : parts.getOperamountlist()) {
                result.append(item.getArticle().getId() + "#" + item.getAmount() + "#" + item.getIdcomercial());
                result.append("|");
            }
            articles_amount_opers = result.length() > 0 ? result.substring(0, result.length() - 1) : "";
        }

        StringRequest strReq;
        strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jObj = new JSONObject(response);
                    LogManager.getInstance().info(getClass().getCanonicalName(), jObj.getString("success"));

                    if (jObj.getString("success").equals("true")) {
                        callBack.onCreatePartServiceTaskCallBack(true, Integer.parseInt(jObj.getString("idParte")));
                    } else {
                        callBack.onCreatePartServiceTaskCallBack(false, 0);
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
                params.put("CliCod", parts.getClient().getClicode());
                params.put("ObraCod", parts.getObra().getObraCod());
                params.put("firma", parts.getPartsign());
                params.put("firmacliente", parts.getPartsignclient());
                params.put("descripcion", parts.getDescription());
                params.put("mEmpleados", articles_amount_materials);
                params.put("operarios", articles_amount_opers);
                params.put("status", String.valueOf(status));
                params.put("prmdate", parts.getPartdate());

                if (parts.getId() != 999999)
                    params.put("idParte", String.valueOf(parts.getId()));

                return params;
            }
        };

        GPApplication.getInstance().addToRequestQueue(strReq, tag_string_req);

        strReq.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 5,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    public interface CreatePartServiceTaskCallBack {
        void onCreatePartServiceTaskCallBack(Boolean status, int id);

        void onError(String message);
    }

}
