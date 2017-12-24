package org.matychp.yal.launcher;

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

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ListView listView;

    ItemAdapter itemAdapter;

    List<Item> apps;

    private static final int NEW_APPS = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        apps = new ArrayList<>();
        loadApps();
        addAddButton();

        itemAdapter = new ItemAdapter(MainActivity.this, R.layout.item, apps);

        listView = findViewById(R.id.lv_apps);
        listView.setAdapter(itemAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if(apps.get(position).getPack().compareTo("") == 0) {
                    Intent intent = new Intent (MainActivity.this, SelectApps.class);
                    startActivityForResult(intent, NEW_APPS);
                } else {
                    openApp(apps.get(position).getPack());
                }
            }
        });
    }

    private void addAddButton(){
        Resources res = getResources();
        Drawable addIcon = res.getDrawable(R.drawable.ic_add_box_black_24dp);

        apps.add(new Item(
                getString(R.string.btn_editapps_activity_main),
                "",
                addIcon
        ));
    }

    private void openApp(String packageName){
        Intent intent = getPackageManager().getLaunchIntentForPackage(packageName);
        if(intent != null) {
            startActivity(intent);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == NEW_APPS){
            if(resultCode == Activity.RESULT_OK) {
                //List<String> newApps = data.getStringArrayListExtra("new_apps");
                apps.clear();
                loadApps();
                addAddButton();
                itemAdapter.notifyDataSetChanged();
            } else {

            }
        }
    }

    private void stringToApps(List<String> newApps) {
        PackageManager pm = getPackageManager();
        for(String app: newApps){
            ApplicationInfo appInfo = null;
            try {
                appInfo = pm.getApplicationInfo(app, PackageManager.GET_META_DATA);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }

            apps.add(new Item(
                    pm.getApplicationLabel(appInfo).toString(),
                    appInfo.packageName,
                    pm.getApplicationIcon(appInfo)
            ));
        }
    }

    private void loadApps(){
        Gson gson = new Gson();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String savedList = preferences.getString("Apps", null);
        if (savedList != null){
            Type type = new TypeToken<List<String>>(){}.getType();
            List<String> apps = gson.fromJson(savedList, type);

            stringToApps(apps);
        } else {
        }
    }
}
