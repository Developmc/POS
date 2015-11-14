package com.example.multiable.pos.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.multiable.pos.Data.ProductInfo;
import com.example.multiable.pos.R;

import java.util.ArrayList;

/**
 * Created by macremote on 2015/10/28.
 */
public class ProductAdapter extends BaseAdapter {
    private ArrayList<ProductInfo> products ;
    private Context context ;

    public ProductAdapter(Context context,ArrayList<ProductInfo> products){
        this.context = context ;
        this.products = products ;
    }
    @Override
    public int getCount() {
        return products.size();
    }

    @Override
    public Object getItem(int position) {
        return products.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder ;
        if(convertView==null){
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_content,null) ;
            viewHolder.itemContent = (LinearLayout)convertView.findViewById(R.id.itemContent) ;
            viewHolder.tv_item = (TextView)convertView.findViewById(R.id.tv_item) ;
            viewHolder.tv_name = (TextView)convertView.findViewById(R.id.tv_name) ;
            viewHolder.tv_size = (TextView)convertView.findViewById(R.id.tv_size) ;
            viewHolder.tv_unit = (TextView)convertView.findViewById(R.id.tv_unit) ;
            viewHolder.tv_qty = (TextView)convertView.findViewById(R.id.tv_qty) ;
            viewHolder.tv_unitPrice = (TextView)convertView.findViewById(R.id.tv_unitPrice) ;
            viewHolder.tv_disc = (TextView)convertView.findViewById(R.id.tv_disc) ;
            viewHolder.tv_totalPrice = (TextView)convertView.findViewById(R.id.tv_totalPrice) ;
            convertView.setTag(viewHolder);
        }
        else{
            viewHolder=(ViewHolder)convertView.getTag();
        }
//        if(position%2==0){
//            viewHolder.itemContent.setBackgroundResource(R.drawable.shape_circular_bead_gray_light_small);
//        }
        viewHolder.tv_item.setText(String.valueOf(position+1));
        viewHolder.tv_name.setText(products.get(position).getName());
        viewHolder.tv_size.setText(products.get(position).getSize());
        viewHolder.tv_unit.setText(products.get(position).getUnit());
        viewHolder.tv_qty.setText(String.valueOf(products.get(position).getQty()));
        viewHolder.tv_unitPrice.setText(String.valueOf(products.get(position).getUnitPrice()));
        viewHolder.tv_disc.setText(String.valueOf(products.get(position).getDisc()));
        viewHolder.tv_totalPrice.setText(String.valueOf(products.get(position).getTotalPrice()));

        return convertView;
    }

    private class ViewHolder {
        private LinearLayout itemContent ;
        private TextView tv_item ;
        private TextView tv_name ;
        private TextView tv_size ;
        private TextView tv_unit ;
        private TextView tv_qty ;
        private TextView tv_unitPrice ;
        private TextView tv_disc ;
        private TextView tv_totalPrice ;


    }

}
