package com.rumshaaamir.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;
import java.util.Calendar;

/**
 * Created by Rumsha Aamir on 1/24/2018.
 */
public class BroadCastReceiverAlarm extends BroadcastReceiver {
    private String TAG = "BroadCastReceiverAlarm";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "BroadCastReceiverAlarm called!");

        //Toast.makeText(context, TAG+" called!", Toast.LENGTH_SHORT).show();

        String alarmType = intent.getStringExtra("type");
        int id = intent.getIntExtra("index", 0);
        //Toast.makeText(context,alarmType,Toast.LENGTH_SHORT).show();

        Calendar calendar = Calendar.getInstance();

        Intent intent_startAcivity = new Intent(context, ActivitySimpleAlarm.class);
        intent_startAcivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent_startAcivity.putExtra("index", id);
        intent_startAcivity.putExtra("calender", calendar);
        context.startActivity(intent_startAcivity);
    }
}