package com.app.ambruster.gestiondepedidos.data.service.task;


import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.app.ambruster.gestiondepedidos.GPApplication;
import com.app.ambruster.gestiondepedidos.data.model.adArticle;
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

public class ArticleServiceTask {

    ArticleServiceTaskCallBack callBack;

    public ArticleServiceTask(ArticleServiceTaskCallBack callBack) {
        this.callBack = callBack;
    }

    public void CallService(final adUser user, final int page, final int familyid, final String search) {

        String tag_string_req = "get_article";
        final ArrayList<adArticle> list = new ArrayList<>();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                ServiceManager.getInstance().getURL(8).toString(), new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jObj = new JSONObject(response);
                    LogManager.getInstance().info(getClass().getCanonicalName(), jObj.getString("success"));

                    if (jObj.getString("success").equals("true")) {

                        JSONArray ja = jObj.getJSONArray("articles");
                        adArticle article;

                        if (ja.length() > 0) {
                            for (int i = 0; i < ja.length(); i++) {

                                article = new adArticle();
                                article.setId(Integer.parseInt(ja.getJSONObject(i).getString("idarticulo")));
                                article.setArtcode(ja.getJSONObject(i).getString("artcod"));
                                article.setArtname(ja.getJSONObject(i).getString("artnom"));
                                article.setArtimageurl(ja.getJSONObject(i).getString("artimagen"));
                                article.setArtprecio(ja.getJSONObject(i).getString("precio"));

                                list.add(article);
                            }

                            callBack.onArticleServiceTaskCallBack(list);

                            if (ja.length() < ServiceManager.LIMIT) {
                                ArticleListFragment.loadmore = false;
                            }
                        } else {
                            callBack.onArticleServiceTaskCallBack(new ArrayList<adArticle>());
                            ArticleListFragment.loadmore = false;
                        }
                    } else {
                        callBack.onArticleServiceTaskCallBack(new ArrayList<adArticle>());
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
                params.put("idFamily", familyid + "");
                params.put("search", search);
                return params;
            }
        };


        strReq.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 5,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        GPApplication.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    public interface ArticleServiceTaskCallBack {
        void onArticleServiceTaskCallBack(ArrayList<adArticle> articles);

        void onError(String message);
    }

}
