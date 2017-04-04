package com.app.ambruster.gestiondepedidos.data.dao.task;


import android.util.Log;

import com.app.ambruster.gestiondepedidos.data.ApplicationDatabaseManager;
import com.app.ambruster.gestiondepedidos.data.dao.MessageDAO;
import com.app.ambruster.gestiondepedidos.data.model.adMessage;
import com.app.ambruster.gestiondepedidos.data.model.adUser;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MessageDAOTask {

    public MessageDAOTaskCallBack callBack;
    public MessageDAO messagedao;

    public MessageDAOTask(MessageDAOTaskCallBack callBack) {
        this.callBack = callBack;
    }

    public void Execute(final adUser user) {
        messagedao = new MessageDAO(ApplicationDatabaseManager.getInstance().getHelper());
        try {
            List<adMessage> results = messagedao.getDao().queryBuilder().where().eq("user_id", user).and().eq("status", 1).query();

            if (results.size() > 0) {
                Log.d("Messages ", results.size() + "");
                callBack.onMessageDAOTaskCallBack((ArrayList<adMessage>) results);
            } else {
                callBack.onMessageDAOTaskCallBack(new ArrayList<adMessage>());
            }
        } catch (SQLException e) {
            e.printStackTrace();
            callBack.onError(e.getMessage());
        }
    }

    public interface MessageDAOTaskCallBack {
        void onMessageDAOTaskCallBack(ArrayList<adMessage> messages);

        void onError(String message);
    }

}
