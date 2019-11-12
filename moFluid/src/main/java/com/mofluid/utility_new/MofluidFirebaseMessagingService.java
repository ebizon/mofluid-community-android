package com.mofluid.utility_new;

import android.app.NotificationManager;
import android.content.Context;
import android.media.RingtoneManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.mofluid.magento2.R;

public class MofluidFirebaseMessagingService extends FirebaseMessagingService{
    private final String TAG="MofluidFirebaseMessagingService";
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        L.d(TAG,"message received");
        L.d(TAG,"message="+remoteMessage);
        showNotification(remoteMessage.getNotification().getBody());
    }

    @Override
    public void onDeletedMessages() {
        super.onDeletedMessages();
        L.d(TAG,"message deleted");
    }

    @Override
    public void onMessageSent(String s) {
        super.onMessageSent(s);
        L.d(TAG,"message sent");
    }

    @Override
    public void onSendError(String s, Exception e) {
        super.onSendError(s, e);
        L.d(TAG,"error received");
        L.d(TAG,"error="+e.getMessage());
    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        this.registerAppID(s);
        L.d(TAG,"new token="+s);
    }
    private void showNotification(String message){
        L.d(TAG,"shownotification message="+message);
       Uri sound_uri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notification=new NotificationCompat.Builder(this,"id");
        notification.setAutoCancel(true)
                     .setSmallIcon(R.drawable.appicon)
                      .setContentTitle("sample")
                      .setContentText(message)
                      .setSound(sound_uri);
        NotificationManager notificationManager=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0,notification.build());
    }
private void registerAppID(String token){
String deviceID= Settings.Secure.getString(this.getContentResolver(),Settings.Secure.ANDROID_ID);
String url=URLManager.getRegisterDeiceforPushURL(deviceID);
ResponseManager manager=new ResponseManager(ResponseManager.REQUEST_TYPE_GET, url, null, null, new ResponseReceived() {
    @Override
    public void onResponseReceived(Object response, int response_code) {
                L.d(TAG,"response="+response);
    }
});
manager.get();
}
}
