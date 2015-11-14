package com.example.multiable.pos.CustomView;

import android.content.Context;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.multiable.pos.R;

public class LoginInputBoxView extends LinearLayout{
	private ImageView box,clear ;
	private EditText editText ;

	public LoginInputBoxView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public LoginInputBoxView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    inflater.inflate(R.layout.custom_login_inputbox, this) ;
	    box = (ImageView)findViewById(R.id.box) ;
	    clear = (ImageView)findViewById(R.id.clear) ;
	    editText = (EditText)findViewById(R.id.editText) ;
	    
	    clear.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				editText.setText("");
			}
		});
	    //输入框焦点监听
	    editText.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if(hasFocus)
				{
					clear.setVisibility(View.VISIBLE);
				}
				else {
					clear.setVisibility(View.GONE);
				}
			}
		});
	}
	
	/**设置图片
	 * @param resId
	 */
	public void setImageView(int resId)
	{
		box.setImageResource(resId);
	}
	/**设置提示
	 * @param resId
	 */
	public void setHint(int resId)
	{
		editText.setHint(resId);
	}
	/**设置是否为密码
	 * @param resId
	 */
	public void setPassword()
	{
		editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
	}
    /**设置文字
     * @return
     */
    public String getText()
    {
    	return editText.getText().toString() ;
    }
    public void setText(int resId)
    {
    	editText.setText(resId);
    }
    public void setText(CharSequence text)
    {
    	editText.setText(text);
    }
}
