package com.app.ambruster.gestiondepedidos.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.app.ambruster.gestiondepedidos.GPApplication;
import com.app.ambruster.gestiondepedidos.data.ApplicationDatabaseManager;
import com.app.ambruster.gestiondepedidos.data.dao.IOperationDAO;
import com.app.ambruster.gestiondepedidos.data.dao.RoleDAO;
import com.app.ambruster.gestiondepedidos.data.dao.VersionDAO;
import com.app.ambruster.gestiondepedidos.data.model.adRole;
import com.app.ambruster.gestiondepedidos.data.model.appVersion;
import com.app.ambruster.gestiondepedidos.data.service.ServiceManager;
import com.app.ambruster.gestiondepedidos.log.LogManager;
import com.app.ambruster.gestiondepedidos.configuration.DeviceInformationManager;
import com.app.ambruster.gestiondepedidos.util.background.BackgroundProcess;
import com.app.ambruster.gestiondepedidos.util.background.BackgroundProcessEvent;
import com.crashlytics.android.Crashlytics;
import com.app.ambruster.gestiondepedidos.R;
import com.app.ambruster.gestiondepedidos.util.Location;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import io.fabric.sdk.android.Fabric;

public class SplashScreenActivity extends Activity {

    boolean complete = false;
    boolean error = false;
    private VersionDAO versiondao;
    private RoleDAO roledao;
    public static boolean firstime = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());

        setContentView(R.layout.splash_screen);
        //new Location(this, null).LoadLocation();
        BackgroundProcess local = new BackgroundProcess(this);
        firstime = true;

        local.startWithoutDialog(new BackgroundProcessEvent() {
            @Override
            public void postProcess() {

                if (error) {
                    Toast.makeText(getApplicationContext(), R.string.error_application, Toast.LENGTH_LONG).show();
                    new View(SplashScreenActivity.this).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                        }
                    }, 2000);
                } else {
                    VerifyApplication();
                }
            }

            @Override
            public void process() {
                if (Environment.getExternalStorageState().equals("mounted")) {
                    Seed();
                }
            }
        });
    }

    public void Seed() {
        versiondao = new VersionDAO(ApplicationDatabaseManager.getInstance().getHelper());
        roledao = new RoleDAO(ApplicationDatabaseManager.getInstance().getHelper());

        appVersion version = new appVersion();
        version.setId(1);
        version = versiondao.Get(version);

        if (version == null) {
            try {
                version = new appVersion();
                version.setId(1);
                version.setFirstime(false);
                version.setDate(new Date());
                version.setVersion(getString(R.string.application_version));
                version.setName(DeviceInformationManager.getInstance().getDeviceName());
                version.setImei(DeviceInformationManager.getInstance().getImei(this));
                version.setSdk(DeviceInformationManager.getInstance().getSdkVersion());
                version.setLinenumber(DeviceInformationManager.getInstance().getLineNumber(this));

                versiondao.Create(version, IOperationDAO.OPERATION_INSERT);
                complete = true;

                adRole role = new adRole();
                role.setId(1);
                role.setName("ROLE_COMMERCIAL");
                roledao.Create(role, IOperationDAO.OPERATION_INSERT);

                role = new adRole();
                role.setId(2);
                role.setName("ROLE_SUPPLIER");
                roledao.Create(role, IOperationDAO.OPERATION_INSERT);

            } catch (Exception e) {
                e.printStackTrace();
                LogManager.getInstance().error(getClass().getCanonicalName(), e.getMessage());
                error = true;
            }
        } else {
            complete = true;
        }

        ApplicationDatabaseManager.getInstance().Export(SplashScreenActivity.this, Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "external_sd/");
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    public void StartApplication() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreenActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }, 2000);
    }

    public void CloseApplication() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, 3000);
    }

    @Override
    public void onBackPressed() {
    }

    private void VerifyApplication() {

        String tag_string_req = "req_start";
        StringRequest strReq = new StringRequest(Request.Method.POST,
                ServiceManager.getInstance().getURL(6).toString(), new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jObj = new JSONObject(response);
                    String success = jObj.getString("success");
                    LogManager.getInstance().info(getClass().getCanonicalName(), success);

                    if (success.equals("true")) {
                        StartApplication();

                    } else {
                        String errorMsg = jObj.getString("code");

                        if (errorMsg.equals("1")) {
                            errorMsg = getString(R.string.restricted_access);
                        } else if (errorMsg.equals("2")) {
                            errorMsg = getString(R.string.old_application);
                        }

                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();

                        CloseApplication();
                    }
                } catch (JSONException e) {
                    LogManager.getInstance().info(getClass().getCanonicalName(), e.getMessage());
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), R.string.error_conection, Toast.LENGTH_LONG).show();
                new View(SplashScreenActivity.this).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                }, 3000);
            }
        }) {

            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded";
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                params.put("version", getString(R.string.application_version));
                params.put("brand", "");

                if (DeviceInformationManager.getInstance().getSdkVersion() != null) {
                    String var = DeviceInformationManager.getInstance().getSdkVersion();
                    params.put("sdk", var);
                } else {
                    params.put("sdk", "");
                }

                if (DeviceInformationManager.getInstance().getSimSerialNumber(SplashScreenActivity.this) != null) {
                    String var = DeviceInformationManager.getInstance().getSimSerialNumber(SplashScreenActivity.this);
                    params.put("simserial", var);
                } else {
                    params.put("simserial", "");
                }

                if (DeviceInformationManager.getInstance().getImei(SplashScreenActivity.this) != null) {
                    String var = DeviceInformationManager.getInstance().getImei(SplashScreenActivity.this);
                    params.put("imei", var);
                } else {
                    params.put("imei", "");
                }

                if (DeviceInformationManager.getInstance().getLineNumber(SplashScreenActivity.this) != null) {
                    String var = DeviceInformationManager.getInstance().getLineNumber(SplashScreenActivity.this);
                    params.put("linenumber", var);
                } else {
                    params.put("linenumber", "");
                }

                if (DeviceInformationManager.getInstance().getMacAddress(SplashScreenActivity.this) != null) {
                    String var = DeviceInformationManager.getInstance().getMacAddress(SplashScreenActivity.this);
                    params.put("mac", var);
                } else {
                    params.put("mac", "");
                }

                if (DeviceInformationManager.getInstance().getDeviceName() != null) {
                    String var = DeviceInformationManager.getInstance().getDeviceName();
                    params.put("name", var);
                } else {
                    params.put("name", "");
                }

                return params;
            }
        };

        GPApplication.getInstance().addToRequestQueue(strReq, tag_string_req);
    }


}
