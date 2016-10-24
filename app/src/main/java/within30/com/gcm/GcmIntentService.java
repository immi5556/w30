package within30.com.gcm;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.json.JSONObject;

import java.util.List;

import within30.com.LandingActivity;
import within30.com.MapsActivity;
import within30.com.R;
import within30.com.dataobjects.CustomerDO;
import within30.com.utilities.W30Database;

/**
 * This {@code IntentService} does the actual handling of the GCM message.
 * {@code GcmBroadcastReceiver} (a {@code WakefulBroadcastReceiver}) holds a
 * partial wake lock for this service while the service does its work. When the
 * service is finished, it calls {@code completeWakefulIntent()} to release the
 * wake lock.
 */
public class GcmIntentService extends IntentService {

    //Project-Number
    private static final String PROJECT_ID = "595437713631";


    int dot = 200;      // Length of a Morse Code "dot" in milliseconds
    int dash = 500;     // Length of a Morse Code "dash" in milliseconds
    int short_gap = 200;    // Length of Gap Between dots/dashes
    int medium_gap = 500;   // Length of Gap Between Letters
    int long_gap = 1000;    // Length of Gap Between Words
    long[] pattern = {
            0,  // Start immediately
            dot, short_gap, dot, short_gap, dot,    // s
            medium_gap,
            dash, short_gap, dash, short_gap, dash, // o
            medium_gap,
            dot, short_gap, dot, short_gap, dot,    // s
            long_gap
    };

    public GcmIntentService() {
        super(PROJECT_ID);

    }


    @Override
    protected void onHandleIntent(Intent intent) {

        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        // The getMessageType() intent parameter must be the intent you received
        // in your BroadcastReceiver.
        try{
            sendNotification(extras.get("gcm.notification.title").toString(), extras.get("gcm.notification.body").toString());
        }catch(Exception e){
            e.printStackTrace();
        }

        // Release the wake lock provided by the WakefulBroadcastReceiver.
        GcmBroadcastReceiver.completeWakefulIntent(intent);

    }

    // Put the message into a notification and post it.
    // This is just one simple example of what you might choose to do with
    // a GCM message.
    private void sendNotification(String title,String data) {



       /* WakeLock screenLock = ((PowerManager) getSystemService(POWER_SERVICE)).newWakeLock(
                PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "TAG");
        screenLock.acquire();*/
        String message = "";
        String type= "";
        try{
            JSONObject jsonObject = new JSONObject(data);
            type = jsonObject.getString("type");
            message = jsonObject.getString("subdomain");
        }catch(Exception e){
            e.printStackTrace();
        }
        if(type.equalsIgnoreCase("newAppointment")){
            if (MapsActivity.is_app_running) {
                Intent i = new Intent("within30.com.USER_ACTION");
                i.putExtra("pushMessageType",type);
                i.putExtra("subdomain",message);
                sendBroadcast(i);
              //  screenLock.release();
                return;
            }
        }
    }


}
