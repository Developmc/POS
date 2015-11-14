package com.example.multiable.pos.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.multiable.pos.Data.GridViewItemInfo;
import com.example.multiable.pos.R;

import java.util.ArrayList;

/**
 * Created by macremote on 2015/10/28.
 */
public class GridViewAdapter extends BaseAdapter {
    private Context context ;
    private ArrayList<GridViewItemInfo> datas;
    public GridViewAdapter(Context context,ArrayList<GridViewItemInfo> datas){
        this.context = context ;
        this.datas = datas ;
    }
    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder ;
        if(convertView==null){
            viewHolder = new ViewHolder() ;
            convertView = LayoutInflater.from(context).inflate(R.layout.item_gridview,null);
            viewHolder.iv_logo=(ImageView)convertView.findViewById(R.id.iv_logo);
            viewHolder.tv_name=(TextView)convertView.findViewById(R.id.tv_name);
            convertView.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder)convertView.getTag();
        }
        viewHolder.tv_name.setText(datas.get(position).getName());
        viewHolder.iv_logo.setImageResource(datas.get(position).getLogo());
        return convertView;
    }
    private class ViewHolder{
        private ImageView iv_logo ;
        private TextView tv_name ;
    }
}
