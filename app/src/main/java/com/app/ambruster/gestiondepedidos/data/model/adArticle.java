
package com.app.ambruster.gestiondepedidos.data.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.ArrayList;


@DatabaseTable(tableName = "Article")
public class adArticle  {

    @DatabaseField(id = true)
    public int id;
    @DatabaseField(index = true)
    public int family_id;
    @DatabaseField(index = true)
    public String artcode;
    @DatabaseField(index = true)
    public String artname;
    @DatabaseField(index = true, canBeNull = true)
    public String artfamcode;
    @DatabaseField(canBeNull = true)
    public String artfamname;
    @DatabaseField(canBeNull = true)
    public String artlanguaje;
    @DatabaseField(index = true, canBeNull = true)
    public String artprecio;
    @DatabaseField(index = true, canBeNull = true)
    public String artnational;
    @DatabaseField(index = true, canBeNull = true)
    public String artinternational;
    @DatabaseField(index = true, canBeNull = true)
    public String artimageurl;

    public adArticle() {
    }

    public adArticle(int family_id, String artcode, String artname, String artfamcode, String artfamname, String artlanguaje, String artprecio, String artnational, String artinternational, String artimageurl) {
        this.family_id = family_id;
        this.artcode = artcode;
        this.artname = artname;
        this.artfamcode = artfamcode;
        this.artfamname = artfamname;
        this.artlanguaje = artlanguaje;
        this.artprecio = artprecio;
        this.artnational = artnational;
        this.artinternational = artinternational;
        this.artimageurl = artimageurl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFamily_id() {
        return family_id;
    }

    public void setFamily_id(int family_id) {
        this.family_id = family_id;
    }

    public String getArtcode() {
        return artcode;
    }

    public void setArtcode(String artcode) {
        this.artcode = artcode;
    }

    public String getArtname() {
        return artname;
    }

    public void setArtname(String artname) {
        this.artname = artname;
    }

    public String getArtfamcode() {
        return artfamcode;
    }

    public void setArtfamcode(String artfamcode) {
        this.artfamcode = artfamcode;
    }

    public String getArtfamname() {
        return artfamname;
    }

    public void setArtfamname(String artfamname) {
        this.artfamname = artfamname;
    }

    public String getArtlanguaje() {
        return artlanguaje;
    }

    public void setArtlanguaje(String artlanguaje) {
        this.artlanguaje = artlanguaje;
    }

    public String getArtprecio() {
        return artprecio;
    }

    public void setArtprecio(String artprecio) {
        this.artprecio = artprecio;
    }

    public String getArtnational() {
        return artnational;
    }

    public void setArtnational(String artnational) {
        this.artnational = artnational;
    }

    public String getArtinternational() {
        return artinternational;
    }

    public void setArtinternational(String artinternational) {
        this.artinternational = artinternational;
    }

    public String getArtimageurl() {
        return artimageurl;
    }

    public void setArtimageurl(String artimageurl) {
        this.artimageurl = artimageurl;
    }
}
