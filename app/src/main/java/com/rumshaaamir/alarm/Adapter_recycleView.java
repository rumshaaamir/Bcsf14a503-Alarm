package com.rumshaaamir.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.jar.Pack200;

/**
 * Created by Rumsha Aamir on 1/24/2018.
 */

public class Adapter_recycleView extends RecyclerView.Adapter<Adapter_recycleView.ViewHolder> {


    // Define listener member variable
    private OnItemClickListener listener;
    // Define the listener interface
    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }
    // Define the method that allows the parent activity or fragment to define the listener
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder /*implements View.OnClickListener*/ {

        public TextView textView_time;
        public TextView textView_alarmDes;
        public TextView textView_date;
        public SwitchCompat aSwitch_alarm;
        public CardView cardView;


        public ViewHolder(View itemView) {
            super(itemView);

            textView_time=(TextView)itemView.findViewById(R.id.time_textView);
            textView_alarmDes=(TextView)itemView.findViewById(R.id.alramDescription_textView);
            textView_date=(TextView)itemView.findViewById(R.id.date_textView);
            aSwitch_alarm=(SwitchCompat) itemView.findViewById(R.id.alarm_switch);
            cardView=(CardView)itemView.findViewById(R.id.cardView);

            //itemView.setOnClickListener(this);

            /*itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position=getAdapterPosition();

                    if(position!=RecyclerView.NO_POSITION) {
                        Contact contact=contactList.get(position);

                        Toast.makeText(getContext(),"ca "+contact.getName(),Toast.LENGTH_SHORT).show();
                    }
                }
            });*/
        }

        /*@Override
        public void onClick(View view) {
         int position=getAdapterPosition();

            if(position!=RecyclerView.NO_POSITION) {
                Contact contact=contactList.get(position);

                Toast.makeText(getContext(),contact.getName(),Toast.LENGTH_SHORT).show();
            }
        }*/
    }

    //private List<Contact> contactList;
    private ArrayList<AlarmData> marrayList_alarmData;
    private Context context;
    private DBAdapter_alarmdata dbAdapter_alarmdata;

    public Adapter_recycleView(Context context,ArrayList<AlarmData> alarmDataArrayList) {
        //this.contactList=contacts;
        this.context=context;
        marrayList_alarmData=alarmDataArrayList;
        dbAdapter_alarmdata=new DBAdapter_alarmdata(getContext());
    }

    private Context getContext() {
        return context;
    }




    @Override
    public Adapter_recycleView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());

        View contactView=layoutInflater.inflate(R.layout.cardview_am,parent,false);

        ViewHolder viewHolder=new ViewHolder(contactView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final Adapter_recycleView.ViewHolder holder, final int position) {

        Date date;
        final Calendar calendar;

        int hour,min;
        int day,month,year;

        String time,Date="";
        String[] Days=new String[7];
        for(int i=0;i<7;i++) {
            Days[i]=null;
        }

        //Contact contact=contactList.get(position);

        final AlarmData alarmData=marrayList_alarmData.get(position);
        final int index=AlaramData_singletonClass.get(getContext()).getList_alarmData().indexOf(alarmData);


        Days=alarmData.getAlarmDays();

        date=alarmData.getAlarmTime();
        //hour=date.getHours();
        //min=date.getMinutes();
        //time=hour+":"+min;

        if(MainActivity.TimeFormate(getContext())==24) {

            setTime_24Formate(holder.textView_time,date);
            

        }else if(MainActivity.TimeFormate(getContext())==12) {

            setTime_12Formate(holder.textView_time,date);
        }

        //TextView textViewT= holder.textView_time;
        //textViewT.setText(time);



        if(chechDays(Days)) {
            for(int i=0;i<7;i++) {
                if(Days[i]!=null) {
                    Date += Days[i];
                    Date += " ";
                }
            }

            TextView textViewD= holder.textView_date;
            textViewD.setText(Date);

        }else if(!chechDays(Days)) {
            calendar=alarmData.getAlarmDate();
            day=calendar.get(Calendar.DAY_OF_MONTH);
            month=calendar.get(Calendar.MONTH);
            year=calendar.get(Calendar.YEAR);

            Date=day+"/"+month+"/"+year;

            TextView textViewD= holder.textView_date;
            textViewD.setText(Date);
        }




        TextView textViewDi=holder.textView_alarmDes;
        textViewDi.setText(alarmData.getAlarmTitle());

        SwitchCompat aSwitch_=holder.aSwitch_alarm;
        if(alarmData.getOnOff()==1) {
            aSwitch_.setText("On");
            aSwitch_.setChecked(true);
            //holder.cardView.setCardBackgroundColor(getContext().getColor(R.color.colorLiteBlue));
            holder.cardView.setCardBackgroundColor(Color.rgb(230,250,250));
        }else {
            aSwitch_.setChecked(false);
            aSwitch_.setText("Off");
            //holder.cardView.setCardBackgroundColor(getContext().getColor(R.color.colorWhite));
            holder.cardView.setCardBackgroundColor(Color.rgb(252,252,252));
        }


        holder.aSwitch_alarm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {


                if(holder.aSwitch_alarm.isChecked()) {
                    holder.aSwitch_alarm.setText("On");
                    holder.aSwitch_alarm.setChecked(true);

                    Toast.makeText(getContext(),"Alarm ON",Toast.LENGTH_SHORT).show();

                    /*Date date1;
                    int hour1,min1;
                    String time1;
                    date1=alarmData.getAlarmTime();
                    hour1=date1.getHours();
                    min1=date1.getMinutes();
                    time1=hour1+":"+min1;

                    Toast.makeText(getContext(),time1,Toast.LENGTH_SHORT).show();*/



                    //alarmData.setOnOff(1);
                    //Toast.makeText(getContext(), "Index: "+AlaramData_singletonClass.get(getContext()).getList_alarmData().indexOf(alarmData)+" ", Toast.LENGTH_LONG).show();

                    //AlarmData alarmData_updated = new AlarmData();
                    //alarmData_updated = alarmData;
                    //alarmData_updated.setOnOff(1);

                    AlaramData_singletonClass.get(getContext()).getList_alarmData().remove(position);
                    alarmData.setOnOff(1);
                    AlaramData_singletonClass.get(getContext()).getList_alarmData().add(position,alarmData);

                    //AlaramData_singletonClass.get(getContext()).getList_alarmData().remove(alarmData);
                    //alarmData.setOnOff(0);
                    //AlaramData_singletonClass.get(getContext()).getList_alarmData().add(alarmData_updated);

                    /*resetDataBase();
                    updateDatabase();
                    AlaramData_singletonClass.get(getContext()).getList_alarmData().clear();
                    restoreDate();*/

                    //Toast.makeText(getContext(), alarmData.getId()+" ", Toast.LENGTH_SHORT).show();

                    /*MainActivity.mpendingIntent= PendingIntent.getBroadcast(getContext(), 0, MainActivity.malarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                    if(MainActivity.mpendingIntent!=null) {
                        MainActivity.mpendingIntent.cancel();
                        MainActivity.malarmManager.cancel(MainActivity.mpendingIntent);
                    }*/

                    Date date=new Date();
                    date=alarmData.getAlarmTime();
                    //date.setMinutes(45);
                    //date.setHours(18);
                    date.setSeconds(0);

                    Calendar calendar;
                    calendar=alarmData.getAlarmDate();
                    calendar.setTime(date);

                    Calendar calenderCurrent=Calendar.getInstance();
                    Date dateCurrent=calenderCurrent.getTime();

                    /*if((calenderCurrent.get(Calendar.DAY_OF_MONTH)==calendar.get(Calendar.DAY_OF_MONTH) &&
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

                        MainActivity.mpendingIntent = PendingIntent.getBroadcast(getContext(), alarmData.getId(), MainActivity.malarmIntent, 0);
                        MainActivity.malarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), MainActivity.mpendingIntent);

                    }*/
                    //holder.cardView.setCardBackgroundColor(getContext().getColor(R.color.colorLiteBlue));

                    MainActivity.mpendingIntent = PendingIntent.getBroadcast(getContext(), alarmData.getId(), MainActivity.malarmIntent, 0);
                    MainActivity.malarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), MainActivity.mpendingIntent);


                    holder.cardView.setCardBackgroundColor(Color.rgb(230,250,250));



                    /*//global
                    Toast.makeText(getContext(),"On ON Switch!, Global",Toast.LENGTH_SHORT).show();
                    for(AlarmData alarmData:AlaramData_singletonClass.get(getContext()).getList_alarmData()) {

                        Toast.makeText(getContext(),"Title: "+alarmData.getAlarmTitle(),Toast.LENGTH_SHORT).show();

                        Toast.makeText(getContext(),"Path: "+alarmData.getRingtonePatth(),Toast.LENGTH_SHORT).show();

                        Toast.makeText(getContext(),"Type: "+alarmData.getAlarmType(),Toast.LENGTH_SHORT).show();

                        for(int i=0;i<2;i++) {
                            int[] data=new int[2];
                            data=alarmData.getAlarmSnooze();
                            Toast.makeText(getContext(),"snooze 1: "+data[i]+" ",Toast.LENGTH_SHORT).show();
                        }

                        Toast.makeText(getContext(),"onOff: "+alarmData.getOnOff()+" ",Toast.LENGTH_SHORT).show();

                        Calendar icalendar=alarmData.getAlarmDate();
                        int d,m,y;
                        d=icalendar.get(Calendar.DAY_OF_MONTH);
                        m=icalendar.get(Calendar.MONTH);
                        y=icalendar.get(Calendar.YEAR);
                        Toast.makeText(getContext(),d+" "+m+" "+y,Toast.LENGTH_SHORT).show();

                        int h,min;
                        Date idate=alarmData.getAlarmTime();
                        h=idate.getHours();
                        min=idate.getMinutes();
                        Toast.makeText(getContext(),h+" "+min,Toast.LENGTH_SHORT).show();

                        String[] strings=new String[7];
                        strings=alarmData.getAlarmDays();
                        for(int i=0;i<7;i++) {
                            Toast.makeText(getContext(), strings[i]+" ",Toast.LENGTH_SHORT).show();
                        }

                    }


                    //local
                    Toast.makeText(getContext(),"On ON Switch! ,Local",Toast.LENGTH_SHORT).show();
                    for(AlarmData alarmData:marrayList_alarmData) {

                        Toast.makeText(getContext(),"Title: "+alarmData.getAlarmTitle(),Toast.LENGTH_SHORT).show();

                        Toast.makeText(getContext(),"Path: "+alarmData.getRingtonePatth(),Toast.LENGTH_SHORT).show();

                        Toast.makeText(getContext(),"Type: "+alarmData.getAlarmType(),Toast.LENGTH_SHORT).show();

                        for(int i=0;i<2;i++) {
                            int[] data=new int[2];
                            data=alarmData.getAlarmSnooze();
                            Toast.makeText(getContext(),"snooze 1: "+data[i]+" ",Toast.LENGTH_SHORT).show();
                        }

                        Toast.makeText(getContext(),"onOff: "+alarmData.getOnOff()+" ",Toast.LENGTH_SHORT).show();

                        Calendar icalendar=alarmData.getAlarmDate();
                        int d,m,y;
                        d=icalendar.get(Calendar.DAY_OF_MONTH);
                        m=icalendar.get(Calendar.MONTH);
                        y=icalendar.get(Calendar.YEAR);
                        Toast.makeText(getContext(),d+" "+m+" "+y,Toast.LENGTH_SHORT).show();

                        int h,min;
                        Date idate=alarmData.getAlarmTime();
                        h=idate.getHours();
                        min=idate.getMinutes();
                        Toast.makeText(getContext(),h+" "+min,Toast.LENGTH_SHORT).show();

                        String[] strings=new String[7];
                        strings=alarmData.getAlarmDays();
                        for(int i=0;i<7;i++) {
                            Toast.makeText(getContext(), strings[i]+" ",Toast.LENGTH_SHORT).show();
                        }

                    }*/


                    //Adapter_recycleView.this.notifyAll();
                }else {
                    holder.aSwitch_alarm.setText("Off");
                    holder.aSwitch_alarm.setChecked(false);

                    Toast.makeText(getContext(),"Alarm OFF",Toast.LENGTH_SHORT).show();

                    //Toast.makeText(getContext(), "position: "+position, Toast.LENGTH_SHORT).show();
                    //Toast.makeText(getContext(), "index: "+index, Toast.LENGTH_SHORT).show();

                   /* Date date1;
                    int hour1,min1;
                    String time1;
                    date1=alarmData.getAlarmTime();
                    hour1=date1.getHours();
                    min1=date1.getMinutes();
                    time1=hour1+":"+min1;

                    Toast.makeText(getContext(),time1,Toast.LENGTH_SHORT).show();*/

                    //alarmData.setOnOff(0);

                    /*AlaramData_singletonClass.get(getContext()).getList_alarmData().remove(alarmData);
                    alarmData.setOnOff(0);
                    AlaramData_singletonClass.get(getContext()).getList_alarmData().add(alarmData);*/

                    /*resetDataBase();
                    updateDatabase();
                    AlaramData_singletonClass.get(getContext()).getList_alarmData().clear();
                    restoreDate();*/

                    //notifyDataSetChanged();

                    /*AlarmData alarmData_updated = new AlarmData();
                    alarmData_updated = alarmData;
                    alarmData_updated.setOnOff(0);*/

                    AlaramData_singletonClass.get(getContext()).getList_alarmData().remove(position);
                    alarmData.setOnOff(0);
                    AlaramData_singletonClass.get(getContext()).getList_alarmData().add(position,alarmData);

                    //Toast.makeText(getContext(), alarmData.getId()+" ", Toast.LENGTH_SHORT).show();

                    Date date=new Date();
                    date=alarmData.getAlarmTime();
                    date.setSeconds(0);

                    Calendar calender=Calendar.getInstance();
                    calender.setTime(date);

                    //cancelAlarms();
                    //alarm_onOff();

                    MainActivity.mpendingIntent= PendingIntent.getBroadcast(getContext(), alarmData.getId(), MainActivity.malarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                    if(MainActivity.mpendingIntent!=null) {
                        MainActivity.mpendingIntent.cancel();
                        MainActivity.malarmManager.cancel(MainActivity.mpendingIntent);
                    }

                    /*resetDataBase();
                    updateDatabase();
                    AlaramData_singletonClass.get(getContext()).getList_alarmData().clear();
                    restoreDate();*/



                    //holder.cardView.setCardBackgroundColor(getContext().getColor(R.color.colorWhite));
                    holder.cardView.setCardBackgroundColor(Color.rgb(252,252,252));

                    /*//global
                    Toast.makeText(getContext(),"On OFF Switch!, Global",Toast.LENGTH_SHORT).show();
                    for(AlarmData alarmData:AlaramData_singletonClass.get(getContext()).getList_alarmData()) {

                        Toast.makeText(getContext(),"Title: "+alarmData.getAlarmTitle(),Toast.LENGTH_SHORT).show();

                        Toast.makeText(getContext(),"Path: "+alarmData.getRingtonePatth(),Toast.LENGTH_SHORT).show();

                        Toast.makeText(getContext(),"Type: "+alarmData.getAlarmType(),Toast.LENGTH_SHORT).show();

                        for(int i=0;i<2;i++) {
                            int[] data=new int[2];
                            data=alarmData.getAlarmSnooze();
                            Toast.makeText(getContext(),"snooze 1: "+data[i]+" ",Toast.LENGTH_SHORT).show();
                        }

                        Toast.makeText(getContext(),"onOff: "+alarmData.getOnOff()+" ",Toast.LENGTH_SHORT).show();

                        Calendar icalendar=alarmData.getAlarmDate();
                        int d,m,y;
                        d=icalendar.get(Calendar.DAY_OF_MONTH);
                        m=icalendar.get(Calendar.MONTH);
                        y=icalendar.get(Calendar.YEAR);
                        Toast.makeText(getContext(),d+" "+m+" "+y,Toast.LENGTH_SHORT).show();

                        int h,min;
                        Date idate=alarmData.getAlarmTime();
                        h=idate.getHours();
                        min=idate.getMinutes();
                        Toast.makeText(getContext(),h+" "+min,Toast.LENGTH_SHORT).show();

                        String[] strings=new String[7];
                        strings=alarmData.getAlarmDays();
                        for(int i=0;i<7;i++) {
                            Toast.makeText(getContext(), strings[i]+" ",Toast.LENGTH_SHORT).show();
                        }

                    }


                    //local
                    Toast.makeText(getContext(),"On OFF Switch! ,Local",Toast.LENGTH_SHORT).show();
                    for(AlarmData alarmData:marrayList_alarmData) {

                        Toast.makeText(getContext(),"Title: "+alarmData.getAlarmTitle(),Toast.LENGTH_SHORT).show();

                        Toast.makeText(getContext(),"Path: "+alarmData.getRingtonePatth(),Toast.LENGTH_SHORT).show();

                        Toast.makeText(getContext(),"Type: "+alarmData.getAlarmType(),Toast.LENGTH_SHORT).show();

                        for(int i=0;i<2;i++) {
                            int[] data=new int[2];
                            data=alarmData.getAlarmSnooze();
                            Toast.makeText(getContext(),"snooze 1: "+data[i]+" ",Toast.LENGTH_SHORT).show();
                        }

                        Toast.makeText(getContext(),"onOff: "+alarmData.getOnOff()+" ",Toast.LENGTH_SHORT).show();

                        Calendar icalendar=alarmData.getAlarmDate();
                        int d,m,y;
                        d=icalendar.get(Calendar.DAY_OF_MONTH);
                        m=icalendar.get(Calendar.MONTH);
                        y=icalendar.get(Calendar.YEAR);
                        Toast.makeText(getContext(),d+" "+m+" "+y,Toast.LENGTH_SHORT).show();

                        int h,min;
                        Date idate=alarmData.getAlarmTime();
                        h=idate.getHours();
                        min=idate.getMinutes();
                        Toast.makeText(getContext(),h+" "+min,Toast.LENGTH_SHORT).show();

                        String[] strings=new String[7];
                        strings=alarmData.getAlarmDays();
                        for(int i=0;i<7;i++) {
                            Toast.makeText(getContext(), strings[i]+" ",Toast.LENGTH_SHORT).show();
                        }

                    }*/


                    //Adapter_recycleView.this.notifyAll();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return marrayList_alarmData.size();
    }

    public void add() {
        //contactList.add(position, item);
        //notifyItemInserted(position);
        notifyDataSetChanged();

    }


    public void remove(Contact contact) {
        //contactList.remove(position);
        //contactList.remove(contact);
        //notifyItemRemoved(position);
    }

    private boolean chechDays(String[] Days) {


        for(int i=0;i<7;i++) {
            if (Days[i]!=null) {
                return  true;
            }
        }

        return false;
    }

    private void setTime_24Formate(TextView textView, Date date) {
        int hour,min;
        String time="";

        hour=date.getHours();
        min=date.getMinutes();

        if(hour<10) {
            time="0"+hour+":";
        }else {
            time=hour+":";
        }

        if(min<10) {
            time+="0"+min;
        }else{
            time+=min;
        }

        textView.setText(time);
    }

    private void setTime_12Formate(TextView textView, Date date) {
        int hour,min;
        String time="";
        int val=0;

        hour=date.getHours();
        min=date.getMinutes();

        if(hour<10) {

            if(hour==0) {
                time="12:";
            }else {

                time = "0" + hour + ":";
            }
            val=1;
        }else if(hour>=10 && hour<=12) {
            time=hour+":";

            if(hour<12) {
                val=1;
            }else if(hour==12){
                val=2;
            }

        }else if(hour>12) {

            val=2;

            if(hour==13) {
                time="01:";
            }else if(hour==14) {
                time="02:";
            }else if(hour==15) {
                time="03:";
            }else if(hour==16) {
                time="04:";
            }else if(hour==17) {
                time="05:";
            }else if(hour==18) {
                time="06:";
            }else if(hour==19) {
                time="07:";
            }else if(hour==20) {
                time="08:";
            }else if(hour==21) {
                time="09:";
            }else if(hour==22) {
                time="10:";
            }else if(hour==23) {
                time="11:";
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

        textView.setText(time);
    }

    private void cancelAlarms() {

        for(int i=0;i<10;i++) {
            MainActivity.mpendingIntent = PendingIntent.getBroadcast(getContext(), i,
                    MainActivity.malarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            if (MainActivity.mpendingIntent != null) {
                MainActivity.mpendingIntent.cancel();
                MainActivity.malarmManager.cancel(MainActivity.mpendingIntent);
            }
        }
    }


    private void alarm_onOff() {
        for(AlarmData alarmData:AlaramData_singletonClass.get(getContext()).getList_alarmData()) {
            if(alarmData.getOnOff()==1) {

                Toast.makeText(getContext(), alarmData.getId()+", "+alarmData.getOnOff(), Toast.LENGTH_SHORT).show();

                Date dateCurrent=new Date();

                Date date;
                date=alarmData.getAlarmTime();
                //date.setHours(21);
                //date.setMinutes(8);
                date.setSeconds(0);

                Calendar calendar;
                calendar=alarmData.getAlarmDate();
                calendar.setTime(date);

                Toast.makeText(getContext(), date.getHours()+":"+date.getMinutes(), Toast.LENGTH_SHORT).show();

                //mpendingIntent=PendingIntent.getBroadcast(getApplicationContext(),alarmData.getId(),malarmIntent,
                //PendingIntent.FLAG_CANCEL_CURRENT);
                //mpendingIntent=PendingIntent.getBroadcast(getApplicationContext(),alarmData.getId(),malarmIntent,
                //PendingIntent.FLAG_UPDATE_CURRENT);
                MainActivity.malarmIntent.putExtra("index",alarmData.getId()+1);
                MainActivity.mpendingIntent=PendingIntent.getBroadcast(getContext(),alarmData.getId(),MainActivity.malarmIntent,PendingIntent.FLAG_ONE_SHOT);
                //malarmManager.set(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),mpendingIntent);

                if (android.os.Build.VERSION.SDK_INT >= 19) {
                    MainActivity.malarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), MainActivity.mpendingIntent);
                    /*if(date.getHours()==dateCurrent.getHours() && date.getMinutes()==dateCurrent.getMinutes()) {
                        malarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), mpendingIntent);
                    }*/
                } else {
                    /*if(date.getMinutes()>dateCurrent.getMinutes()) {
                        malarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), mpendingIntent);
                    }*/
                    MainActivity.malarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), MainActivity.mpendingIntent);
                }

                /*PowerManager pm = (PowerManager) getApplicationContext().getSystemService(Context.POWER_SERVICE);
                PowerManager.WakeLock wakeLock = pm.newWakeLock((PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP), "TAG");
                wakeLock.acquire();*/

            }else if(alarmData.getOnOff()==0) {

                Toast.makeText(getContext(), alarmData.getId()+", "+alarmData.getOnOff(), Toast.LENGTH_SHORT).show();

                MainActivity.mpendingIntent= PendingIntent.getBroadcast(getContext(), alarmData.getId(),
                        MainActivity.malarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                if(MainActivity.mpendingIntent!=null) {
                    MainActivity.mpendingIntent.cancel();
                    MainActivity.malarmManager.cancel(MainActivity.mpendingIntent);

                    Toast.makeText(getContext(), "canceled alarm", Toast.LENGTH_SHORT).show();
                }
            }
        }



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

        for (AlarmData alarmData:AlaramData_singletonClass.get(getContext()).getList_alarmData()) {

            //readData(alarmData);

            DBAdapter_alarmdata dbAdapter_alarmdata=new DBAdapter_alarmdata(getContext());

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
                //Log.i(TAG,"Alarm Basic updated!");
            }else {
                //Log.i(TAG,"Alarm Basic not updated!");
            }

            date=alarmData.getAlarmTime();
            hour=date.getHours();
            minutes=date.getMinutes();
            status=dbAdapter_alarmdata.updateAlarmTime(id,hour,minutes);
            if(status) {
                //Log.i(TAG,"Alarm time updated!");
            }else {
                //Log.i(TAG,"Alarm time not updated!");
            }

            snooze=alarmData.getAlarmSnooze();
            notime=snooze[0];
            min=snooze[1];
            status=dbAdapter_alarmdata.updateAlarmSnooze(id,notime,min);
            if(status) {
                //Log.i(TAG,"Alarm snooze updated!");
            }else {
                //Log.i(TAG,"Alarm snooze not updated!");
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
                    //Log.i(TAG,"Alarm date updated!");
                }else {
                    //Log.i(TAG,"Alarm date not updated!");
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
                    //Log.i(TAG,"Alarm days updated!");
                }else {
                    //Log.i(TAG,"Alarm days not updated!");
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

                    AlaramData_singletonClass.get(getContext()).getList_alarmData().add(alarmData);

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




}
