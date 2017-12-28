package org.matychp.yal.launcher.Activities;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.matychp.yal.R;
import org.matychp.yal.launcher.Adapters.AppAdapter;
import org.matychp.yal.launcher.POJO.App;
import org.matychp.yal.launcher.POJO.AppIcon;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Home extends AppCompatActivity {

    private ListView listView;

    private AppAdapter appAdapter;

    private List<AppIcon> apps;

    private static final int NEW_APPS = 1;
    private static final int SETTINGS = 2;

    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        apps = new ArrayList<>();
        loadApps();
        Collections.sort(apps);
        loadFunctions();

        appAdapter = new AppAdapter(Home.this, R.layout.app, apps);
        listView = findViewById(R.id.lv_apps);
        listView.setAdapter(appAdapter);
        addOnClickListener();
    }

    /**
     * Carga el Listener para el ListView Apps.
     */
    private void addOnClickListener() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if (apps.get(position).getPkg().compareTo("") == 0) {
                    String name = apps.get(position).getName();
                    if (name.compareTo(getString(R.string.btn_editapps_activity_main)) == 0) {
                        Intent intent = new Intent(Home.this, SelectApps.class);
                        startActivityForResult(intent, NEW_APPS);
                    } else if (name.compareTo(getString(R.string.btn_settings_activity_main)) == 0) {
                        Intent intent = new Intent(Home.this, Settings.class);
                        startActivityForResult(intent, SETTINGS);
                    }
                } else {
                    openApp(apps.get(position).getActivity(), apps.get(position).getPkg());
                }
            }
        });
    }

    /**
     * Carga los botones con funciones para la lista de la activity Home.
     */
    private void loadFunctions() {
        Resources res = getResources();
        Drawable icon = null;

        //Select Apps Button
        if (preferences.getBoolean("swt_editapps", true)) {
            icon = res.getDrawable(R.drawable.ic_select_apps);

            apps.add(new AppIcon(
                    getString(R.string.btn_editapps_activity_main),
                    "SelectApps.class",
                    icon
            ));
        }

        //Settings Button
        icon = res.getDrawable(R.drawable.ic_settings);
        apps.add(new AppIcon(
                getString(R.string.btn_settings_activity_main),
                "Settings.class",
                icon
        ));
    }

    /**
     * Inicia una aplicación seleccionada.
     *
     * @param packageName
     */
    private void openApp(String activityName, String packageName) {
        Intent intent = new Intent();
        intent.setClassName(packageName, activityName);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == NEW_APPS) {
            if (resultCode == Activity.RESULT_OK) {
                apps.clear();
                loadApps();
                loadFunctions();
                appAdapter.notifyDataSetChanged();
                Toast.makeText(this, getString(R.string.successfully_edited_applications), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.canceled), Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == SETTINGS) {
            if (resultCode == Activity.RESULT_OK) {
                apps.clear();
                loadApps();
                loadFunctions();
                appAdapter.notifyDataSetChanged();
                Toast.makeText(this, getString(R.string.successfully_edited_settings), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.canceled), Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Unknown requestCode", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Carga las aplicaciones seleccionadas (con la activity SelectApps) para mostrarse en la activity Home.
     */
    private void loadApps() {
        PackageManager pm = getPackageManager();
        Gson gson = new Gson();

        String savedList = preferences.getString("Apps", null);
        if (savedList != null) {
            Type type = new TypeToken<List<App>>() {
            }.getType();
            List<App> savedApps = gson.fromJson(savedList, type);
            for (App app : savedApps){
                Drawable icon = getResources().getDrawable(R.drawable.ic_launcher_background);
                try {
                    icon = pm.getActivityIcon(new ComponentName(
                            app.getPkg(),
                            app.getActivity()));
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
                AppIcon appIcon = new AppIcon(
                        app.getName(),
                        app.getPkg(),
                        app.getActivity(),
                        icon);
                apps.add(appIcon);
            }
        }
    }

    @Override
    public void onBackPressed() {
    }
}
