package com.app.ambruster.gestiondepedidos.data.service;

import android.util.SparseArray;

public class ServiceManager {

    public static final int TIMEOUT = 10000;
    public static final int LIMIT = 30;
    private SparseArray<String> urls = new SparseArray<>();
    private static ServiceManager singleton;

    //private static String domain = " http://www.onesoft.es/requestapprestfull/web/";
    private static String domain = " http://104.236.246.144/pruebarequestapprestfull/web/";

    private ServiceManager() {
        this.urls.put(2, domain + "login_check");
        this.urls.put(3, domain + "logout/$username/$token");
        this.urls.put(4, domain + "register");
        this.urls.put(5, domain + "is_authenticated/$username/$token");
        this.urls.put(6, domain + "mobile_check");
        this.urls.put(7, domain + "family");
        this.urls.put(8, domain + "articles");
        this.urls.put(9, domain + "clients");
        this.urls.put(10, domain + "requests");
        this.urls.put(11, domain + "create_request");
        this.urls.put(12, domain + "send_message");
        this.urls.put(13, domain + "remove_request/$username/$token/$idRequest");
        this.urls.put(14, domain + "send_request/$username/$token/$idRequest");
        this.urls.put(15, domain + "create_request");
        this.urls.put(16, domain + "get_request");
        this.urls.put(17, domain + "parts");
        this.urls.put(18, domain + "obras");
        this.urls.put(19, domain + "operarios");
        this.urls.put(20, domain + "createPart");
        this.urls.put(21, domain + "get_part");
        this.urls.put(22, domain + "remove_part/$username/$token/$idParte");
        this.urls.put(23, domain + "send_part/$username/$token/$idParte");
        this.urls.put(24, domain + "comercials");


    }

    public synchronized static ServiceManager getInstance() {
        if (singleton == null) {
            singleton = new ServiceManager();
        }
        return singleton;
    }

    public String getURL(int paramInt) {
        return this.urls.get(paramInt, "");

    }
}
