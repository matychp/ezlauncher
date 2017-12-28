package org.matychp.yal.launcher.POJO;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;

public class AppIcon extends App{

    private Drawable icon;

    public AppIcon(String name, String pkg, String act, Drawable icon) {
        super(name, pkg, act);
        this.icon = icon;
    }

    public AppIcon(String name, String act, Drawable icon) {
        super(name, "", act);
        this.icon = icon;
    }

    public Drawable getIcon() {
        return icon;
    }
}
