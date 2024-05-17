package com.example.myapplicationlo.fragmentMenu;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.Random;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {

            Map<String, String> data = remoteMessage.getData();


            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
                CharSequence name="LemubiReminderChannel";
                String description ="Channel for Lemubi Reminder";
                int importance = NotificationManager.IMPORTANCE_DEFAULT;
                NotificationChannel channel = new NotificationChannel("reservationNotification",name,importance);
                channel.setDescription(description);

                NotificationManager notificationManager = this.getSystemService(NotificationManager.class);
                notificationManager.createNotificationChannel(channel);
            }


            String givenDateString = data.get("body");
            long timeInMilliseconds=0;
            Date oneHourBack=null;

            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdff = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {

                cal.setTime(sdff.parse(givenDateString));
                cal.add(Calendar.DATE, -1);
                oneHourBack = cal.getTime();
                Log.d("timeInMilliseconds",oneHourBack.toString());

            } catch (ParseException e) {
                e.printStackTrace();
            }


            SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");
            try {
                Date mDate = sdf.parse(oneHourBack.toString());
                timeInMilliseconds = mDate.getTime();


            } catch (ParseException e) {
                e.printStackTrace();
            }

            Random random = new Random();
            int id = random.nextInt(9999 - 1000) + 1000;

            Intent intent = new Intent(this, ReminderBroadcast.class);
            intent.putExtra("title", "Remind");
            intent.putExtra("text", "reservation with Doctor "+data.get("title"));
            intent.putExtra("id", id);

            PendingIntent pending = PendingIntent.getBroadcast(this, id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager alarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);

            alarmManager.set(AlarmManager.RTC_WAKEUP,timeInMilliseconds,pending);

        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {

        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }




}
