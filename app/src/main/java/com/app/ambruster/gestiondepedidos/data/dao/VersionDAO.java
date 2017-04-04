
package com.app.ambruster.gestiondepedidos.data.dao;

import android.util.Log;
import com.app.ambruster.gestiondepedidos.data.ApplicationDatabaseHelper;
import com.app.ambruster.gestiondepedidos.data.exception.VersionException;
import com.app.ambruster.gestiondepedidos.data.model.appVersion;
import com.app.ambruster.gestiondepedidos.log.LogManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.Callable;



public class VersionDAO extends AbstractDAO<appVersion, Integer> implements IOperationDAO<appVersion> {

    private ApplicationDatabaseHelper helper;

    public VersionDAO(ApplicationDatabaseHelper helper) {
        this.helper = helper;
    }

    public ApplicationDatabaseHelper getHelper() {
        return helper;
    }

    @Override
    public Class<appVersion> getEntityClass() {
        return appVersion.class;
    }

    @Override
    public Dao<appVersion, Integer> getDao() {
        try {
            Dao dao = DaoManager.createDao(getHelper().getConnectionSource(), getEntityClass());
            return dao;
        } catch (Exception localException) {
            localException.printStackTrace();
            LogManager.getInstance().error(getClass().getCanonicalName(), localException.getMessage());
        }
        return null;
    }


    public void Create(appVersion l, int operation) throws VersionException {
        try {

            switch (operation) {
                case IOperationDAO.OPERATION_INSERT:
                    getDao().create(l);
                    Log.i("Created: ", "" + l.getId() + " -- " + " Ads");
                    break;
                case IOperationDAO.OPERATION_INSERT_OR_UPDATE:
                    getDao().createOrUpdate(l);
                    Log.i("Created: ", "" + l.getId() + " -- " + " Ads");
                    break;
                case IOperationDAO.OPERATION_INSERT_IF_NOT_EXISTS:
                    getDao().createIfNotExists(l);
                    Log.i("Created: ", "" + l.getId() + " -- " + " Ads");
                    break;
            }

        } catch (SQLException e) {
            LogManager.getInstance().error(getClass().getCanonicalName(), e.getMessage());

            e.printStackTrace();
        }
    }

    @Override
    public appVersion Get(appVersion object) {
        appVersion version = null;
        try {
            version = getDao().queryForSameId(object);
        } catch (SQLException e) {
            LogManager.getInstance().error(getClass().getCanonicalName(), e.getMessage());
            e.printStackTrace();
        }
        return version;
    }

    public void Delete(appVersion version) throws VersionException {
        try {
            getDao().delete(version);
        } catch (SQLException e) {
            LogManager.getInstance().error(getClass().getCanonicalName(), e.getMessage());
            e.printStackTrace();
        }
    }

    public void Refresh(appVersion version) throws VersionException {
        try {
            getDao().refresh(version);
        } catch (SQLException e) {
            LogManager.getInstance().error(getClass().getCanonicalName(), e.getMessage());
            e.printStackTrace();
        }
    }

    public void Update(appVersion version) throws VersionException {
        try {
            getDao().update(version);
        } catch (SQLException e) {
            LogManager.getInstance().error(getClass().getCanonicalName(), e.getMessage());
            e.printStackTrace();
        }
    }

    public long CountOf() throws VersionException {
        long count = 0;
        try {
            count = getDao().countOf();
        } catch (SQLException e) {
            LogManager.getInstance().error(getClass().getCanonicalName(), e.getMessage());
            e.printStackTrace();
        }
        return count;
    }

    public void CreateList(final ArrayList<appVersion> list, final int operation) throws VersionException {
        try {
            getDao().callBatchTasks(new Callable<Object>() {

                @Override
                public Object call() throws Exception {

                    for (appVersion ac : list) {
                        Create(ac, operation);
                    }
                    return null;
                }
            });

        } catch (Exception e) {
            LogManager.getInstance().error("Inserting List ", e.getMessage());

        }
    }

    @Override
    public ArrayList<appVersion> GetList() {
        return null;
    }

}
