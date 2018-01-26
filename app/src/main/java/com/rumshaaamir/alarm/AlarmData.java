package com.rumshaaamir.alarm;

import android.content.Context;
import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

/**
 * Created by Rumsha Aamir on 1/24/2018.
 */

public class AlarmData implements Serializable, Comparable<AlarmData>{
    private String alarmTitle;
    private String ringtonePatth;
    private Date alarmTime;
    private Calendar alarmDate;
    private String[] alarmDays;
    private int id;
    private int onOff;
    private String alarmType;

    private int[] alarmSnooze;

    public AlarmData() {
        this.alarmTitle = null;
        this.ringtonePatth = null;
        this.alarmTime = new Date();
        this.alarmDate = Calendar.getInstance();
        this.id=0;
        this.onOff=0;
        this.alarmType=null;

        alarmSnooze=new int[2];
        for(int i=0;i<2;i++) {
            alarmSnooze[i]=-1;
        }
        alarmDays=new String[7];
        for(int i=0;i<7;i++) {
            alarmDays[i]=null;
        }
    }



    public AlarmData(int id,String alarmTitle, String ringtonePatth,Date alarmTime, Calendar alarmDate,
                     int onOff,String alarmType) {
        this.alarmTitle = alarmTitle;
        this.ringtonePatth = ringtonePatth;
        this.alarmTime = alarmTime;
        this.alarmDate = alarmDate;
        this.onOff=onOff;
        this.id=0;
        this.alarmType=alarmType;
    }

    public int[] getAlarmSnooze() {
        return alarmSnooze;
    }

    public void setAlarmSnooze(int[] alarmSnooze) {
        for(int i=0;i<2;i++) {
            this.alarmSnooze[i]=alarmSnooze[i];
        }
    }

    public String getAlarmType() {
        return alarmType;
    }

    public void setAlarmType(String alarmType) {
        this.alarmType = alarmType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOnOff() {
        return onOff;
    }

    public void setOnOff(int onOff) {
        this.onOff = onOff;
    }

    public String getAlarmTitle() {
        return alarmTitle;
    }

    public void setAlarmTitle(String alarmTitle) {
        this.alarmTitle = alarmTitle;
    }

    public String getRingtonePatth() {
        return ringtonePatth;
    }

    public void setRingtonePatth(String ringtonePatth) {
        this.ringtonePatth = ringtonePatth;
    }

    public Date getAlarmTime() {
        return alarmTime;
    }

    public void setAlarmTime(Date alarmTime) {
        int h,m;

        h=alarmTime.getHours();
        m=alarmTime.getMinutes();

        this.alarmTime.setHours(h);
        this.alarmTime.setMinutes(m);
    }

    public Calendar getAlarmDate() {
        return alarmDate;
    }

    public void setAlarmDate(Calendar alarmDate) {
        int d,m,y;

        d=alarmDate.get(Calendar.DAY_OF_MONTH);
        m=alarmDate.get(Calendar.MONTH);
        y=alarmDate.get(Calendar.YEAR);

        this.alarmDate.set(y,m,d);
    }

    public String[] getAlarmDays() {
        return alarmDays;
    }

    public void setAlarmDays(String[] alarmDays) {
        for (int i=0;i<7;i++) {
            this.alarmDays[i]=alarmDays[i];
        }
    }

    /*@Override
    public boolean equals(Object obj) {



        if(obj==this) {
            return true;
        }

        if(!(obj instanceof AlarmData)) {
            return false;
        }

        AlarmData alarmData=(AlarmData)obj;

        if(alarmData==null) {
            return false;
        }

        return this.alarmTitle.equalsIgnoreCase(alarmData.alarmTitle) && this.ringtonePatth.equalsIgnoreCase(alarmData.ringtonePatth) &&
                this.alarmType.equalsIgnoreCase(alarmData.alarmType) && this.onOff == alarmData.onOff && compareDate(this, alarmData) &&
                compareTime(this, alarmData) && compareSnooze(this, alarmData) && compareDays(this, alarmData);

    }*/

    private boolean compareDays(AlarmData alarmData1,AlarmData alarmData2) {
        String[] days1=new String[7];
        String[] days2=new String[7];

        days1=alarmData1.getAlarmDays();
        days2=alarmData2.getAlarmDays();

        for(int i=0;i<7;i++) {
            if(!days1[1].equals(days2[i])) {
                return false;
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

        if(date1.getMinutes()==date2.getMinutes() && date1.getHours()==date2.getHours()) {
            return true;
        }else {
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




    @Override
    public int compareTo(@NonNull AlarmData alarmData) {






        return 0;
    }
}
