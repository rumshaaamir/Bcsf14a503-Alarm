package com.rumshaaamir.alarm;

import android.app.Activity;
import android.app.KeyguardManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

/**
 * Created by Rumsha Aamir on 1/24/2018.
 */
public class ActivitySimpleAlarm extends AppCompatActivity {
    private String TAG="TAG_ServiceSimpleAlarm";

    private DBAdapter_alarmdata dbAdapter_alarmdata;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simplealarm);

        String title="Alarm Ringing";
        SpannableString s = new SpannableString(title);
        s.setSpan(new ForegroundColorSpan(Color.DKGRAY), 0, title.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        getSupportActionBar().setTitle(s);

        final TextView time_tv=(TextView)findViewById(R.id.time_textView_simpleaa);

        dbAdapter_alarmdata=new DBAdapter_alarmdata(this);

        setupData();

        //Toast.makeText(this, TAG, Toast.LENGTH_SHORT).show();

        Bundle bundle=getIntent().getExtras();
        final Calendar calendar= (Calendar) bundle.get("calender");
        //final int index=bundle.getInt("index");

        //Toast.makeText(this, index+" ", Toast.LENGTH_SHORT).show();

        PowerManager pm = (PowerManager) getApplicationContext().getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wakeLock = pm.newWakeLock((PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP), "TAG");
        wakeLock.acquire();

        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        window.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        /*KeyguardManager keyguardManager = (KeyguardManager) getApplicationContext().getSystemService(Context.KEYGUARD_SERVICE);
        KeyguardManager.KeyguardLock keyguardLock = keyguardManager.newKeyguardLock("TAG");
        keyguardLock.disableKeyguard();*/


        KeyguardManager keyguardManager = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
        KeyguardManager.KeyguardLock keyguardLock = keyguardManager.newKeyguardLock("TAG");
        keyguardLock.disableKeyguard();
        runOnUiThread(new Runnable(){
            public void run(){
                getWindow().addFlags(
                        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
            }
        });
        final Intent intent_startService=new Intent(this,ServiceSimpleAlarm.class);
        //intent_startService.putExtra(TAG_ServiceSimpleAlarm,1);
        startService(intent_startService);

        String time=null;

        final Date dateCurrent=calendar.getTime();

        if(MainActivity.TimeFormate(getApplicationContext())==24) {
            time=setTime_24Formate(dateCurrent.getHours(),dateCurrent.getMinutes());
            time_tv.setText(time);
        }else if(MainActivity.TimeFormate(getApplicationContext())==12) {
            time=setTime_12Formate(dateCurrent.getHours(),dateCurrent.getMinutes());
            time_tv.setText(time);
        }

        Button button_stopAlarm=(Button)findViewById(R.id.stopAlarm_btn);
        button_stopAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopService(intent_startService);


                //Toast.makeText(getApplicationContext(), "-99", Toast.LENGTH_SHORT).show();

                /*AlarmData alarmData=AlaramData_singletonClass.get(getApplicationContext()).getList_alarmData().get(index);
                alarmData.setOnOff(0);
                AlaramData_singletonClass.get(getApplicationContext()).getList_alarmData().remove(index);
                AlaramData_singletonClass.get(getApplicationContext()).getList_alarmData().add(alarmData);*/



                Date date;
                //Toast.makeText(ActivitySimpleAlarm.this, date.getHours()+":"+date.getMinutes(), Toast.LENGTH_SHORT).show();

                /*if(AlaramData_singletonClass.get(getApplicationContext()).getList_alarmData()==null) {
                    Toast.makeText(ActivitySimpleAlarm.this, "null", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(ActivitySimpleAlarm.this, "not null", Toast.LENGTH_SHORT).show();
                }*/




                for(AlarmData alarmData:AlaramData_singletonClass.get(getApplicationContext()).getList_alarmData()) {
                    date=alarmData.getAlarmTime();

                    //Toast.makeText(ActivitySimpleAlarm.this, "in", Toast.LENGTH_SHORT).show();
                    //Toast.makeText(ActivitySimpleAlarm.this, date.getHours()+":"+date.getMinutes(), Toast.LENGTH_SHORT).show();
                    //Toast.makeText(ActivitySimpleAlarm.this, dateCurrent.getHours()+":"+dateCurrent.getMinutes(), Toast.LENGTH_SHORT).show();
                    /*if(date.getHours()==dateCurrent.getHours() && date.getMinutes()==dateCurrent.getMinutes()) {
                        Toast.makeText(ActivitySimpleAlarm.this, "in", Toast.LENGTH_SHORT).show();
                    }*/

                    if(date.getHours()==dateCurrent.getHours() && date.getMinutes()==dateCurrent.getMinutes()) {

                        //Toast.makeText(ActivitySimpleAlarm.this, "in", Toast.LENGTH_SHORT).show();
                        //Toast.makeText(ActivitySimpleAlarm.this, date.getHours()+":"+date.getMinutes(), Toast.LENGTH_SHORT).show();
                        //Toast.makeText(ActivitySimpleAlarm.this, dateCurrent.getHours()+":"+dateCurrent.getMinutes(), Toast.LENGTH_SHORT).show();

                        AlarmData alarmData_updated = new AlarmData();
                        alarmData_updated = alarmData;
                        alarmData_updated.setOnOff(0);

                        //AlaramData_singletonClass.get(getApplicationContext()).g
                        AlaramData_singletonClass.get(getApplicationContext()).getList_alarmData().remove(alarmData);
                        AlaramData_singletonClass.get(getApplicationContext()).getList_alarmData().add(alarmData_updated);

                        /*DBAdapter_alarmdata dbAdapter_alarmdata=new DBAdapter_alarmdata(getApplicationContext());
                        dbAdapter_alarmdata.open();
                        dbAdapter_alarmdata.updateAlarmBasic(alarmData.getId()+1,0,alarmData.getAlarmType(),alarmData.getRingtonePatth(),alarmData.getAlarmTitle());
                        dbAdapter_alarmdata.close();*/

                        //AlaramData_singletonClass.get(getApplicationContext()).getList_alarmData().remove(alarmData);
                        //AlaramData_singletonClass.get(getApplicationContext()).getList_alarmData().clear();

                        resetDataBase();
                        updateDatabase();
                        AlaramData_singletonClass.get(getApplicationContext()).getList_alarmData().clear();
                        restoreDate();

                        //Toast.makeText(ActivitySimpleAlarm.this, alarmData_updated.getId()+" ", Toast.LENGTH_SHORT).show();

                        /*MainActivity.mpendingIntent= PendingIntent.getBroadcast(getApplicationContext(), alarmData_updated.getId(),
                                MainActivity.malarmIntent,0);
                        if(MainActivity.mpendingIntent!=null) {
                            MainActivity.mpendingIntent.cancel();
                            MainActivity.malarmManager.cancel(MainActivity.mpendingIntent);

                            //Toast.makeText(ActivitySimpleAlarm.this, "enter", Toast.LENGTH_SHORT).show();
                        }*/

                        System.exit(0);
                    }
                }

                //intent_startService.putExtra(TAG_ServiceSimpleAlarm,0);
                //startService(intent_startService);
                //stopService(intent_startService);

            }
        });



    }

    private void setupData() {
        if(AlaramData_singletonClass.get(getApplicationContext()).getList_alarmData().size()!=0) {

            resetDataBase();
        }
        updateDatabase();
        AlaramData_singletonClass.get(getApplicationContext()).getList_alarmData().clear();
        restoreDate();
    }

    private void resetDataBase() {
        dbAdapter_alarmdata.open();

        dbAdapter_alarmdata.updateAlarmBasic(1,-1,"0","0","0");
        dbAdapter_alarmdata.updateAlarmBasic(2,-1,"0","0","0");
        dbAdapter_alarmdata.updateAlarmBasic(3,-1,"0","0","0");;
        dbAdapter_alarmdata.updateAlarmBasic(4,-1,"0","0","0");
        dbAdapter_alarmdata.updateAlarmBasic(5,-1,"0","0","0");;
        dbAdapter_alarmdata.updateAlarmBasic(6,-1,"0","0","0");
        dbAdapter_alarmdata.updateAlarmBasic(7,-1,"0","0","0");
        dbAdapter_alarmdata.updateAlarmBasic(8,-1,"0","0","0");
        dbAdapter_alarmdata.updateAlarmBasic(9,-1,"0","0","0");
        dbAdapter_alarmdata.updateAlarmBasic(10,-1,"0","0","0");

        dbAdapter_alarmdata.updateAlarmDays(1,-1,-1,-1,-1,-1,-1,-1);
        dbAdapter_alarmdata.updateAlarmDays(2,-1,-1,-1,-1,-1,-1,-1);
        dbAdapter_alarmdata.updateAlarmDays(3,-1,-1,-1,-1,-1,-1,-1);
        dbAdapter_alarmdata.updateAlarmDays(4,-1,-1,-1,-1,-1,-1,-1);
        dbAdapter_alarmdata.updateAlarmDays(5,-1,-1,-1,-1,-1,-1,-1);
        dbAdapter_alarmdata.updateAlarmDays(6,-1,-1,-1,-1,-1,-1,-1);
        dbAdapter_alarmdata.updateAlarmDays(7,-1,-1,-1,-1,-1,-1,-1);
        dbAdapter_alarmdata.updateAlarmDays(8,-1,-1,-1,-1,-1,-1,-1);
        dbAdapter_alarmdata.updateAlarmDays(9,-1,-1,-1,-1,-1,-1,-1);
        dbAdapter_alarmdata.updateAlarmDays(10,-1,-1,-1,-1,-1,-1,-1);

        dbAdapter_alarmdata.updateAlarmSnooze(1,-1,-1);
        dbAdapter_alarmdata.updateAlarmSnooze(2,-1,-1);
        dbAdapter_alarmdata.updateAlarmSnooze(3,-1,-1);
        dbAdapter_alarmdata.updateAlarmSnooze(4,-1,-1);
        dbAdapter_alarmdata.updateAlarmSnooze(5,-1,-1);
        dbAdapter_alarmdata.updateAlarmSnooze(6,-1,-1);
        dbAdapter_alarmdata.updateAlarmSnooze(7,-1,-1);
        dbAdapter_alarmdata.updateAlarmSnooze(8,-1,-1);
        dbAdapter_alarmdata.updateAlarmSnooze(9,-1,-1);
        dbAdapter_alarmdata.updateAlarmSnooze(10,-1,-1);

        dbAdapter_alarmdata.updateAlarmDate(1,-1,-1,-1);
        dbAdapter_alarmdata.updateAlarmDate(2,-1,-1,-1);
        dbAdapter_alarmdata.updateAlarmDate(3,-1,-1,-1);
        dbAdapter_alarmdata.updateAlarmDate(4,-1,-1,-1);
        dbAdapter_alarmdata.updateAlarmDate(5,-1,-1,-1);
        dbAdapter_alarmdata.updateAlarmDate(6,-1,-1,-1);
        dbAdapter_alarmdata.updateAlarmDate(7,-1,-1,-1);
        dbAdapter_alarmdata.updateAlarmDate(8,-1,-1,-1);
        dbAdapter_alarmdata.updateAlarmDate(9,-1,-1,-1);
        dbAdapter_alarmdata.updateAlarmDate(10,-1,-1,-1);

        dbAdapter_alarmdata.updateAlarmTime(1,-1,-1);
        dbAdapter_alarmdata.updateAlarmTime(2,-1,-1);
        dbAdapter_alarmdata.updateAlarmTime(3,-1,-1);
        dbAdapter_alarmdata.updateAlarmTime(4,-1,-1);
        dbAdapter_alarmdata.updateAlarmTime(5,-1,-1);
        dbAdapter_alarmdata.updateAlarmTime(6,-1,-1);
        dbAdapter_alarmdata.updateAlarmTime(7,-1,-1);
        dbAdapter_alarmdata.updateAlarmTime(8,-1,-1);
        dbAdapter_alarmdata.updateAlarmTime(9,-1,-1);
        dbAdapter_alarmdata.updateAlarmTime(10,-1,-1);

        dbAdapter_alarmdata.close();
    }

    private void updateDatabase() {
        int id, onOff;
        String type, path, title;

        int mon=-1, tue=-1, wed=-1, thur=-1, fri=-1, sat=-1, sun=-1;
        String[] days=new String[7];
        for (int i=0;i<7;i++) {
            days[i]=null;
        }

        int notime, min;
        int[] snooze=new int[2];

        int day, month, year;
        Calendar calendar;

        int hour, minutes;
        Date date;

        boolean status;

        for (AlarmData alarmData:AlaramData_singletonClass.get(getApplicationContext()).getList_alarmData()) {

            //readData(alarmData);

            DBAdapter_alarmdata dbAdapter_alarmdata=new DBAdapter_alarmdata(getApplicationContext());

            dbAdapter_alarmdata.open();

            id=alarmData.getId();
            //Toast.makeText(getApplicationContext(), id+" a", Toast.LENGTH_SHORT).show();
            id=id+1;
            //Toast.makeText(getApplicationContext(),id+" b",Toast.LENGTH_SHORT).show();
            onOff=alarmData.getOnOff();
            type=alarmData.getAlarmType();
            path=alarmData.getRingtonePatth();
            //Toast.makeText(this, path, Toast.LENGTH_SHORT).show();
            if(path==null) {
                path="null";
            }
            title=alarmData.getAlarmTitle();
            status=dbAdapter_alarmdata.updateAlarmBasic(id,onOff,type,path,title);
            if(status) {
                Log.i(TAG,"Alarm Basic updated!");
            }else {
                Log.i(TAG,"Alarm Basic not updated!");
            }

            date=alarmData.getAlarmTime();
            hour=date.getHours();
            minutes=date.getMinutes();
            status=dbAdapter_alarmdata.updateAlarmTime(id,hour,minutes);
            if(status) {
                Log.i(TAG,"Alarm time updated!");
            }else {
                Log.i(TAG,"Alarm time not updated!");
            }

            snooze=alarmData.getAlarmSnooze();
            notime=snooze[0];
            min=snooze[1];
            status=dbAdapter_alarmdata.updateAlarmSnooze(id,notime,min);
            if(status) {
                Log.i(TAG,"Alarm snooze updated!");
            }else {
                Log.i(TAG,"Alarm snooze not updated!");
            }

            days=alarmData.getAlarmDays();
            /*for(int i=0;i<7;i++) {
                Toast.makeText(getApplicationContext(),days[i],Toast.LENGTH_SHORT).show();
            }*/
            if(days[0]==null && days[1]==null && days[2]==null && days[3]==null && days[4]==null && days[5]==null
                    && days[6]==null) {

                //Toast.makeText(getApplicationContext(),"days null",Toast.LENGTH_SHORT).show();
                calendar=alarmData.getAlarmDate();
                day=calendar.get(Calendar.DAY_OF_MONTH);
                month=calendar.get(Calendar.MONTH);
                year=calendar.get(Calendar.YEAR);
                status=dbAdapter_alarmdata.updateAlarmDate(id,day,month,year);
                if(status) {
                    Log.i(TAG,"Alarm date updated!");
                }else {
                    Log.i(TAG,"Alarm date not updated!");
                }

            }else {
                //Toast.makeText(getApplicationContext(),"days not null",Toast.LENGTH_SHORT).show();

                for(int i=0;i<7;i++) {
                    if(Objects.equals(days[i], "Monday")) {
                        //Toast.makeText(getApplicationContext(),"Monday",Toast.LENGTH_SHORT).show();
                        mon=1;
                    }

                    if(Objects.equals(days[i], "Tuesday")) {
                        //Toast.makeText(getApplicationContext(),"Tuesday",Toast.LENGTH_SHORT).show();
                        tue=1;
                    }

                    if(Objects.equals(days[i], "Wednesday")) {
                        //Toast.makeText(getApplicationContext(),"Wednesday",Toast.LENGTH_SHORT).show();
                        wed=1;
                    }

                    if(Objects.equals(days[i], "Thursday")) {
                        //Toast.makeText(getApplicationContext(),"Thursday",Toast.LENGTH_SHORT).show();
                        thur=1;
                    }

                    if(Objects.equals(days[i], "Friday")) {
                        //Toast.makeText(getApplicationContext(),"Friday",Toast.LENGTH_SHORT).show();
                        fri=1;
                    }

                    if(Objects.equals(days[i], "Saturday")) {
                        //Toast.makeText(getApplicationContext(),"saturday",Toast.LENGTH_SHORT).show();
                        sat=1;
                    }

                    if(Objects.equals(days[i], "Sunday")) {
                        //Toast.makeText(getApplicationContext(),"Sunday",Toast.LENGTH_SHORT).show();
                        sun=1;
                    }
                }

                //Toast.makeText(getApplicationContext(),mon+" "+tue+" "+wed+" "+thur+" "+fri+" "+" "+sat+" "+sun,Toast.LENGTH_SHORT).show();

                status=dbAdapter_alarmdata.updateAlarmDays(id,mon,tue,wed,thur,fri,sat,sun);
                if(status) {
                    Log.i(TAG,"Alarm days updated!");
                }else {
                    Log.i(TAG,"Alarm days not updated!");
                }


            }

            dbAdapter_alarmdata.close();

        }
    }

    private void restoreDate() {

        //AlaramData_singletonClass.get(getApplicationContext()).getList_alarmData().clear();

        dbAdapter_alarmdata.open();
        Cursor cursor=dbAdapter_alarmdata.getData();

        AlarmData alarmData=new AlarmData();
        Date date=new Date();
        Calendar calendar=Calendar.getInstance();
        int[] snooze=new int[2];
        String[] days=new String[7];

        if(cursor.moveToFirst()) {



            do {

                if(cursor.getInt(5)!=-1 && cursor.getInt(6)!=-1) {


                    alarmData = new AlarmData();
                    date = new Date();
                    date.setSeconds(0);

                    for (int i = 0; i < 2; i++) {
                        snooze[0] = -1;
                        snooze[1] = -1;
                    }

                    for (int i = 0; i < 7; i++) {
                        days[i] = null;
                    }

                    //String ab = "id: " + cursor.getInt(0) + " ,onoff: " + cursor.getInt(1) + " ,type: " + cursor.getString(2) +
                    //", path: " + cursor.getString(3) + ", title: " + cursor.getString(4) + "\n";
                    alarmData.setId(cursor.getInt(0) - 1);
                    alarmData.setOnOff(cursor.getInt(1));
                    alarmData.setAlarmType(cursor.getString(2));
                    alarmData.setRingtonePatth(cursor.getString(3));
                    alarmData.setAlarmTitle(cursor.getString(4));

                    //String at = "hour: " + cursor.getInt(5) + " ," + cursor.getInt(6) + "\n";
                    date.setHours(cursor.getInt(5));
                    date.setMinutes(cursor.getInt(6));
                    alarmData.setAlarmTime(date);

                    //String adt = "day: " + cursor.getInt(7) + " ,month: " + cursor.getInt(8) + " ,year: " + cursor.getInt(9) + "\n";
                    calendar.set(cursor.getInt(9), cursor.getInt(8), cursor.getInt(7));
                    alarmData.setAlarmDate(calendar);

                    //String as = "duration: " + cursor.getInt(10) + " ,notime: " + cursor.getInt(11) + "\n";
                    snooze[0] = cursor.getInt(10);
                    snooze[1] = cursor.getInt(11);
                    alarmData.setAlarmSnooze(snooze);

                    //String ad = "mon: " + cursor.getInt(12) + " ,tue: " + cursor.getInt(13) + " ,wed: " + cursor.getInt(14) +
                    //" ,thur: " + cursor.getInt(15) + " ,fri: " + cursor.getInt(16) + " ,sat: " + cursor.getInt(17) +
                    //" ,sun: " + cursor.getInt(18) + "\n";

                    if (cursor.getInt(12) == 1) {
                        days[0] = "Monday";
                    }

                    if (cursor.getInt(13) == 1) {
                        days[1] = "Tuesday";
                    }

                    if (cursor.getInt(14) == 1) {
                        days[2] = "Wednesday";
                    }

                    if (cursor.getInt(15) == 1) {
                        days[3] = "Thursday";
                    }

                    if (cursor.getInt(16) == 1) {
                        days[4] = "Friday";
                    }

                    if (cursor.getInt(17) == 1) {
                        days[5] = "Saturday";
                    }

                    if (cursor.getInt(18) == 1) {
                        days[6] = "Sunday";
                    }

                    alarmData.setAlarmDays(days);

                    AlaramData_singletonClass.get(getApplicationContext()).getList_alarmData().add(alarmData);

                    //readData(alarmData);


                    //String data = ab + at + adt + as + ad;

                    //Toast.makeText(getApplicationContext(), data, Toast.LENGTH_SHORT).show();
                }
            }while (cursor.moveToNext());
        }

        dbAdapter_alarmdata.close();
    }

    private String setTime_24Formate(int hour,int min) {

        String time="";



        if(hour<10) {
            time+="0"+hour+":";
        }else {
            time+=hour+":";
        }

        if(min<10) {
            time+="0"+min;
        }else{
            time+=min;
        }

        return time;
    }

    private String setTime_12Formate(int hour,int min) {

        String time="";
        int val=0;


        if(hour<10) {

            if(hour==0) {
                time+="12:";
            }else {

                time += "0" + hour + ":";
            }
            val=1;
        }else if(hour>=10 && hour<=12) {
            time+=hour+":";

            if(hour<12) {
                val=1;
            }else if(hour==12){
                val=2;
            }

        }else if(hour>12) {

            val=2;

            if(hour==13) {
                time+="01:";
            }else if(hour==14) {
                time+="02:";
            }else if(hour==15) {
                time+="03:";
            }else if(hour==16) {
                time+="04:";
            }else if(hour==17) {
                time+="05:";
            }else if(hour==18) {
                time+="06:";
            }else if(hour==19) {
                time+="07:";
            }else if(hour==20) {
                time+="08:";
            }else if(hour==21) {
                time+="09:";
            }else if(hour==22) {
                time+="10:";
            }else if(hour==23) {
                time+="11:";
            }

        }

        if(min<10) {
            time+="0"+min;
        }else{
            time+=min;
        }

        if(val==1) {

            time += " AM";
        }else {
            time+=" PM";
        }

        return time;

    }
}
