package com.rumshaaamir.alarm;

import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;

/**
 * Created by Rumsha Aamir on 1/24/2018.
 */

public class UserComparator implements Comparator<AlarmData> {
    @Override
    public int compare(AlarmData alarmData, AlarmData t1) {
        Date date1=alarmData.getAlarmTime();
        Date date2=t1.getAlarmTime();

        return date1.compareTo(date2);
    }
}
