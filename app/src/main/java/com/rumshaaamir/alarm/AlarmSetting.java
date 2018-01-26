package com.rumshaaamir.alarm;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.format.Time;
import android.text.style.ForegroundColorSpan;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.time.DayOfWeek;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

/**
 * Created by Rumsha Aamir on 1/24/2018.
 */

public class AlarmSetting extends AppCompatActivity {

    public String malarmType=null;

    private String TAG="AlarmSetting";

    final int mPERMISSION_REQUESTCODE=1;

    int mstatement=0;

    private EditText meditText_alarmTone;

    final  int mrepeatAlarm_requestCode=2;

    final int msnoozeAlarm_requestCode=3;

    private TextView mtextView_displayAlarm_time;
    private EditText meditText_title;

    private EditText editText_desAlarm;

    //data for calender
    int hour;
    int min;
    int year;
    int month;
    int day;

    //data for days
    static String[] stringsDays;

    private int[] snoozeValues;

    static Date date=new Date();
    static Calendar calendar=Calendar.getInstance();

    private String mtonePath;

    private AlarmData malarmData;

    String TAG_activityPreview="ActivityPreviewAlarm";

    int mHasData=-1;

    private AlarmData malarmDataP;

    int mindex=-1;

    private Button mbutton_save;

    DBAdapter_alarmdata dbAdapter_alarmdata;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alaramset);

        malarmDataP=new AlarmData();

        mindex=getIntent().getIntExtra(TAG_activityPreview,-1);

        //setting title for action bar and also change the color of the text on title bar
        String titleBar="Set Alarm";
        //setTitle(titleBar);

        if(mindex!=-1) {
            titleBar="Update Alarm";
            //mbutton_save.setText("Update");
        }

        SpannableString s = new SpannableString(titleBar);
        s.setSpan(new ForegroundColorSpan(Color.DKGRAY), 0, titleBar.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        getSupportActionBar().setTitle(s);



        initialize_snoozeValues();
        initialize_stingDays();

        //malarmDataP= (AlarmData) getIntent().getSerializableExtra(TAG_activityPreview);

        dbAdapter_alarmdata=new DBAdapter_alarmdata(this);


        mtextView_displayAlarm_time=(TextView)findViewById(R.id.dateDisplay_textView);
        //meditText_title=(EditText)findViewById(R.id.alramDescription_EditText);

        editText_desAlarm=(EditText)findViewById(R.id.alramDescription_EditText);
        //editText_desAlarm.setTag(editText_desAlarm.getKeyListener());
        //editText_desAlarm.setKeyListener(null);
        editText_desAlarm.setEnabled(false);
        editText_desAlarm.setFocusable(false);

        meditText_alarmTone=(EditText)findViewById(R.id.tonealram_editText_aas);
        meditText_alarmTone.setEnabled(false);
        meditText_alarmTone.setFocusable(false);

        mtonePath=getResources().getResourceName(R.raw.default_tone);
        String tone=getResources().getString(R.string.defaulttone);
        meditText_alarmTone.setText(tone);

        mbutton_save=(Button)findViewById(R.id.save_button_asa);


        //Toast.makeText(getApplicationContext(),"Has data of index: "+mindex,Toast.LENGTH_SHORT).show();
        //Toast.makeText(getApplicationContext(),mindex,Toast.LENGTH_SHORT).show();
        //malarmDataP=AlaramData_singletonClass.get(getApplicationContext()).getList_alarmData().get(mindex);
        //readData(malarmDataP);

        if(mindex==-1) {
            //Toast.makeText(getApplicationContext(),"Null",Toast.LENGTH_SHORT).show();
            mHasData=0;
        }else {
            //Toast.makeText(getApplicationContext(),"Has data of index: "+mindex,Toast.LENGTH_SHORT).show();
            //Date date=malarmDataP.getAlarmTime();
            //int h=date.getHours();
            //int m=date.getMinutes();
            //Toast.makeText(getApplicationContext(),h+":"+m,Toast.LENGTH_SHORT).show();

            mbutton_save.setText("Update");

            mHasData=1;

            malarmDataP=AlaramData_singletonClass.get(getApplicationContext()).getList_alarmData().get(mindex);
            //readData(malarmDataP);
            populateDate(malarmDataP);
        }

        //button setting alarm type
        final Button button_alarmType=(Button)findViewById(R.id.typeAlram_button);
        button_alarmType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu_alarmType=new PopupMenu(AlarmSetting.this,button_alarmType);
                popupMenu_alarmType.getMenuInflater().inflate(R.menu.menu_popup_alarmtype_alarmset,popupMenu_alarmType.getMenu());

                popupMenu_alarmType.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.simpleAlarm_item_popup_aas:
                                malarmType="Simple Alarm";
                                break;
                            case R.id.mathAlarm_item_popup_aas:
                                malarmType="Math Alarm";
                                break;
                            case R.id.mapAlarm_item_popup_aas:
                                malarmType="Location Alarm";
                                break;
                        }
                        Toast.makeText(AlarmSetting.this,malarmType+" selected!",Toast.LENGTH_SHORT).show();
                        return true;
                    }
                });

                popupMenu_alarmType.show();
            }
        });

        //button setting repeat alarm
        Button button_repeatAlarm=(Button)findViewById(R.id.repeatAlram_button);
        button_repeatAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_startActivity=new Intent(getApplication(),ActivityRepeatAlarm.class);
                startActivityForResult(intent_startActivity,mrepeatAlarm_requestCode);
            }
        });

        //button setting snoze alarm
        Button button_snozeAlarm=(Button)findViewById(R.id.snozeAlarm_button);
        button_snozeAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_startActivity=new Intent(getApplication(),ActivitySnozeAlarm.class);
                startActivityForResult(intent_startActivity,msnoozeAlarm_requestCode);
            }
        });

        //EditText editText_desAlarm=(EditText)findViewById(R.id.alramDescription_textView_aas);
        Button button_desAlarm=(Button)findViewById(R.id.alarmDesciption_button);
        button_desAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editText_desAlarm.setFocusable(true);
                editText_desAlarm.setEnabled(true);
                editText_desAlarm.setFocusableInTouchMode(true);
            }
        });


        //time picker button
        Button button_timePicker=(Button)findViewById(R.id.time_button);
        button_timePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DialogFragment newFragment = new TimePickerDialogeFregment();
                newFragment.show(getSupportFragmentManager(),"TimePicker");


            }
        });

        //date picker button
        Button button_datePicker=(Button)findViewById(R.id.calender_button);
        button_datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DialogFragment newFragment = new CalenderPickerDialogeFragment();
                newFragment.show(getSupportFragmentManager(),"DatePicker");
            }
        });


        //ring tone picker button
        final Button button_tonePicker=(Button)findViewById(R.id.soundAlram_button);
        button_tonePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //play();

                /*Intent intent_tonePicker=new Intent();
                intent_tonePicker.setAction(Intent.ACTION_GET_CONTENT);
                intent_tonePicker.setType("audio./*");
                startActivityForResult(Intent.createChooser(intent_tonePicker,"Choose Sound File"),6);*/

                /*Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_ALARM);
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "Select Tone");
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, (Uri) null);
                startActivityForResult(intent, 5);*/

                /*Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "Select ringtone for notifications:");
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_SILENT, false);
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, true);
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE,RingtoneManager.TYPE_RINGTONE);
                startActivityForResult(intent,999);*/

                /*PopupMenu popupMenu_alarmTone=new PopupMenu(AlarmSetting.this,button_tonePicker);
                popupMenu_alarmTone.getMenuInflater().inflate(R.menu.menu_pop_ringtonetype_alarmset,popupMenu_alarmTone.getMenu());

                popupMenu_alarmTone.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        String type=null;

                        Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
                        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "Select ringtone for notifications:");
                        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_SILENT, false);
                        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, true);
                        //intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE,RingtoneManager.TYPE_NOTIFICATION);
                        //startActivityForResult(intent,999);

                        switch (item.getItemId()) {
                            case R.id.alarmTone_item:
                                type="RingtoneManager.TYPE_ALARM";

                                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE,type);
                                startActivityForResult(intent,999);

                                return true;
                            case R.id.ringTone_item:
                                type="RingtoneManager.TYPE_RINGTONE";

                                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE,type);
                                startActivityForResult(intent,999);
                                return true;

                            case  R.id.notificationTone_item:
                                type="TYPE_NOTIFICATION";
                                //String string="TYPE_ALL";

                                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE,type);
                                startActivityForResult(intent,999);
                                return true;
                            default:
                                return false;
                        }

                    }
                });

                popupMenu_alarmTone.show();*/

                checkPermission_externalStorage(button_tonePicker);

            }


        });



        mbutton_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(precaution()) {

                    if(mHasData==1) {
                       malarmData=new AlarmData();

                        //Toast.makeText(getApplicationContext(),"has value: "+mHasData,Toast.LENGTH_SHORT).show();

                        //Toast.makeText(getApplicationContext(), "Previous data before!", Toast.LENGTH_LONG).show();
                        //readData(malarmDataP);

                        //AlaramData_singletonClass.get(getApplicationContext()).getList_alarmData().remove(malarmDataP);

                        //Toast.makeText(getApplicationContext(), "Previous data after!", Toast.LENGTH_LONG).show();
                        //Toast.makeText(getApplicationContext(), "Index: "+AlaramData_singletonClass.get(getApplicationContext()).getList_alarmData().indexOf(malarmDataP)+" ", Toast.LENGTH_LONG).show();


                        updateData();

                        int hour=date.getHours();
                        int min=date.getMinutes();

                        if (AlaramData_singletonClass.get(getApplicationContext()).compareTime(malarmData)) {
                            //Toast.makeText(getApplicationContext(), "same time", Toast.LENGTH_SHORT).show();
                        } else {
                            //Toast.makeText(getApplicationContext(), "different time", Toast.LENGTH_SHORT).show();
                        }

                        if (AlaramData_singletonClass.get(getApplicationContext()).compareTime(date)) {
                            //Toast.makeText(getApplicationContext(), "different time", Toast.LENGTH_SHORT).show();
                        } else {
                            //Toast.makeText(getApplicationContext(), "same time", Toast.LENGTH_SHORT).show();
                        }

                        //if(AlaramData_singletonClass.get(getApplicationContext(),com))

                        int day=calendar.get(Calendar.DAY_OF_MONTH);
                        int month=calendar.get(Calendar.MONTH);
                        int year=calendar.get(Calendar.YEAR);

                        if (AlaramData_singletonClass.get(getApplicationContext()).compareDate(day,month,year)) {
                            //Toast.makeText(getApplicationContext(), "different Date", Toast.LENGTH_SHORT).show();
                        } else {
                            //Toast.makeText(getApplicationContext(), "same Date", Toast.LENGTH_SHORT).show();
                        }

                        if(!AlaramData_singletonClass.get(getApplicationContext()).compareDays(stringsDays)) {
                            //Toast.makeText(getApplicationContext(), "diffeent Days", Toast.LENGTH_SHORT).show();
                        }else {
                            //Toast.makeText(getApplicationContext(), "same Days", Toast.LENGTH_SHORT).show();
                        }

                        //deleteAlarm(mindex+1);
                        AlaramData_singletonClass.get(getApplicationContext()).getList_alarmData().remove(mindex);

                        AlaramData_singletonClass.get(getApplicationContext()).getList_alarmData().indexOf(malarmData);

                        //Toast.makeText(AlarmSetting.this, AlaramData_singletonClass.get(getApplicationContext()).getList_alarmData().indexOf(malarmDataP)
                        //+" ", Toast.LENGTH_SHORT).show();


                        getIndex();

                        //AlaramData_singletonClass.get(getApplicationContext()).getList_alarmData().add(malarmData);

                        if((AlaramData_singletonClass.get(getApplicationContext()).compareTime(malarmData) &&
                                AlaramData_singletonClass.get(getApplicationContext()).compareDate(day,month,year)) ||
                                (AlaramData_singletonClass.get(getApplicationContext()).compareTime(malarmData) &&
                                        AlaramData_singletonClass.get(getApplicationContext()).compareDays(stringsDays))) {

                            Toast.makeText(getApplicationContext(),"Alarm Updated!",Toast.LENGTH_SHORT).show();
                        }else {
                            //Toast.makeText(getApplicationContext(),"Not Occurence!",Toast.LENGTH_SHORT).show();
                            AlaramData_singletonClass.get(getApplicationContext()).add(malarmData);
                        }


                        /*for(AlarmData alarmData: AlaramData_singletonClass.get(getApplicationContext()).getList_alarmData()) {
                            readData(alarmData);
                        }*/

                        /*if((AlaramData_singletonClass.get(getApplicationContext()).compareTime(malarmData) &&
                                AlaramData_singletonClass.get(getApplicationContext()).compareDate(day,month,year)) ||
                                (AlaramData_singletonClass.get(getApplicationContext()).compareTime(malarmData) &&
                                        AlaramData_singletonClass.get(getApplicationContext()).compareDays(stringsDays))) {

                            Toast.makeText(getApplicationContext(),"Old Alarm content saved!",Toast.LENGTH_SHORT).show();
                        }else {
                            //Toast.makeText(getApplicationContext(),"Not Occurence!",Toast.LENGTH_SHORT).show();
                            AlaramData_singletonClass.get(getApplicationContext()).add(malarmData);
                            Toast.makeText(getApplicationContext(),"Alarm update!",Toast.LENGTH_SHORT).show();
                            //AlaramData_singletonClass.get(getApplicationContext()).getList_alarmData().remove(mindex);
                        }*/

                        //Toast.makeText(getApplication(),"Orignal data!",Toast.LENGTH_SHORT).show();
                        //readData(malarmDataP);
                        /*if(AlaramData_singletonClass.get(getApplicationContext()).getList_alarmData().contains(malarmDataP)) {
                            Toast.makeText(getApplicationContext(), "FOund", Toast.LENGTH_LONG).show();
                        }else {
                            Toast.makeText(getApplicationContext(), "not Found", Toast.LENGTH_LONG).show();
                        }*/
                        //Toast.makeText(getApplicationContext(),"Copied data!",Toast.LENGTH_SHORT).show();
                        //readData(malarmData);
                        //Toast.makeText(getApplication(),"Index: "+AlaramData_singletonClass.get(getApplicationContext()).
                                //getList_alarmData().indexOf(malarmData),Toast.LENGTH_SHORT).show();
                        /*if(AlaramData_singletonClass.get(getApplication()).getList_alarmData().contains(malarmData)) {
                            Toast.makeText(getApplicationContext(), "FOund", Toast.LENGTH_LONG).show();
                        }else {
                            Toast.makeText(getApplicationContext(), "not Found", Toast.LENGTH_LONG).show();
                        }*/
                        /*if(mindex==-1) {
                            Toast.makeText(getApplicationContext(), "Alarm saved successfully!", Toast.LENGTH_LONG).show();
                        }
                        if(compareStrings(malarmData,malarmDataP)==true && compareDate(malarmData,malarmDataP)==true &&
                                compareTime(malarmData,malarmDataP)==true && compareDays(malarmData,malarmDataP)==true &&
                                compareSnooze(malarmData,malarmDataP)==true) {
                            //Toast.makeText(getApplicationContext(),"Found match",Toast.LENGTH_SHORT).show();
                            Toast.makeText(getApplicationContext(),"No change in data, Unable to update!",Toast.LENGTH_SHORT).show();
                        }else {
                            //Toast.makeText(getApplicationContext(),"no match",Toast.LENGTH_SHORT).show();
                            Toast.makeText(getApplicationContext(),"Alarm Updated!",Toast.LENGTH_SHORT).show();
                            AlaramData_singletonClass.get(getApplicationContext()).getList_alarmData().remove(mindex);
                            AlaramData_singletonClass.get(getApplicationContext()).getList_alarmData().add(malarmData);

                            mtextView_displayAlarm_time.setText(" ");
                            mtextView_displayAlarm_time=null;
                            malarmType=null;

                            Intent intent_startActivity=new Intent(AlarmSetting.this,MainActivity.class);
                            startActivity(intent_startActivity);
                        }*/



                        //Toast.makeText(getApplicationContext(),"Updated!",Toast.LENGTH_SHORT).show();

                        //readData(malarmData);

                        /*if(malarmData.equals(malarmDataP)) {
                            Toast.makeText(getApplicationContext(),"Data same!",Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(getApplicationContext(),"Data not same!",Toast.LENGTH_SHORT).show();
                        }*/

                        //AlaramData_singletonClass.get(getApplicationContext()).getList_alarmData().remove(mindex);
                        //Toast.makeText(getApplicationContext(), mindex+" ", Toast.LENGTH_LONG).show();
                        //AlaramData_singletonClass.get(getApplicationContext()).getList_alarmData().add(malarmData);

                        mtextView_displayAlarm_time.setText(" ");
                        mtextView_displayAlarm_time=null;
                        malarmType=null;

                        calendar=Calendar.getInstance();
                        date=new Date();

                        Intent intent_startActivity=new Intent(AlarmSetting.this,MainActivity.class);
                        startActivity(intent_startActivity);

                        /*Intent intent_startActivity=new Intent(this,MainActivity.class);
                        setResult(99,intent_startActivity);
                        finish();*/


                    }else if(mHasData==0) {

                        saveData();
                        //malarmData=new AlarmData();

                        //Toast.makeText(getApplicationContext(),"has value: "+mHasData,Toast.LENGTH_SHORT).show();
                        //AlaramData_singletonClass.get(getApplicationContext()).add(malarmData);



                        //readData(malarmData);

                        if(AlaramData_singletonClass.get(getApplicationContext()).getList_alarmData().contains(malarmData)) {
                            //Toast.makeText(getApplicationContext(),"has",Toast.LENGTH_SHORT).show();
                        }

                        for(AlarmData alarmData:AlaramData_singletonClass.get(getApplicationContext()).getList_alarmData()) {
                            //readData(alarmData);
                        }

                        if(!AlaramData_singletonClass.get(getApplicationContext()).getList_alarmData().isEmpty()) {

                            int hour=date.getHours();
                            int min=date.getMinutes();

                            if (AlaramData_singletonClass.get(getApplicationContext()).compareTime(malarmData)) {
                                //Toast.makeText(getApplicationContext(), "same time", Toast.LENGTH_SHORT).show();
                            } else {
                                //Toast.makeText(getApplicationContext(), "different time", Toast.LENGTH_SHORT).show();
                            }

                            if (AlaramData_singletonClass.get(getApplicationContext()).compareTime(date)) {
                                //Toast.makeText(getApplicationContext(), "different time", Toast.LENGTH_SHORT).show();
                            } else {
                                //Toast.makeText(getApplicationContext(), "same time", Toast.LENGTH_SHORT).show();
                            }

                            //if(AlaramData_singletonClass.get(getApplicationContext(),com))

                            int day=calendar.get(Calendar.DAY_OF_MONTH);
                            int month=calendar.get(Calendar.MONTH);
                            int year=calendar.get(Calendar.YEAR);

                            if (AlaramData_singletonClass.get(getApplicationContext()).compareDate(day,month,year)) {
                                //Toast.makeText(getApplicationContext(), "different Date", Toast.LENGTH_SHORT).show();
                            } else {
                                //Toast.makeText(getApplicationContext(), "same Date", Toast.LENGTH_SHORT).show();
                            }

                            if(!AlaramData_singletonClass.get(getApplicationContext()).compareDays(stringsDays)) {
                                //Toast.makeText(getApplicationContext(), "diffeent Days", Toast.LENGTH_SHORT).show();
                            }else {
                                //Toast.makeText(getApplicationContext(), "same Days", Toast.LENGTH_SHORT).show();
                            }

                            if((AlaramData_singletonClass.get(getApplicationContext()).compareTime(malarmData) &&
                                    AlaramData_singletonClass.get(getApplicationContext()).compareDate(day,month,year)) ||
                                    (AlaramData_singletonClass.get(getApplicationContext()).compareTime(malarmData) &&
                                            AlaramData_singletonClass.get(getApplicationContext()).compareDays(stringsDays))) {

                                Toast.makeText(getApplicationContext(),"Clash!",Toast.LENGTH_SHORT).show();
                            }else {
                                //Toast.makeText(getApplicationContext(),"Not Occurence!",Toast.LENGTH_SHORT).show();
                                AlaramData_singletonClass.get(getApplicationContext()).add(malarmData);
                            }

                        }else if(AlaramData_singletonClass.get(getApplicationContext()).getList_alarmData().isEmpty()) {
                            AlaramData_singletonClass.get(getApplicationContext()).add(malarmData);
                        }






                        Toast.makeText(getApplicationContext(), "Alarm saved successfully!", Toast.LENGTH_LONG).show();

                        mtextView_displayAlarm_time.setText(" ");
                        mtextView_displayAlarm_time=null;
                        malarmType=null;

                        calendar=Calendar.getInstance();
                        date=new Date();

                        //Toast.makeText(getApplicationContext(),"saved!",Toast.LENGTH_SHORT).show();

                        //readData(malarmData);

                        Intent intent_startActivity=new Intent(AlarmSetting.this,MainActivity.class);
                        startActivity(intent_startActivity);


                    }


                }else {
                    Toast.makeText(getApplicationContext(), "Unable to save Alarm!", Toast.LENGTH_LONG).show();
                }
            }
        });

        Button button_cancel=(Button)findViewById(R.id.cancel_button_asa);
        button_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Canceled Alarm!", Toast.LENGTH_LONG).show();
                finish();
            }
        });


        //rough work
        //TimePicker timePicker=(TimePicker)findViewById(R.id.timePicker);
        //timePicker.setIs24HourView(true)
        //;


        /*AlarmData alarmData=AlaramData_singletonClass.get(getApplicationContext()).get_alarmData(1);

        Toast.makeText(getApplicationContext(),alarmData.getId()+" ",Toast.LENGTH_SHORT).show();*/



        //Toast.makeText(getApplicationContext(),val+" ",Toast.LENGTH_SHORT).show();






    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.menu_pop_ringtonetype_alarmset,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        String type=null;

        Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "Select ringtone for notifications:");
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_SILENT, false);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, true);
        //intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE,RingtoneManager.TYPE_NOTIFICATION);
        //startActivityForResult(intent,999);

        switch (item.getItemId()) {
            case R.id.alarmTone_item:
                type="RingtoneManager.TYPE_ALARM";

                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE,type);
                startActivityForResult(intent,999);

                return true;
            case R.id.ringTone_item:
                type="RingtoneManager.TYPE_RINGTONE";

                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE,type);
                startActivityForResult(intent,999);
                return true;

            case  R.id.notificationTone_item:
                type="TYPE_NOTIFICATION";

                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE,type);
                startActivityForResult(intent,999);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }*/

    @Override
    protected void onResume() {
        super.onResume();
        //Toast.makeText(this,time,Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode==RESULT_OK && requestCode==5) {
            //Toast.makeText(getApplicationContext(),"Get",Toast.LENGTH_SHORT).show();
        }

        if(resultCode!=RESULT_OK && requestCode==999) {

        }

        if(resultCode==RESULT_OK && requestCode==999) {
            //Toast.makeText(this, "null", Toast.LENGTH_SHORT).show();
            Uri uri = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
            Ringtone ringtone = RingtoneManager.getRingtone(this, uri);
            String title = ringtone.getTitle(this);
            Toast.makeText(this, title, Toast.LENGTH_SHORT).show();


            if(Objects.equals(title, "Unknown ringtone")) {
                mtonePath=getResources().getResourceName(R.raw.default_tone);
                title=getResources().getString(R.string.defaulttone);
                meditText_alarmTone.setText(title);
                Toast.makeText(getApplicationContext(), title, Toast.LENGTH_LONG).show();

                //MediaPlayer media=MediaPlayer.create(getApplicationContext(),R.raw.default_tone);
                //media.start();

            }else {

                mtonePath=uri.toString();
                meditText_alarmTone.setText(title);
                Toast.makeText(getApplicationContext(), title, Toast.LENGTH_LONG).show();
            }


            /*if (uri != null) {
                mtonePath = uri.toString();

                //Uri myUrie = Uri.parse(Environment.getExternalStorageDirectory().getPath()+ringTonePath);

                //Toast.makeText(getApplicationContext(), "Path : "+ringTonePath, Toast.LENGTH_LONG).show();

                MediaPlayer mediaPlayer = new MediaPlayer();
                //mediaPlayer=null;


                //Uri myUri = uri; // initialize Uri here

                //mtonePath=uri.toString();

                //Toast.makeText(getApplicationContext(), "Path : "+mtonePath, Toast.LENGTH_LONG).show();

                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                if(mediaPlayer==null) {
                    //Toast.makeText(getApplicationContext(),"media player null",Toast.LENGTH_SHORT).show();
                }else {
                    //Toast.makeText(getApplicationContext(),"media player not null",Toast.LENGTH_SHORT).show();
                }
                try {
                    mediaPlayer.setDataSource(getApplicationContext(), uri);
                    mediaPlayer.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }



                mediaPlayer.start();
            }*/
        }

        if(requestCode==mrepeatAlarm_requestCode && resultCode!=Activity.RESULT_CANCELED) {
            //Toast.makeText(getApplicationContext(),"Got!",Toast.LENGTH_SHORT).show();
            if(resultCode== Activity.RESULT_OK) {
                //Toast.makeText(getApplicationContext(),"Got1!",Toast.LENGTH_SHORT).show();
            }

            String TAG_inner="REPEAT_ALARM";

            String days="Days";

            //Toast.makeText(getApplication(),data.getIntExtra(days,-1),Toast.LENGTH_SHORT).show();


            stringsDays=data.getStringArrayExtra(TAG_inner);

            //Toast.makeText(getApplication(), stringsDays.length + " ", Toast.LENGTH_LONG).show();

            for (int i = 0; i < stringsDays.length; i++) {
                //Toast.makeText(getApplicationContext(), stringsDays[i], Toast.LENGTH_LONG).show();
            }

            AlarmSetting.calendar.set(0,0,0);

            String Days;

            Days="Days : ";

            for (int i=0;i<7;i++) {
                if(stringsDays[i]!=null) {


                    Days += stringsDays[i];
                    Days+=" ";
                }
            }

            //int hour, min;

            //hour=AlarmSetting.date.getHours();
            //min=AlarmSetting.date.getMinutes();

            String time=null;

            if(MainActivity.TimeFormate(getApplicationContext())==24) {
                time=setTime_24Formate(AlarmSetting.date.getHours(),AlarmSetting.date.getMinutes());
            }else if(MainActivity.TimeFormate(getApplicationContext())==12) {
                time=setTime_12Formate(AlarmSetting.date.getHours(),AlarmSetting.date.getMinutes());
            }

            //time="Time: "+hour+" : "+min;


            mtextView_displayAlarm_time.setText(time+" ,"+Days);


            /*if(strings_nameDatys!=null) {


                Toast.makeText(getApplication(), strings_nameDatys.length + " ", Toast.LENGTH_LONG).show();

                for (int i = 0; i < strings_nameDatys.length; i++) {
                    Toast.makeText(getApplicationContext(), strings_nameDatys[i], Toast.LENGTH_LONG).show();
                }
            }*/
        }

        /*if(requestCode==mrepeatAlarm_requestCode) {
            Toast.makeText(getApplicationContext(),"Got!",Toast.LENGTH_SHORT).show();
            if(resultCode== Activity.RESULT_OK) {
                Toast.makeText(getApplicationContext(),"Got1!",Toast.LENGTH_SHORT).show();
            }

            String TAG_inner="REPEAT_ALARM";

            String days="Days";

            //Toast.makeText(getApplication(),data.getIntExtra(days,-1),Toast.LENGTH_SHORT).show();


            String []strings_nameDatys=data.getStringArrayExtra(days);

            Toast.makeText(getApplication(), strings_nameDatys.length + " ", Toast.LENGTH_LONG).show();

            for (int i = 0; i < strings_nameDatys.length; i++) {
                Toast.makeText(getApplicationContext(), strings_nameDatys[i], Toast.LENGTH_LONG).show();
            }

            /*if(strings_nameDatys!=null) {


                Toast.makeText(getApplication(), strings_nameDatys.length + " ", Toast.LENGTH_LONG).show();

                for (int i = 0; i < strings_nameDatys.length; i++) {
                    Toast.makeText(getApplicationContext(), strings_nameDatys[i], Toast.LENGTH_LONG).show();
                }
            }*/
        /*}*/

        if(requestCode==msnoozeAlarm_requestCode && resultCode!=Activity.RESULT_CANCELED) {
            //Toast.makeText(getApplicationContext(),"Got!",Toast.LENGTH_SHORT).show();
            if(resultCode==Activity.RESULT_OK) {
                //Toast.makeText(getApplicationContext(),"Got1!",Toast.LENGTH_SHORT).show();
            }

            String TAG_inner="SNOOZE_ALARM";

            snoozeValues=data.getIntArrayExtra(TAG_inner);


            for(int i=0;i<2;i++) {
                //Toast.makeText(getApplication(),"RETURN: "+snoozeValues[i]+" ",Toast.LENGTH_LONG).show();
            }

           //Toast.makeText(getApplication(),"called",Toast.LENGTH_LONG).show();
        }
    }

    void play() {
        String fileName = "Basic Bell.mp3";
        String completePath = Environment.getDataDirectory()+ "/" + fileName;

        //getExternalStorageDirectory()

        File file = new File(completePath);

        Uri myUri1 = Uri.fromFile(file);
        MediaPlayer mPlayer = new MediaPlayer();
        mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mPlayer.setDataSource(getApplicationContext(), myUri1);
        } catch (IllegalArgumentException e) {
            Toast.makeText(getApplicationContext(), "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
        } catch (SecurityException e) {
            Toast.makeText(getApplicationContext(), "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
        } catch (IllegalStateException e) {
            Toast.makeText(getApplicationContext(), "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            mPlayer.prepare();
        } catch (IllegalStateException e) {
            Toast.makeText(getApplicationContext(), "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(), "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
        }
        mPlayer.start();
    }

    private void checkPermission_externalStorage(Button button_tonePicker) {

        if(Build.VERSION.SDK_INT>=23) {

            if(checkPermission()) {
                //Toast.makeText(getApplication(),"Permission already granted",Toast.LENGTH_LONG).show();

                showMenu_toneType(button_tonePicker);
            }else {
                Toast.makeText(getApplication(),"Request for permission",Toast.LENGTH_LONG).show();
                requestPermission();
            }
        }else {
            Toast.makeText(getApplication(),"Permission not required",Toast.LENGTH_LONG).show();
            showMenu_toneType(button_tonePicker);
        }

    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(AlarmSetting.this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private void requestPermission() {

        /*if (ActivityCompat.shouldShowRequestPermissionRationale(AlarmSetting.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //ActivityCompat.requestPermissions(AlarmSetting.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            Toast.makeText(AlarmSetting.this, "Read External Storage permission allows us to do play External Files. " +
                    "Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
        } else {


        }*/

        //ActivityCompat.requestPermissions(AlarmSetting.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

        //if(ActivityCompat.requestPermissions(AlarmSetting.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1))

        ActivityCompat.requestPermissions(AlarmSetting.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

        if(mstatement==0 || mstatement==2) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(AlarmSetting.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Toast.makeText(AlarmSetting.this, "Read External Storage permission allows us to do play External Files. " +
                        "Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case mPERMISSION_REQUESTCODE:
                if(grantResults[0]== PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplication(),"Permission granted",Toast.LENGTH_LONG).show();
                    mstatement=1;
                }else {
                    Toast.makeText(getApplication(),"Permission not granted",Toast.LENGTH_LONG).show();
                    mstatement=2;
                }
                break;

        }



    }

    private void showMenu_toneType(Button button_tonePicker) {
        PopupMenu popupMenu_alarmTone=new PopupMenu(AlarmSetting.this,button_tonePicker);
        popupMenu_alarmTone.getMenuInflater().inflate(R.menu.menu_pop_ringtonetype_alarmset,popupMenu_alarmTone.getMenu());

        popupMenu_alarmTone.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                String type=null;

                Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "Select ringtone for notifications:");
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_SILENT, false);
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, true);
                //intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE,RingtoneManager.TYPE_NOTIFICATION);
                //startActivityForResult(intent,999);

                switch (item.getItemId()) {
                    case R.id.alarmTone_item:
                        type="RingtoneManager.TYPE_ALARM";

                        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE,type);
                        startActivityForResult(intent,999);

                        return true;
                    case R.id.defaultTone_item:
                        /*type="RingtoneManager.TYPE_RINGTONE";

                        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE,type);
                        startActivityForResult(intent,999);*/
                        mtonePath=getResources().getResourceName(R.raw.default_tone);
                        meditText_alarmTone.setText("Default Tone");
                        Toast.makeText(getApplicationContext(),mtonePath,Toast.LENGTH_SHORT).show();
                        return true;

                    /*case  R.id.notificationTone_item:
                        type="TYPE_NOTIFICATION";
                        //String string="TYPE_ALL";

                        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE,type);
                        startActivityForResult(intent,999);
                        return true;*/
                    default:
                        return false;
                }

            }
        });

        popupMenu_alarmTone.show();

    }

    private void initialize_snoozeValues() {
        snoozeValues=new int[2];
        for(int i=0;i<2;i++) {
            snoozeValues[i]=-1;
        }


    }

    private void initialize_stingDays() {
        stringsDays=new String[7];
        for(int i=0;i<7;i++) {
            stringsDays[i]=null;
        }
    }

    private boolean check_stringsDays() {

        int val=1;

        for(int i=0;i<7;i++) {
            if (stringsDays[i]!=null) {
                return true;
            }
        }

        return false;



    }

    static void destroy_stringDays() {
        for(int i=0;i<7;i++) {
            stringsDays[i]=null;
        }
    }

    private void saveData() {
        malarmData=new AlarmData();

        if(editText_desAlarm.getText()==null) {
            //Toast.makeText(getApplicationContext(),"Text check for null!",Toast.LENGTH_SHORT).show();
        }else {
            //Toast.makeText(getApplicationContext(),"Text check for not null: "+editText_desAlarm.getText().toString(),Toast.LENGTH_SHORT).show();
        }
        malarmData.setAlarmTitle(editText_desAlarm.getText().toString());
        //Toast.makeText(getApplicationContext(),"Title: "+malarmData.getAlarmTitle(),Toast.LENGTH_SHORT).show();
        malarmData.setRingtonePatth(mtonePath);
        Toast.makeText(getApplicationContext(),"Path: "+malarmData.getRingtonePatth(),Toast.LENGTH_SHORT).show();
        malarmData.setAlarmType(malarmType);
        //Toast.makeText(getApplicationContext(),"Type: "+malarmData.getAlarmType(),Toast.LENGTH_SHORT).show();
        malarmData.setAlarmSnooze(snoozeValues);
        for(int i=0;i<2;i++) {
            int[] data=new int[2];
            data=malarmData.getAlarmSnooze();
            //Toast.makeText(getApplicationContext(),"snooze 1: "+data[i]+" ",Toast.LENGTH_SHORT).show();
        }
        malarmData.setOnOff(1);
        //Toast.makeText(getApplicationContext(),"onOff: "+malarmData.getOnOff()+" ",Toast.LENGTH_SHORT).show();
        malarmData.setAlarmDate(calendar);
        Calendar icalendar=malarmData.getAlarmDate();
        int d,m,y;
        d=icalendar.get(Calendar.DAY_OF_MONTH);
        m=icalendar.get(Calendar.MONTH);
        y=icalendar.get(Calendar.YEAR);
        //Toast.makeText(getApplicationContext(),d+" "+m+" "+y,Toast.LENGTH_SHORT).show();
        malarmData.setAlarmTime(date);
        int h,min;
        Date idate=malarmData.getAlarmTime();
        h=idate.getHours();
        min=idate.getMinutes();
        //Toast.makeText(getApplicationContext(),h+" "+min,Toast.LENGTH_SHORT).show();
        malarmData.setAlarmDays(stringsDays);
        String[] strings=new String[7];
        strings=malarmData.getAlarmDays();
        for(int i=0;i<7;i++) {
            //Toast.makeText(getApplicationContext(), strings[i]+" ",Toast.LENGTH_SHORT).show();
        }

        //malarmData.setId(AlaramData_singletonClass.get(getApplicationContext()).getList_alarmData().size()+1);
        getIndex();
        //Toast.makeText(getApplication(),"id: "+malarmData.getId(),Toast.LENGTH_SHORT).show();

        //Toast.makeText(getApplication(),"size: "+AlaramData_singletonClass.get(getApplicationContext()).
                //getList_alarmData().size(),Toast.LENGTH_SHORT).show();

        //AlaramData_singletonClass.get(getApplicationContext()).getList_alarmData().remove(malarmData);


        /*if (malarmDataP.equals(malarmData)) {
            Toast.makeText(getApplicationContext(),"obj equal!",Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(getApplicationContext(),"obj not same",Toast.LENGTH_SHORT).show();
        }*/
        //AlaramData_singletonClass.get(getApplicationContext()).add(malarmData);
    }

    private void updateData() {
        malarmData=new AlarmData();

        malarmData.setAlarmTitle(editText_desAlarm.getText().toString());

        malarmData.setRingtonePatth(mtonePath);

        malarmData.setAlarmType(malarmType);

        malarmData.setAlarmSnooze(snoozeValues);

        malarmData.setOnOff(1);

        malarmData.setAlarmDate(calendar);
        Calendar icalendar=malarmData.getAlarmDate();
        int d,m,y;
        d=icalendar.get(Calendar.DAY_OF_MONTH);
        m=icalendar.get(Calendar.MONTH);
        y=icalendar.get(Calendar.YEAR);

        malarmData.setAlarmTime(date);
        int h,min;
        Date idate=malarmData.getAlarmTime();
        h=idate.getHours();
        min=idate.getMinutes();

        malarmData.setAlarmDays(stringsDays);

    }

    private void populateDate(AlarmData alarmData) {
        populateTime(alarmData);
        populateTone(alarmData);
        malarmType=alarmData.getAlarmType();
        //Toast.makeText(getApplicationContext(),malarmType,Toast.LENGTH_SHORT).show();
        //Toast.makeText(getApplicationContext(),"populate title :"+alarmData.getAlarmTitle(),Toast.LENGTH_SHORT).show();
        editText_desAlarm.setText(alarmData.getAlarmTitle());
        populateSnooze(alarmData);
        calendar=alarmData.getAlarmDate();
        date=alarmData.getAlarmTime();
    }

    private void populateSnooze(AlarmData alarmData) {
        snoozeValues=new int[2];
        snoozeValues=alarmData.getAlarmSnooze();

        for(int i=0;i<2;i++) {
            //Toast.makeText(getApplicationContext(),"snooze"+snoozeValues[i]+" ",Toast.LENGTH_SHORT).show();
        }
    }

    private void populateTone(AlarmData alarmData) {

        if(alarmData.getRingtonePatth()==null) {
            meditText_alarmTone.setText("Default!");
        }else {


            Uri uri = Uri.parse(alarmData.getRingtonePatth());

            Ringtone ringtone = RingtoneManager.getRingtone(this, uri);
            String title = ringtone.getTitle(this);

            meditText_alarmTone.setText(title);
            mtonePath=uri.toString();
            //Toast.makeText(getApplicationContext(),mtonePath,Toast.LENGTH_SHORT).show();

            if(Objects.equals(ringtone.getTitle(getApplicationContext()), "Unknown ringtone")) {
                meditText_alarmTone.setText("Default Tone");
            }

        }
    }

    private void populateTime(AlarmData alarmData) {
        Date date;
        Calendar calendar;

        String[] Days=new String[7];
        String time="";

        date=alarmData.getAlarmTime();
        //calendar=alarmData.getAlarmDate();

        //int hour,min;
        //int day,month,year;

        //Toast.makeText(getApplicationContext(),"TIME!",Toast.LENGTH_SHORT).show();

        //Toast.makeText(getApplicationContext(),date.getHours()+";"+date.getMinutes(),Toast.LENGTH_SHORT).show();

        if(MainActivity.TimeFormate(getApplicationContext())==24) {
            time=setTime_24Formate(date.getHours(),date.getMinutes());
        }else if(MainActivity.TimeFormate(getApplicationContext())==12) {
            time=setTime_12Formate(date.getHours(),date.getMinutes());
        }

        time+=" ";


        Days=alarmData.getAlarmDays();

        if(chechDays(alarmData.getAlarmDays())) {
            for(int i=0;i<7;i++) {
                if(Days[i]!=null) {
                    time += Days[i];
                    time += " ";
                }
            }



        }else if(!chechDays(alarmData.getAlarmDays())) {
            calendar=alarmData.getAlarmDate();
            day=calendar.get(Calendar.DAY_OF_MONTH);
            month=calendar.get(Calendar.MONTH);
            year=calendar.get(Calendar.YEAR);

            time+=day+"/"+month+"/"+year;

        }

        mtextView_displayAlarm_time.setText(time);
    }

    private boolean precaution() {
        if(editText_desAlarm.getText().toString()==null || malarmType==null || mtextView_displayAlarm_time==null) {
            return false;
        }else {
            return true;
        }
    }

    private boolean chechDays(String[] stringsDays) {

        int val = 1;

        for (int i = 0; i < 7; i++) {
            if (stringsDays[i] != null) {
                return true;
            }
        }

        return false;


    }

    private String setTime_24Formate(int hour,int min) {

        String time="Time: ";



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

        String time="Time: ";
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

    private boolean compareStrings(AlarmData alarmData1, AlarmData alarmData2) {

        if(alarmData1.getAlarmTitle().equalsIgnoreCase(alarmData2.getAlarmTitle()) &&
                alarmData1.getAlarmType().equalsIgnoreCase(alarmData2.getAlarmType())) {

            if(alarmData1.getRingtonePatth()!=null && alarmData2.getRingtonePatth()!=null) {
                if(alarmData1.getRingtonePatth().equalsIgnoreCase(alarmData2.getRingtonePatth())) {
                    return true;
                }else {
                    return false;
                }
            }

            //Toast.makeText(getApplicationContext(),alarmData1.getRingtonePatth()+" ")

            return true;
        }else {
            return false;
        }
    }

    private boolean compareDays(AlarmData alarmData1,AlarmData alarmData2) {
        String[] days1=new String[7];
        String[] days2=new String[7];

        days1=alarmData1.getAlarmDays();
        days2=alarmData2.getAlarmDays();

        for(int i=0;i<7;i++) {
            if(days1[i]!=null && days2[i]!=null) {
                if (!days1[1].equals(days2[i])) {
                    return false;
                }
            }
        }

        return true;
    }

    private boolean compareSnooze(AlarmData alarmData1,AlarmData alarmData2) {
        int[] snooze1=new int[2];
        int[] snooze2=new int[2];

        snooze1=alarmData1.getAlarmSnooze();
        snooze2=alarmData2.getAlarmSnooze();

        for (int i=0;i<2;i++) {
            if(snooze1[i]!=snooze2[i]) {
                return false;
            }
        }

        return true;
    }

    private boolean compareTime(AlarmData alarmData1, AlarmData alarmData2) {
        Date date1,date2;

        date1=alarmData1.getAlarmTime();
        date2=alarmData2.getAlarmTime();

        Toast.makeText(getApplicationContext(),"date1 "+date1.getHours()+":"+date1.getMinutes(),Toast.LENGTH_SHORT).show();
        Toast.makeText(getApplicationContext(),"date2 "+date2.getHours()+":"+date2.getMinutes(),Toast.LENGTH_SHORT).show();

        if(date1.getMinutes()==date2.getMinutes() && date1.getHours()==date2.getHours()) {
            Toast.makeText(getApplicationContext(),"return true",Toast.LENGTH_SHORT).show();
            return true;
        }else {
            Toast.makeText(getApplicationContext(),"return false",Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private boolean compareDate(AlarmData alarmData1, AlarmData alarmData2) {
        Calendar calendar1,calendar2;

        calendar1=alarmData1.getAlarmDate();
        calendar2=alarmData2.getAlarmDate();

        if(calendar1.get(Calendar.DAY_OF_MONTH)==calendar2.get(Calendar.DAY_OF_MONTH) &&
                calendar1.get(Calendar.MONTH)== calendar2.get(Calendar.MONTH) &&
                calendar1.get(Calendar.YEAR)==calendar2.get(Calendar.YEAR)) {
            return true;
        }else {
            return false;
        }
    }


    private void getIndex() {

        int[] array=new int[10];
        //int i=0;

        for(int j=0;j<10;j++) {
            array[j]=-1;
        }

        //Toast.makeText(getApplicationContext(),"Size: "+AlaramData_singletonClass.
                //get(getApplicationContext()).getList_alarmData().size(),Toast.LENGTH_SHORT).show();

        if(AlaramData_singletonClass.get(getApplicationContext()).getList_alarmData().isEmpty()) {
            malarmData.setId(0);
            //malarmData.setId(7);
            ////malarmData.setId(3);
            //malarmData.setId(8);
            return;
        }else {

            for (AlarmData alarmData : AlaramData_singletonClass.get(getApplicationContext()).getList_alarmData()) {


                //Toast.makeText(getApplicationContext(),"before: "+i+" ",Toast.LENGTH_SHORT).show();

                //array[i]=alarmData.getId();

                if(alarmData.getId()==0) {
                    array[0]=alarmData.getId();
                }else if(alarmData.getId()==1) {
                    array[1]=alarmData.getId();
                }else if(alarmData.getId()==2) {
                    array[2]=alarmData.getId();
                }else if(alarmData.getId()==3) {
                    array[3]=alarmData.getId();
                }else if(alarmData.getId()==4) {
                    array[4]=alarmData.getId();
                }else if(alarmData.getId()==5) {
                    array[5]=alarmData.getId();
                }else if(alarmData.getId()==6) {
                    array[6]=alarmData.getId();
                }else if(alarmData.getId()==7) {
                    array[7]=alarmData.getId();
                }else if(alarmData.getId()==8) {
                    array[8]=alarmData.getId();
                }else if(alarmData.getId()==9) {
                    array[9]=alarmData.getId();
                }

                //i++;
                //Toast.makeText(getApplicationContext(),"after: "+i+" ",Toast.LENGTH_SHORT).show();
            }

            for(int i=0;i<10;i++) {
                if(array[i]!=i) {
                    malarmData.setId(i);
                    //Toast.makeText(getApplicationContext(),"index: "+i,Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            /*Toast.makeText(getApplicationContext(),i+" ",Toast.LENGTH_SHORT).show();

            int index=-1;
            int status=0;

            int var=0;

            for(int j=0;j<=i;j++) {
                Toast.makeText(getApplicationContext(),j+"j ",Toast.LENGTH_SHORT).show();
                Toast.makeText(getApplicationContext(),array[j]+" ",Toast.LENGTH_SHORT).show();
                if(array[j]==var) {
                    var++;
                }else {
                    for(int l=0;l<10;l++) {
                        if(array[j]!=array[l]) {
                            //index=l;
                            //Toast.makeText(getApplicationContext(),l+"l",Toast.LENGTH_SHORT).show();
                            //break;

                            if(array[j]==array[l]) {
                                status++;

                                if(status==2) {
                                    index=l;
                                    break;
                                }
                            }
                        }
                    }



                    malarmData.setId(index);
                    Toast.makeText(getApplicationContext(),"index: "+index,Toast.LENGTH_SHORT).show();
                    Toast.makeText(getApplicationContext(),malarmData.getId()+" ",Toast.LENGTH_SHORT).show();
                    return;
                }
             }*/

             //malarmData.setId(index);


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







}
