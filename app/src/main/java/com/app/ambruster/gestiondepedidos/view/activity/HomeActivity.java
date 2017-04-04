package com.app.ambruster.gestiondepedidos.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.app.ambruster.gestiondepedidos.R;
import com.app.ambruster.gestiondepedidos.configuration.SessionManager;
import com.app.ambruster.gestiondepedidos.data.ApplicationDatabaseManager;
import com.app.ambruster.gestiondepedidos.data.dao.UserDAO;
import com.app.ambruster.gestiondepedidos.data.model.adUser;
import com.app.ambruster.gestiondepedidos.util.CustomDateFormat;
import com.app.ambruster.gestiondepedidos.view.fragment.ClientListFragment;
import com.app.ambruster.gestiondepedidos.view.fragment.CoreFragment;
import com.app.ambruster.gestiondepedidos.view.fragment.FamilyListFragment;
import com.app.ambruster.gestiondepedidos.view.fragment.MessageListFragment;
import com.app.ambruster.gestiondepedidos.view.fragment.PartsListFragment;
import com.app.ambruster.gestiondepedidos.view.fragment.RequestListFragment;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.typeface.FontAwesome;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.accountswitcher.AccountHeader;
import com.mikepenz.materialdrawer.accountswitcher.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileSettingDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.mikepenz.materialdrawer.model.interfaces.Nameable;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    boolean doubleBackToExitPressedOnce = false;
    private Drawer result = null;
    private AccountHeader headerResult = null;
    private SessionManager session;
    private UserDAO userdao;
    private adUser user;
    private String date;
    private CoreFragment activeFragment;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        user = getUser();
        date = CustomDateFormat.getCurrentTimeWOhour(new Date());

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        IProfile profile = new ProfileDrawerItem().withName(user.comname).withEmail(date).withIcon(getResources().getDrawable(R.drawable.profilewhite));
        headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withCompactStyle(true)
                .withCurrentProfileHiddenInList(true)
                .withHeaderBackground(R.drawable.header)
                .addProfiles(
                        profile,
                        new ProfileSettingDrawerItem().withName(getString(R.string.logoff)).withIcon(GoogleMaterial.Icon.gmd_exit_to_app).withIdentifier(-1))
                .withSelectionListEnabledForSingleProfile(false)
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile iProfile, boolean b) {
                        if (iProfile instanceof ProfileSettingDrawerItem) {
                            if (iProfile.getName().equals(getString(R.string.logoff)))
                                logoutUser();
                        }
                        return false;
                    }
                })
                .withSavedInstance(savedInstanceState)
                .build();

        result = new DrawerBuilder(this)
                .withRootView(R.id.drawer_container)
                .withToolbar(toolbar)
                .withAccountHeader(headerResult)
                .withActionBarDrawerToggleAnimated(true)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName(R.string.requests).withIcon(FontAwesome.Icon.faw_pencil_square).withIdentifier(7),
                        new PrimaryDrawerItem().withName(R.string.clients).withIcon(FontAwesome.Icon.faw_user).withIdentifier(2),
                        new PrimaryDrawerItem().withName(R.string.family).withIcon(FontAwesome.Icon.faw_cart_plus).withIdentifier(3),
                        new PrimaryDrawerItem().withName(R.string.messages).withIcon(FontAwesome.Icon.faw_send).withIdentifier(4),
                        new PrimaryDrawerItem().withName(R.string.parts).withIcon(FontAwesome.Icon.faw_pencil).withIdentifier(5),
                        new DividerDrawerItem(),
                        new PrimaryDrawerItem().withName(R.string.application_version).withIcon(FontAwesome.Icon.faw_cog).setEnabled(false).withIdentifier(5)
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(AdapterView<?> parent, View view, int position, long id, IDrawerItem drawerItem) {

                        if (drawerItem instanceof Nameable) {
                            toolbar.setTitle(((Nameable) drawerItem).getNameRes());
                            toolbar.setSubtitle("");
                        }

                        if (drawerItem.getIdentifier() == 3) {
                            addFragment(FamilyListFragment.newInstance(getUser()));
                        } else if (drawerItem.getIdentifier() == 2) {
                            addFragment(ClientListFragment.newInstance(getUser()));
                        } else if (drawerItem.getIdentifier() == 7) {
                            addFragment(RequestListFragment.newInstance(getUser()));
                        } else if (drawerItem.getIdentifier() == 4) {
                            addFragment(MessageListFragment.newInstance(getUser()));
                        } else if (drawerItem.getIdentifier() == 5) {
                            addFragment(PartsListFragment.newInstance(getUser()));
                        }

                        return false;
                    }
                })
                .withAnimateDrawerItems(true)
                .withSavedInstance(savedInstanceState)
                .build();

        if (SplashScreenActivity.firstime) {
            result.openDrawer();
            SplashScreenActivity.firstime = false;
        }

        session = new SessionManager(getApplicationContext());
        if (!session.isLoggedIn()) {
            logoutUser();
        }

        toolbar.setTitle(R.string.app_name);

    }

    public Toolbar getToolbar() {
        return toolbar;
    }

    public void setToolbar(Toolbar toolbar) {
        this.toolbar = toolbar;
    }

    public adUser getUser() {
        user = new adUser();
        userdao = new UserDAO(ApplicationDatabaseManager.getInstance().getHelper());

        try {
            List<adUser> results = userdao.getDao().queryBuilder().where().eq("active", true).query();

            if (results.size() > 0)
                user = results.get(0);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;

    }

    private void logoutUser() {
        session.setLogin(false);
        user = getUser();

        if (user != null) {
            user.token = "";
            user.setActive(false);
            userdao.Update(user);
        }


        new View(HomeActivity.this).postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }, 1000);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState = result.saveInstanceState(outState);
        outState = headerResult.saveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        if (result != null && result.isDrawerOpen()) {
            result.closeDrawer();
        } else {

            if (doubleBackToExitPressedOnce) {
                SplashScreenActivity.firstime = true;
                super.onBackPressed();
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, R.string.exit_app, Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        }
    }

    public void addFragment(CoreFragment fragment) {

        if (activeFragment != null)
            activeFragment.onPause();

        activeFragment = fragment;
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_container, fragment)
                .commitAllowingStateLoss();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
