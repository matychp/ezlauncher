package org.matychp.yal.launcher.Activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

import org.matychp.yal.R;

public class Settings extends AppCompatActivity{

    Switch swt_selectapps;

    Button btn_done, btn_cancel;

    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = preferences.edit();

        swt_selectapps = findViewById(R.id.swt_editapps);
        btn_done = findViewById(R.id.btn_done_activity_settings);
        btn_cancel = findViewById(R.id.btn_cancel_activity_settings);

        loadSettings();
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
                editor.putBoolean("swt_editapps",swt_selectapps.isChecked());
                editor.commit();
                setResult(Activity.RESULT_OK, intent);
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
     * Carga los settings almacenados localmente.
     */
    private void loadSettings() {
        swt_selectapps.setChecked(preferences.getBoolean("swt_editapps", true));
    }
}
