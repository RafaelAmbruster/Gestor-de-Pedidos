
package com.app.ambruster.gestiondepedidos.data.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;


@DatabaseTable(tableName = "Obra")
public class adObra {

    @DatabaseField(id = true)
    public int id;
    @DatabaseField(index = true, canBeNull = true)
    public String ObraCod;
    @DatabaseField(index = true, canBeNull = true)
    public String ObraName;

    public adObra(int id, String obraCod, String obraName) {
        this.id = id;
        ObraCod = obraCod;
        ObraName = obraName;
    }

    public adObra() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getObraCod() {
        return ObraCod;
    }

    public void setObraCod(String obraCod) {
        ObraCod = obraCod;
    }

    public String getObraName() {
        return ObraName;
    }

    public void setObraName(String obraName) {
        ObraName = obraName;
    }
}
