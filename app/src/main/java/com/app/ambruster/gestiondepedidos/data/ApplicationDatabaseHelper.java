package com.app.ambruster.gestiondepedidos.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.app.ambruster.gestiondepedidos.configuration.ConfigurationManager;
import com.app.ambruster.gestiondepedidos.data.model.adArticle;
import com.app.ambruster.gestiondepedidos.data.model.adArticleAmount;
import com.app.ambruster.gestiondepedidos.data.model.adCategory;
import com.app.ambruster.gestiondepedidos.data.model.adFamily;
import com.app.ambruster.gestiondepedidos.data.model.adMessage;
import com.app.ambruster.gestiondepedidos.data.model.adPartObra;
import com.app.ambruster.gestiondepedidos.data.model.adRequest;
import com.app.ambruster.gestiondepedidos.data.model.adRole;
import com.app.ambruster.gestiondepedidos.data.model.adUser;
import com.app.ambruster.gestiondepedidos.data.model.appVersion;
import com.app.ambruster.gestiondepedidos.log.LogManager;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class ApplicationDatabaseHelper extends OrmLiteSqliteOpenHelper {

    public static final String DATABASE_NAME = "data.db";
    private static final int DATABASE_VERSION = 5;

    public ApplicationDatabaseHelper(Context context) {
        super(context, ConfigurationManager.getInstance().getPath(3) + DATABASE_NAME, null,
                DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
        try {

            TableUtils.createTable(connectionSource, appVersion.class);
            TableUtils.createTable(connectionSource, adUser.class);
            TableUtils.createTable(connectionSource, adRole.class);
            TableUtils.createTable(connectionSource, adCategory.class);
            TableUtils.createTable(connectionSource, adFamily.class);
            TableUtils.createTable(connectionSource, adArticle.class);
            TableUtils.createTable(connectionSource, adRequest.class);
            TableUtils.createTable(connectionSource, adMessage.class);
            TableUtils.createTable(connectionSource, adArticleAmount.class);


            Log.i(ApplicationDatabaseHelper.class.getCanonicalName(), " Created new entries");

        } catch (SQLException e) {
            LogManager.getInstance().error(getClass().getCanonicalName(), e.getMessage());
            Log.e(ApplicationDatabaseHelper.class.getCanonicalName(), "Can't create the new entries", e);

        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource,
                          int oldVersion, int newVersion) {


        List<String> allSql = new ArrayList<>();
        switch (oldVersion) {
            case 5:

                try {

                    TableUtils.dropTable(connectionSource, appVersion.class, true);
                    TableUtils.dropTable(connectionSource, adUser.class, true);
                    TableUtils.dropTable(connectionSource, adRole.class, true);
                    TableUtils.dropTable(connectionSource, adCategory.class, true);
                    TableUtils.dropTable(connectionSource, adFamily.class, true);
                    TableUtils.dropTable(connectionSource, adArticle.class, true);
                    TableUtils.dropTable(connectionSource, adRequest.class, true);
                    TableUtils.dropTable(connectionSource, adMessage.class, true);
                    TableUtils.dropTable(connectionSource, adArticleAmount.class, true);
                    TableUtils.dropTable(connectionSource, adPartObra.class, true);

                      /*Creating dropped tables*/

                    TableUtils.createTable(connectionSource, appVersion.class);
                    TableUtils.createTable(connectionSource, adUser.class);
                    TableUtils.createTable(connectionSource, adRole.class);
                    TableUtils.createTable(connectionSource, adCategory.class);
                    TableUtils.createTable(connectionSource, adFamily.class);
                    TableUtils.createTable(connectionSource, adArticle.class);
                    TableUtils.createTable(connectionSource, adRequest.class);
                    TableUtils.createTable(connectionSource, adMessage.class);
                    TableUtils.createTable(connectionSource, adArticleAmount.class);
                    TableUtils.createTable(connectionSource, adPartObra.class);

                } catch (SQLException e) {
                    e.printStackTrace();
                }


                break;
        }

        for (String sql : allSql) {
            db.execSQL(sql);
        }
    }


    @Override
    public void close() {
        super.close();
    }
}
