
package com.app.ambruster.gestiondepedidos.data.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.ArrayList;



@DatabaseTable(tableName = "Category")
public class adCategory extends AbstractIntegerIdentifier {

    @DatabaseField(index = true)
    public int parent_id;
    @DatabaseField(index = true)
    public boolean favorite;
    public ArrayList<adCategory> subcategories;


    public adCategory() {
    }

    public adCategory(int parent_id) {
        this.parent_id = parent_id;
    }

    public adCategory(int parent_id, boolean favorite) {
        this.parent_id = parent_id;
        this.favorite = favorite;

    }

    public adCategory(int parent_id, boolean favorite, ArrayList<adCategory> subcategories) {
        this.parent_id = parent_id;
        this.favorite = favorite;

        this.subcategories = subcategories;
    }

    public int getParent_id() {
        return parent_id;
    }

    public void setParent_id(int parent_id) {
        this.parent_id = parent_id;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public ArrayList<adCategory> getSubcategories() {
        return subcategories;
    }

    public void setSubcategories(ArrayList<adCategory> subcategories) {
        this.subcategories = subcategories;
    }
}
