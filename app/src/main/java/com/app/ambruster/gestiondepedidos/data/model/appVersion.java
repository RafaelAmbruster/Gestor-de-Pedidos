package com.app.ambruster.gestiondepedidos.data.model;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

/**
 * Created by Ambruster on 1/24/2015.
 */


@DatabaseTable(tableName = "Version")
public class appVersion {

    @DatabaseField(id = true)
    public int id;
    @DatabaseField(index = true, dataType = DataType.DATE_LONG, canBeNull = true)
    public Date date;
    @DatabaseField(defaultValue = "false")
    public Boolean current;
    @DatabaseField(canBeNull = true)
    public String version;
    @DatabaseField(defaultValue = "true")
    public Boolean firstime;
    @DatabaseField(canBeNull = true)
    public String brand;
    @DatabaseField(canBeNull = true)
    public String sdk;
    @DatabaseField(canBeNull = true)
    public String simserial;
    @DatabaseField(canBeNull = true)
    public String imei;
    @DatabaseField(canBeNull = true)
    public String linenumber;
    @DatabaseField(canBeNull = true)
    public String mac;
    @DatabaseField(canBeNull = true)
    public String name;

    public appVersion() {
    }

    public appVersion(int id, Date date, Boolean current, String version, Boolean firstime) {
        this.date = date;
        this.id = id;
        this.current = current;
        this.version = version;
        this.firstime = firstime;
    }

    public appVersion(int id, Date date, Boolean current, String version, Boolean firstime, String brand, String sdk, String simserial, String imei, String linenumber, String mac, String name) {
        this.id = id;
        this.date = date;
        this.current = current;
        this.version = version;
        this.firstime = firstime;
        this.brand = brand;
        this.sdk = sdk;
        this.simserial = simserial;
        this.imei = imei;
        this.linenumber = linenumber;
        this.mac = mac;
        this.name = name;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Boolean getCurrent() {
        return current;
    }

    public void setCurrent(Boolean current) {
        this.current = current;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Boolean getFirstime() {
        return firstime;
    }

    public void setFirstime(Boolean firstime) {
        this.firstime = firstime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getSdk() {
        return sdk;
    }

    public void setSdk(String sdk) {
        this.sdk = sdk;
    }

    public String getSimserial() {
        return simserial;
    }

    public void setSimserial(String simserial) {
        this.simserial = simserial;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getLinenumber() {
        return linenumber;
    }

    public void setLinenumber(String linenumber) {
        this.linenumber = linenumber;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
