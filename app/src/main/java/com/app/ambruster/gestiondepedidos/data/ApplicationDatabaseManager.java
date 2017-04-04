package com.app.ambruster.gestiondepedidos.data;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Environment;
import android.util.Log;
import com.app.ambruster.gestiondepedidos.log.LogManager;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;


public class ApplicationDatabaseManager {


    private static ApplicationDatabaseManager instance;

    private ApplicationDatabaseHelper helper;

    private ApplicationDatabaseManager(Context ctx) {
        helper = new ApplicationDatabaseHelper(ctx);
    }

    public static void init(Context ctx) {
        if (null == instance) {
            instance = new ApplicationDatabaseManager(ctx);
        }
    }

    public static ApplicationDatabaseManager getInstance() {
        return instance;
    }

    public ApplicationDatabaseHelper getHelper() {
        return helper;
    }

    public void Export(Context context, String path) {

        Context c = context;
        try {

            File sd = new File(path);
            File data = Environment.getDataDirectory();

            if (sd.canWrite()) {

                String currentDBPath = "//data//" + c.getPackageName() + "//databases//" + ApplicationDatabaseHelper.DATABASE_NAME;
                String backupDBPath = ApplicationDatabaseHelper.DATABASE_NAME;
                File currentDB = new File(data, currentDBPath);
                File backupDB = new File(sd, backupDBPath);

                FileChannel src = new FileInputStream(currentDB).getChannel();
                FileChannel dst = new FileOutputStream(backupDB).getChannel();
                dst.transferFrom(src, 0, src.size());
                src.close();
                dst.close();

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            LogManager.getInstance().error(getClass().getCanonicalName() + " Exporting Database!", e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            LogManager.getInstance().error(getClass().getCanonicalName() + " Exporting Database!", e.getMessage());
        }

    }

    public boolean Import(Context c) {
        boolean flag = Create(c);
        boolean ready = true;
        if (!flag) {
            try {

                AssetManager assetManager = c.getAssets();
                String assets[];
                assets = assetManager.list("data");

                OutputStream databaseOutputStream = new FileOutputStream(
                        "//data//data//" + c.getPackageName() + "//databases//" + ApplicationDatabaseHelper.DATABASE_NAME);
                InputStream databaseInputStream;
                byte[] buffer = new byte[1024];

                for (String str : assets) {
                    Log.i("Assets db name", str);

                    databaseInputStream = c.getAssets().open("data/" +
                            str);
                    while ((databaseInputStream.read(buffer)) > 0) {
                        databaseOutputStream.write(buffer);
                    }
                    databaseInputStream.close();
                }
                databaseOutputStream.flush();
                databaseOutputStream.close();


            } catch (IOException e) {
                ready = false;
                e.printStackTrace();
                LogManager.getInstance().error(getClass().getCanonicalName() + " Importing Database!", e.getMessage());
            }
        }
        return ready;
    }

    private boolean Create(Context c) {
        boolean flag;

        File db = new File("//data//data//" + c.getPackageName() + "//databases//" + ApplicationDatabaseHelper.DATABASE_NAME);
        flag = db.exists();

        if (!flag) {
            File parent = db.getParentFile();
            if (parent != null)
                parent.mkdirs();
        }
        return flag;
    }




}