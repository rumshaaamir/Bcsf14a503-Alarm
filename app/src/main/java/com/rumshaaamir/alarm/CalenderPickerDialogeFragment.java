package com.rumshaaamir.alarm;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Rumsha Aamir on 1/24/2018.
 */

public class CalenderPickerDialogeFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        //Create and return a new instance of TimePickerDialog
        return new DatePickerDialog(getActivity(),
                AlertDialog.THEME_HOLO_LIGHT,this,year,month,day);
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        //Toast.makeText(getActivity(),"ok",Toast.LENGTH_SHORT).show();
        ((TextView)getActivity().findViewById(R.id.dateDisplay_textView)).append(" ,Date: "+i+"/"+i1+"/"+i2);

        AlarmSetting.calendar.set(i,i1,i2);

        int year, month, day;
        int hour, min;

        /*year=AlarmSetting.calendar.get(Calendar.YEAR);
        month=AlarmSetting.calendar.get(Calendar.MONTH);
        day=AlarmSetting.calendar.get(Calendar.DAY_OF_MONTH);*/

        //hour=AlarmSetting.date.getHours();
        //min=AlarmSetting.date.getMinutes();

        String time=null, date;

        //time="Time: "+hour+" : "+min;

        if(MainActivity.TimeFormate(getContext())==24) {
            time=setTime_24Formate(AlarmSetting.date.getHours(),AlarmSetting.date.getMinutes());
        }else if(MainActivity.TimeFormate(getContext())==12) {
            time=setTime_12Formate(AlarmSetting.date.getHours(),AlarmSetting.date.getMinutes());
        }


        //date="Date: "+day+"/"+month+"/"+year;

        AlarmSetting.destroy_stringDays();

        Calendar calendar=Calendar.getInstance();
        //Toast.makeText(getContext(), calendar.before(AlarmSetting.calendar)+" ", Toast.LENGTH_SHORT).show();

        if(calendar.before(AlarmSetting.calendar)) {
            year=AlarmSetting.calendar.get(Calendar.YEAR);
            month=AlarmSetting.calendar.get(Calendar.MONTH);
            day=AlarmSetting.calendar.get(Calendar.DAY_OF_MONTH);

            date="Date: "+day+"/"+month+"/"+year;

            ((TextView)getActivity().findViewById(R.id.dateDisplay_textView)).setText(time+" ,"+date);
        }else {

            if(AlarmSetting.calendar.get(Calendar.YEAR)==calendar.get(Calendar.YEAR) &&
                    AlarmSetting.calendar.get(Calendar.MONTH)==calendar.get(Calendar.MONTH ) &&
                    AlarmSetting.calendar.get(Calendar.DAY_OF_MONTH)==calendar.get(Calendar.DAY_OF_MONTH)) {

                //Toast.makeText(getContext(), "Equal", Toast.LENGTH_SHORT).show();
                year = AlarmSetting.calendar.get(Calendar.YEAR);
                month = AlarmSetting.calendar.get(Calendar.MONTH);
                day = AlarmSetting.calendar.get(Calendar.DAY_OF_MONTH);

                date = "Date: " + day + "/" + month + "/" + year;

                ((TextView) getActivity().findViewById(R.id.dateDisplay_textView)).setText(time + " ," + date);
            }else {

                Toast.makeText(getContext(), "Unable to select Date has been passed!", Toast.LENGTH_SHORT).show();

                AlarmSetting.calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));

                year = AlarmSetting.calendar.get(Calendar.YEAR);
                month = AlarmSetting.calendar.get(Calendar.MONTH);
                day = AlarmSetting.calendar.get(Calendar.DAY_OF_MONTH);

                date = "Date: " + day + "/" + month + "/" + year;

                ((TextView) getActivity().findViewById(R.id.dateDisplay_textView)).setText(time + " ," + date);
            }
        }


        //((TextView)getActivity().findViewById(R.id.dateDisplay_textView)).setText(time+" ,"+date);
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

    private void formatting() {

    }

}
