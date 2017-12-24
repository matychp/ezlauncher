package org.matychp.yal.launcher;

import android.graphics.drawable.Drawable;

import java.io.Serializable;

public class Item{

    private String name, pack;
    private Drawable icon;

    public Item(String name, String pack, Drawable icon) {
        this.name = name;
        this.pack = pack;
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
}
