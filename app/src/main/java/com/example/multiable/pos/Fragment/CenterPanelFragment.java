package com.example.multiable.pos.Fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.multiable.pos.CustomView.LeftMenu;
import com.example.multiable.pos.R;

/**
 * Created by macremote on 2015/10/27.
 */
public class CenterPanelFragment extends Fragment {
    private View view;
//    private LeftMenu leftMenuLayout ;
    private CenterPanelContentFragment centerPanelContentFragment ;
    private CenterPanelLeftFragment centerPanelLeftFragment;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_center_panel,container,false);
//        leftMenuLayout = (LeftMenu)view.findViewById(R.id.leftMenu) ;

        initView() ;
        return view;
    }

    private void initView() {
        centerPanelContentFragment = new CenterPanelContentFragment();
        centerPanelLeftFragment = new CenterPanelLeftFragment() ;
        FragmentManager fragmentManager = getFragmentManager() ;
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(LeftMenu.MIDDLE_MENU_ID,centerPanelContentFragment) ;
        fragmentTransaction.add(LeftMenu.LEFT_MENU_ID,centerPanelLeftFragment).commit() ;
    }
}
