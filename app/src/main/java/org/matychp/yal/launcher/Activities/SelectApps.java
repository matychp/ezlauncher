package org.matychp.yal.launcher.Activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
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
import org.matychp.yal.launcher.POJO.App;
import org.matychp.yal.launcher.POJO.AppIcon;
import org.matychp.yal.launcher.POJO.CheckableApp;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SelectApps extends AppCompatActivity {

    private ListView listView;

    private CheckableAppAdapter checkableAppAdapter;

    private List<CheckableApp> apps;

    private Button btn_done, btn_cancel;

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
                saveApps(apps);
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
     * Carga las aplicaciones en la lista.
     * Si existen apps seleccionadas para la lista de la Activity Home, estas se cargan con el checkbox en true.
     * Si no existe el archivo local de aplicaciones seleccionadas, la lista se carga con todos los checkbox en false.
     */
    private void getApps(){
        PackageManager pm = getPackageManager();

        List<App> savedApps = loadApps();

        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> launchables = pm.queryIntentActivities(intent, 0);

        for(ResolveInfo ri: launchables){
            ActivityInfo activityInfo = ri.activityInfo;

            String name = activityInfo.loadLabel(pm).toString();
            String pkg = activityInfo.packageName;
            String activity = activityInfo.name;
            Drawable icon = activityInfo.loadIcon(pm);

            CheckableApp app = new CheckableApp(
                    name,
                    pkg,
                    activity,
                    icon,
                    isContained(savedApps, new App(name, pkg, activity)));
            apps.add(app);
        }
    }

    /**
     * Retorna true si una aplicación está contenida en la lista de aplicaciones seleccionadas almacenadas,
     * false en caso contrario.
     * @param savedApps Lista de aplicaciones seleccionadas almacenada.
     * @param app Una activity a comparar si está contenida.
     * @return boolean.
     */
    private boolean isContained(List<App> savedApps, App app){
        if (savedApps == null) return false;
        if(savedApps.contains(app)){
            return true;
        }
        return false;
    }

    /**
     * Retorna el archivo Apps almacenado localmente, que contiene todas las aplicaciones seleccionadas para la activity Home.
     * @return Lista de aplicaciones almacenadas con valor true en la selección.
     */
    private List<App> loadApps(){
        Gson gson = new Gson();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String savedList = preferences.getString("Apps", null);
        if (savedList != null){
            Type type = new TypeToken<List<App>>(){}.getType();
            List<App> apps = gson.fromJson(savedList, type);

            return apps;
        }
        return null;
    }

    /**
     * Guarda la nueva lista de aplicaciones seleccionadas en un archivo local.
     * @param toSave: nueva lista de aplicaciones seleccionadas a almacenar.
     */
    private void saveApps(List<CheckableApp> toSave) {
        List<App> appsToSave = new ArrayList<>();
        for (CheckableApp cApp: toSave){
            if (cApp.isChecked()){
                appsToSave.add(new App(cApp.getName(),cApp.getPkg(),cApp.getActivity()));
            }
        }

        Gson gson = new Gson();
        String jsonList = gson.toJson(appsToSave);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("Apps", jsonList);
        editor.commit();
    }
}
