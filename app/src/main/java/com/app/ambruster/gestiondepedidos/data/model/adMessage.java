package com.app.ambruster.gestiondepedidos.data.model;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;


@DatabaseTable(tableName = "Message")
public class adMessage {

    @DatabaseField(generatedId = true)
    public int id;
    @DatabaseField(index = true, dataType = DataType.DATE_LONG, canBeNull = true)
    public Date created;
    @DatabaseField(canBeNull = true)
    public String text;
    @DatabaseField(canBeNull = true)
    public int requestid;
    @DatabaseField(canBeNull = true)
    public String clientname;
    @DatabaseField(canBeNull = true)
    public int status;
    @DatabaseField(foreign = true, foreignAutoRefresh = true, index = true, canBeNull = false)
    public adUser user;

    public adMessage() {
    }

    public adMessage(int id, Date created, String text, int requestid, String clientname, int status) {
        this.id = id;
        this.created = created;
        this.text = text;
        this.requestid = requestid;
        this.clientname = clientname;
        this.status = status;
    }

    public adMessage(int id, Date created, String text, int requestid, String clientname, int status, adUser user) {
        this.id = id;
        this.created = created;
        this.text = text;
        this.requestid = requestid;
        this.clientname = clientname;
        this.status = status;
        this.user = user;
    }


    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getRequestid() {
        return requestid;
    }

    public void setRequestid(int requestid) {
        this.requestid = requestid;
    }

    public String getClientname() {
        return clientname;
    }

    public void setClientname(String clientname) {
        this.clientname = clientname;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public adUser getUser() {
        return user;
    }

    public void setUser(adUser user) {
        this.user = user;
    }
}
