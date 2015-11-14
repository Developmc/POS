package com.example.multiable.pos.Fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.multiable.pos.Activity.MainActivity;
import com.example.multiable.pos.R;

/**
 * Created by macremote on 2015/11/12.
 */
public class MemberDetailsFragment extends Fragment {
    private View view ;
    private ImageView iv_cancel ;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_member_datails,container,false);
        initView() ;
        return view;
    }

    private void initView() {
        iv_cancel = (ImageView)view.findViewById(R.id.iv_cancel) ;
        iv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //移除当前的fragment
                getFragmentManager().beginTransaction().remove(getFragmentManager().findFragmentByTag(
                        "MemberDetailsFragment")).commit() ;
                //显示隐藏的主fragment
                MainActivity mainActivity = (MainActivity)getActivity();
                mainActivity.showFragment();
            }
        });
    }
}