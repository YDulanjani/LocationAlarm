package yamuna.com.locationalarm;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

//Service for Alarm Task


public class AlarmService extends IntentService {
    private NotificationManager alarmNotificationManager;
    private String msg;
    public AlarmService() {
        super("AlarmService");
        this.msg=msg;
    }


    public AlarmService(String msg) {
        super("AlarmService");
        this.msg=msg;
    }

    @Override
    public void onHandleIntent(Intent intent) {
        sendNotification("You have Notifications Go to LocationAlarm");
    }

    private void sendNotification(String msg) {
        Log.d("AlarmService", "Preparing to send notification...: " + msg);
        alarmNotificationManager = (NotificationManager) this
                .getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, MainActivity.class), 0);

        NotificationCompat.Builder alamNotificationBuilder = new NotificationCompat.Builder(
                this).setContentTitle("Alarm").setSmallIcon(R.mipmap.ic_launcher)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
                .setContentText(msg);


        PendingIntent notIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, MainActivity.class), 0);

        alamNotificationBuilder.addAction(R.mipmap.ic_launcher,"Stop",contentIntent);

        alamNotificationBuilder.setContentIntent(contentIntent);
        alarmNotificationManager.notify(1, alamNotificationBuilder.build());

        Log.d("AlarmService", "Notification sent.");
    }


}


/*
public class AlarmService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }




    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Let it continue running until it is stopped.

        Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        if (alarmUri == null) {
            alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        }
        Ringtone ringtone = RingtoneManager.getRingtone(this.getBaseContext(), alarmUri);
        if(ringtone.isPlaying()){
            ringtone.stop();
        }else{
            ringtone.play();
        }


        Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();


        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
    }
}*/





