package org.matychp.yal.launcher;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;

import java.io.Serializable;

public class Item implements Comparable<Item>{

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

    @Override
    public int compareTo(@NonNull Item i) {
        if (name.compareTo(i.getName()) > 0){
            return 1;
        } else if (name.compareTo(i.getName()) < 0){
            return -1;
        } else {
            return 0;
        }
    }
}
