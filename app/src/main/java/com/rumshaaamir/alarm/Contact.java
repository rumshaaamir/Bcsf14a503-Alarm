package com.rumshaaamir.alarm;

import java.util.ArrayList;

/**
 * Created by Rumsha Aamir on 1/24/2018.
 */

public class Contact {
    String time;
    String date;
    String dis;
    boolean online;

    public Contact(String time ,String date ,String dis ,boolean online) {
        this.time=time;
        this.date=date;
        this.dis=dis;
        this.online = online;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDis() {
        return dis;
    }

    public void setDis(String dis) {
        this.dis = dis;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public static int getLastContactId() {
        return lastContactId;
    }

    public static void setLastContactId(int lastContactId) {
        Contact.lastContactId = lastContactId;
    }

    private static int lastContactId = 0;

    public static ArrayList<Contact> createContactsList(int numContacts) {
        ArrayList<Contact> contacts = new ArrayList<Contact>();

        for (int i = 1; i <= numContacts; i++) {
            Contact contact=new Contact(i+" : 00",(i+1)+"/"+(i+1)+"/"+"200"+(i+1),"Morning Alarm",true);
            contacts.add(contact);
        }

        return contacts;
    }
}
