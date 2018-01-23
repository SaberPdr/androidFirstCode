package com.bearapp.calllistener;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

public class CallListenerService extends Service {
    private static final String TAG = "CallListenerService";
    private TelephonyManager tm;
    private MyPhoneStateListener listener;

    public CallListenerService() {
    }


    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate>>>>>>>");
        // 获取电话管理器
        tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        // 监听电话状态
        listener = new MyPhoneStateListener();
        tm.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
    }


    private class MyPhoneStateListener extends PhoneStateListener {
        AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        int oldRingMode = -1;
        int oldVolume = -1;

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            boolean open = PreferenceManager
                    .getDefaultSharedPreferences(getApplicationContext())
                    .getBoolean("pref_global_switch", true);
            if (!open) {
                return;
            }
            String listenContact = PreferenceManager
                    .getDefaultSharedPreferences(getApplicationContext())
                    .getString("pref_listen_contact", "");
            if (!listenContact.replaceAll("-", "").replaceAll(" ", "").contains(incomingNumber)) {
                return;
            }

            switch (state) {
                case TelephonyManager.CALL_STATE_RINGING:   //响铃
                    Toast.makeText(getApplicationContext(), String.format(getString(R.string.who_call_in), incomingNumber), Toast.LENGTH_SHORT).show();
                    int ringMode = audioManager.getRingerMode();
//                    Toast.makeText(getApplicationContext(), "ringMode=" + ringMode, Toast.LENGTH_SHORT).show();
                    int volume = audioManager.getStreamVolume(AudioManager.STREAM_RING);
                    int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_RING);
//                    Toast.makeText(getApplicationContext(), "volume=" + volume + ", maxVolume=" + maxVolume, Toast.LENGTH_SHORT).show();
                    if (ringMode == AudioManager.RINGER_MODE_SILENT || ringMode == AudioManager.RINGER_MODE_VIBRATE || volume == 0) {
                        oldVolume = volume;
                        oldRingMode = ringMode;
                        audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                        audioManager.setStreamVolume(AudioManager.STREAM_RING, maxVolume / 3, AudioManager.FLAG_PLAY_SOUND);
                    }

                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:   //接听
//                    Toast.makeText(getApplicationContext(), "接听" + incomingNumber, Toast.LENGTH_SHORT).show();
                    if (oldRingMode > -1) {
                        audioManager.setRingerMode(oldRingMode);
                    }
                    if (oldVolume > -1) {
                        audioManager.setStreamVolume(AudioManager.STREAM_RING, oldVolume, AudioManager.FLAG_PLAY_SOUND);
                    }
                    break;
                case TelephonyManager.CALL_STATE_IDLE:      //空闲
//                    Toast.makeText(getApplicationContext(), "挂断" + incomingNumber, Toast.LENGTH_SHORT).show();
                    if (oldRingMode > -1) {
                        audioManager.setRingerMode(oldRingMode);
                    }
                    if (oldVolume > -1) {
                        audioManager.setStreamVolume(AudioManager.STREAM_RING, oldVolume, AudioManager.FLAG_PLAY_SOUND);
                    }
                    break;
            }

        }
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand>>>>>>>");
        // 设置为前台服务
        Intent notifyIntent = new Intent(this, SettingsActivity.class);
        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent notifyPendingIntent = PendingIntent.getActivity(this, 1, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new NotificationCompat.Builder(getApplicationContext())
                .setSmallIcon(R.drawable.ic_notification_ring)
                .setColor(getResources().getColor(R.color.green))
                .setContentTitle(getString(R.string.notification_title))
                .setContentIntent(notifyPendingIntent)
                .build();
        startForeground(1, notification);
        return START_REDELIVER_INTENT;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        stopForeground(true);
        tm.listen(listener, PhoneStateListener.LISTEN_NONE);
        listener = null;
    }
}
