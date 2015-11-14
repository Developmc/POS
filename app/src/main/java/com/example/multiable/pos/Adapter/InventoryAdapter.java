package com.example.multiable.pos.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.multiable.pos.Data.ProductInventoryInfo;
import com.example.multiable.pos.R;

import java.util.ArrayList;

/**
 * Created by macremote on 2015/10/31.
 */
public class InventoryAdapter extends BaseAdapter {
    private Context context ;
    private ArrayList<ProductInventoryInfo> productInventoryInfos;
    public InventoryAdapter(Context context,ArrayList<ProductInventoryInfo> productInventoryInfos){
        this.context = context;
        this.productInventoryInfos = productInventoryInfos ;
    }
    @Override
    public int getCount() {
        return productInventoryInfos.size();
    }

    @Override
    public Object getItem(int position) {
        return productInventoryInfos.get(position);
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_inventory_search_content,null);
            viewHolder.tv_product = (TextView)convertView.findViewById(R.id.tv_product) ;
            viewHolder.tv_description = (TextView)convertView.findViewById(R.id.tv_description) ;
            viewHolder.tv_tw_shop = (TextView)convertView.findViewById(R.id.tv_tw_shop) ;
            viewHolder.tv_tst_shop = (TextView)convertView.findViewById(R.id.tv_tst_shop) ;
            viewHolder.tv_total_qty = (TextView)convertView.findViewById(R.id.tv_total_qty) ;
            viewHolder.tv_unit = (TextView)convertView.findViewById(R.id.tv_unit) ;
            convertView.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder)convertView.getTag();
        }
        viewHolder.tv_product.setText(productInventoryInfos.get(position).getName());
        viewHolder.tv_description.setText(productInventoryInfos.get(position).getDesc());
        viewHolder.tv_tw_shop.setText(String.valueOf(productInventoryInfos.get(position).getTwShop()));
        viewHolder.tv_tst_shop.setText(String.valueOf(productInventoryInfos.get(position).getTstShop()));
        viewHolder.tv_total_qty.setText(String.valueOf(productInventoryInfos.get(position).getTotalQty()));
        viewHolder.tv_unit.setText(productInventoryInfos.get(position).getUnit());
        return convertView;
    }
    private class ViewHolder{
        private TextView tv_product ;
        private TextView tv_description ;
        private TextView tv_tw_shop ;
        private TextView tv_tst_shop ;
        private TextView tv_total_qty ;
        private TextView tv_unit ;
    }
}
