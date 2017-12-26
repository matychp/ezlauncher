package org.matychp.yal.launcher.POJO;

import android.graphics.drawable.Drawable;

public class CheckableApp extends App {

    private boolean checked;

    public CheckableApp(String name, String pack, Drawable icon, boolean checked) {
        super(name, pack, icon);
        this.checked = checked;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
