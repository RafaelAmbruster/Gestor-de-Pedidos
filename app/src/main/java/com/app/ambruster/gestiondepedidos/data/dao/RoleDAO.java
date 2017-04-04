
package com.app.ambruster.gestiondepedidos.data.dao;


import com.app.ambruster.gestiondepedidos.data.ApplicationDatabaseHelper;
import com.app.ambruster.gestiondepedidos.data.exception.RoleException;
import com.app.ambruster.gestiondepedidos.data.model.adRole;
import com.app.ambruster.gestiondepedidos.log.LogManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.Callable;


public class RoleDAO extends AbstractDAO<adRole, Integer> implements IOperationDAO<adRole> {

    private ApplicationDatabaseHelper helper;

    public RoleDAO(ApplicationDatabaseHelper helper) {
        this.helper = helper;
    }

    public ApplicationDatabaseHelper getHelper() {
        return helper;
    }

    @Override
    public Class<adRole> getEntityClass() {
        return adRole.class;
    }

    @Override
    public Dao<adRole, Integer> getDao() {
        try {
            Dao dao = DaoManager.createDao(getHelper().getConnectionSource(), getEntityClass());
            return dao;
        } catch (Exception localException) {
            localException.printStackTrace();
            LogManager.getInstance().error(getClass().getCanonicalName(), localException.getMessage());
        }
        return null;
    }


    public void Create(adRole l, int operation) throws RoleException {
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
    public adRole Get(adRole object) {
        adRole role = null;
        try {
            role = getDao().queryForSameId(object);
        } catch (SQLException e) {
            LogManager.getInstance().error(getClass().getCanonicalName(), e.getMessage());
            e.printStackTrace();
        }
        return role;
    }

    public void Delete(adRole object) throws RoleException {
        try {
            getDao().delete(object);
        } catch (SQLException e) {
            LogManager.getInstance().error(getClass().getCanonicalName(), e.getMessage());
            e.printStackTrace();
        }
    }

    public void Refresh(adRole object) throws RoleException {
        try {
            getDao().refresh(object);
        } catch (SQLException e) {
            LogManager.getInstance().error(getClass().getCanonicalName(), e.getMessage());
            e.printStackTrace();
        }
    }

    public void Update(adRole object) throws RoleException {
        try {
            getDao().update(object);
        } catch (SQLException e) {
            LogManager.getInstance().error(getClass().getCanonicalName(), e.getMessage());
            e.printStackTrace();
        }
    }

    public long CountOf() throws RoleException {
        long count = 0;
        try {
            count = getDao().countOf();
        } catch (SQLException e) {
            LogManager.getInstance().error(getClass().getCanonicalName(), e.getMessage());
            e.printStackTrace();
        }
        return count;
    }

    public void CreateList(final ArrayList<adRole> list, final int operation) throws RoleException {
        try {
            getDao().callBatchTasks(new Callable<Object>() {

                @Override
                public Object call() throws Exception {

                    for (adRole ac : list) {
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
    public ArrayList<adRole> GetList() {
        ArrayList<adRole> roles = null;
        ArrayList<adRole> subroles;
        ArrayList<adRole> subrolesnames;

        try {
            QueryBuilder<adRole, Integer> builder = getDao()
                    .queryBuilder();
            Where<adRole, Integer> where = builder.where();
            where.eq("parent_id", 0);
            builder.orderBy("name", true);
            roles = (ArrayList<adRole>) getDao().query(builder.prepare());

            for (adRole role : roles) {
                subroles = GetList(role);

                subrolesnames = new ArrayList<>();

                for (adRole subrole : subroles) {
                    if (subrole.getName() != null)
                        subrolesnames.add(subrole);
                }

                if (subrolesnames != null)
                    role.setSubroles(subrolesnames);
            }

        } catch (SQLException e) {
            LogManager.getInstance().error(getClass().getCanonicalName(), e.getMessage());
            e.printStackTrace();
        }
        return roles;
    }


    public ArrayList<adRole> GetList(adRole object) {
        ArrayList<adRole> categories = null;

        try {
            QueryBuilder<adRole, Integer> builder = getDao()
                    .queryBuilder();
            Where<adRole, Integer> where = builder.where();
            where.eq("parent_id", object.getId());
            builder.orderBy("name", true);
            categories = (ArrayList<adRole>) getDao().query(builder.prepare());
        } catch (SQLException e) {
            LogManager.getInstance().error(getClass().getCanonicalName(), e.getMessage());
            e.printStackTrace();
        }
        return categories;
    }


}
