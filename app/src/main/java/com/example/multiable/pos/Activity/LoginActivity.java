package com.example.multiable.pos.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;

import com.example.multiable.pos.CustomView.LoginInputBoxView;
import com.example.multiable.pos.R;
import com.example.multiable.pos.util.MyMessage;

/**
 * Created by macremote on 2015/11/14.
 */
public class LoginActivity extends Activity {
    private LoginInputBoxView lv_userName,lv_password,lv_ip,lv_port;
    private Button btn_login ;
    private String userName,password,ip,port;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case MyMessage.LOGIN_SUCCESS:
                    break ;
                case MyMessage.LOGIN_FALSE:
                    break;
            }
        }
    } ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView() ;
    }

    private void initView() {
        lv_userName = (LoginInputBoxView)findViewById(R.id.userName);
        lv_password = (LoginInputBoxView)findViewById(R.id.password);
        //设置为密码类型
        lv_password.setPassword();
        lv_ip = (LoginInputBoxView)findViewById(R.id.ip);
        lv_port = (LoginInputBoxView)findViewById(R.id.port) ;
        btn_login=(Button)findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //check data frist
                checkData();
                //start thread
            }
        });
    }
    public void checkData(){
        //TODO
        userName = lv_userName.getText().toString().trim();
        password = lv_password.getText().toString().trim();
        ip = lv_ip.getText().toString().trim();
        port = lv_port.getText().toString().trim() ;
    }
    private class LoginThread extends Thread{
        @Override
        public void run() {
            super.run();

        }
    }
}
