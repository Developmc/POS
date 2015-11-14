package com.example.multiable.pos.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.multiable.pos.R;

/**
 * Created by macremote on 2015/10/28.
 */
public class UpPullTypeAdapter extends BaseAdapter {
    private Context context;
    private boolean[] leftArray ;
    public UpPullTypeAdapter(Context context,boolean[] leftArray) {
        this.context = context;
        this.leftArray = leftArray ;
    }

    @Override
    public int getCount() {
        return leftArray.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
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
            convertView= LayoutInflater.from(context).inflate(R.layout.item_up_pull_menu,null);
            viewHolder.tv_name=(TextView)convertView.findViewById(R.id.tv_name);
            viewHolder.tv_num=(TextView)convertView.findViewById(R.id.tv_num);
            viewHolder.ll_choose=(LinearLayout)convertView.findViewById(R.id.ll_choose);
            convertView.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder)convertView.getTag();
        }
        //单选
        if (leftArray[position]) {
            viewHolder.ll_choose.setBackgroundResource(R.drawable.shape_circular_bead);
        }
        else {
            //该颜色应与布局一致，不然很难看
            viewHolder.ll_choose.setBackgroundResource(R.drawable.blue_gray_selector) ;
        }
        return convertView;
    }
    private class ViewHolder{
        private TextView tv_name,tv_num;
        private LinearLayout ll_choose ;
    }
    /**更改数据
     * @param position
     */
    public void setLeftArray(int position) {
        //重置rightArray ;
        for(int i=0;i<leftArray.length;i++){
            leftArray[i] = false ;
        }
        leftArray[position] = true ;
    }
}
