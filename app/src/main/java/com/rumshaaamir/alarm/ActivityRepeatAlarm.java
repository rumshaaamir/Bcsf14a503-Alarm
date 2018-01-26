package com.rumshaaamir.alarm;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

/**
 * Created by Rumsha Aamir on 1/24/2018.
 */

public class ActivityRepeatAlarm extends AppCompatActivity {


    private CheckBox mcheckBox_mon;
    private CheckBox mcheckBox_tues;
    private CheckBox mcheckBox_wed;
    private CheckBox mcheckBox_thurs;
    private CheckBox mcheckBox_fri;
    private CheckBox mcheckBox_sat;
    private CheckBox mcheckBox_sun;

    private boolean mlastValue=false;

    private String TAG="REPEAT_ALARM";

    private String []mstrings_nameDays;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repeatalarm_aas);

        //setting title for action bar and also change the color of the text on title bar
        String title="Repeat Alarm";
        SpannableString s = new SpannableString(title);
        s.setSpan(new ForegroundColorSpan(Color.DKGRAY), 0, title.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        getSupportActionBar().setTitle(s);

        initializeComponents();

        mstrings_nameDays=new String[7];
        for (int i=0;i<7;i++) {
            mstrings_nameDays[0]=null;
        }

        Button button_cancel=(Button)findViewById(R.id.cancel_button);
        button_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Button button_save=(Button)findViewById(R.id.save_button);
        button_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkCondition();

                int requestCode=2;
                String days="Days";
                Intent intent_startActivity=new Intent();
                intent_startActivity.putExtra(TAG,mstrings_nameDays);
                setResult(requestCode,intent_startActivity);
                finish();
            }
        });


        /*//rough work
        final CheckBox checkBox=(CheckBox)findViewById(R.id.mon_checkBox);
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //GradientDrawable gradientDrawable=new GradientDrawable();
                //gradientDrawable.setColor(Color.RED);
                //checkBox.setBackground(gradientDrawable);

            }
        });*/

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.menu_activityrepeatalarm,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.selectAll:

                if(mlastValue==true) {
                    changeCondition(false);
                    mlastValue=false;
                }else if(mlastValue==false) {
                    changeCondition(true);
                    mlastValue=true;
                }

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }


    }

    private void initializeComponents() {
        mcheckBox_mon=(CheckBox)findViewById(R.id.mon_checkBox);
        mcheckBox_tues=(CheckBox)findViewById(R.id.tues_checkBox);
        mcheckBox_wed=(CheckBox)findViewById(R.id.wed_checkBox);
        mcheckBox_thurs=(CheckBox)findViewById(R.id.thurs_checkBox);
        mcheckBox_fri=(CheckBox)findViewById(R.id.fri_checkBox);
        mcheckBox_sat=(CheckBox)findViewById(R.id.sat_checkBox);
        mcheckBox_sun=(CheckBox)findViewById(R.id.sun_checkBox);
    }

    private void changeCondition(boolean value) {
        mcheckBox_mon.setChecked(value);
        mcheckBox_tues.setChecked(value);
        mcheckBox_wed.setChecked(value);
        mcheckBox_thurs.setChecked(value);
        mcheckBox_fri.setChecked(value);
        mcheckBox_sat.setChecked(value);
        mcheckBox_sun.setChecked(value);
    }


    private boolean checkCondition_mon() {
        if (mcheckBox_mon.isChecked()) {
            return true;
        }else {
            return false;
        }
    }

    private boolean checkCondition_tues() {
        if (mcheckBox_tues.isChecked()) {
            return true;
        }else {
            return false;
        }
    }

    private boolean checkCondition_wed() {
        if (mcheckBox_wed.isChecked()) {
            return true;
        }else {
            return false;
        }
    }

    private boolean checkCondition_thurs() {
        if (mcheckBox_thurs.isChecked()) {
            return true;
        }else {
            return false;
        }
    }

    private boolean checkCondition_fri() {
        if (mcheckBox_fri.isChecked()) {
            return true;
        }else {
            return false;
        }
    }

    private boolean checkCondition_sat() {
        if (mcheckBox_sat.isChecked()) {
            return true;
        }else {
            return false;
        }
    }

    private boolean checkCondition_sun() {
        if (mcheckBox_sun.isChecked()) {
            return true;
        }else {
            return false;
        }
    }

    private void checkCondition() {

        int i=0;

        if(checkCondition_mon()) {
            mstrings_nameDays[i]="Monday";
            i++;
        }

        if(checkCondition_tues()) {
            mstrings_nameDays[i]="Tuesday";
            i++;
        }

        if(checkCondition_wed()) {
            mstrings_nameDays[i]="Wednesday";
            i++;
        }

        if(checkCondition_thurs()) {
            mstrings_nameDays[i]="Thursday";
            i++;
        }

        if(checkCondition_fri()) {
            mstrings_nameDays[i]="Friday";
            i++;
        }

        if(checkCondition_sat()) {
            mstrings_nameDays[i]="Saturday";
            i++;
        }

        if(checkCondition_sun()) {
            mstrings_nameDays[i]="Sunday";
            i++;
        }

        /*if(mcheckBox_mon.isChecked()) {
            mstrings_nameDays=new String[1];
            mstrings_nameDays[0]="Monday";
        }else if(mcheckBox_mon.isChecked() && mcheckBox_tues.isChecked()) {
            mstrings_nameDays=new String[2];
            mstrings_nameDays[0]="Monday";
            mstrings_nameDays[1]="Tuesday";
        }else if(mcheckBox_mon.isChecked() && mcheckBox_tues.isChecked() && mcheckBox_wed.isChecked()) {
            mstrings_nameDays=new String[3];
            mstrings_nameDays[0]="Monday";
            mstrings_nameDays[1]="Tuesday";
            mstrings_nameDays[2]="Wednesday";
        }else if(mcheckBox_mon.isChecked() && mcheckBox_tues.isChecked() && mcheckBox_wed.isChecked()
                && mcheckBox_thurs.isChecked()) {
            mstrings_nameDays=new String[4];
            mstrings_nameDays[0]="Monday";
            mstrings_nameDays[1]="Tuesday";
            mstrings_nameDays[2]="Wednesday";
            mstrings_nameDays[3]="Thursday";
        }else if(mcheckBox_mon.isChecked() && mcheckBox_tues.isChecked() && mcheckBox_wed.isChecked()
                && mcheckBox_thurs.isChecked() && mcheckBox_fri.isChecked()) {
            mstrings_nameDays=new String[5];
            mstrings_nameDays[0]="Monday";
            mstrings_nameDays[1]="Tuesday";
            mstrings_nameDays[2]="Wednesday";
            mstrings_nameDays[3]="Thursday";
            mstrings_nameDays[4]="Friday";
        }else if(mcheckBox_mon.isChecked() && mcheckBox_tues.isChecked() && mcheckBox_wed.isChecked()
                && mcheckBox_thurs.isChecked() && mcheckBox_fri.isChecked() && mcheckBox_sat.isChecked()) {
            mstrings_nameDays=new String[6];
            mstrings_nameDays[0]="Monday";
            mstrings_nameDays[1]="Tuesday";
            mstrings_nameDays[2]="Wednesday";
            mstrings_nameDays[3]="Thursday";
            mstrings_nameDays[4]="Friday";
            mstrings_nameDays[5]="Saturday";
        }else if(mcheckBox_mon.isChecked() && mcheckBox_tues.isChecked() && mcheckBox_wed.isChecked()
                && mcheckBox_thurs.isChecked() && mcheckBox_fri.isChecked() && mcheckBox_sat.isChecked()
                && mcheckBox_sun.isChecked()) {
            mstrings_nameDays=new String[7];
            mstrings_nameDays[0]="Monday";
            mstrings_nameDays[1]="Tuesday";
            mstrings_nameDays[2]="Wednesday";
            mstrings_nameDays[3]="Thursday";
            mstrings_nameDays[4]="Friday";
            mstrings_nameDays[5]="Saturday";
            mstrings_nameDays[5]="Sunday";
        }*/
    }



    /*private void changecolor(final CheckBox checkBox1) {
        checkBox1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                compoundButton.setBackground(Color.parseColor("#FFFFFF"));
            }
        });
    }*/
}
