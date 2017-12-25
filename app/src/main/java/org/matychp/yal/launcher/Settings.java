package org.matychp.yal.launcher;

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

    Switch swt_editapps;

    Button btn_done;

    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = preferences.edit();

        swt_editapps = findViewById(R.id.swt_editapps);
        btn_done = findViewById(R.id.btn_done_activity_settings);

        loadSettings();

        swt_editapps.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean state) {
                if (state){
                    editor.putBoolean("swt_editapps", true);
                } else {
                    editor.putBoolean("swt_editapps", false);
                }
                editor.commit();
            }
        });

        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });
    }

    private void loadSettings() {
        swt_editapps.setChecked(preferences.getBoolean("swt_editapps", true));
    }
}
