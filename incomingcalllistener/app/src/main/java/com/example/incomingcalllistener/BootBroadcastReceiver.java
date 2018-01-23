package com.example.incomingcalllistener;

import android.content.BroadcastReceiver;
import android.util.Log;
import android.widget.Toast;
import android.content.Context;
import android.content.Intent;

public class BootBroadcastReceiver extends BroadcastReceiver {
	  // 系统启动完成  
    static final String ACTION = "android.intent.action.BOOT_COMPLETED"; 
	@Override
	public void onReceive(Context context, Intent intent) {
		Log.d("TAG1","开机自启动服务");

//		 if (intent.getAction().equals(ACTION)) { 
		Intent service = new Intent(context, PhoneService.class);//显式
		service.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
		context.startService(service);//Intent激活组件(Service)
		 }
//	}

}
