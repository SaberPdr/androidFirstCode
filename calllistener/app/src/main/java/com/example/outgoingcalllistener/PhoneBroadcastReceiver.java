package com.example.outgoingcalllistener;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class PhoneBroadcastReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		String number = getResultData();
		System.out.println("电话号码：  "+number);
		if("5556".equals(number)){
			setResultData(null);//挂断电话
		}else{
			number = "12593"+ number;
			setResultData(number);
		}
	}

}
