package com.rumshaaamir.alarm;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;

/**
 * Created by Rumsha Aamir on 1/24/2018.
 */
public class About extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        //setting title for action bar and also change the color of the text on title bar
        String titleBar = "About";
        //setTitle(titleBar);
        SpannableString s = new SpannableString(titleBar);
        s.setSpan(new ForegroundColorSpan(Color.DKGRAY), 0, titleBar.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        getSupportActionBar().setTitle(s);
    }
}