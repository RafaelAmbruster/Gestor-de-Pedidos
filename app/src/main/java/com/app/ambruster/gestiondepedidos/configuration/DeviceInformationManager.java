package com.app.ambruster.gestiondepedidos.configuration;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import com.app.ambruster.gestiondepedidos.log.LogManager;


public class DeviceInformationManager {

    private static DeviceInformationManager singleton;

    public synchronized static DeviceInformationManager getInstance() {
        if (singleton == null) {
            singleton = new DeviceInformationManager();
        }
        return singleton;
    }

    public String getImei(Context paramContext) {
        try {
            String str = ((TelephonyManager) paramContext
                    .getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();

            return str;
        } catch (Exception localException) {
            LogManager.getInstance().error(getClass().getCanonicalName(),
                    localException.getMessage());
        }
        return "";
    }

    public String getLineNumber(Context paramContext) {
        try {
            String str = ((TelephonyManager) paramContext
                    .getSystemService(Context.TELEPHONY_SERVICE)).getLine1Number();
            return str;
        } catch (Exception localException) {
            LogManager.getInstance().error(getClass().getCanonicalName(),
                    localException.getMessage());
        }
        return "";
    }

    public String getSdkVersion() {
        return String.valueOf(Build.VERSION.SDK_INT);
    }

    public String getSimSerialNumber(Context paramContext) {
        try {
            String str = ((TelephonyManager) paramContext
                    .getSystemService(Context.TELEPHONY_SERVICE)).getSimSerialNumber();
            return str;
        } catch (Exception localException) {
            LogManager.getInstance().error(getClass().getCanonicalName(),
                    localException.getMessage());
        }
        return "";
    }

    public String getMacAddress(Context context) {

        try {
            WifiManager wimanager = (WifiManager) context
                    .getSystemService(Context.WIFI_SERVICE);
            String str = wimanager.getConnectionInfo().getMacAddress();

            return str;
        } catch (Exception localException) {
            LogManager.getInstance().error(getClass().getCanonicalName(),
                    localException.getMessage());
        }
        return "";

    }

    public String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        } else {
            return capitalize(manufacturer) + " " + model;
        }
    }

    private String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }

}
