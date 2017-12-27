package org.matychp.yal.launcher.Activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.matychp.yal.R;
import org.matychp.yal.launcher.Adapters.AppAdapter;
import org.matychp.yal.launcher.POJO.App;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Home extends AppCompatActivity {

    private ListView listView;

    private AppAdapter appAdapter;

    private List<App> apps;

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
    private void addOnClickListener(){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if(apps.get(position).getPack().compareTo("") == 0) {
                    String name = apps.get(position).getName();
                    if(name.compareTo(getString(R.string.btn_editapps_activity_main)) == 0){
                        Intent intent = new Intent (Home.this, SelectApps.class);
                        startActivityForResult(intent, NEW_APPS);
                    } else if(name.compareTo(getString(R.string.btn_settings_activity_main)) == 0){
                        Intent intent = new Intent (Home.this, Settings.class);
                        startActivityForResult(intent, SETTINGS);
                    }
                } else {
                    openApp(apps.get(position).getPack());
                }
            }
        });
    }

    /**
     * Carga los botones con funciones para la lista de la activity Home.
     */
    private void loadFunctions() {
        Resources res = getResources();

        //Select Apps Button
        if(preferences.getBoolean("swt_editapps", true)){
            Drawable addIcon = res.getDrawable(R.drawable.ic_select_apps);

            apps.add(new App(
                    getString(R.string.btn_editapps_activity_main),
                    addIcon
            ));
        }

        //Settings Button
        Drawable addIcon = res.getDrawable(R.drawable.ic_settings);
        apps.add(new App(
                getString(R.string.btn_settings_activity_main),
                addIcon
        ));
    }

    /**
     * Inicia una aplicación seleccionada.
     * @param packageName
     */
    private void openApp(String packageName){
        Intent intent = getPackageManager().getLaunchIntentForPackage(packageName);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == NEW_APPS){
            if(resultCode == Activity.RESULT_OK) {
                apps.clear();
                loadApps();
                loadFunctions();
                appAdapter.notifyDataSetChanged();
                Toast.makeText(this, getString(R.string.successfully_edited_applications),Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.canceled),Toast.LENGTH_SHORT).show();
            }
        } else if(requestCode == SETTINGS){
            if(resultCode == Activity.RESULT_OK) {
                apps.clear();
                loadApps();
                loadFunctions();
                appAdapter.notifyDataSetChanged();
                Toast.makeText(this, getString(R.string.successfully_edited_settings),Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.canceled),Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Unknown requestCode",Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Convierte una lista de packages en items que pueden ser usados en el ListView de la activity Home.
     * @param newApps
     */
    private void stringToApps(List<String> newApps) {
        PackageManager pm = getPackageManager();
        for(String app: newApps){
            ApplicationInfo appInfo = null;
            try {
                appInfo = pm.getApplicationInfo(app, PackageManager.GET_META_DATA);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }

            apps.add(new App(
                    pm.getApplicationLabel(appInfo).toString(),
                    appInfo.packageName,
                    pm.getApplicationIcon(appInfo)
            ));
        }
    }

    /**
     * Carga las aplicaciones seleccionadas (con la activity SelectApps) para mostrarse en la activity Home.
     */
    private void loadApps(){
        Gson gson = new Gson();

        String savedList = preferences.getString("Apps", null);
        if (savedList != null){
            Type type = new TypeToken<List<String>>(){}.getType();
            List<String> apps = gson.fromJson(savedList, type);

            stringToApps(apps);
        }
    }

    @Override
    public void onBackPressed() {
    }
}
