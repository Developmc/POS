package com.example.multiable.pos.Fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.example.multiable.pos.Adapter.GridViewAdapter;
import com.example.multiable.pos.Data.GridViewItemInfo;
import com.example.multiable.pos.R;

import java.util.ArrayList;

/**
 * Created by macremote on 2015/11/2.
 */
public class PayFragment extends Fragment {
    private View view ;private GridView gridView;
    private GridViewAdapter adapter ;
    private ArrayList<GridViewItemInfo> datas ;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_pay,container,false);
        initData();
        initView();
        return view;
    }
    private void initData() {
        datas = new ArrayList<>() ;
        datas.add(new GridViewItemInfo("Cash",R.mipmap.ico_cash));
        datas.add(new GridViewItemInfo("$100 Voucher",R.mipmap.ico_100));
        datas.add(new GridViewItemInfo("$50 Voucher",R.mipmap.ico_50));
        datas.add(new GridViewItemInfo("American Express",R.mipmap.ico_ae));
        datas.add(new GridViewItemInfo("MasterCard",R.mipmap.ico_mastercard));
        datas.add(new GridViewItemInfo("UnionPay",R.mipmap.ico_unionpay));

    }

    private void initView() {
        gridView = (GridView)view.findViewById(R.id.gridView);
        adapter = new GridViewAdapter(getActivity(),datas);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(), "" + position, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
