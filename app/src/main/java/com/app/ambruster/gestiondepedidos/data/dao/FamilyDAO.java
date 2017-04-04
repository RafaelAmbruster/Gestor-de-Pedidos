package com.app.ambruster.gestiondepedidos.data.dao;


import com.app.ambruster.gestiondepedidos.data.ApplicationDatabaseHelper;
import com.app.ambruster.gestiondepedidos.data.exception.UserException;
import com.app.ambruster.gestiondepedidos.data.model.adFamily;
import com.app.ambruster.gestiondepedidos.log.LogManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.Callable;


public class FamilyDAO extends AbstractDAO<adFamily, Integer> implements IOperationDAO<adFamily> {

    private ApplicationDatabaseHelper helper;

    public FamilyDAO(ApplicationDatabaseHelper helper) {
        this.helper = helper;
    }

    public ApplicationDatabaseHelper getHelper() {
        return helper;
    }

    @Override
    public Class<adFamily> getEntityClass() {
        return adFamily.class;
    }

    @Override
    public Dao<adFamily, Integer> getDao() {
        try {
            Dao dao = DaoManager.createDao(getHelper().getConnectionSource(), getEntityClass());
            return dao;
        } catch (Exception localException) {
            localException.printStackTrace();
            LogManager.getInstance().error(getClass().getCanonicalName(), localException.getMessage());
        }
        return null;
    }


    public void Create(adFamily l, int operation) throws UserException {
        try {

            switch (operation) {
                case IOperationDAO.OPERATION_INSERT:
                    getDao().create(l);

                    break;
                case IOperationDAO.OPERATION_INSERT_OR_UPDATE:
                    getDao().createOrUpdate(l);

                    break;
                case IOperationDAO.OPERATION_INSERT_IF_NOT_EXISTS:
                    getDao().createIfNotExists(l);

                    break;
            }

        } catch (SQLException e) {
            LogManager.getInstance().error(getClass().getCanonicalName(), e.getMessage());

            e.printStackTrace();
        }
    }

    @Override
    public adFamily Get(adFamily object) {
        adFamily family = null;
        try {
            family = getDao().queryForSameId(object);
        } catch (SQLException e) {
            LogManager.getInstance().error(getClass().getCanonicalName(), e.getMessage());
            e.printStackTrace();
        }
        return family;
    }

    public void Delete(adFamily object) throws UserException {
        try {
            getDao().delete(object);
        } catch (SQLException e) {
            LogManager.getInstance().error(getClass().getCanonicalName(), e.getMessage());
            e.printStackTrace();
        }
    }

    public void Refresh(adFamily object) throws UserException {
        try {
            getDao().refresh(object);
        } catch (SQLException e) {
            LogManager.getInstance().error(getClass().getCanonicalName(), e.getMessage());
            e.printStackTrace();
        }
    }

    public void Update(adFamily object) throws UserException {
        try {
            getDao().update(object);
        } catch (SQLException e) {
            LogManager.getInstance().error(getClass().getCanonicalName(), e.getMessage());
            e.printStackTrace();
        }
    }

    public long CountOf() throws UserException {
        long count = 0;
        try {
            count = getDao().countOf();
        } catch (SQLException e) {
            LogManager.getInstance().error(getClass().getCanonicalName(), e.getMessage());
            e.printStackTrace();
        }
        return count;
    }

    public void CreateList(final ArrayList<adFamily> list, final int operation) throws UserException {
        try {
            getDao().callBatchTasks(new Callable<Object>() {

                @Override
                public Object call() throws Exception {

                    for (adFamily ac : list) {
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
    public ArrayList<adFamily> GetList() {
        return null;
    }


    public ArrayList<adFamily> GetList(adFamily object) {
        return null;
    }


}
