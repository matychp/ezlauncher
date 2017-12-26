package org.matychp.yal.launcher.Activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.matychp.yal.R;
import org.matychp.yal.launcher.Adapters.CheckableAppAdapter;
import org.matychp.yal.launcher.POJO.CheckableApp;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SelectApps extends AppCompatActivity {

    ListView listView;

    CheckableAppAdapter checkableAppAdapter;

    List<CheckableApp> apps;

    Button btn_done, btn_cancel;

    /**
     * Obtiene todas las aplicaciones instaladas, las ordena, permite seleccionarlas para que se muestren
     * en la activity Home, y almacena una lista de las aplicaciones seleccionadas de forma local.
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_apps);

        apps = new ArrayList<>();
        getApps();
        Collections.sort(apps);

        checkableAppAdapter = new CheckableAppAdapter(SelectApps.this, R.layout.checkable_app, apps);

        listView = findViewById(R.id.lv_checkList);
        listView.setAdapter(checkableAppAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });

        btn_done = findViewById(R.id.btn_done);
        btn_cancel = findViewById(R.id.btn_cancel);

        addOnClickListener();
    }


    /**
     * Agrega los Listener de esta activity.
     */
    private void addOnClickListener(){
        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                setResult(Activity.RESULT_OK, intent);
                saveApps(appsToString());
                finish();
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(Activity.RESULT_CANCELED);
                finish();
            }
        });
    }

    /**
     * Extrae el package name de cada aplicación seleccionada hacia una lista, esta lista será la almacenada como archivo local.
     * @return Una lista del package name de cada aplicación seleccionada.
     */
    private List<String> appsToString() {
        List<String> newApps = new ArrayList<>();

        for(CheckableApp i: apps){
            if(i.isChecked()){
                newApps.add(i.getPack());
            }
        }
        return newApps;
    }

    /**
     * Carga las aplicaciones en la lista.
     * Si existen apps seleccionadas para la lista de la Activity Home, estas se cargan con el checkbox en true.
     * Si no existe el archivo local de aplicaciones seleccionadas, la lista se carga con todos los checkbox en false.
     */
    private void getApps(){
        PackageManager pm = getPackageManager();
        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);

        List<String> savedApps = loadApps();

        if( savedApps != null ){
            for(ApplicationInfo packageInfo : packages){
                if ((packageInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0){
                    if(savedApps.contains(packageInfo.packageName)){
                        apps.add(new CheckableApp(
                                pm.getApplicationLabel(packageInfo).toString(),
                                packageInfo.packageName,
                                pm.getApplicationIcon(packageInfo),
                                true
                        ));
                    } else {
                        apps.add(new CheckableApp(
                                pm.getApplicationLabel(packageInfo).toString(),
                                packageInfo.packageName,
                                pm.getApplicationIcon(packageInfo),
                                false
                        ));
                    }
                }
            }
        } else {
            for(ApplicationInfo packageInfo : packages){
                if ((packageInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0){
                    apps.add(new CheckableApp(
                            pm.getApplicationLabel(packageInfo).toString(),
                            packageInfo.packageName,
                            pm.getApplicationIcon(packageInfo),
                            false
                    ));
                }
            }
        }
    }

    /**
     * Retorna el archivo Apps almacenado localmente, que contiene todas las aplicaciones seleccionadas para la activity Home.
     * @return Lista de aplicaciones almacenadas con valor true en la selección.
     */
    private List<String> loadApps(){
        Gson gson = new Gson();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String savedList = preferences.getString("Apps", null);
        if (savedList != null){
            Type type = new TypeToken<List<String>>(){}.getType();
            List<String> apps = gson.fromJson(savedList, type);

            return apps;
        }
        return null;
    }

    /**
     * Guarda la nueva lista de aplicaciones seleccionadas en un archivo local.
     * @param newApps: nueva lista de aplicaciones seleccionadas a almacenar.
     */
    private void saveApps(List<String> newApps) {
        Gson gson = new Gson();
        String jsonList = gson.toJson(newApps);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("Apps", jsonList);
        editor.commit();
    }
}
