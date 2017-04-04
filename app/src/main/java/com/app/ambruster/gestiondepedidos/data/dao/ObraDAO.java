
package com.app.ambruster.gestiondepedidos.data.dao;

import android.util.Log;

import com.app.ambruster.gestiondepedidos.data.ApplicationDatabaseHelper;
import com.app.ambruster.gestiondepedidos.data.dao.AbstractDAO;
import com.app.ambruster.gestiondepedidos.data.dao.IOperationDAO;
import com.app.ambruster.gestiondepedidos.data.exception.ObraException;
import com.app.ambruster.gestiondepedidos.data.model.adObra;
import com.app.ambruster.gestiondepedidos.log.LogManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.Callable;


public class ObraDAO extends AbstractDAO<adObra, Integer> implements IOperationDAO<adObra> {

    private ApplicationDatabaseHelper helper;

    public ObraDAO(ApplicationDatabaseHelper helper) {
        this.helper = helper;
    }

    public ApplicationDatabaseHelper getHelper() {
        return helper;
    }

    @Override
    public Class<adObra> getEntityClass() {
        return adObra.class;
    }

    @Override
    public Dao<adObra, Integer> getDao() {
        try {
            Dao dao = DaoManager.createDao(getHelper().getConnectionSource(), getEntityClass());
            return dao;
        } catch (Exception localException) {
            localException.printStackTrace();
            LogManager.getInstance().error(getClass().getCanonicalName(), localException.getMessage());
        }
        return null;
    }


    public void Create(adObra l, int operation) throws ObraException {
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
    public adObra Get(adObra object) {
        adObra obra = null;
        try {
            obra = getDao().queryForSameId(object);
        } catch (SQLException e) {
            LogManager.getInstance().error(getClass().getCanonicalName(), e.getMessage());
            e.printStackTrace();
        }
        return obra;
    }

    public void Delete(adObra version) throws ObraException {
        try {
            getDao().delete(version);
        } catch (SQLException e) {
            LogManager.getInstance().error(getClass().getCanonicalName(), e.getMessage());
            e.printStackTrace();
        }
    }

    public void Refresh(adObra obra) throws ObraException {
        try {
            getDao().refresh(obra);
        } catch (SQLException e) {
            LogManager.getInstance().error(getClass().getCanonicalName(), e.getMessage());
            e.printStackTrace();
        }
    }

    public void Update(adObra pob) throws ObraException {
        try {
            getDao().update(pob);
        } catch (SQLException e) {
            LogManager.getInstance().error(getClass().getCanonicalName(), e.getMessage());
            e.printStackTrace();
        }
    }

    public long CountOf() throws ObraException {
        long count = 0;
        try {
            count = getDao().countOf();
        } catch (SQLException e) {
            LogManager.getInstance().error(getClass().getCanonicalName(), e.getMessage());
            e.printStackTrace();
        }
        return count;
    }

    public void CreateList(final ArrayList<adObra> list, final int operation) throws ObraException {
        try {
            getDao().callBatchTasks(new Callable<Object>() {

                @Override
                public Object call() throws Exception {

                    for (adObra ac : list) {
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
    public ArrayList<adObra> GetList() {
        return null;
    }

}
