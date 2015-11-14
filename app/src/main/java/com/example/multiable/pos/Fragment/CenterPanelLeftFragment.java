package com.example.multiable.pos.Fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.multiable.pos.Adapter.GridViewAdapter;
import com.example.multiable.pos.Data.GridViewItemInfo;
import com.example.multiable.pos.Activity.MainActivity;
import com.example.multiable.pos.R;

import java.util.ArrayList;

/**
 * Created by macremote on 2015/10/28.
 */
public class CenterPanelLeftFragment extends Fragment {
    private View view ;
    private GridView gridView;
    private GridViewAdapter adapter ;
    private ArrayList<GridViewItemInfo> datas ;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_center_panel_left,container,false);
        initData();
        initView();
        return view ;
    }

    private void initData() {
        datas = new ArrayList<>() ;
        datas.add(new GridViewItemInfo("New Member",R.mipmap.ico_newmember));
        datas.add(new GridViewItemInfo("View Member",R.mipmap.ico_viewmember));
        datas.add(new GridViewItemInfo("Member Details",R.mipmap.ico_memberdetails));
        datas.add(new GridViewItemInfo("Item Discount",R.mipmap.ico_itemdiscount));
        datas.add(new GridViewItemInfo("Invoice Discount",R.mipmap.ico_invoicediscount));
        datas.add(new GridViewItemInfo("View Stock",R.mipmap.ico_viewstock));
        datas.add(new GridViewItemInfo("Return Product",R.mipmap.ico_returnproduct));
        datas.add(new GridViewItemInfo("Cancel Item",R.mipmap.ico_cancelitem));
        datas.add(new GridViewItemInfo("Pre-order",R.mipmap.ico_preorder));

    }

    private void initView() {
        gridView = (GridView)view.findViewById(R.id.gridView);
        adapter = new GridViewAdapter(getActivity(),datas);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //隐藏主fragment
                MainActivity mainActivity = (MainActivity)getActivity() ;


                if(position==0){
                    mainActivity.hideFragment();
                    //replace Main fragment
                    getFragmentManager().beginTransaction()
                            .add(R.id.container, new NewMemberFragment(),"NewMemberFragment")
                            .addToBackStack(null)   //把事务添加到一个后退栈中
                            .commit();
                }
                if(position==1){
                    mainActivity.hideFragment();
                    //replace Main fragment
                    getFragmentManager().beginTransaction()
                            .add(R.id.container, new ViewMemberFragment(),"ViewMemberFragment")
                            .addToBackStack(null)   //把事务添加到一个后退栈中
                            .commit();
                }
                if(position==2){
                    mainActivity.hideFragment();
                    //replace Main fragment
                    getFragmentManager().beginTransaction()
                            .add(R.id.container, new MemberDetailsFragment(),"MemberDetailsFragment")
                            .addToBackStack(null)   //把事务添加到一个后退栈中
                            .commit();
                }
                else if(position==5){
                    mainActivity.hideFragment();
                    getFragmentManager().beginTransaction()
                            .add(R.id.container, new InventorySearchFragment(),"InventorySearchFragment")
                            .addToBackStack(null)   //把事务添加到一个后退栈中
                            .commit();
                }



            }
        });
    }
}
