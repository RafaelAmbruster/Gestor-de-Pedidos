package com.app.ambruster.gestiondepedidos.data.model;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;


@DatabaseTable(tableName = "User")
public class adUser {

    @DatabaseField(id = true)
    public int id;
    @DatabaseField(index = true, dataType = DataType.DATE_LONG, canBeNull = true)
    public Date created;
    @DatabaseField(defaultValue = "true")
    public Boolean active;
    @DatabaseField(canBeNull = true)
    public String name;
    @DatabaseField(canBeNull = true)
    public String comcode;
    @DatabaseField(canBeNull = true)
    public String password;
    @DatabaseField(canBeNull = true)
    public String token;
    @DatabaseField(canBeNull = true)
    public String comname;
    @DatabaseField(canBeNull = true)
    public String serie;
    @DatabaseField(canBeNull = true)
    public String counter;
    @DatabaseField(canBeNull = true)
    public String showapp;
    @DatabaseField(canBeNull = true)
    public String nationalitem;
    @DatabaseField(canBeNull = true)
    public String internationalitem;
    @DatabaseField(index = true, dataType = DataType.DATE_LONG, canBeNull = true)
    public Date lastlogin;

    public adUser() {
    }

    public adUser(int id, Date created, Boolean active, String name, String comcode, String password, String token, String comname, String serie, String counter, String showapp, String nationalitem, String internationalitem) {
        this.id = id;
        this.created = created;
        this.active = active;
        this.name = name;
        this.comcode = comcode;
        this.password = password;
        this.token = token;
        this.comname = comname;
        this.serie = serie;
        this.counter = counter;
        this.showapp = showapp;
        this.nationalitem = nationalitem;
        this.internationalitem = internationalitem;
    }

    public adUser(int id, Date created, Boolean active, String name, String comcode, String password, String token, String comname, String serie, String counter, String showapp, String nationalitem, String internationalitem, Date lastlogin) {
        this.id = id;
        this.created = created;
        this.active = active;
        this.name = name;
        this.comcode = comcode;
        this.password = password;
        this.token = token;
        this.comname = comname;
        this.serie = serie;
        this.counter = counter;
        this.showapp = showapp;
        this.nationalitem = nationalitem;
        this.internationalitem = internationalitem;
        this.lastlogin = lastlogin;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComcode() {
        return comcode;
    }

    public void setComcode(String comcode) {
        this.comcode = comcode;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getComname() {
        return comname;
    }

    public void setComname(String comname) {
        this.comname = comname;
    }

    public String getSerie() {
        return serie;
    }

    public void setSerie(String serie) {
        this.serie = serie;
    }

    public String getCounter() {
        return counter;
    }

    public void setCounter(String counter) {
        this.counter = counter;
    }

    public String getShowapp() {
        return showapp;
    }

    public void setShowapp(String showapp) {
        this.showapp = showapp;
    }

    public String getNationalitem() {
        return nationalitem;
    }

    public void setNationalitem(String nationalitem) {
        this.nationalitem = nationalitem;
    }

    public String getInternationalitem() {
        return internationalitem;
    }

    public void setInternationalitem(String internationalitem) {
        this.internationalitem = internationalitem;
    }

    public Date getLastlogin() {
        return lastlogin;
    }

    public void setLastlogin(Date lastlogin) {
        this.lastlogin = lastlogin;
    }
}
