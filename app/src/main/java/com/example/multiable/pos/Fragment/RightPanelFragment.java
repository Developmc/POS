package com.example.multiable.pos.Fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.multiable.pos.CustomView.LeftMenu;
import com.example.multiable.pos.Activity.MainActivity;
import com.example.multiable.pos.R;

/**
 * Created by macremote on 2015/10/27.
 */
public class RightPanelFragment extends Fragment {
    private View view ;
    private ImageView bt_pay,bt_memberDetails ;
    //pay和function切换时文字改变
    private TextView tv_pay_function ;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_right_panel,container,false) ;
        initView() ;

        return view;
    }

    private void initView() {
        bt_pay = (ImageView)view.findViewById(R.id.bt_pay);
        tv_pay_function = (TextView)view.findViewById(R.id.tv_pay_function) ;
        tv_pay_function.setGravity(View.TEXT_ALIGNMENT_CENTER);
        bt_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tv_pay_function.getText().toString().equals(getString(R.string.payment))){
                    //点击pay，将centerPanelContentFragment切换为payFragment(直接fragment通信，不可取)
                    PayFragment payFragment = new PayFragment();
                    FragmentManager fragmentManager = getFragmentManager() ;
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(LeftMenu.MIDDLE_MENU_ID,payFragment).commit();
                    //切换显示的文字
                    tv_pay_function.setText(getString(R.string.shopping));
                }
                else{
                    CenterPanelContentFragment centerPanelContentFragment =
                            new CenterPanelContentFragment() ;
                    getFragmentManager().beginTransaction().replace(
                            LeftMenu.MIDDLE_MENU_ID,centerPanelContentFragment).commit() ;
                    //切换显示的文字
                    tv_pay_function.setText(getString(R.string.payment));

                }
            }
        });
        bt_memberDetails=(ImageView)view.findViewById(R.id.bt_memberDetails) ;
        bt_memberDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //replace Main fragment
                getFragmentManager().beginTransaction()
                        .add(R.id.container, new MemberDetailsFragment(), "MemberDetailsFragment")
                        .addToBackStack(null)
                        .commit() ;
                //隐藏主界面
                MainActivity mainActivity = (MainActivity)getActivity();
                mainActivity.hideFragment();
            }
        });
    }
}
