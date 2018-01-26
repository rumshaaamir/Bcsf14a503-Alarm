package com.rumshaaamir.alarm;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Environment;
import android.os.PowerManager;
import android.os.StatFs;
import android.provider.Settings;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.format.DateFormat;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import com.rumshaaamir.alarm.ActivityPreviewAlarm;
import com.rumshaaamir.alarm.AlaramData_singletonClass;
import com.rumshaaamir.alarm.AlarmData;

import java.io.File;
import java.io.Serializable;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Timer;

public class MainActivity extends AppCompatActivity {

    //for log chat
    String TAG="MainActivity";
    String TAG_previewActivity="ActivityPreviewAlarm";

    //button
    Button button_newAlarm;

    AlaramData_singletonClass malaramData_singletonClass;

    RecyclerView recyclerView;

    Adapter_recycleView adapter_recycleView;

    DBAdapter_alarmdata dbAdapter_alarmdata;

    AlarmData malarmData;

    int[] index=new int[10];

    static int x=0;

    static Intent malarmIntent;
    static PendingIntent mpendingIntent;
    static AlarmManager malarmManager;

    private String malramType="Simple Alram";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Toast.makeText(getApplicationContext(), "on create", Toast.LENGTH_SHORT).show();
        Log.i(TAG,"on create");

        //setting title for action bar
        //setTitle(getResources().getString(R.string.title_am));

