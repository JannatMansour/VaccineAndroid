package com.example.myapplicationlo.fragmentMenu;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.myapplicationlo.R;

import java.util.Date;

public class ReminderBroadcast extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {

int id;
        NotificationCompat.Builder builder= new NotificationCompat.Builder(context,"reservationNotification")
                .setSmallIcon(R.drawable.ic_baseline_lock_24)
                .setContentTitle(intent.getStringExtra("title"))
                .setContentText(intent.getStringExtra("text"))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager=NotificationManagerCompat.from(context);
        id=intent.getIntExtra("id",0);
        notificationManager.notify(id,builder.build());
    }
}