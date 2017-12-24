package org.matychp.yal.launcher;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import org.matychp.yal.R;

import java.util.List;

public class ItemWCBAdapter extends ArrayAdapter<ItemWCB> implements View.OnClickListener{

    private Context context;

    public ItemWCBAdapter(Context context, int resourceId, List<ItemWCB> items){
        super(context, resourceId, items);
        this.context = context;
    }

    @Override
    public void onClick(View view) {
        CheckBox checkBox = (CheckBox) view;
        int position = (Integer) view.getTag();
        getItem(position).setChecked(checkBox.isChecked());
    }

    private class ViewHolder{
        CheckBox cb_selected;
        TextView tv_name;
        ImageView iv_icon;
    }

    public View getView(int position, View convertView, ViewGroup parent){
        ViewHolder holder;
        ItemWCB rowItem = getItem(position);

        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if(convertView == null){
            convertView =   mInflater.inflate(R.layout.item_with_checkbox, null);
            holder = new ViewHolder();
            holder.cb_selected = convertView.findViewById(R.id.cb_selected);
            holder.tv_name = convertView.findViewById(R.id.tv_name);
            holder.iv_icon = convertView.findViewById(R.id.iv_icon);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if(rowItem != null){
            holder.cb_selected.setTag(position);
            holder.cb_selected.setChecked(rowItem.isChecked());
            holder.cb_selected.setOnClickListener(this);
            holder.tv_name.setText(rowItem.getName());
            holder.iv_icon.setImageDrawable(rowItem.getIcon());
        }

        return convertView;
    }
}
