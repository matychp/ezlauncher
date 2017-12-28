package org.matychp.yal.launcher.POJO;

import android.graphics.drawable.Drawable;

public class CheckableApp extends AppIcon {

    private boolean checked;

    public CheckableApp(String name, String pack, String act, Drawable icon, boolean checked) {
        super(name, pack, act, icon);
        this.checked = checked;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String toString(){
        return super.toString() + "\nState: " + checked;
    }
}
