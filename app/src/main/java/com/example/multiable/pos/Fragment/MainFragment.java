package com.example.multiable.pos.Fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.multiable.pos.CustomView.UpPullMenu;
import com.example.multiable.pos.R;

/**
 * Created by macremote on 2015/10/30.
 */
public class MainFragment extends Fragment {
    private View view ;
    private HomeFragment homeFragment;
    private UpPullMenuFragment upPullMenuFragment;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_main,container,false);
        initView();
        return view;
    }

    private void initView() {
        homeFragment = new HomeFragment();
        getFragmentManager().beginTransaction().add(UpPullMenu.MIDDLE_ID,homeFragment).commit();

        upPullMenuFragment = new UpPullMenuFragment();
        getFragmentManager().beginTransaction().add(UpPullMenu.BOTTOM_ID,upPullMenuFragment).commit();
    }
}
