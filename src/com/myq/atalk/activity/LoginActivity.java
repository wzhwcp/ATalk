package com.myq.atalk.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.myq.atalk.R;
import com.myq.atalk.bean.CircleImageView;
import com.myq.atalk.util.MyUtil;
import com.myq.atalk.util.SmackUtil;

/**
 * 登录界面，项目启动界面
 * @author quan
 *
 */
public class LoginActivity extends Activity {

	private EditText tv_username;
	private EditText tv_password;
	private TextView tv_register;
	private Button btn_login;
	private CircleImageView iv_head;
	private Handler handler=new Handler(){				//判断是否连接到服务器端
		public void handleMessage(android.os.Message msg) {
			Toast.makeText(LoginActivity.this, msg.what+"", Toast.LENGTH_SHORT).show();;
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.login);
		new Thread(new Runnable() {			//新建一个线程去连接服务器，成功发送1，失败发送0

			@Override
			public void run() {
				// TODO Auto-generated method stub
				if(SmackUtil.connectServer()){
					handler.sendEmptyMessage(1);
				}else{
					handler.sendEmptyMessage(0);
				}
				
			}
		}).start();
		init();
	}

	private void init(){
		tv_username = (EditText) findViewById(R.id.login_username);
		tv_password = (EditText) findViewById(R.id.login_password);
		tv_register = (TextView) findViewById(R.id.login_register);
		btn_login = (Button) findViewById(R.id.login_login);
		iv_head=(CircleImageView)findViewById(R.id.login_headview);
		btn_login.setOnClickListener(new LoginClickListener());
		tv_register.setOnClickListener(new LoginClickListener());
	}
	//登录按钮点击事件
	public class LoginClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.login_login:			//登录并更改头像
				final String username = tv_username.getText().toString();
				final String password = tv_password.getText().toString();
				Bitmap bmp=null;
				if (SmackUtil.login(username, password)) {
					Toast.makeText(LoginActivity.this, "登录成功",
							Toast.LENGTH_SHORT).show();
					if((bmp=SmackUtil.getUserImage(username))!=null){
						iv_head.setImageBitmap(bmp);
					}
					startActivity(new Intent(LoginActivity.this,MainActivity.class));
					finish();
				} else {
					Toast.makeText(LoginActivity.this, "登录失败",
							Toast.LENGTH_SHORT).show();
				}
				break;
			case R.id.login_register:

				break;
			default:
				break;
			}
		}

	}

}
