
package com.app.ambruster.gestiondepedidos.data.dao;


import com.app.ambruster.gestiondepedidos.data.ApplicationDatabaseHelper;
import com.app.ambruster.gestiondepedidos.data.exception.CategoryException;
import com.app.ambruster.gestiondepedidos.data.model.adCategory;
import com.app.ambruster.gestiondepedidos.log.LogManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.Callable;


public class CategoryDAO extends AbstractDAO<adCategory, Integer> implements IOperationDAO<adCategory> {

    private ApplicationDatabaseHelper helper;

    public CategoryDAO(ApplicationDatabaseHelper helper) {
        this.helper = helper;
    }

    public ApplicationDatabaseHelper getHelper() {
        return helper;
    }

    @Override
    public Class<adCategory> getEntityClass() {
        return adCategory.class;
    }

    @Override
    public Dao<adCategory, Integer> getDao() {
        try {
            Dao dao = DaoManager.createDao(getHelper().getConnectionSource(), getEntityClass());
            return dao;
        } catch (Exception localException) {
            localException.printStackTrace();
            LogManager.getInstance().error(getClass().getCanonicalName(), localException.getMessage());
        }
        return null;
    }


    public void Create(adCategory l, int operation) throws CategoryException {
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
    public adCategory Get(adCategory object) {
        adCategory category = null;
        try {
            category = getDao().queryForSameId(object);
        } catch (SQLException e) {
            LogManager.getInstance().error(getClass().getCanonicalName(), e.getMessage());
            e.printStackTrace();
        }
        return category;
    }

    public void Delete(adCategory object) throws CategoryException {
        try {
            getDao().delete(object);
        } catch (SQLException e) {
            LogManager.getInstance().error(getClass().getCanonicalName(), e.getMessage());
            e.printStackTrace();
        }
    }

    public void Refresh(adCategory object) throws CategoryException {
        try {
            getDao().refresh(object);
        } catch (SQLException e) {
            LogManager.getInstance().error(getClass().getCanonicalName(), e.getMessage());
            e.printStackTrace();
        }
    }

    public void Update(adCategory object) throws CategoryException {
        try {
            getDao().update(object);
        } catch (SQLException e) {
            LogManager.getInstance().error(getClass().getCanonicalName(), e.getMessage());
            e.printStackTrace();
        }
    }

    public long CountOf() throws CategoryException {
        long count = 0;
        try {
            count = getDao().countOf();
        } catch (SQLException e) {
            LogManager.getInstance().error(getClass().getCanonicalName(), e.getMessage());
            e.printStackTrace();
        }
        return count;
    }

    public void CreateList(final ArrayList<adCategory> list, final int operation) throws CategoryException {
        try {
            getDao().callBatchTasks(new Callable<Object>() {

                @Override
                public Object call() throws Exception {

                    for (adCategory ac : list) {
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
    public ArrayList<adCategory> GetList() {
        ArrayList<adCategory> categories = null;
        ArrayList<adCategory> subcategories;
        ArrayList<adCategory> subcategoriesnames;

        try {
            QueryBuilder<adCategory, Integer> builder = getDao()
                    .queryBuilder();
            Where<adCategory, Integer> where = builder.where();
            where.eq("parent_id", 0);
            builder.orderBy("name", true);
            categories = (ArrayList<adCategory>) getDao().query(builder.prepare());

            for (adCategory category : categories) {
                subcategories = GetList(category);

                subcategoriesnames = new ArrayList<>();

                for (adCategory subcategory : subcategories) {
                    if (subcategory.getName() != null)
                        subcategoriesnames.add(subcategory);
                }

                if (subcategoriesnames != null)
                    category.setSubcategories(subcategoriesnames);
            }

        } catch (SQLException e) {
            LogManager.getInstance().error(getClass().getCanonicalName(), e.getMessage());
            e.printStackTrace();
        }
        return categories;
    }


    public ArrayList<adCategory> GetList(adCategory object) {
        ArrayList<adCategory> categories = null;

        try {
            QueryBuilder<adCategory, Integer> builder = getDao()
                    .queryBuilder();
            Where<adCategory, Integer> where = builder.where();
            where.eq("parent_id", object.getId());
            builder.orderBy("name", true);
            categories = (ArrayList<adCategory>) getDao().query(builder.prepare());
        } catch (SQLException e) {
            LogManager.getInstance().error(getClass().getCanonicalName(), e.getMessage());
            e.printStackTrace();
        }
        return categories;
    }


}
