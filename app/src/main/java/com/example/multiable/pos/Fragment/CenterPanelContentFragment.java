package com.example.multiable.pos.Fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.multiable.pos.Adapter.ProductAdapter;
import com.example.multiable.pos.Data.ProductInfo;
import com.example.multiable.pos.R;

import java.util.ArrayList;

/**
 * Created by macremote on 2015/10/28.
 */
public class CenterPanelContentFragment extends Fragment {
    private View view;
    private ListView listView ;
    private ArrayList<ProductInfo> productInfos;
    private ProductAdapter adapter ;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_center_panel_content,container,false);
        initData() ;
        initView() ;
        return view ;
    }

    private void initData() {
        productInfos = new ArrayList<ProductInfo>() ;
        productInfos.add(new ProductInfo("36×75×100","枕袋","-",1.0,1875.00,0,1875.00)) ;
        productInfos.add(new ProductInfo("36×75×100","枕袋","-",1.0,1875.00,0,1875.00)) ;
        productInfos.add(new ProductInfo("36×75×100","枕袋","-",1.0,1875.00,0,1875.00)) ;
        productInfos.add(new ProductInfo("36×75×100","枕袋","-",1.0,1875.00,0,1875.00)) ;
        productInfos.add(new ProductInfo("36×75×100","枕袋","-",1.0,1875.00,0,1875.00)) ;
        productInfos.add(new ProductInfo("36×75×100","枕袋","-",1.0,1875.00,0,1875.00)) ;
        productInfos.add(new ProductInfo("36×75×100","枕袋","-",1.0,1875.00,0,1875.00)) ;
        productInfos.add(new ProductInfo("36×75×100","枕袋","-",1.0,1875.00,0,1875.00)) ;
        productInfos.add(new ProductInfo("36×75×100","枕袋","-",1.0,1875.00,0,1875.00)) ;
        productInfos.add(new ProductInfo("36×75×100","枕袋","-",1.0,1875.00,0,1875.00)) ;
        productInfos.add(new ProductInfo("36×75×100","枕袋","-",1.0,1875.00,0,1875.00)) ;
        productInfos.add(new ProductInfo("36×75×100","枕袋","-",1.0,1875.00,0,1875.00)) ;
        productInfos.add(new ProductInfo("36×75×100","枕袋","-",1.0,1875.00,0,1875.00)) ;


    }
    private void initView() {

        listView = (ListView)view.findViewById(R.id.listView) ;
        adapter = new ProductAdapter(getActivity(),productInfos);
        listView.setAdapter(adapter);
    }

}
