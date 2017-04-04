
package com.app.ambruster.gestiondepedidos.data.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;


@DatabaseTable(tableName = "ArticleAmount")
public class adArticleAmount {

    @DatabaseField(id = true)
    public int id;
    @DatabaseField(foreign = true, foreignAutoRefresh = true, index = true, canBeNull = false)
    public adArticle article;
    public String amount;
    public String idcomercial;

    public adArticleAmount() {
    }

    public adArticleAmount(int id, adArticle article, String amount) {
        this.id = id;
        this.article = article;
        this.amount = amount;
    }

    public adArticleAmount(int id, adArticle article, String amount, String idcomercial) {
        this.id = id;
        this.article = article;
        this.amount = amount;
        this.idcomercial = idcomercial;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public adArticle getArticle() {
        return article;
    }

    public void setArticle(adArticle article) {
        this.article = article;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getIdcomercial() {
        return idcomercial;
    }

    public void setIdcomercial(String idcomercial) {
        this.idcomercial = idcomercial;
    }
}
