package com.rumshaaamir.alarm;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

/**
 * Created by Rumsha Aamir on 1/24/2018.
 */

public class ActivityPreviewAlarm extends AppCompatActivity {

    String TAG="ActivityPreviewAlarm";

    private AlarmData alarmData;

    int mindex;

    public int va;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_previewalarm);

        //setting title for action bar and also change the color of the text on title bar
        String title="Preview Alarm";
        SpannableString s = new SpannableString(title);
        s.setSpan(new ForegroundColorSpan(Color.DKGRAY), 0, title.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        getSupportActionBar().setTitle(s);


        /*AlarmData alarmData=new AlarmData();
        if (savedInstanceState != null) {
            Toast.makeText(getApplicationContext(),"Not Null!",Toast.LENGTH_SHORT).show();
            alarmData= (AlarmData) savedInstanceState.getSerializable(TAG);
            readData(alarmData);
        }else {
            Toast.makeText(getApplicationContext(),"Null!",Toast.LENGTH_SHORT).show();
        }*/

        //alarmData=AlaramData_singletonClass.get(getApplicationContext()).getList_alarmData().get(0);
        //alarmData= (AlarmData) getIntent().getSerializableExtra(TAG);
        mindex=getIntent().getIntExtra(TAG,-1);
        //int index=AlaramData_singletonClass.get(getApplicationContext()).getList_alarmData().indexOf(alarmData);
        //Toast.makeText(getApplicationContext(),"Index: "+mindex, Toast.LENGTH_LONG).show();
        //readData(alarmData);
        alarmData=AlaramData_singletonClass.get(getApplicationContext()).getList_alarmData().get(mindex);



        //Intent intent=new Intent();
        //AlarmData alarmData1= (AlarmData) intent.getSerializableExtra(TAG);

        TextView textView_alarmTitle=(TextView)findViewById(R.id.alarmTitle1_textView_apa);
        TextView textView_alarmDate=(TextView)findViewById(R.id.alarmDate1_textView_apa);
        TextView textView_alarmTone=(TextView)findViewById(R.id.alarmTone1_textView_apa);
        TextView textView_alarmType=(TextView)findViewById(R.id.alarmType1_textView_apa);
        TextView textView_alarmSnooze=(TextView)findViewById(R.id.alarmSnooze1_textView_apa);

        showData(textView_alarmTitle,textView_alarmDate,textView_alarmTone,textView_alarmType,
                textView_alarmSnooze,alarmData);





    }

    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==99 && resultCode== Activity.RESULT_OK) {
            Intent intent_startActivity=new Intent(this,MainActivity.class);
            setResult(99,intent_startActivity);
            finish();
        }
    }*/


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.menu_activity_previewalarm,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.edit:
                Intent intent_startActivity=new Intent(getApplication(),AlarmSetting.class);
                //AlaramData_singletonClass.get(getApplicationContext()).getList_alarmData().remove(alarmData);
                //Toast.makeText(getApplicationContext(), "Index: "+AlaramData_singletonClass.get(getApplicationContext()).getList_alarmData().indexOf(alarmData)+" ", Toast.LENGTH_LONG).show();
                intent_startActivity.putExtra(TAG,mindex);
                startActivity(intent_startActivity);

                /*readData(alarmData);
                AlaramData_singletonClass.get(getApplicationContext()).getList_alarmData().remove(mindex);

                for(AlarmData data:AlaramData_singletonClass.get(getApplicationContext()).getList_alarmData()) {
                    //readData(data);
                }*/
            case R.id.delete:
                AlaramData_singletonClass.get(getApplicationContext()).getList_alarmData().remove(mindex);
                finish();
            default:
                return super.onOptionsItemSelected(item);

        }


    }

    private void showData(TextView textViewTitle,TextView textViewDate,TextView textViewTone,
                          TextView textViewType,TextView textViewSnooze,AlarmData alarmData) {
        textViewTitle.setText(alarmData.getAlarmTitle());
        textViewType.setText(alarmData.getAlarmType());

        //Toast.makeText(getApplicationContext(),"showData: "+alarmData.getAlarmTitle(),Toast.LENGTH_SHORT).show();

        if(checkSnooze(alarmData)) {
            String snooze;
            int[] snoozeValues=new int[2];

            snoozeValues=alarmData.getAlarmSnooze();

            snooze=snoozeValues[1]+" Minute latter";
            snooze+=" For "+snoozeValues[0]+" Time";

            textViewSnooze.setText(snooze);
        }else {
            String snooze;

            snooze="No snooze time";

            textViewSnooze.setText(snooze);
        }

        setRingtone(textViewTone,alarmData);

        String time=null;
        String[] Days=new String[7];
        Days=alarmData.getAlarmDays();
        Calendar calendar;
        int day,month,year;

        Date date=alarmData.getAlarmTime();


        if(MainActivity.TimeFormate(getApplicationContext())==24) {
            time=setTime_24Formate(date.getHours(),date.getMinutes());
        }else if(MainActivity.TimeFormate(getApplicationContext())==12) {
            time=setTime_12Formate(date.getHours(),date.getMinutes());
        }

        time+=" ";

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

        textViewDate.setText(time);



    }

    private boolean checkSnooze(AlarmData alarmData) {
        int[] snooze=new int[2];
        snooze=alarmData.getAlarmSnooze();

        for(int i=0;i<2;i++) {
            if(snooze[i]==-1) {
                return false;
            }
        }

        return true;
    }

    private void setRingtone(TextView textViewTone, AlarmData alarmData) {

        if(alarmData.getRingtonePatth()==null) {
            textViewTone.setText("Default!");
        }else {

            Uri uri = Uri.parse(alarmData.getRingtonePatth());
            //Toast.makeText(this, uri.toString(), Toast.LENGTH_SHORT).show();

            Ringtone ringtone = RingtoneManager.getRingtone(this, uri);
            textViewTone.setText(ringtone.getTitle(this));
            //Toast.makeText(this,ringtone.getTitle(getApplicationContext()) , Toast.LENGTH_SHORT).show();

            if(Objects.equals(ringtone.getTitle(getApplicationContext()), "Unknown ringtone")) {
                textViewTone.setText("Default Tone");
            }
        }

        //Toast.makeText(this, alarmData.getRingtonePatth(), Toast.LENGTH_SHORT).show();
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
}
