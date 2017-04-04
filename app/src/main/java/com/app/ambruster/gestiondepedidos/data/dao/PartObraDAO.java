
package com.app.ambruster.gestiondepedidos.data.dao;

import android.util.Log;

import com.app.ambruster.gestiondepedidos.data.ApplicationDatabaseHelper;
import com.app.ambruster.gestiondepedidos.data.exception.PartObraException;
import com.app.ambruster.gestiondepedidos.data.model.adPartObra;
import com.app.ambruster.gestiondepedidos.log.LogManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.Callable;


public class PartObraDAO extends AbstractDAO<adPartObra, Integer> implements IOperationDAO<adPartObra> {

    private ApplicationDatabaseHelper helper;

    public PartObraDAO(ApplicationDatabaseHelper helper) {
        this.helper = helper;
    }

    public ApplicationDatabaseHelper getHelper() {
        return helper;
    }

    @Override
    public Class<adPartObra> getEntityClass() {
        return adPartObra.class;
    }

    @Override
    public Dao<adPartObra, Integer> getDao() {
        try {
            Dao dao = DaoManager.createDao(getHelper().getConnectionSource(), getEntityClass());
            return dao;
        } catch (Exception localException) {
            localException.printStackTrace();
            LogManager.getInstance().error(getClass().getCanonicalName(), localException.getMessage());
        }
        return null;
    }


    public void Create(adPartObra l, int operation) throws PartObraException {
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
    public adPartObra Get(adPartObra object) {
        adPartObra version = null;
        try {
            version = getDao().queryForSameId(object);
        } catch (SQLException e) {
            LogManager.getInstance().error(getClass().getCanonicalName(), e.getMessage());
            e.printStackTrace();
        }
        return version;
    }

    public void Delete(adPartObra version) throws PartObraException {
        try {
            getDao().delete(version);
        } catch (SQLException e) {
            LogManager.getInstance().error(getClass().getCanonicalName(), e.getMessage());
            e.printStackTrace();
        }
    }

    public void Refresh(adPartObra version) throws PartObraException {
        try {
            getDao().refresh(version);
        } catch (SQLException e) {
            LogManager.getInstance().error(getClass().getCanonicalName(), e.getMessage());
            e.printStackTrace();
        }
    }

    public void Update(adPartObra pob) throws PartObraException {
        try {
            getDao().update(pob);
        } catch (SQLException e) {
            LogManager.getInstance().error(getClass().getCanonicalName(), e.getMessage());
            e.printStackTrace();
        }
    }

    public long CountOf() throws PartObraException {
        long count = 0;
        try {
            count = getDao().countOf();
        } catch (SQLException e) {
            LogManager.getInstance().error(getClass().getCanonicalName(), e.getMessage());
            e.printStackTrace();
        }
        return count;
    }

    public void CreateList(final ArrayList<adPartObra> list, final int operation) throws PartObraException {
        try {
            getDao().callBatchTasks(new Callable<Object>() {

                @Override
                public Object call() throws Exception {

                    for (adPartObra ac : list) {
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
    public ArrayList<adPartObra> GetList() {
        return null;
    }

}
