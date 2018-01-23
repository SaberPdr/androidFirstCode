package com.example.incomingcalllistener;

import android.app.Activity;
import android.os.Bundle;
/*
 * 必须有这个MainActivbity，因为在android4.0以上的版本，要求receiver必须由用于自己启动
 */
public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		finish();
	}
}
