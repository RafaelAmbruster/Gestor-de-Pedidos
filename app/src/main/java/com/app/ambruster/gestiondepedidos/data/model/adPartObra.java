
package com.app.ambruster.gestiondepedidos.data.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.ArrayList;


@DatabaseTable(tableName = "Parts")
public class adPartObra {

    @DatabaseField(id = true)
    public int id;
    @DatabaseField(foreign = true, foreignAutoRefresh = true, index = true, canBeNull = true)
    public adClient client;
    @DatabaseField(foreign = true, foreignAutoRefresh = true, index = true, canBeNull = true)
    public adUser commercial;
    @DatabaseField(foreign = true, foreignAutoRefresh = true, index = true, canBeNull = true)
    public adObra obra;
    @DatabaseField(index = true, canBeNull = true)
    public String state;
    @DatabaseField(index = true, canBeNull = true)
    public String partcode;
    @DatabaseField(index = true, canBeNull = true)
    public String partserie;
    @DatabaseField(canBeNull = true)
    public String partdate;
    @DatabaseField(canBeNull = true)
    public String partlanguaje;
    @DatabaseField(index = true, canBeNull = true)
    public String partsign;
    @DatabaseField(index = true, canBeNull = true)
    public String partsignclient;
    @DatabaseField(canBeNull = true)
    public String partdaterec;
    @DatabaseField(index = true, canBeNull = true)
    public int partrelart;
    @DatabaseField(canBeNull = true)
    public String description;
    public ArrayList<adArticleAmount> maetrialamountlist;
    public ArrayList<adArticleAmount> operamountlist;


    public adPartObra() {
    }

    public adPartObra(int id, adClient client, adUser commercial, adObra obra, String state, String partcode, String partserie, String partdate, String partlanguaje, String partsign, String partdaterec, int partrelart, ArrayList<adArticleAmount> maetrialamountlist, ArrayList<adArticleAmount> operamountlist) {
        this.id = id;
        this.client = client;
        this.commercial = commercial;
        this.obra = obra;
        this.state = state;
        this.partcode = partcode;
        this.partserie = partserie;
        this.partdate = partdate;
        this.partlanguaje = partlanguaje;
        this.partsign = partsign;
        this.partdaterec = partdaterec;
        this.partrelart = partrelart;
        this.maetrialamountlist = maetrialamountlist;
        this.operamountlist = operamountlist;
    }

    public adPartObra(int id, adClient client, adUser commercial, adObra obra, String state, String partcode, String partserie, String partdate, String partlanguaje, String partsign, String partsignclient, String partdaterec, int partrelart, String description, ArrayList<adArticleAmount> maetrialamountlist, ArrayList<adArticleAmount> operamountlist) {
        this.id = id;
        this.client = client;
        this.commercial = commercial;
        this.obra = obra;
        this.state = state;
        this.partcode = partcode;
        this.partserie = partserie;
        this.partdate = partdate;
        this.partlanguaje = partlanguaje;
        this.partsign = partsign;
        this.partsignclient = partsignclient;
        this.partdaterec = partdaterec;
        this.partrelart = partrelart;
        this.description = description;
        this.maetrialamountlist = maetrialamountlist;
        this.operamountlist = operamountlist;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public adClient getClient() {
        return client;
    }

    public void setClient(adClient client) {
        this.client = client;
    }

    public adUser getCommercial() {
        return commercial;
    }

    public void setCommercial(adUser commercial) {
        this.commercial = commercial;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPartcode() {
        return partcode;
    }

    public void setPartcode(String partcode) {
        this.partcode = partcode;
    }

    public String getPartserie() {
        return partserie;
    }

    public void setPartserie(String partserie) {
        this.partserie = partserie;
    }

    public String getPartdate() {
        return partdate;
    }

    public void setPartdate(String partdate) {
        this.partdate = partdate;
    }

    public String getPartlanguaje() {
        return partlanguaje;
    }

    public void setPartlanguaje(String partlanguaje) {
        this.partlanguaje = partlanguaje;
    }

    public String getPartsign() {
        return partsign;
    }

    public void setPartsign(String partsign) {
        this.partsign = partsign;
    }

    public String getPartdaterec() {
        return partdaterec;
    }

    public void setPartdaterec(String partdaterec) {
        this.partdaterec = partdaterec;
    }

    public int getPartrelart() {
        return partrelart;
    }

    public void setPartrelart(int partrelart) {
        this.partrelart = partrelart;
    }

    public ArrayList<adArticleAmount> getMaetrialamountlist() {
        return maetrialamountlist;
    }

    public void setMaetrialamountlist(ArrayList<adArticleAmount> maetrialamountlist) {
        this.maetrialamountlist = maetrialamountlist;
    }

    public ArrayList<adArticleAmount> getOperamountlist() {
        return operamountlist;
    }

    public void setOperamountlist(ArrayList<adArticleAmount> operamountlist) {
        this.operamountlist = operamountlist;
    }

    public adObra getObra() {
        return obra;
    }

    public void setObra(adObra obra) {
        this.obra = obra;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPartsignclient() {
        return partsignclient;
    }

    public void setPartsignclient(String partsignclient) {
        this.partsignclient = partsignclient;
    }
}
