package org.matychp.yal.launcher;

import android.graphics.drawable.Drawable;

public class ItemWCB extends Item{

    private boolean checked;

    public ItemWCB(String name, String pack, Drawable icon, boolean checked) {
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
