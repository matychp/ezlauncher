package org.matychp.yal.launcher.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.matychp.yal.R;
import org.matychp.yal.launcher.POJO.App;

import java.util.List;

public class AppAdapter extends ArrayAdapter<App>{

    private Context context;

    public AppAdapter(Context context, int resourceId, List<App> apps){
        super(context, resourceId, apps);
        this.context = context;
    }

    private class ViewHolder{
        TextView tv_name;
        ImageView iv_icon;
    }

    public View getView(int position, View convertView, ViewGroup parent){
        ViewHolder holder;
        App rowApp = getItem(position);

        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if(convertView == null){
            convertView =   mInflater.inflate(R.layout.app, null);
            holder = new ViewHolder();
            holder.tv_name = convertView.findViewById(R.id.tv_name);
            holder.iv_icon = convertView.findViewById(R.id.iv_icon);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)   convertView.getTag();
        }

        if(rowApp != null){
            holder.tv_name.setText(rowApp.getName());
            holder.iv_icon.setImageDrawable(rowApp.getIcon());
        }

        return convertView;
    }
}
