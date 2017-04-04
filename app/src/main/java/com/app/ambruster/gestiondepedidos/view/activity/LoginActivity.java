package com.app.ambruster.gestiondepedidos.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.app.ambruster.gestiondepedidos.GPApplication;
import com.app.ambruster.gestiondepedidos.R;
import com.app.ambruster.gestiondepedidos.configuration.DeviceInformationManager;
import com.app.ambruster.gestiondepedidos.configuration.SessionManager;
import com.app.ambruster.gestiondepedidos.data.ApplicationDatabaseManager;
import com.app.ambruster.gestiondepedidos.data.dao.IOperationDAO;
import com.app.ambruster.gestiondepedidos.data.dao.UserDAO;
import com.app.ambruster.gestiondepedidos.data.model.adUser;
import com.app.ambruster.gestiondepedidos.data.service.ServiceManager;
import com.app.ambruster.gestiondepedidos.log.LogManager;
import com.dd.processbutton.iml.ActionProcessButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends Activity {

    private EditText inputEmail;
    private EditText inputPassword;
    private SessionManager session;
    private ActionProcessButton btnLogin;
    private MaterialDialog dialog;
    private String email;
    private String password;
    private UserDAO userdao;

    public void Init() {
        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        btnLogin = (ActionProcessButton) findViewById(R.id.btnLogin);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Init();

        session = new SessionManager(getApplicationContext());

        if (session.isLoggedIn()) {
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }

        btnLogin.setMode(ActionProcessButton.Mode.ENDLESS);
        btnLogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                email = inputEmail.getText().toString();
                password = inputPassword.getText().toString();


                if (email.trim().length() > 0 && password.trim().length() > 0) {
                    checkLogin(email, password);
                    modifyVisual(false);

                } else {

                    dialog = new MaterialDialog.Builder(LoginActivity.this)
                            .content(R.string.fill_the_credentials)
                            .contentGravity(GravityEnum.CENTER)
                            .contentColor(getResources().getColor(R.color.black95))
                            .positiveText(R.string.agree)
                            .show();
                }
            }
        });
    }

    private void modifyVisual(Boolean state) {
        btnLogin.setEnabled(state);
        inputEmail.setEnabled(state);
        inputPassword.setEnabled(state);
    }

    private void createUser(JSONObject data) {

        try {
            userdao = new UserDAO(ApplicationDatabaseManager.getInstance().getHelper());
            adUser user = new adUser();
            user.setId(data.getInt("id"));
            user.setActive(true);
            user.setComcode(data.getString("username"));
            user.setName(data.getString("username"));
            user.setComname(data.getString("name"));
            user.setToken(data.getString("token"));
            Log.d("TOKEN",data.getString("token"));
            user.setLastlogin(new Date());

            userdao.Create(user, IOperationDAO.OPERATION_INSERT_OR_UPDATE);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void checkLogin(final String email, final String password) {

        String tag_string_req = "req_login";
        btnLogin.setProgress(1);
        StringRequest strReq = new StringRequest(Method.POST,
                ServiceManager.getInstance().getURL(2).toString(), new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jObj = new JSONObject(response);
                    String success = jObj.getString("success");


                    if (success.equals("true")) {

                        session.setLogin(true);
                        btnLogin.setProgress(100);

                        createUser(jObj.getJSONObject("data"));

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(LoginActivity.this,
                                        HomeActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }, 1000);

                    } else {

                        String errorMsg = jObj.getString("message");
                        btnLogin.setProgress(-1);
                        btnLogin.setText("Denegado");

                        dialog = new MaterialDialog.Builder(LoginActivity.this)
                                .content(errorMsg)
                                .contentGravity(GravityEnum.CENTER)
                                .contentColor(getResources().getColor(R.color.black95))
                                .positiveText(R.string.agree)
                                .show();

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                modifyVisual(true);
                                btnLogin.setProgress(0);
                            }
                        }, 3000);


                    }
                } catch (JSONException e) {
                    LogManager.getInstance().info(getClass().getCanonicalName(), e.getMessage());
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                btnLogin.setProgress(-1);
                btnLogin.setText("Error");

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        modifyVisual(true);
                        btnLogin.setProgress(0);
                    }
                }, 1000);

                dialog = new MaterialDialog.Builder(LoginActivity.this)
                        .content(error.getMessage())
                        .contentGravity(GravityEnum.CENTER)
                        .contentColor(getResources().getColor(R.color.black95))
                        .positiveText(R.string.agree)
                        .show();
            }
        }) {

            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded";
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("_username", email);
                params.put("_password", password);

                if (DeviceInformationManager.getInstance().getMacAddress(LoginActivity.this) != null) {
                    String var = DeviceInformationManager.getInstance().getMacAddress(LoginActivity.this);
                    params.put("mac", var);
                } else {
                    params.put("mac", "");
                }
                return params;
            }

        };

        strReq.setRetryPolicy(new DefaultRetryPolicy(
                ServiceManager.TIMEOUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        GPApplication.getInstance().addToRequestQueue(strReq, tag_string_req);
    }
}
