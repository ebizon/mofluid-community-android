package com.mofluid.magento2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.ebizon.fluid.Utils.MySharedPreferences;

public class SplashActivity extends Activity{

	String ID;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ID= getIntent().getStringExtra("StoreID");
		setContentView(R.layout.activity_splash);
		long delayMillis = 3000;
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
            if(MySharedPreferences.getInstance().get(MySharedPreferences.isLogedin)!=null && MySharedPreferences.getInstance().get(MySharedPreferences.isLogedin).equalsIgnoreCase("true"))
             	gotoMainActivity();
             else
             	gotoLoginDemo();
				
			}
		}, delayMillis);
	}
	private void gotoMainActivity(){
		Intent intent=new Intent(SplashActivity.this, com.mofluid.magento2.MainActivity.class);
		if(ID!=null)
		intent.putExtra("StoreID",ID);
		SplashActivity.this.startActivity(intent);
		SplashActivity.this.finish();
	}
	private void gotoLoginDemo(){
		Intent intent=new Intent(SplashActivity.this, com.mofluid.magento2.LoginDemo.class);
		if(ID!=null)
		intent.putExtra("StoreID",ID);
		SplashActivity.this.startActivity(intent);
		SplashActivity.this.finish();
	}

}
