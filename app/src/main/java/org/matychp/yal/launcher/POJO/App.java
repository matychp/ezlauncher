package org.matychp.yal.launcher.POJO;

import java.util.Objects;

public class App implements Comparable<App>{

    private String name, pkg, activity;

    public App(String name, String pkg, String act) {
        this.name = name;
        this.pkg = pkg;
        this.activity = act;
    }

    public String getName() {
        return name;
    }

    public String getPkg() {
        return pkg;
    }

    public String getActivity() {
        return activity;
    }

    @Override
    public int compareTo(App i) {
        if (name.compareTo(i.getName()) > 0){
            return 1;
        } else if (name.compareTo(i.getName()) < 0){
            return -1;
        } else {
            return 0;
        }
    }

    @Override
    public String toString(){
        return "Name: " + this.name +
                "\nPack: " + this.pkg +
                "\nActivity: " + this.activity;
    }

    @Override
    public boolean equals(Object o) {
        // self check
        if (this == o)
            return true;
        // null check
        if (o == null)
            return false;
        // type check and cast
        if (getClass() != o.getClass())
            return false;
        App app = (App) o;
        // field comparison
        return Objects.equals(name, app.getName())
                && Objects.equals(pkg, app.getPkg())
                && Objects.equals(activity, app.getActivity());
    }
}
