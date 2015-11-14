package com.example.multiable.pos.Fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.multiable.pos.Adapter.InventoryAdapter;
import com.example.multiable.pos.Data.ProductInventoryInfo;
import com.example.multiable.pos.Activity.MainActivity;
import com.example.multiable.pos.R;

import java.util.ArrayList;

/**
 * Created by macremote on 2015/10/30.
 */
public class InventorySearchFragment extends Fragment {
    private View view ;
    private ListView listView ;
    private InventoryAdapter adapter ;
    private ArrayList<ProductInventoryInfo> productInventoryInfos;
    private ImageView iv_cancel,iv_refresh ;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_inventory_search,container,false);
        initData();
        initView();
        return view ;
    }

    private void initView() {
        iv_cancel=(ImageView)view.findViewById(R.id.iv_cancel) ;
        iv_refresh=(ImageView)view.findViewById(R.id.iv_refresh) ;
        iv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //移除当前的fragment
                getFragmentManager().beginTransaction().remove(getFragmentManager().findFragmentByTag(
                        "InventorySearchFragment")).commit() ;
                //显示隐藏的主fragment
                MainActivity mainActivity = (MainActivity)getActivity();
                mainActivity.showFragment();
            }
        });
        iv_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        listView = (ListView)view.findViewById(R.id.listView);
        adapter = new InventoryAdapter(getActivity(),productInventoryInfos);
        listView.setAdapter(adapter);

    }

    private void initData() {
        productInventoryInfos = new ArrayList<>() ;
        productInventoryInfos.add(new ProductInventoryInfo("0817","white 36*75",6,4,10,"pc")) ;
        productInventoryInfos.add(new ProductInventoryInfo("0817","white 36*75",6,4,10,"pc")) ;
        productInventoryInfos.add(new ProductInventoryInfo("0817","white 36*75",6,4,10,"pc")) ;
        productInventoryInfos.add(new ProductInventoryInfo("0817","white 36*75",6,4,10,"pc")) ;
        productInventoryInfos.add(new ProductInventoryInfo("0817","white 36*75",6,4,10,"pc")) ;
        productInventoryInfos.add(new ProductInventoryInfo("0817","white 36*75",6,4,10,"pc")) ;
        productInventoryInfos.add(new ProductInventoryInfo("0817","white 36*75",6,4,10,"pc")) ;
        productInventoryInfos.add(new ProductInventoryInfo("0817","white 36*75",6,4,10,"pc")) ;
        productInventoryInfos.add(new ProductInventoryInfo("0817","white 36*75",6,4,10,"pc")) ;
        productInventoryInfos.add(new ProductInventoryInfo("0817","white 36*75",6,4,10,"pc")) ;
        productInventoryInfos.add(new ProductInventoryInfo("0817","white 36*75",6,4,10,"pc")) ;
        productInventoryInfos.add(new ProductInventoryInfo("0817","white 36*75",6,4,10,"pc")) ;
    }
}