        //setting title for action bar and also change the color of the text on title bar
        String title="Alarm";
        SpannableString s = new SpannableString(title);
        s.setSpan(new ForegroundColorSpan(Color.DKGRAY), 0, title.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        getSupportActionBar().setTitle(s);

        malarmIntent=new Intent(getApplicationContext(),BroadCastReceiverAlarm.class);
        malarmIntent.putExtra("type",malramType);
        malarmManager=(AlarmManager)getSystemService(ALARM_SERVICE);

        /*mpendingIntent=PendingIntent.getBroadcast(getApplicationContext(),0,malarmIntent,PendingIntent.FLAG_NO_CREATE);
        if(mpendingIntent!=null) {
            Toast.makeText(this, "not null", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(this, "null", Toast.LENGTH_SHORT).show();
        }*/



        dbAdapter_alarmdata=new DBAdapter_alarmdata(this);
        //readInitialize_database();

        if(checkEmpty_database()) {
            initializeDatabase();
            //readInitialize_database();
            //Toast.makeText(getApplicationContext(),"initialization",Toast.LENGTH_SHORT).show();
        }else {

            if(AlaramData_singletonClass.get(getApplicationContext()).getList_alarmData().size()!=0) {

                resetDataBase();
            }
            updateDatabase();
            AlaramData_singletonClass.get(getApplicationContext()).getList_alarmData().clear();
            restoreDate();

            //readInitialize_database();

            //updateDatabase();
            //AlaramData_singletonClass.get(getApplicationContext()).getList_alarmData().clear();
            //restoreDate();

            /*for(AlarmData alarmData:AlaramData_singletonClass.get(getApplicationContext()).getList_alarmData()) {
                readData(alarmData);
            }*/


            /*if(AlaramData_singletonClass.get(getApplicationContext()).getList_alarmData().size()==0) {
                restoreDate();
            }

            for(AlarmData alarmData:AlaramData_singletonClass.get(getApplicationContext()).getList_alarmData()) {
                readData(alarmData);
            }*/

            //onStop();

            Date date=new Date();
            date.setMinutes(1);
            date.setHours(21);
            date.setSeconds(0);

            Calendar calendar=Calendar.getInstance();
            calendar.setTime(date);

            //mpendingIntent=PendingIntent.getBroadcast(getApplicationContext(),0,malarmIntent,0);
            //malarmManager.set(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),mpendingIntent);

            date.setMinutes(2);
            date.setHours(21);
            date.setSeconds(0);
            calendar.setTime(date);

            //mpendingIntent=PendingIntent.getBroadcast(getApplicationContext(),0,malarmIntent,0);
            //malarmManager.set(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),mpendingIntent);

            cancelAlarms();
            alarm_onOff();

            /*for(AlarmData alarmData:AlaramData_singletonClass.get(getApplicationContext()).getList_alarmData()) {
                if(alarmData.getOnOff()==1) {

                    Date date=new Date();
                    date=alarmData.getAlarmTime();
                    date.setSeconds(0);

                    Calendar calendar=Calendar.getInstance();
                    calendar=alarmData.getAlarmDate();
                    calendar.setTime(date);

                    mpendingIntent=PendingIntent.getBroadcast(getApplicationContext(),alarmData.getId(),malarmIntent,
                            PendingIntent.FLAG_UPDATE_CURRENT);
                    malarmManager.set(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),mpendingIntent);

                }else if(alarmData.getOnOff()==0) {

                    mpendingIntent= PendingIntent.getBroadcast(getApplicationContext(), 0, MainActivity.malarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                    if(mpendingIntent!=null) {
                        mpendingIntent.cancel();
                        malarmManager.cancel(mpendingIntent);
                    }
                }
            }*/
        }


        recyclerView=(RecyclerView)findViewById(R.id.recycleView);
        //final ArrayList<Contact> contactArrayList;
        //contactArrayList= Contact.createContactsList(10);
        adapter_recycleView =new Adapter_recycleView(this,AlaramData_singletonClass.get(getApplicationContext()).getList_alarmData());
        recyclerView.setAdapter(adapter_recycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this,LinearLayoutManager.VERTICAL));
        recyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplication(),"click",Toast.LENGTH_SHORT).show();
            }
        });
        recyclerView.getItemAnimator();
        registerForContextMenu(recyclerView);
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(),
                recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(getApplicationContext(),"Preview Alarm",Toast.LENGTH_SHORT).show();

                //onStop();

                AlarmData alarmData=AlaramData_singletonClass.get(getApplicationContext()).getList_alarmData().get(position);
                //oast.makeText(getApplicationContext(), "Index: "+AlaramData_singletonClass.get(getApplicationContext()).getList_alarmData().indexOf(alarmData)+" ", Toast.LENGTH_LONG).show();
                int index=-1;
                //index=AlaramData_singletonClass.get(getApplicationContext()).getList_alarmData().indexOf(alarmData);
                index=AlaramData_singletonClass.get(getApplicationContext()).getList_alarmData().indexOf(alarmData);

                //Toast.makeText(MainActivity.this, "position: "+position, Toast.LENGTH_SHORT).show();
                //Toast.makeText(MainActivity.this, "index: "+index, Toast.LENGTH_SHORT).show();

                Date d=alarmData.getAlarmTime();
                //Toast.makeText(MainActivity.this, d.getHours()+":"+d.getMinutes(), Toast.LENGTH_SHORT).show();

                deleteAlarm(index+1);

                Intent intent_startActivity=new Intent(MainActivity.this,ActivityPreviewAlarm.class);
                intent_startActivity.putExtra(TAG_previewActivity, index);
                startActivity(intent_startActivity);



                //Intent intent_startActivity=new Intent(getApplication(),ActivityPreviewAlarm.class);
                //startActivity(intent_startActivity);
            }

            @Override
            public void onLongItemClick(View view, int position) {
                Toast.makeText(getApplicationContext(),"Alarm Deleted!",Toast.LENGTH_SHORT).show();
                //Contact contact=contactArrayList.get(position);
                //Toast.makeText(getApplicationContext(),position+" ",Toast.LENGTH_SHORT).show();


                AlarmData alarmData= AlaramData_singletonClass.get(getApplicationContext()).getList_alarmData().get(position);

                deleteAlarm(alarmData.getId()+1);

                //Toast.makeText(MainActivity.this, alarmData.getId()+" ", Toast.LENGTH_SHORT).show();
                //readData(alarmData);
                //readInitialize_database();

                MainActivity.mpendingIntent= PendingIntent.getBroadcast(getApplicationContext(), alarmData.getId(), MainActivity.malarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                if(MainActivity.mpendingIntent!=null) {
                    MainActivity.mpendingIntent.cancel();
                    MainActivity.malarmManager.cancel(MainActivity.mpendingIntent);
                }

                //readData(alarmData);

                //Toast.makeText(getApplicationContext(),"Deleted index: "+alarmData.getId(),Toast.LENGTH_SHORT).show();

                //AlaramData_singletonClass.get(getApplicationContext()).getList_alarmData().remove(alarmData);
                AlaramData_singletonClass.get(getApplicationContext()).getList_alarmData().remove(alarmData);
                Date d=alarmData.getAlarmTime();
                //Toast.makeText(MainActivity.this, d.getHours()+":"+d.getMinutes(), Toast.LENGTH_SHORT).show();
                //AlaramData_singletonClass.get(getApplicationContext()).delete(alarmData.getId());

                //alarmData=AlaramData_singletonClass.get(getApplicationContext()).getList_alarmData().get(position);

                //readData(alarmData);





                adapter_recycleView.notifyDataSetChanged();

                //onStop();




                /*String opption[]={"YES!","NO!"};
                ListView listView_menu=(ListView)findViewById(R.id.listView_menu);
                ArrayAdapter<String> stringArrayAdapter=new ArrayAdapter<String>(getApplication(),R.layout.customdialoge_conformation);
                listView_menu.setAdapter(stringArrayAdapter);
                registerForContextMenu(listView_menu);*/


                /*//Contact contact=new Contact("0","0","0",true);
                adapter_recycleView.remove(contact);
                adapter_recycleView.notifyDataSetChanged();*/
            }
        }));




        //add new alarm
        Boolean isClickedDummy=true;
        button_newAlarm=(Button)findViewById(R.id.newAlarm_button);
        //button_newAlarm.setBackgroundResource(R.drawable.round_button);
        alarmNew_buttonFunction(button_newAlarm,true);


        /*//rough work for practice
        CardView cardView=(CardView)findViewById(R.id.cardView);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_startActivity=new Intent(MainActivity.this,ActivityPreviewAlarm.class);
                startActivity(intent_startActivity);
            }
        });*/





        /*ArrayList<AlarmData> alarmDataArrayList;
        alarmDataArrayList=AlaramData_singletonClass.get(getApplicationContext()).getList_alarmData();*/

        /*AlarmData alarmData=alarmDataArrayList.get(1)
        alarmData.getId();*/

        //Toast.makeText(getApplicationContext(),alarmData.getId()+" ",Toast.LENGTH_SHORT).show();

        /*for (AlarmData alarmData:alarmDataArrayList) {
            Toast.makeText(getApplicationContext(),alarmData.getId()+" ",Toast.LENGTH_SHORT).show();
        }*/

        /*AlaramData_singletonClass.get(getApplicationContext()).add(1);
        Toast.makeText(getApplicationContext(),"1 is added",Toast.LENGTH_SHORT).show();
        AlaramData_singletonClass.get(getApplicationContext()).add(2);
        Toast.makeText(getApplicationContext(),"2 is added",Toast.LENGTH_SHORT).show();
        AlaramData_singletonClass.get(getApplicationContext()).add(3);
        Toast.makeText(getApplicationContext(),"3 is added",Toast.LENGTH_SHORT).show();
        AlaramData_singletonClass.get(getApplicationContext()).add(4);
        Toast.makeText(getApplicationContext(),"4 is added",Toast.LENGTH_SHORT).show();*/


        //Toast.makeText(getApplicationContext(),getAvailableInternalMemorySize()+" ",Toast.LENGTH_SHORT).show();

        //Collections.sort(AlaramData_singletonClass.get(getApplicationContext()).getList_alarmData());

        //Collections.sort(AlaramData_singletonClass.get(getApplicationContext()).getList_alarmData(),new UserComparator());


        //dbAdapter_alarmdata.open();

        /*dbAdapter_alarmdata.initializeAlarmBasic(1,-1,"0","0","0",-1);
        dbAdapter_alarmdata.initializeAlarmBasic(2,-1,"0","0","0",-1);
        dbAdapter_alarmdata.initializeAlarmBasic(3,-1,"0","0","0",-1);;
        dbAdapter_alarmdata.initializeAlarmBasic(4,-1,"0","0","0",-1);
        dbAdapter_alarmdata.initializeAlarmBasic(5,-1,"0","0","0",-1);;
        dbAdapter_alarmdata.initializeAlarmBasic(6,-1,"0","0","0",-1);
        dbAdapter_alarmdata.initializeAlarmBasic(7,-1,"0","0","0",-1);
        dbAdapter_alarmdata.initializeAlarmBasic(8,-1,"0","0","0",-1);
        dbAdapter_alarmdata.initializeAlarmBasic(9,-1,"0","0","0",-1);
        dbAdapter_alarmdata.initializeAlarmBasic(10,-1,"0","0","0",-1);

        dbAdapter_alarmdata.initializeAlarmDays(1,-1,-1,-1,-1,-1,-1,-1,-1);
        dbAdapter_alarmdata.initializeAlarmDays(2,-1,-1,-1,-1,-1,-1,-1,-1);
        dbAdapter_alarmdata.initializeAlarmDays(3,-1,-1,-1,-1,-1,-1,-1,-1);
        dbAdapter_alarmdata.initializeAlarmDays(4,-1,-1,-1,-1,-1,-1,-1,-1);
        dbAdapter_alarmdata.initializeAlarmDays(5,-1,-1,-1,-1,-1,-1,-1,-1);
        dbAdapter_alarmdata.initializeAlarmDays(6,-1,-1,-1,-1,-1,-1,-1,-1);
        dbAdapter_alarmdata.initializeAlarmDays(7,-1,-1,-1,-1,-1,-1,-1,-1);
        dbAdapter_alarmdata.initializeAlarmDays(8,-1,-1,-1,-1,-1,-1,-1,-1);
        dbAdapter_alarmdata.initializeAlarmDays(9,-1,-1,-1,-1,-1,-1,-1,-1);
        dbAdapter_alarmdata.initializeAlarmDays(10,-1,-1,-1,-1,-1,-1,-1,-1);

        dbAdapter_alarmdata.initializeAlarmSnooze(1,-1,-1,-1);
        dbAdapter_alarmdata.initializeAlarmSnooze(2,-1,-1,-1);
        dbAdapter_alarmdata.initializeAlarmSnooze(3,-1,-1,-1);
        dbAdapter_alarmdata.initializeAlarmSnooze(4,-1,-1,-1);
        dbAdapter_alarmdata.initializeAlarmSnooze(5,-1,-1,-1);
        dbAdapter_alarmdata.initializeAlarmSnooze(6,-1,-1,-1);
        dbAdapter_alarmdata.initializeAlarmSnooze(7,-1,-1,-1);
        dbAdapter_alarmdata.initializeAlarmSnooze(8,-1,-1,-1);
        dbAdapter_alarmdata.initializeAlarmSnooze(9,-1,-1,-1);
        dbAdapter_alarmdata.initializeAlarmSnooze(10,-1,-1,-1);

        dbAdapter_alarmdata.initializeAlarmDate(1,-1,-1,-1,-1);
        dbAdapter_alarmdata.initializeAlarmDate(2,-1,-1,-1,-1);
        dbAdapter_alarmdata.initializeAlarmDate(3,-1,-1,-1,-1);
        dbAdapter_alarmdata.initializeAlarmDate(4,-1,-1,-1,-1);
        dbAdapter_alarmdata.initializeAlarmDate(5,-1,-1,-1,-1);
        dbAdapter_alarmdata.initializeAlarmDate(6,-1,-1,-1,-1);
        dbAdapter_alarmdata.initializeAlarmDate(7,-1,-1,-1,-1);
        dbAdapter_alarmdata.initializeAlarmDate(8,-1,-1,-1,-1);
        dbAdapter_alarmdata.initializeAlarmDate(9,-1,-1,-1,-1);
        dbAdapter_alarmdata.initializeAlarmDate(10,-1,-1,-1,-1);

        dbAdapter_alarmdata.initializeAlarmTime(1,-1,-1,-1);
        dbAdapter_alarmdata.initializeAlarmTime(2,-1,-1,-1);
        dbAdapter_alarmdata.initializeAlarmTime(3,-1,-1,-1);
        dbAdapter_alarmdata.initializeAlarmTime(4,-1,-1,-1);
        dbAdapter_alarmdata.initializeAlarmTime(5,-1,-1,-1);
        dbAdapter_alarmdata.initializeAlarmTime(6,-1,-1,-1);
        dbAdapter_alarmdata.initializeAlarmTime(7,-1,-1,-1);
        dbAdapter_alarmdata.initializeAlarmTime(8,-1,-1,-1);
        dbAdapter_alarmdata.initializeAlarmTime(9,-1,-1,-1);
        dbAdapter_alarmdata.initializeAlarmTime(10,-1,-1,-1);*/

        /*dbAdapter_alarmdata.close();

        dbAdapter_alarmdata.open();

        Cursor cursor=null;
        cursor=dbAdapter_alarmdata.getAlarmBasic_inilialized();
        if (cursor.getCount()==0) {
            Toast.makeText(getApplicationContext(),"null",Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(getApplicationContext(),"not null",Toast.LENGTH_SHORT).show();
        }
        if(cursor.moveToFirst()) {
            do {
                displayInitialized_alramBasic(cursor);
            }while (cursor.moveToNext());
        }

        cursor=dbAdapter_alarmdata.getAlarmDays_inilialized();
        if(cursor.moveToFirst()) {
            do {
                displayInitialize_alarmDays(cursor);
            }while (cursor.moveToNext());
        }

        //Cursor cursor;
        cursor=dbAdapter_alarmdata.getAlarmSnooze_inilialized();
        if(cursor.moveToFirst()) {
            do {
                displayInitialized_alarmSnooze(cursor);
            }while (cursor.moveToNext());
        }

        cursor=dbAdapter_alarmdata.getAlarmDate_inilialized();
        if(cursor.moveToFirst()) {
            do {
                displayInitilized_alramDate(cursor);
            }while (cursor.moveToNext());
        }

        cursor=dbAdapter_alarmdata.getAlarmTime_inilialized();
        if(cursor.moveToFirst()) {
            do {
                displayInitialized_alramTime(cursor);
            }while (cursor.moveToNext());
        }




        dbAdapter_alarmdata.close();*/





    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.menu_context_activity_main,menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.yes:
                break;
            case R.id.no:
                break;

            default:
                return super.onContextItemSelected(item);
        }

        return false;

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.menu_activity_main,menu);
        return  true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {



        switch (item.getItemId()) {
            case R.id.about_item:
                Log.i(TAG,"You have clicked about button new activity started!");
                //start the about class
                Intent intent=new Intent(MainActivity.this,About.class);
                startActivity(intent);

                return true;
            case R.id.settings_item:


                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG,"MainActivity button background reseting!");
        button_newAlarm.setBackgroundResource(R.drawable.round_button);

        //Toast.makeText(getApplicationContext(),"Resumed!",Toast.LENGTH_LONG).show();

        /*ArrayList<AlarmData> alarmDataArrayList=new ArrayList<AlarmData>();
        alarmDataArrayList=AlaramData_singletonClass.get(getApplicationContext()).getList_alarmData();


        for(AlarmData alarmData:alarmDataArrayList) {

            Toast.makeText(getApplicationContext(),"Title: "+alarmData.getAlarmTitle(),Toast.LENGTH_SHORT).show();

            Toast.makeText(getApplicationContext(),"Path: "+alarmData.getRingtonePatth(),Toast.LENGTH_SHORT).show();

            Toast.makeText(getApplicationContext(),"Type: "+alarmData.getAlarmType(),Toast.LENGTH_SHORT).show();

            for(int i=0;i<2;i++) {
                int[] data=new int[2];
                data=alarmData.getAlarmSnooze();
                Toast.makeText(getApplicationContext(),"snooze 1: "+data[i]+" ",Toast.LENGTH_SHORT).show();
            }

            Toast.makeText(getApplicationContext(),"onOff: "+alarmData.getOnOff()+" ",Toast.LENGTH_SHORT).show();

            Calendar icalendar=alarmData.getAlarmDate();
            int d,m,y;
            d=icalendar.get(Calendar.DAY_OF_MONTH);
            m=icalendar.get(Calendar.MONTH);
            y=icalendar.get(Calendar.YEAR);
            Toast.makeText(getApplicationContext(),d+" "+m+" "+y,Toast.LENGTH_SHORT).show();

            int h,min;
            Date idate=alarmData.getAlarmTime();
            h=idate.getHours();
            min=idate.getMinutes();
            Toast.makeText(getApplicationContext(),h+" "+min,Toast.LENGTH_SHORT).show();

            String[] strings=new String[7];
            strings=alarmData.getAlarmDays();
            for(int i=0;i<7;i++) {
                Toast.makeText(getApplicationContext(), strings[i]+" ",Toast.LENGTH_SHORT).show();
            }

        }*/

        //AlaramData_singletonClass.get(getApplicationContext()).getList_alarmData().sort();
        //adapter_recycleView.add();
        adapter_recycleView.add();

        Collections.sort(AlaramData_singletonClass.get(getApplicationContext()).getList_alarmData(),new UserComparator());

       /* Toast.makeText(getApplicationContext(),AlaramData_singletonClass.get(getApplicationContext()).getList_alarmData().
                size()+" ",Toast.LENGTH_SHORT).show();

        for(int i=0;i<AlaramData_singletonClass.get(getApplicationContext()).getList_alarmData().
                size();i++) {
            AlarmData alarmData=new AlarmData();
            alarmData=AlaramData_singletonClass.get(getApplicationContext()).getList_alarmData().get(i);
            Toast.makeText(getApplicationContext(),alarmData.getId()+" id",Toast.LENGTH_SHORT).show();
        }

        for(int i=0;i<AlaramData_singletonClass.get(getApplicationContext()).getList_alarmData().size();i++) {
            for(int j=0;j<AlaramData_singletonClass.get(getApplicationContext()).getList_alarmData().size()-1;j++) {
                AlarmData alarmData1=new AlarmData();
                AlarmData alarmData2=new AlarmData();

                alarmData1=AlaramData_singletonClass.get(getApplicationContext()).getList_alarmData().get(j);
                alarmData2=AlaramData_singletonClass.get(getApplicationContext()).getList_alarmData().get(j+1);

                if(alarmData1.getId()>alarmData2.getId()) {

                }
            }
        }*/

        //AlaramData_singletonClass.get(getApplicationContext()).getList_alarmData().a

        /*for(AlarmData alarmData:AlaramData_singletonClass.get(getApplicationContext()).getList_alarmData()) {
            Toast.makeText(getApplicationContext(),"on resume: "+alarmData.getId(),Toast.LENGTH_SHORT).show();
        }*/




        //updateDatabase();
        //readInitialize_database();


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //updateDatabase();
        //Toast.makeText(getApplicationContext(), "on destroy", Toast.LENGTH_SHORT).show();
        //Log.i(TAG,"on destroy");

    }

    @Override
    protected void onStop() {
        super.onStop();

        /*if(AlaramData_singletonClass.get(getApplicationContext()).getList_alarmData().size()!=0) {

            initializeDatabase();
            updateDatabase();
            AlaramData_singletonClass.get(getApplicationContext()).getList_alarmData().clear();
            restoreDate();
            readInitialize_database();
        }*/

        resetDataBase();
        updateDatabase();
        AlaramData_singletonClass.get(getApplicationContext()).getList_alarmData().clear();
        restoreDate();

        //alarm_onOff();

        /*for(AlarmData alarmData:AlaramData_singletonClass.get(getApplicationContext()).getList_alarmData()) {
            if(alarmData.getOnOff()==1) {

                Date date=new Date();
                date=alarmData.getAlarmTime();
                date.setSeconds(0);

                Calendar calendar=Calendar.getInstance();
                calendar=alarmData.getAlarmDate();
                calendar.setTime(date);

                mpendingIntent=PendingIntent.getBroadcast(getApplicationContext(),alarmData.getId(),malarmIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);
                malarmManager.set(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),mpendingIntent);

            }else if(alarmData.getOnOff()==0) {

                mpendingIntent= PendingIntent.getBroadcast(getApplicationContext(), 0, MainActivity.malarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                if(mpendingIntent!=null) {
                    mpendingIntent.cancel();
                    malarmManager.cancel(mpendingIntent);
                }
            }
        }*/

        //readInitialize_database();

        //adapter_recycleView.notifyDataSetChanged();


        //Toast.makeText(getApplicationContext(), "on stop", Toast.LENGTH_SHORT).show();
        Log.i(TAG,"on stop");

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        //Toast.makeText(getApplicationContext(), "on restart", Toast.LENGTH_SHORT).show();
        Log.i(TAG,"on restart");
        //AlaramData_singletonClass.get(getApplicationContext()).getList_alarmData().clear();
        //restoreDate();
        //readInitialize_database();


        //restoreDate();


        /*AlaramData_singletonClass.get(getApplicationContext()).getList_alarmData().clear();
        for(AlarmData data:AlaramData_singletonClass.get(getApplicationContext()).getList_alarmData()) {
            readData(data);
        }
        restoreDate();

        adapter_recycleView.notifyDataSetChanged();*/
        /*for(AlarmData data:AlaramData_singletonClass.get(getApplicationContext()).getList_alarmData()) {
            readData(data);
        }*/

        /*AlaramData_singletonClass.get(getApplicationContext()).getList_alarmData().clear();
        readInitialize_database();
        restoreDate();*/

        //onStop();

        //readInitialize_database();

    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        //Toast.makeText(getApplicationContext(), "on deattached", Toast.LENGTH_SHORT).show();
        Log.i(TAG,"on deatached");
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        //Toast.makeText(getApplicationContext(), "on attached", Toast.LENGTH_SHORT).show();
        Log.i(TAG,"on atached");
        //initializeDatabase();
        //updateDatabase();
        //AlaramData_singletonClass.get(getApplicationContext()).getList_alarmData().clear();
        //restoreDate();
        //readInitialize_database();
    }



    /*@Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG,"Destroy");

        int id, onOff;
        String type, path, title;

        int mon=-1, tue=-1, wed=-1, thur=-1, fri=-1, sat=-1, sun=-1;
        String[] days=new String[7];

        int notime, min;
        int[] snooze=new int[2];

        int day, month, year;
        Calendar calendar;

        int hour, minutes;
        Date date;

        boolean status;

        for (AlarmData alarmData:AlaramData_singletonClass.get(getApplicationContext()).getList_alarmData()) {

            DBAdapter_alarmdata dbAdapter_alarmdata=new DBAdapter_alarmdata(getApplicationContext());

            dbAdapter_alarmdata.open();

            id=alarmData.getId();
            id=id+1;
            onOff=alarmData.getOnOff();
            type=alarmData.getAlarmType();
            path=alarmData.getRingtonePatth();
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
            if(days[0]==null && days[1 ]==null && days[2]==null && days[3]==null && days[4]==null && days[5]==null
                    && days[6]==null) {

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
                if(days[0]!=null) {
                    mon=1;
                }

                if(days[1]!=null) {
                    tue=1;
                }

                if(days[2]!=null) {
                    wed=1;
                }

                if(days[3]!=null) {
                    thur=1;
                }


                if(days[4]!=null) {
                    fri=1;
                }

                if(days[5]!=null) {
                    sat=1;
                }

                if(days[6]!=null) {
                    sun=1;
                }

                status=dbAdapter_alarmdata.updateAlarmDays(id,mon,tue,wed,thur,fri,sat,sun);
                if(status) {
                    Log.i(TAG,"Alarm days updated!");
                }else {
                    Log.i(TAG,"Alarm days not updated!");
                }


            }

            dbAdapter_alarmdata.close();


        }
    }*/

    /*@Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG,"Stop");

        int id, onOff;
        String type, path, title;

        int mon=-1, tue=-1, wed=-1, thur=-1, fri=-1, sat=-1, sun=-1;
        String[] days=new String[7];

        int notime, min;
        int[] snooze=new int[2];

        int day, month, year;
        Calendar calendar;

        int hour, minutes;
        Date date;

        boolean status;

        for (AlarmData alarmData:AlaramData_singletonClass.get(getApplicationContext()).getList_alarmData()) {

            DBAdapter_alarmdata dbAdapter_alarmdata=new DBAdapter_alarmdata(getApplicationContext());

            dbAdapter_alarmdata.open();

            id=alarmData.getId();
            onOff=alarmData.getOnOff();
            type=alarmData.getAlarmType();
            path=alarmData.getRingtonePatth();
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
            if(days[0]==null && days[1 ]==null && days[2]==null && days[3]==null && days[4]==null && days[5]==null
                    && days[6]==null) {

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
                if(days[0]!=null) {
                    mon=1;
                }

                if(days[1]!=null) {
                    tue=1;
                }

                if(days[2]!=null) {
                    wed=1;
                }

                if(days[3]!=null) {
                    thur=1;
                }


                if(days[4]!=null) {
                    fri=1;
                }

                if(days[5]!=null) {
                    sat=1;
                }

                if(days[6]!=null) {
                    sun=1;
                }

                status=dbAdapter_alarmdata.updateAlarmDays(id,mon,tue,wed,thur,fri,sat,sun);
                if(status) {
                    Log.i(TAG,"Alarm days updated!");
                }else {
                    Log.i(TAG,"Alarm days not updated!");
                }


            }

            dbAdapter_alarmdata.close();


        }
    }*/

    /*@Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG,"Start");
    }*/


    private void alarmNew_buttonFunction(Button button, final Boolean isClicked) {
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
               // System.out.print("asd");
                Log.i(TAG,"You have clicked add new alarm button!");
                if(isClicked) {
                    v.setBackgroundColor(Color.parseColor("#707878"));
                    //isClickedDummy = false;
                }

                //Toast.makeText(getApplicationContext(),AlaramData_singletonClass.get(getApplicationContext()).getList_alarmData().size()+" ",Toast.LENGTH_SHORT).show();

                if(AlaramData_singletonClass.get(getApplicationContext()).getList_alarmData().size()==10) {
                    Toast.makeText(getApplicationContext(),"No space for new alarm!" +
                            "Need to delete one alarm!",Toast.LENGTH_SHORT).show();
                    button_newAlarm.setBackgroundResource(R.drawable.round_button);
                }else {
                    Log.i(TAG,"Alarm setting activity started!");
                    //start the about class
                    Intent intent=new Intent(MainActivity.this,AlarmSetting.class);
                    startActivity(intent);
                }





            }
        });
    }


    static int TimeFormate(Context context) {
        if(DateFormat.is24HourFormat(context)) {
            return 24;
        }else {
            return 12;
        }
    }

    /*public static long getAvailableInternalMemorySize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize, availableBlocks;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            blockSize = stat.getBlockSizeLong();
            availableBlocks = stat.getAvailableBlocksLong();
        } else {
            blockSize = stat.getBlockSize();
            availableBlocks = stat.getAvailableBlocks();
        }
        return availableBlocks * blockSize;
    }*/

    private void displayInitialized_alramBasic(Cursor cursor) {
        Toast.makeText(getApplicationContext(),"id: "+cursor.getString(0)+" ,onOff: "+cursor.getString(1)+
                        " ,type: "+cursor.getString(2)+" ,path: "+cursor.getString(3)+" ,title: "+cursor.getString(4)+
                        " ,status: "+cursor.getString(5),
                Toast.LENGTH_SHORT).show();
    }

    private void displayInitialize_alarmDays(Cursor cursor) {
        Toast.makeText(getApplicationContext(),"id: "+cursor.getString(0)+" ,mon: "+cursor.getString(1)+" ,tues: "+
                cursor.getString(2)+" ,wed: "+cursor.getString(3)+" ,thur: "+cursor.getString(4)+" fri: "+
                cursor.getString(5)+" ,sat: "+cursor.getString(6)+" ,sun: "+cursor.getString(7)+" ,status: "+
                cursor.getString(8),Toast.LENGTH_SHORT).show();
    }

    private void displayInitialized_alarmSnooze(Cursor cursor) {
        Toast.makeText(getApplicationContext(),"id: "+cursor.getString(0)+" ,noTime: "+cursor.getString(1)+" ,min: "+
                cursor.getString(2)+" ,status: "+cursor.getString(3),Toast.LENGTH_SHORT).show();
    }

    private void displayInitilized_alramDate(Cursor cursor) {
        Toast.makeText(getApplicationContext(),"id: "+cursor.getString(0)+" ,day: "+cursor.getString(1)+" ,month: "+
                cursor.getString(2)+" ,year: "+cursor.getString(3)+" ,status: "+cursor.getString(4),Toast.LENGTH_SHORT).show();
    }

    private void displayInitialized_alramTime(Cursor cursor) {
        Toast.makeText(getApplicationContext(),"id: "+cursor.getString(0)+" ,hour: "+cursor.getString(1)+" ,min: "+
                cursor.getString(2)+" ,status: "+cursor.getString(3),Toast.LENGTH_SHORT).show();
    }

    private void initializeDatabase() {
        dbAdapter_alarmdata.open();

        dbAdapter_alarmdata.initializeAlarmBasic(1,-1,"0","0","0",-1);
        dbAdapter_alarmdata.initializeAlarmBasic(2,-1,"0","0","0",-1);
        dbAdapter_alarmdata.initializeAlarmBasic(3,-1,"0","0","0",-1);;
        dbAdapter_alarmdata.initializeAlarmBasic(4,-1,"0","0","0",-1);
        dbAdapter_alarmdata.initializeAlarmBasic(5,-1,"0","0","0",-1);;
        dbAdapter_alarmdata.initializeAlarmBasic(6,-1,"0","0","0",-1);
        dbAdapter_alarmdata.initializeAlarmBasic(7,-1,"0","0","0",-1);
        dbAdapter_alarmdata.initializeAlarmBasic(8,-1,"0","0","0",-1);
        dbAdapter_alarmdata.initializeAlarmBasic(9,-1,"0","0","0",-1);
        dbAdapter_alarmdata.initializeAlarmBasic(10,-1,"0","0","0",-1);

        dbAdapter_alarmdata.initializeAlarmDays(1,-1,-1,-1,-1,-1,-1,-1,-1);
        dbAdapter_alarmdata.initializeAlarmDays(2,-1,-1,-1,-1,-1,-1,-1,-1);
        dbAdapter_alarmdata.initializeAlarmDays(3,-1,-1,-1,-1,-1,-1,-1,-1);
        dbAdapter_alarmdata.initializeAlarmDays(4,-1,-1,-1,-1,-1,-1,-1,-1);
        dbAdapter_alarmdata.initializeAlarmDays(5,-1,-1,-1,-1,-1,-1,-1,-1);
        dbAdapter_alarmdata.initializeAlarmDays(6,-1,-1,-1,-1,-1,-1,-1,-1);
        dbAdapter_alarmdata.initializeAlarmDays(7,-1,-1,-1,-1,-1,-1,-1,-1);
        dbAdapter_alarmdata.initializeAlarmDays(8,-1,-1,-1,-1,-1,-1,-1,-1);
        dbAdapter_alarmdata.initializeAlarmDays(9,-1,-1,-1,-1,-1,-1,-1,-1);
        dbAdapter_alarmdata.initializeAlarmDays(10,-1,-1,-1,-1,-1,-1,-1,-1);

        dbAdapter_alarmdata.initializeAlarmSnooze(1,-1,-1,-1);
        dbAdapter_alarmdata.initializeAlarmSnooze(2,-1,-1,-1);
        dbAdapter_alarmdata.initializeAlarmSnooze(3,-1,-1,-1);
        dbAdapter_alarmdata.initializeAlarmSnooze(4,-1,-1,-1);
        dbAdapter_alarmdata.initializeAlarmSnooze(5,-1,-1,-1);
        dbAdapter_alarmdata.initializeAlarmSnooze(6,-1,-1,-1);
        dbAdapter_alarmdata.initializeAlarmSnooze(7,-1,-1,-1);
        dbAdapter_alarmdata.initializeAlarmSnooze(8,-1,-1,-1);
        dbAdapter_alarmdata.initializeAlarmSnooze(9,-1,-1,-1);
        dbAdapter_alarmdata.initializeAlarmSnooze(10,-1,-1,-1);

        dbAdapter_alarmdata.initializeAlarmDate(1,-1,-1,-1,-1);
        dbAdapter_alarmdata.initializeAlarmDate(2,-1,-1,-1,-1);
        dbAdapter_alarmdata.initializeAlarmDate(3,-1,-1,-1,-1);
        dbAdapter_alarmdata.initializeAlarmDate(4,-1,-1,-1,-1);
        dbAdapter_alarmdata.initializeAlarmDate(5,-1,-1,-1,-1);
        dbAdapter_alarmdata.initializeAlarmDate(6,-1,-1,-1,-1);
        dbAdapter_alarmdata.initializeAlarmDate(7,-1,-1,-1,-1);
        dbAdapter_alarmdata.initializeAlarmDate(8,-1,-1,-1,-1);
        dbAdapter_alarmdata.initializeAlarmDate(9,-1,-1,-1,-1);
        dbAdapter_alarmdata.initializeAlarmDate(10,-1,-1,-1,-1);

        dbAdapter_alarmdata.initializeAlarmTime(1,-1,-1,-1);
        dbAdapter_alarmdata.initializeAlarmTime(2,-1,-1,-1);
        dbAdapter_alarmdata.initializeAlarmTime(3,-1,-1,-1);
        dbAdapter_alarmdata.initializeAlarmTime(4,-1,-1,-1);
        dbAdapter_alarmdata.initializeAlarmTime(5,-1,-1,-1);
        dbAdapter_alarmdata.initializeAlarmTime(6,-1,-1,-1);
        dbAdapter_alarmdata.initializeAlarmTime(7,-1,-1,-1);
        dbAdapter_alarmdata.initializeAlarmTime(8,-1,-1,-1);
        dbAdapter_alarmdata.initializeAlarmTime(9,-1,-1,-1);
        dbAdapter_alarmdata.initializeAlarmTime(10,-1,-1,-1);

        dbAdapter_alarmdata.close();
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

    private boolean checkEmpty_database() {

        boolean state=false;

        dbAdapter_alarmdata.open();

        Cursor cursor=null;
        cursor=dbAdapter_alarmdata.getAlarmBasic_inilialized();
        if (cursor.getCount()==0) {
            state=true;
        }else {
            state=false;
        }




        dbAdapter_alarmdata.close();

        return state;

    }

    private void readInitialize_database() {
        dbAdapter_alarmdata.open();

        Cursor cursor=null;
        cursor=dbAdapter_alarmdata.getAlarmBasic_inilialized();
        if (cursor.getCount()==0) {
            Toast.makeText(getApplicationContext(),"null",Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(getApplicationContext(),"not null",Toast.LENGTH_SHORT).show();
        }
        if(cursor.moveToFirst()) {
            do {
                displayInitialized_alramBasic(cursor);
            }while (cursor.moveToNext());
        }

        cursor=dbAdapter_alarmdata.getAlarmDays_inilialized();
        if(cursor.moveToFirst()) {
            do {
                displayInitialize_alarmDays(cursor);
            }while (cursor.moveToNext());
        }

        //Cursor cursor;
        cursor=dbAdapter_alarmdata.getAlarmSnooze_inilialized();
        if(cursor.moveToFirst()) {
            do {
                displayInitialized_alarmSnooze(cursor);
            }while (cursor.moveToNext());
        }

        cursor=dbAdapter_alarmdata.getAlarmDate_inilialized();
        if(cursor.moveToFirst()) {
            do {
                displayInitilized_alramDate(cursor);
            }while (cursor.moveToNext());
        }

        cursor=dbAdapter_alarmdata.getAlarmTime_inilialized();
        if(cursor.moveToFirst()) {
            do {
                displayInitialized_alramTime(cursor);
            }while (cursor.moveToNext());
        }




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

        /*Cursor cursor=null;
        malarmData=new AlarmData();
        int i=0;
        int j=0;

        DBAdapter_alarmdata dbAdapter_alarmdata=new DBAdapter_alarmdata(getApplicationContext());
        dbAdapter_alarmdata.open();

        cursor=dbAdapter_alarmdata.getAlarmBasic_inilialized();
        if(cursor.moveToFirst()) {
            do {
                restoreAlarmBasic(cursor, malarmData,j);
                j++;
                //AlaramData_singletonClass.get(getApplicationContext()).getList_alarmData().add(malarmData);
            }while (cursor.moveToNext());
        }

        j=0;

        cursor=dbAdapter_alarmdata.getAlarmTime_inilialized();
        if(cursor.moveToFirst()) {
            do {
                malarmData=AlaramData_singletonClass.get(getApplicationContext()).getList_alarmData().get(i);
                //AlaramData_singletonClass.get(getApplicationContext()).getList_alarmData().remove(i);
                //AlaramData_singletonClass.get(getApplicationContext()).getList_alarmData().add(i,malarmData);
                restoreAlarmTime(cursor,malarmData,i,j);
                i++;
                j++;
            }while (cursor.moveToNext());
        }

        i=0;
        j=0;

        cursor=dbAdapter_alarmdata.getAlarmSnooze_inilialized();
        if(cursor.moveToFirst()) {
            do {
                malarmData=AlaramData_singletonClass.get(getApplicationContext()).getList_alarmData().get(i);
                restoreAlarmSnooze(cursor,malarmData,i,j);
                i++;
                j++;
            }while (cursor.moveToNext());
        }

        i=0;
        j=0;
        cursor=dbAdapter_alarmdata.getAlarmDate_inilialized();
        if(cursor.moveToFirst()) {
            do {
                malarmData = AlaramData_singletonClass.get(getApplicationContext()).getList_alarmData().get(i);
                restoreAlarmDate(cursor, malarmData, i,j);
                i++;
                j++;
            }while (cursor.moveToNext());
        }

        i=0;
        j=0;
        cursor=dbAdapter_alarmdata.getAlarmDays_inilialized();
        if(cursor.moveToFirst()) {
            do {
                malarmData=AlaramData_singletonClass.get(getApplicationContext()).getList_alarmData().get(i);
                restoreAlarmDays(cursor,malarmData,i,j);
                i++;
                j++;
            }while (cursor.moveToNext());
        }

        dbAdapter_alarmdata.close();*/
    }

    private void restoreAlarmBasic(Cursor cursor, AlarmData alarmData, int i) {
        /*//malarmData.setId(Integer.parseInt(cursor.getString(0)));
        malarmData.setId(cursor.getInt(0));
        //malarmData.setOnOff(Integer.parseInt(cursor.getString(1)));
        malarmData.setOnOff(cursor.getInt(1));
        malarmData.setAlarmType(cursor.getString(2));
        malarmData.setRingtonePatth(cursor.getString(3));
        malarmData.setAlarmTitle(cursor.getString(4));*/

        if(cursor.getInt(1)!=-1) {
            index[i]=cursor.getInt(0);
            alarmData.setId(cursor.getInt(0)-1);
            alarmData.setOnOff(Integer.parseInt(cursor.getString(1)));
            alarmData.setOnOff(cursor.getInt(1));
            alarmData.setAlarmType(cursor.getString(2));
            alarmData.setRingtonePatth(cursor.getString(3));
            alarmData.setAlarmTitle(cursor.getString(4));

            Toast.makeText(getApplicationContext(),cursor.getInt(0)+" ",Toast.LENGTH_SHORT).show();

            //AlaramData_singletonClass.get(getApplicationContext()).getList_alarmData().remove(alarmData.getId());
            AlaramData_singletonClass.get(getApplicationContext()).getList_alarmData().add(alarmData);

            /*if(AlaramData_singletonClass.get(getApplicationContext()).getList_alarmData().size()==0) {
                AlaramData_singletonClass.get(getApplicationContext()).getList_alarmData().add(alarmData);
            }else {
                AlaramData_singletonClass.get(getApplicationContext()).getList_alarmData().remove(cursor.getInt(0)-1);
                AlaramData_singletonClass.get(getApplicationContext()).getList_alarmData().add(cursor.getInt(0)-1 ,alarmData);
            }*/
        }
    }

    private void restoreAlarmTime(Cursor cursor, AlarmData alarmData, int i, int j) {

        if(cursor.getInt(0)==index[j]) {

            Date date = new Date();
            date.setSeconds(0);

            date.setHours(cursor.getInt(1));
            date.setMinutes(cursor.getInt(2));

            alarmData.setAlarmTime(date);

            AlaramData_singletonClass.get(getApplicationContext()).getList_alarmData().remove(i);
            AlaramData_singletonClass.get(getApplicationContext()).getList_alarmData().add(i, alarmData);
        }

    }

    private void restoreAlarmSnooze(Cursor cursor, AlarmData alarmData, int i, int k) {
        int[] snooze=new int[2];
        for(int j=0;j<2;j++) {
            snooze[j]=-1;
        }

        if(cursor.getInt(0)==index[k]) {

            snooze[0] = cursor.getInt(1);
            snooze[1] = cursor.getInt(2);

            alarmData.setAlarmSnooze(snooze);

            AlaramData_singletonClass.get(getApplicationContext()).getList_alarmData().remove(i);
            AlaramData_singletonClass.get(getApplicationContext()).getList_alarmData().add(i, alarmData);
        }
    }

    private void restoreAlarmDate(Cursor cursor, AlarmData alarmData, int i, int j) {
        Calendar calendar=Calendar.getInstance();

        int day,month,year;

        if(cursor.getInt(0)==index[j]) {

            day = cursor.getInt(1);
            month = cursor.getInt(2);
            year = cursor.getInt(3);

            calendar.set(year, month, day);

            alarmData.setAlarmDate(calendar);

            AlaramData_singletonClass.get(getApplicationContext()).getList_alarmData().remove(i);
            AlaramData_singletonClass.get(getApplicationContext()).getList_alarmData().add(i, alarmData);
        }
    }

    private void restoreAlarmDays(Cursor cursor, AlarmData alarmData, int i, int k) {
        String[] days=new String[7];
        for(int j=0;j<7;j++) {
            days[i]=null;
        }

        if(cursor.getInt(0)==index[k]) {

            if (cursor.getInt(1) == 1) {
                days[0] = "Monday";
            }

            if (cursor.getInt(2) == 1) {
                days[1] = "Tuesday";
            }

            if (cursor.getInt(3) == 1) {
                days[2] = "Wednesday";
            }

            if (cursor.getInt(4) == 1) {
                days[3] = "Thursday";
            }

            if (cursor.getInt(5) == 1) {
                days[4] = "Friday";
            }

            if (cursor.getInt(6) == 1) {
                days[5] = "Saturday";
            }

            if (cursor.getInt(7) == 1) {
                days[6] = "Sunday";
            }

            alarmData.setAlarmDays(days);

            AlaramData_singletonClass.get(getApplicationContext()).getList_alarmData().remove(i);
            AlaramData_singletonClass.get(getApplicationContext()).getList_alarmData().add(i, alarmData);
        }
    }

    private void deleteAlarm(int id) {
        dbAdapter_alarmdata.open();

        dbAdapter_alarmdata.updateAlarmBasic(id,-1,"0","0","0");

        dbAdapter_alarmdata.updateAlarmDays(id,-1,-1,-1,-1,-1,-1,-1);

        dbAdapter_alarmdata.updateAlarmSnooze(id,-1,-1);

        dbAdapter_alarmdata.updateAlarmDate(id,-1,-1,-1);

        dbAdapter_alarmdata.updateAlarmTime(id,-1,-1);

        dbAdapter_alarmdata.close();
    }


    private void readData(AlarmData alarmData) {

        Toast.makeText(getApplicationContext(),"Title: "+alarmData.getAlarmTitle(),Toast.LENGTH_SHORT).show();

        Toast.makeText(getApplicationContext(),"Path: "+alarmData.getRingtonePatth(),Toast.LENGTH_SHORT).show();

        Toast.makeText(getApplicationContext(),"Type: "+alarmData.getAlarmType(),Toast.LENGTH_SHORT).show();

        for(int i=0;i<2;i++) {
            int[] data=new int[2];
            data=alarmData.getAlarmSnooze();
            Toast.makeText(getApplicationContext(),"snooze 1: "+data[i]+" ",Toast.LENGTH_SHORT).show();
        }

        Toast.makeText(getApplicationContext(),"onOff: "+alarmData.getOnOff()+" ",Toast.LENGTH_SHORT).show();

        Calendar icalendar=alarmData.getAlarmDate();
        int d,m,y;
        d=icalendar.get(Calendar.DAY_OF_MONTH);
        m=icalendar.get(Calendar.MONTH);
        y=icalendar.get(Calendar.YEAR);
        Toast.makeText(getApplicationContext(),d+" "+m+" "+y,Toast.LENGTH_SHORT).show();

        int h,min;
        Date idate=alarmData.getAlarmTime();
        h=idate.getHours();
        min=idate.getMinutes();
        Toast.makeText(getApplicationContext(),h+" "+min,Toast.LENGTH_SHORT).show();

        String[] strings=new String[7];
        strings=alarmData.getAlarmDays();
        for(int i=0;i<7;i++) {
            Toast.makeText(getApplicationContext(), strings[i]+" ",Toast.LENGTH_SHORT).show();
        }

    }

    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==99) {
            onStop();
            adapter_recycleView.notifyDataSetChanged();
        }
    }*/

    private void alarm_onOff() {
        for(AlarmData alarmData:AlaramData_singletonClass.get(getApplicationContext()).getList_alarmData()) {
            if(alarmData.getOnOff()==1) {

                Calendar calenderCurrent=Calendar.getInstance();
                Date dateCurrent=calenderCurrent.getTime();

                //Date dateCurrent=new Date();


                Date date;
                date=alarmData.getAlarmTime();
                //date.setHours(21);
                //date.setMinutes(8);
                date.setSeconds(0);

                Calendar calendar;
                calendar=alarmData.getAlarmDate();
                calendar.setTime(date);

                /*if((calenderCurrent.get(Calendar.DAY_OF_MONTH)==calendar.get(Calendar.DAY_OF_MONTH) &&
                        calenderCurrent.get(Calendar.MONTH)==calendar.get(Calendar.MONTH) &&
                        calenderCurrent.get(Calendar.YEAR )==calendar.get(Calendar.YEAR) &&
                        dateCurrent.getHours()==date.getHours() && dateCurrent.getMinutes()>date.getMinutes()) ||
                        (calenderCurrent.get(Calendar.DAY_OF_MONTH)==calendar.get(Calendar.DAY_OF_MONTH) &&
                                calenderCurrent.get(Calendar.MONTH)==calendar.get(Calendar.MONTH) &&
                                calenderCurrent.get(Calendar.YEAR )==calendar.get(Calendar.YEAR) &&
                                dateCurrent.getHours()>date.getHours() && dateCurrent.getMinutes()<date.getMinutes())
                        ||
                        (calenderCurrent.get(Calendar.DAY_OF_MONTH)==calendar.get(Calendar.DAY_OF_MONTH) &&
                                calenderCurrent.get(Calendar.MONTH)==calendar.get(Calendar.MONTH) &&
                                calenderCurrent.get(Calendar.YEAR )==calendar.get(Calendar.YEAR) &&
                                dateCurrent.getHours()>date.getHours() && dateCurrent.getMinutes()>date.getMinutes()))*/

                //Toast.makeText(this, date.getHours()+":"+date.getMinutes(), Toast.LENGTH_SHORT).show();

                //mpendingIntent=PendingIntent.getBroadcast(getApplicationContext(),alarmData.getId(),malarmIntent,
                        //PendingIntent.FLAG_CANCEL_CURRENT);
                //mpendingIntent=PendingIntent.getBroadcast(getApplicationContext(),alarmData.getId(),malarmIntent,
                        //PendingIntent.FLAG_UPDATE_CURRENT);
                malarmIntent.putExtra("index",alarmData.getId()+1);
                //Toast.makeText(this, alarmData.getId()+" ", Toast.LENGTH_SHORT).show();
                mpendingIntent=PendingIntent.getBroadcast(getApplicationContext(),alarmData.getId(),malarmIntent,0);
                //malarmManager.set(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),mpendingIntent);

               /* if((calenderCurrent.get(Calendar.DAY_OF_MONTH)==calendar.get(Calendar.DAY_OF_MONTH) &&
                        calenderCurrent.get(Calendar.MONTH)==calendar.get(Calendar.MONTH) &&
                        calenderCurrent.get(Calendar.YEAR )==calendar.get(Calendar.YEAR) &&
                        dateCurrent.getHours()==date.getHours() && dateCurrent.getMinutes()<=date.getMinutes()) ||
                        (calenderCurrent.get(Calendar.DAY_OF_MONTH)==calendar.get(Calendar.DAY_OF_MONTH) &&
                                calenderCurrent.get(Calendar.MONTH)==calendar.get(Calendar.MONTH) &&
                                calenderCurrent.get(Calendar.YEAR )==calendar.get(Calendar.YEAR) &&
                                dateCurrent.getHours()<date.getHours() && dateCurrent.getMinutes()<date.getMinutes())
                        ||
                        (calenderCurrent.get(Calendar.DAY_OF_MONTH)==calendar.get(Calendar.DAY_OF_MONTH) &&
                                calenderCurrent.get(Calendar.MONTH)==calendar.get(Calendar.MONTH) &&
                                calenderCurrent.get(Calendar.YEAR )==calendar.get(Calendar.YEAR) &&
                                dateCurrent.getHours()<date.getHours() && dateCurrent.getMinutes()>date.getMinutes())) {


                }*/

                if (android.os.Build.VERSION.SDK_INT >= 19) {
                    malarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), mpendingIntent);
                    /*if(date.getHours()==dateCurrent.getHours() && date.getMinutes()==dateCurrent.getMinutes()) {
                        malarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), mpendingIntent);
                    }*/
                } else {
                    /*if(date.getMinutes()>dateCurrent.getMinutes()) {
                        malarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), mpendingIntent);
                    }*/
                    malarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), mpendingIntent);
                }

                /*PowerManager pm = (PowerManager) getApplicationContext().getSystemService(Context.POWER_SERVICE);
                PowerManager.WakeLock wakeLock = pm.newWakeLock((PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP), "TAG");
                wakeLock.acquire();*/

            }else if(alarmData.getOnOff()==0) {

                mpendingIntent= PendingIntent.getBroadcast(getApplicationContext(), alarmData.getId(),
                        malarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                if(mpendingIntent!=null) {
                    mpendingIntent.cancel();
                    malarmManager.cancel(mpendingIntent);
                }
            }
        }



    }

    private void cancelAlarms() {
        /*mpendingIntent=PendingIntent.getBroadcast(getApplicationContext(),0,malarmIntent,PendingIntent.FLAG_NO_CREATE);

        if(mpendingIntent!=null) {
            Toast.makeText(this, "not null", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(this, "null", Toast.LENGTH_SHORT).show();
        }*/

        for(int i=0;i<10;i++) {
            mpendingIntent = PendingIntent.getBroadcast(getApplicationContext(), i,
                    malarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            if (mpendingIntent != null) {
                mpendingIntent.cancel();
                malarmManager.cancel(mpendingIntent);
            }
        }
    }
}
