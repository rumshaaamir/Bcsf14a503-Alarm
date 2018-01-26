package com.rumshaaamir.alarm;

import android.content.Context;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

/**
 * Created by Rumsha Aamir on 1/24/2018.
 */

public class AlaramData_singletonClass {

    private ArrayList<AlarmData> arrayList_alarmData;

    private static AlaramData_singletonClass sAlaramData_singletonClass;
    private Context mContext;


    public AlaramData_singletonClass(Context context) {
        mContext=context;
        arrayList_alarmData=new ArrayList<AlarmData>();



        /*for(int i=0;i<10;i++) {
            AlarmData alarmData=new AlarmData();
            alarmData.setId(i+1);
            if(i%2==0) {
                alarmData.setAlarmTitle("Morning Alarm");
                alarmData.setAlarmDate("07/01/1995");
                alarmData.setAlarmTime("09:00 am");
                alarmData.setAlarmDays("Monday,Tuesday");
                alarmData.setOnOff(0);
            }else {
                alarmData.setAlarmTitle("Evening Alarm");
                alarmData.setAlarmDate("07/02/1995");
                alarmData.setAlarmTime("10:00 am");
                alarmData.setAlarmDays("Thursday,Tuesday");
                alarmData.setOnOff(1);
            }

            arrayList_alarmData.add(alarmData);
        }*/
    }

    public static AlaramData_singletonClass get(Context context) {
        if(sAlaramData_singletonClass==null) {
            sAlaramData_singletonClass=new AlaramData_singletonClass(context);
        }

        return sAlaramData_singletonClass;
    }

    private Context getContex() {
        return mContext;
    }

    public ArrayList<AlarmData> getList_alarmData() {
        return  arrayList_alarmData;
    }

    public AlarmData get_alarmData(int id) {
        for(AlarmData alarmData:arrayList_alarmData) {
            if(alarmData.getId()==id) {
                System.out.println("Found....................................");

                return  alarmData;
            }
        }

        return null;
    }

    /*public void offAlarm(int index) {
        arrayList_alarmData.
    }*/

    public void add(AlarmData alarmData) {
        arrayList_alarmData.add(alarmData);
    }

    public boolean compareTime(Date date) {
        for(AlarmData data:arrayList_alarmData) {
            Date date2=new Date();
            date2=data.getAlarmTime();


            if(date2.compareTo(date)==-1) {
                return true;
            }
        }

        return false;

    }

    public boolean compareTime(AlarmData alarmData) {
        Date date1=new Date();
        date1=alarmData.getAlarmTime();
        date1.setSeconds(0);

        int hour1=date1.getHours();
        int min1=date1.getMinutes();
        int sec1=date1.getSeconds();

        for(AlarmData data:arrayList_alarmData) {
            Date date2=new Date();
            date2=data.getAlarmTime();
            date2.setSeconds(0);

            int hour2=date2.getHours();
            int min2=date2.getMinutes();
            int sec2=date2.getSeconds();

            //Toast.makeText(getContex(),hour1+":"+min1+":"+sec1+",,,,,"+hour2+":"+min2+":"+sec2,Toast.LENGTH_SHORT).show();

            if(hour1==hour2 && min1==min2) {
                return true;
            }
        }

        return false;
    }

    public boolean compareTime(int hourCUrrent, int minCurrent) {

        Date dateCurrent=new Date();
        dateCurrent.setHours(hourCUrrent);
        dateCurrent.setMinutes(minCurrent);
        dateCurrent.setSeconds(0);

        for(AlarmData alarmData:arrayList_alarmData) {
            Date date=new Date();
            date=alarmData.getAlarmTime();
            date.setSeconds(0);


            int hour2=date.getHours();
            int min2=date.getMinutes();

            Toast.makeText(getContex(),hourCUrrent+":"+minCurrent+":"+",,,,,"+hour2+":"+min2+":",Toast.LENGTH_SHORT).show();

            /*if(hourCUrrent==hour2 && minCurrent==min2) {
                return false;
            }*/

            Toast.makeText(getContex(),date.compareTo(dateCurrent)+" ",Toast.LENGTH_SHORT).show();

            if(date.compareTo(dateCurrent)==-1) {
                Toast.makeText(getContex(),"return true",Toast.LENGTH_SHORT).show();
                return true;
            }


        }
        Toast.makeText(getContex(),"return false",Toast.LENGTH_SHORT).show();

        return false;
    }

    public boolean compareDate(int dayCurrent, int monthCurrent, int yearCurrent) {
        Calendar calendarCurrent=Calendar.getInstance();
        calendarCurrent.set(yearCurrent,monthCurrent,dayCurrent);

        for(AlarmData alarmData:arrayList_alarmData) {
            Calendar calendar=Calendar.getInstance();

            calendar=alarmData.getAlarmDate();
            int day=calendar.get(Calendar.DAY_OF_MONTH);
            int month=calendar.get(Calendar.MONTH);
            int year=calendar.get(Calendar.YEAR);

            //Toast.makeText(getContex(),day+"/"+month+"/"+year+",,,,,"+dayCurrent+"/"+monthCurrent+"/"+yearCurrent,
                    //Toast.LENGTH_SHORT).show();

            //Toast.makeText(getContex(),calendar.compareTo(calendarCurrent)+" ",Toast.LENGTH_SHORT).show();

            if(calendar.compareTo(calendarCurrent)==-1) {
                return true;
            }

            /*if(day==dayCurrent && month==monthCurrent && year==yearCurrent) {
                return true;
            }*/
        }

        return false;

    }

    public boolean compareDays(String[] DaysCurrent) {

        String[] Days=new String[7];


        for(AlarmData alarmData:arrayList_alarmData) {
            Days=alarmData.getAlarmDays();

            for(int i=0;i<7;i++) {
                for(int j=0;j<7;j++) {

                    if(DaysCurrent[i]!=null) {

                        if(Days[j]!=null) {

                            //Toast.makeText(getContex(),DaysCurrent[i]+":"+Days[j],Toast.LENGTH_SHORT).show();

                            if(Objects.equals(DaysCurrent[i], Days[j])) {
                                return true;
                            }
                        }
                    }
                }
            }
        }

        return false;
    }

    public void delete(int id) {

        for(AlarmData alarmData:arrayList_alarmData) {
            if(alarmData.getId()==id) {
                arrayList_alarmData.remove(alarmData);
                return;
            }
        }
    }


}
