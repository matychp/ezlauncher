package org.matychp.yal.launcher.Activities;

import android.Manifest;
import android.app.Activity;
import android.app.WallpaperManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

import org.matychp.yal.R;

import java.io.IOException;

public class Settings extends AppCompatActivity{

    private Switch swt_selectapps;

    private Button btn_done, btn_cancel, btn_set_wallpaper;

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    private static final int PICK_IMAGE = 1;
    private static final int MY_PERMISSIONS_REQUEST_SET_WALLPAPER = 2;
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 3;

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
                if(ContextCompat.checkSelfPermission(Settings.this, Manifest.permission.SET_WALLPAPER) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(Settings.this, new String[]{Manifest.permission.SET_WALLPAPER},MY_PERMISSIONS_REQUEST_SET_WALLPAPER);
                }
                if(ContextCompat.checkSelfPermission(Settings.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(Settings.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }

                Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
                getIntent.setType("image/*");

                Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                pickIntent.setType("image/*");

                Intent chooserIntent = Intent.createChooser(getIntent, getString(R.string.select_image));
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
                Toast.makeText(this, getString(R.string.successfully_set_wallpaper), Toast.LENGTH_LONG).show();
            } catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}
