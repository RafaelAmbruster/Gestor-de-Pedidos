package com.app.ambruster.gestiondepedidos.data.dao;


import com.app.ambruster.gestiondepedidos.data.ApplicationDatabaseHelper;
import com.app.ambruster.gestiondepedidos.data.exception.MessageException;
import com.app.ambruster.gestiondepedidos.data.model.adMessage;
import com.app.ambruster.gestiondepedidos.log.LogManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;

import java.sql.SQLException;
import java.util.ArrayList;


public class MessageDAO extends AbstractDAO<adMessage, Integer> implements IOperationDAO<adMessage> {

    private ApplicationDatabaseHelper helper;

    public MessageDAO(ApplicationDatabaseHelper helper) {
        this.helper = helper;
    }

    public ApplicationDatabaseHelper getHelper() {
        return helper;
    }

    @Override
    public Class<adMessage> getEntityClass() {
        return adMessage.class;
    }

    @Override
    public Dao<adMessage, Integer> getDao() {
        try {
            Dao dao = DaoManager.createDao(getHelper().getConnectionSource(), getEntityClass());
            return dao;
        } catch (Exception localException) {
            localException.printStackTrace();
            LogManager.getInstance().error(getClass().getCanonicalName(), localException.getMessage());
        }
        return null;
    }


    public void Create(adMessage l, int operation) throws MessageException {
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
    public adMessage Get(adMessage object) {
        adMessage message = null;
        try {
            message = getDao().queryForSameId(object);
        } catch (SQLException e) {
            LogManager.getInstance().error(getClass().getCanonicalName(), e.getMessage());
            e.printStackTrace();
        }
        return message;
    }

    public void Delete(adMessage object) throws MessageException {
        try {
            getDao().delete(object);
        } catch (SQLException e) {
            LogManager.getInstance().error(getClass().getCanonicalName(), e.getMessage());
            e.printStackTrace();
        }
    }

    public void Refresh(adMessage object) throws MessageException {
        try {
            getDao().refresh(object);
        } catch (SQLException e) {
            LogManager.getInstance().error(getClass().getCanonicalName(), e.getMessage());
            e.printStackTrace();
        }
    }

    public void Update(adMessage object) throws MessageException {
        try {
            getDao().update(object);
        } catch (SQLException e) {
            LogManager.getInstance().error(getClass().getCanonicalName(), e.getMessage());
            e.printStackTrace();
        }
    }

    public long CountOf() throws MessageException {
        long count = 0;
        try {
            count = getDao().countOf();
        } catch (SQLException e) {
            LogManager.getInstance().error(getClass().getCanonicalName(), e.getMessage());
            e.printStackTrace();
        }
        return count;
    }

    public void CreateList(final ArrayList<adMessage> list, final int operation) throws MessageException {

    }

    @Override
    public ArrayList<adMessage> GetList() {
        return null;
    }


    public ArrayList<adMessage> GetList(adMessage object) {
        return null;
    }


}
