package org.matychp.yal.launcher.Activities;

import android.app.Activity;
import android.app.WallpaperManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;

import org.matychp.yal.R;

import java.io.IOException;

public class Settings extends AppCompatActivity{

    Switch swt_selectapps;

    Button btn_done, btn_cancel, btn_set_wallpaper;

    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    private static final int PICK_IMAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = preferences.edit();

        swt_selectapps = findViewById(R.id.swt_editapps);
        btn_done = findViewById(R.id.btn_done_activity_settings);
        btn_cancel = findViewById(R.id.btn_cancel_activity_settings);
        btn_set_wallpaper = findViewById(R.id.btn_set_wallpaper);

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
        btn_set_wallpaper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
                getIntent.setType("image/*");

                Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                pickIntent.setType("image/*");

                Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});

                startActivityForResult(chooserIntent, PICK_IMAGE);
            }
        });
    }

    /**
     * Carga los settings almacenados localmente.
     */
    private void loadSettings() {
        swt_selectapps.setChecked(preferences.getBoolean("swt_editapps", true));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Cambia el Wallpaper.
        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK){
            Uri uri = data.getData();

            WallpaperManager wallmgr = WallpaperManager.getInstance(getApplicationContext());
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                wallmgr.setBitmap(bitmap);
            } catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}
