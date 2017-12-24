package org.matychp.yal.launcher;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.matychp.yal.R;

import java.util.List;

public class ItemAdapter extends ArrayAdapter<Item>{

    private Context context;

    public ItemAdapter(Context context, int resourceId, List<Item> items){
        super(context, resourceId, items);
        this.context = context;
    }

    private class ViewHolder{
        TextView tv_name;
        ImageView iv_icon;
    }

    public View getView(int position, View convertView, ViewGroup parent){
        ViewHolder holder;
        Item rowItem = getItem(position);

        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if(convertView == null){
            convertView =   mInflater.inflate(R.layout.item, null);
            holder = new ViewHolder();
            holder.tv_name = convertView.findViewById(R.id.tv_name);
            holder.iv_icon = convertView.findViewById(R.id.iv_icon);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)   convertView.getTag();
        }

        if(rowItem != null){
            holder.tv_name.setText(rowItem.getName());
            holder.iv_icon.setImageDrawable(rowItem.getIcon());
        }

        return convertView;
    }
}
