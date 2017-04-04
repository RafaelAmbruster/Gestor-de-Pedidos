
package com.app.ambruster.gestiondepedidos.data.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;


@DatabaseTable(tableName = "Article")
public class adClient {

    @DatabaseField(id = true)
    public int id;
    @DatabaseField(index = true)
    public int commercial_id;
    @DatabaseField(index = true)
    public int idrelclicom;
    @DatabaseField(index = true)
    public String clicode;
    @DatabaseField(index = true, canBeNull = true)
    public String cliname;
    @DatabaseField(canBeNull = true)
    public String clilanguaje;
    @DatabaseField(canBeNull = true)
    public String artlanguaje;

    public adClient() {
    }

    public adClient(int id, int commercial_id, int idrelclicom, String clicode, String cliname, String clilanguaje, String artlanguaje) {
        this.id = id;
        this.commercial_id = commercial_id;
        this.idrelclicom = idrelclicom;
        this.clicode = clicode;
        this.cliname = cliname;
        this.clilanguaje = clilanguaje;
        this.artlanguaje = artlanguaje;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCommercial_id() {
        return commercial_id;
    }

    public void setCommercial_id(int commercial_id) {
        this.commercial_id = commercial_id;
    }

    public int getIdrelclicom() {
        return idrelclicom;
    }

    public void setIdrelclicom(int idrelclicom) {
        this.idrelclicom = idrelclicom;
    }

    public String getClicode() {
        return clicode;
    }

    public void setClicode(String clicode) {
        this.clicode = clicode;
    }

    public String getCliname() {
        return cliname;
    }

    public void setCliname(String cliname) {
        this.cliname = cliname;
    }

    public String getClilanguaje() {
        return clilanguaje;
    }

    public void setClilanguaje(String clilanguaje) {
        this.clilanguaje = clilanguaje;
    }

    public String getArtlanguaje() {
        return artlanguaje;
    }

    public void setArtlanguaje(String artlanguaje) {
        this.artlanguaje = artlanguaje;
    }
}
