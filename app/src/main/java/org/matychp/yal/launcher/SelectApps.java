package org.matychp.yal.launcher;

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

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SelectApps extends AppCompatActivity {

    ListView listView;

    ItemWCBAdapter itemWCBAdapter;

    List<ItemWCB> apps;

    Button btn_done;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_apps);

        apps = new ArrayList<>();
        getApps();
        Collections.sort(apps);

        itemWCBAdapter = new ItemWCBAdapter(SelectApps.this, R.layout.item_with_checkbox, apps);

        listView = findViewById(R.id.lv_checkList);
        listView.setAdapter(itemWCBAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });

        btn_done = findViewById(R.id.btn_done);
        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                setResult(Activity.RESULT_OK, intent);
                saveApps(appsToString());
                finish();
            }
        });
    }

    private List<String> appsToString() {
        List<String> newApps = new ArrayList<>();

        for(ItemWCB i: apps){
            if(i.isChecked()){
                newApps.add(i.getPack());
            }
        }

        return newApps;
    }

    private void getApps(){
        PackageManager pm = getPackageManager();
        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);

        List<String> savedApps = loadApps();

        if( savedApps != null ){
            for(ApplicationInfo packageInfo : packages){
                if ((packageInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0){
                    if(savedApps.contains(packageInfo.packageName)){
                        apps.add(new ItemWCB(
                                pm.getApplicationLabel(packageInfo).toString(),
                                packageInfo.packageName,
                                pm.getApplicationIcon(packageInfo),
                                true
                        ));
                    } else {
                        apps.add(new ItemWCB(
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
                    apps.add(new ItemWCB(
                            pm.getApplicationLabel(packageInfo).toString(),
                            packageInfo.packageName,
                            pm.getApplicationIcon(packageInfo),
                            false
                    ));
                }
            }
        }
    }

    private List<String> loadApps(){
        Gson gson = new Gson();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String savedList = preferences.getString("Apps", null);
        if (savedList != null){
            Type type = new TypeToken<List<String>>(){}.getType();
            List<String> apps = gson.fromJson(savedList, type);

            return apps;
        } else {
            //Error.
            return null;
        }
    }

    private void saveApps(List<String> newApps) {
        Gson gson = new Gson();
        String jsonList = gson.toJson(newApps);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("Apps", jsonList);
        editor.commit();
    }
}
