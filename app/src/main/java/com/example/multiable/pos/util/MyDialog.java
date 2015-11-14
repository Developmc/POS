package com.example.multiable.pos.util;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.example.multiable.pos.CustomView.CustomProgressDialog;

public class MyDialog {

	private static ProgressDialog progressDialog ;
	//自定义进度框
	private static CustomProgressDialog customProgressDialog = null;
	/**
	 * @param context
	 * @param title
	 * @param yes 不需要时设为null
	 * @param no 不需要时设为null
	 */
	public static void dialog(Context context,String title,String yes,String no) 
	{
		Builder builder = new Builder(context,AlertDialog.THEME_DEVICE_DEFAULT_DARK);
		builder.setTitle(title);
		if(yes!=null){
			builder.setPositiveButton(yes, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});
		}
		if(no!=null){
			builder.setNegativeButton(no, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});
		}
		builder.setCancelable(false);
		builder.show();
	}

	/**show progress dialog
	 * @param context
	 * @param title
	 * @param message
	 */
	public static void showProgressDialog(Context context,String title,String message){
		progressDialog = new ProgressDialog(context);  
		if(title!=null&&(!title.equals(""))){
			progressDialog.setTitle(title);
		}
		progressDialog.setMessage(message);  
		progressDialog.show();
	}

	/**
	 * hide progress dialog
	 */
	public static void hideProgressDialog(){
		progressDialog.dismiss();
	}

	/**show custom progress dailog
	 * @param context
	 * @param message
	 */
	public static void showCustomProgressDialog(Context context,String message){
		customProgressDialog = CustomProgressDialog.createDialog(context);
		customProgressDialog.setMessage(message);
		customProgressDialog.show();
		//设置进度条是否可以按退回键取消
//		customProgressDialog.setCancelable(true);
		//设置点击进度对话框外的区域对话框不消失
//		customProgressDialog.setCanceledOnTouchOutside(false);
		
	}

	/**
	 * hide custom progress dialog
	 */
	public static void hideCustomProgressDialog(){
		customProgressDialog.dismiss();
	}
}
