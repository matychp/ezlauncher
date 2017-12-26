package org.matychp.yal.launcher.POJO;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;

public class App implements Comparable<App>{

    private String name, pack;
    private Drawable icon;

    public App(String name, String pack, Drawable icon) {
        this.name = name;
        this.pack = pack;
        this.icon = icon;
    }

    public App(String name, Drawable icon) {
        this.name = name;
        this.pack = "";
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public String getPack() {
        return pack;
    }

    public Drawable getIcon() {
        return icon;
    }

    @Override
    public int compareTo(@NonNull App i) {
        if (name.compareTo(i.getName()) > 0){
            return 1;
        } else if (name.compareTo(i.getName()) < 0){
            return -1;
        } else {
            return 0;
        }
    }
}
