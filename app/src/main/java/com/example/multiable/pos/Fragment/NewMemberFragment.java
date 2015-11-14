package com.example.multiable.pos.Fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.multiable.pos.Activity.MainActivity;
import com.example.multiable.pos.R;

public class NewMemberFragment extends Fragment {
    private ImageView iv_sure ,iv_cancel;
    private View view ;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_new_member,container,false) ;
        initView() ;
        return view ;
    }

    private void initView() {
        iv_sure = (ImageView)view.findViewById(R.id.iv_sure);
        iv_cancel = (ImageView)view.findViewById(R.id.iv_cancel);
        iv_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        iv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //移除当前fragment
                getFragmentManager().beginTransaction().remove(getFragmentManager().
                        findFragmentByTag("NewMemberFragment")).commit();
                //显示隐藏的主fragment
                MainActivity mainActivity = (MainActivity)getActivity();
                mainActivity.showFragment();
            }
        });
    }


}
