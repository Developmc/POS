package com.example.multiable.pos.Fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.multiable.pos.R;

/**
 * Created by macremote on 2015/10/27.
 */
public class HomeFragment extends Fragment {
    private static final String TOP_PANEL_FRAGMENT_TAG = "topPanelFragment";
    private static final String CENTER_PANEL_FRAGMENT_TAG = "centerPanelFragment";
    private static final String RIGHT_PANEL_FRAGMENT_TAG = "rightPanelFragment";

    private View view ;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home,container,false);
        initView() ;
        return view ;
    }

    private void initView() {
        TopPanelFragment topPanelFragment = new TopPanelFragment();
        CenterPanelFragment centerPanelFragment = new CenterPanelFragment();
        RightPanelFragment rightPanelFragment = new RightPanelFragment();

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        //记得commit，多个fragment时，只需commit一次
        fragmentTransaction.add(R.id.topPanelFragment,topPanelFragment,TOP_PANEL_FRAGMENT_TAG);
        fragmentTransaction.add(R.id.centerPanelFragment, centerPanelFragment, CENTER_PANEL_FRAGMENT_TAG);
        fragmentTransaction.add(R.id.rightPanelFragment,rightPanelFragment,RIGHT_PANEL_FRAGMENT_TAG).commit() ;

    }
}
