package com.example.multiable.pos.Fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.multiable.pos.Adapter.UpPullTypeAdapter;
import com.example.multiable.pos.Adapter.UpPullTypeChildAdapter;
import com.example.multiable.pos.R;

/**
 * Created by macremote on 2015/10/27.
 */
public class UpPullMenuFragment extends Fragment {
    private View view ;
    private GridView gridViewLeft ;
    private GridView gridViewRight;
    private UpPullTypeAdapter adapterLeft ;
    private UpPullTypeChildAdapter adapterRight ;
    private boolean[] leftArray ;
    private boolean[] rightArray ;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_up_pull_menu,container,false);
        initData() ;
        initView();
        return view ;
    }

    private void initData() {
        leftArray = new boolean[40] ;
        rightArray = new boolean[20] ;
        for (int i=0;i<leftArray.length;i++) {
            leftArray[i] = false ;
        }
        for(int i=0;i<rightArray.length;i++){
            rightArray[i] = false ;
        }
    }

    private void initView() {
        gridViewLeft=(GridView)view.findViewById(R.id.gridViewLeft);
        gridViewRight=(GridView)view.findViewById(R.id.gridViewRight);
        adapterLeft = new UpPullTypeAdapter(getActivity(),leftArray);
        adapterRight = new UpPullTypeChildAdapter(getActivity(),rightArray);
        gridViewLeft.setAdapter(adapterLeft);
        gridViewLeft.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapterLeft.setLeftArray(position);
                adapterLeft.notifyDataSetChanged();
            }
        });

        gridViewRight.setAdapter(adapterRight);
        gridViewRight.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapterRight.setRightArray(position);
                // 重绘刷新视图
                adapterRight.notifyDataSetChanged();
            }
        });
    }
}
