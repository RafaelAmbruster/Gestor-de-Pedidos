
package com.app.ambruster.gestiondepedidos.data.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.ArrayList;

@DatabaseTable(tableName = "Role")
public class adRole extends AbstractIntegerIdentifier {

    @DatabaseField(index = true, canBeNull = true)
    public int parent_id;
    public ArrayList<adRole> subroles;

    public adRole() {
    }

    public adRole(int parent_id) {
        this.parent_id = parent_id;
    }

    public int getParent_id() {
        return parent_id;
    }

    public void setParent_id(int parent_id) {
        this.parent_id = parent_id;
    }

    public ArrayList<adRole> getSubroles() {
        return subroles;
    }

    public void setSubroles(ArrayList<adRole> subroles) {
        this.subroles = subroles;
    }
}
