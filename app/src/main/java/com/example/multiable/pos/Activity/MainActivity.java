package com.example.multiable.pos.Activity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.example.multiable.pos.Fragment.MainFragment;
import com.example.multiable.pos.R;

/**
 * 版本在3.0以上，故使用activity而不是fragmentActivity
 */
public class MainActivity extends FragmentActivity {
    private MainFragment mainFragment ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //获取设备的 smallest width
        Configuration config = getResources().getConfiguration();
        int smallestScreenWidth = config.smallestScreenWidthDp;
        Log.d("smallest width :" , smallestScreenWidth+"");
        setContentView(R.layout.activity_main);
        initView();

    }

    private void initView() {
        mainFragment = new MainFragment() ;
        getFragmentManager().beginTransaction().add(R.id.container,mainFragment).commit();

    }

    /**显示fragment
     * 保存主fragment的状态(通过隐藏显示实现保存)
     */
    public void showFragment(){
        if(mainFragment==null){
            mainFragment = new MainFragment() ;
            getFragmentManager().beginTransaction().add(R.id.container,mainFragment).commit();
        }
        else{
            getFragmentManager().beginTransaction().show(mainFragment).commit() ;
        }
    }

    /**
     * 隐藏fragment
     */
    public void hideFragment(){
        if(mainFragment!=null){
            getFragmentManager().beginTransaction().hide(mainFragment).commit() ;
        }
    }


//    /***************************************************************/
//    /**
//     * 在父类Activity中，构造一个回调接口，并向外提供注册和销毁该回调接口的方法。
//     * 然后在Activity的dispatchTouchEvent函数中遍历所有注册了该回调接口的对象，分发onTouchEvent事件
//     */
//    //触摸事件回调接口
//    public interface MyTouchListener
//    {
//        public boolean onTouchEvent(MotionEvent event);
//    }
//
//    /*
//    * 保存MyTouchListener接口的列表
//    */
//    private ArrayList<MyTouchListener> myTouchListeners = new ArrayList<MainActivity.MyTouchListener>();
//
//    /**
//     * 提供给Fragment通过getActivity()方法来注册自己的触摸事件的方法
//     * @param listener
//     */
//    public void registerMyTouchListener(MyTouchListener listener)
//    {
//        myTouchListeners.add( listener );
//    }
//
//    /**
//     * 提供给Fragment通过getActivity()方法来取消注册自己的触摸事件的方法
//     * @param listener
//     */
//    public void unRegisterMyTouchListener(MyTouchListener listener)
//    {
//        myTouchListeners.remove(listener);
//    }
//
//    /**
//     * 分发触摸事件给所有注册了MyTouchListener的接口
//     */
//    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {
//        // TODO Auto-generated method stub
//        for (MyTouchListener listener : myTouchListeners) {
//            listener.onTouchEvent(ev);
//        }
//        return super.dispatchTouchEvent(ev);
//    }
//    /***************************************************************/

}
