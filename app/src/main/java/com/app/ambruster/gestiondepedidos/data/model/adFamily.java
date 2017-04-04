package com.app.ambruster.gestiondepedidos.data.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "Family")
public class adFamily {

    @DatabaseField(id = true)
    public int id;
    @DatabaseField(canBeNull = true)
    public String url;
    @DatabaseField(canBeNull = true)
    public String code;
    @DatabaseField(canBeNull = true)
    public String name;

    public adFamily() {
    }

    public adFamily(int id, String url, String code, String name) {
        this.id = id;
        this.url = url;
        this.code = code;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
