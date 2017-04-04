
package com.app.ambruster.gestiondepedidos.data.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public abstract class AbstractStringIdentifier extends AbstractIdentifier {

    @DatabaseField(id = true)
    public String id;

    public String getId() {
        return this.id;
    }

    public void setId(String paramString) {
        this.id = paramString;
    }
}

