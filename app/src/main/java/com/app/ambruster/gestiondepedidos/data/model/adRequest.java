package com.app.ambruster.gestiondepedidos.data.model;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.ArrayList;
import java.util.Date;


@DatabaseTable(tableName = "Request")
public class adRequest {

    @DatabaseField(id = true)
    public int id;
    @DatabaseField(index = true, dataType = DataType.DATE_LONG, canBeNull = true)
    public Date requestdate;
    @DatabaseField(foreign = true, foreignAutoRefresh = true, index = true, canBeNull = true)
    public adUser user;
    @DatabaseField(foreign = true, foreignAutoRefresh = true, index = true, canBeNull = false)
    public adClient client;
    @DatabaseField(index = true)
    public int status;
    public ArrayList<adArticleAmount> amountlist;
    public String servicedate;

    public adRequest() {
    }

    public adRequest(int id, Date requestdate, adUser user, adClient client, int status, ArrayList<adArticleAmount> amountlist, String servicedate) {
        this.id = id;
        this.requestdate = requestdate;
        this.user = user;
        this.client = client;
        this.status = status;
        this.amountlist = amountlist;
        this.servicedate = servicedate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getRequestdate() {
        return requestdate;
    }

    public void setRequestdate(Date requestdate) {
        this.requestdate = requestdate;
    }

    public adUser getUser() {
        return user;
    }

    public void setUser(adUser user) {
        this.user = user;
    }

    public adClient getClient() {
        return client;
    }

    public void setClient(adClient client) {
        this.client = client;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public ArrayList<adArticleAmount> getAmountlist() {
        return amountlist;
    }

    public void setAmountlist(ArrayList<adArticleAmount> amountlist) {
        this.amountlist = amountlist;
    }

    public String getServicedate() {
        return servicedate;
    }

    public void setServicedate(String servicedate) {
        this.servicedate = servicedate;
    }
}
