package com.rumshaaamir.alarm;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;

import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Rumsha Aamir on 1/24/2018.
 */

public class TimePickerDialogeFregment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        //Use the current time as the default values for the time picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        //Create and return a new instance of TimePickerDialog
        return new TimePickerDialog(getActivity(), AlertDialog.THEME_HOLO_LIGHT,(TimePickerDialog.OnTimeSetListener) this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    //onTimeSet() callback method
    @SuppressLint("WrongViewCast")
    public void onTimeSet(TimePicker view, int hourOfDay, int minute){

        //Toast.makeText(getActivity(),hourOfDay+" "+minute,Toast.LENGTH_SHORT).show();
        //((TextView)getActivity().findViewById(R.id.dateDisplay_textView)).setText("Time: "+hourOfDay+" : "+minute);

        AlarmSetting.date.setHours(hourOfDay);
        AlarmSetting.date.setMinutes(minute);

        int hour, min;

        hour=AlarmSetting.date.getHours();
        min=AlarmSetting.date.getMinutes();

        int year=AlarmSetting.calendar.get(Calendar.YEAR);
        int month=AlarmSetting.calendar.get(Calendar.MONTH);
        int day=AlarmSetting.calendar.get(Calendar.DAY_OF_MONTH);

        if(year!=0) {


            String time=null, date;

            //time="Time: "+hour+" : "+min;
            if(MainActivity.TimeFormate(getContext())==24) {
                time=setTime_24Formate(AlarmSetting.date.getHours(),AlarmSetting.date.getMinutes());
            }else if(MainActivity.TimeFormate(getContext())==12) {
                time=setTime_12Formate(AlarmSetting.date.getHours(),AlarmSetting.date.getMinutes());
            }

            date="Date: "+day+"/"+month+"/"+year;

            AlarmSetting.destroy_stringDays();

            ((TextView)getActivity().findViewById(R.id.dateDisplay_textView)).setText(time+" ,"+date);
        }else {


            String Days = null;

            Days = "Days : ";

            for (int i = 0; i < 7; i++) {
                if (AlarmSetting.stringsDays[i] != null) {


                    Days += AlarmSetting.stringsDays[i];
                    Days += ", ";
                }
            }

            String time=null;

            //time = "Time: " + hour + " : " + min;
            if(MainActivity.TimeFormate(getContext())==24) {
                time=setTime_24Formate(AlarmSetting.date.getHours(),AlarmSetting.date.getMinutes());
            }else if(MainActivity.TimeFormate(getContext())==12) {
                time=setTime_12Formate(AlarmSetting.date.getHours(),AlarmSetting.date.getMinutes());
            }

            ((TextView) getActivity().findViewById(R.id.dateDisplay_textView)).setText(Days + " ," + time);
        }


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
