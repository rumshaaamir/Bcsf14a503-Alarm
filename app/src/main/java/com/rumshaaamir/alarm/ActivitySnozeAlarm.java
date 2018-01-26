package com.rumshaaamir.alarm;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

/**
 * Created by Rumsha Aamir on 1/24/2018.
 */

public class ActivitySnozeAlarm extends AppCompatActivity {

    private CheckBox mcheckBox_oneTime;
    private CheckBox mcheckBox_twoTime;
    private CheckBox mcheckBox_threeTime;
    private CheckBox mcheckBox_fiveTime;

    private CheckBox mcheckBox_oneMin;
    private CheckBox mcheckBox_threeMin;
    private CheckBox mcheckBox_fiveMin;
    private CheckBox mcheckBox_sevenMin;
    private CheckBox mcheckBox_tenMin;
    private CheckBox mcheckBox_fifteenMin;

    private int []state;

    private String TAG="SNOOZE_ALARM";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snozealarm_aas);

        //setting title for action bar and also change the color of the text on title bar
        String title="Snooze Alarm";
        SpannableString s = new SpannableString(title);
        s.setSpan(new ForegroundColorSpan(Color.DKGRAY), 0, title.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        getSupportActionBar().setTitle(s);

        initializeComponents();

        state=new int[2];

        mcheckBox_oneTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pairT1();
            }
        });

        mcheckBox_twoTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pairT2();
            }
        });

        mcheckBox_threeTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pairT3();
            }
        });

        mcheckBox_fiveTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pairT5();
            }
        });

        mcheckBox_oneMin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pairM1();
            }
        });

        mcheckBox_threeMin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pairM3();
            }
        });

        mcheckBox_fiveMin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pairM5();
            }
        });

        mcheckBox_sevenMin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pairM7();
            }
        });

        mcheckBox_tenMin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pairM10();
            }
        });

        mcheckBox_fifteenMin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pairM15();
            }
        });

        Button button_save=(Button)findViewById(R.id.save_button_saa);
        button_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                saveStates();
                int requestCode=3;

                Intent intent_startActivity=new Intent();
                intent_startActivity.putExtra(TAG,state);
                setResult(requestCode,intent_startActivity);

                /*for(int i=0;i<2;i++) {
                    Toast.makeText(getApplication(),state[i]+" ",Toast.LENGTH_SHORT).show();
                }*/

                finish();



            }
        });

        Button button_cancel=(Button)findViewById(R.id.cancel_button_saa);
        button_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    private void initializeComponents() {
        mcheckBox_oneTime=(CheckBox)findViewById(R.id.one_checkBox);
        mcheckBox_twoTime=(CheckBox)findViewById(R.id.two_checkBox);
        mcheckBox_threeTime=(CheckBox)findViewById(R.id.three_checkBox);
        mcheckBox_fiveTime=(CheckBox)findViewById(R.id.five_checkBox);

        mcheckBox_oneMin=(CheckBox)findViewById(R.id.one_min_checkBox);
        mcheckBox_threeMin=(CheckBox)findViewById(R.id.thre_min_checkBox);
        mcheckBox_fiveMin=(CheckBox)findViewById(R.id.five_min_checkBox);
        mcheckBox_sevenMin=(CheckBox)findViewById(R.id.seven_min_checkBox);
        mcheckBox_tenMin=(CheckBox)findViewById(R.id.ten_min_checkBox);
        mcheckBox_fifteenMin=(CheckBox)findViewById(R.id.fifteen_min_checkBox);
    }

    private void pairT1() {
        mcheckBox_oneTime.setChecked(true);
        mcheckBox_twoTime.setChecked(false);
        mcheckBox_threeTime.setChecked(false);
        mcheckBox_fiveTime.setChecked(false);
    }

    private void pairT2() {
        mcheckBox_twoTime.setChecked(true);
        mcheckBox_oneTime.setChecked(false);
        mcheckBox_threeTime.setChecked(false);
        mcheckBox_fiveTime.setChecked(false);
    }

    private void pairT3() {
        mcheckBox_threeTime.setChecked(true);
        mcheckBox_oneTime.setChecked(false);
        mcheckBox_twoTime.setChecked(false);
        mcheckBox_fiveTime.setChecked(false);
    }

    private void pairT5() {
        mcheckBox_fiveTime.setChecked(true);
        mcheckBox_oneTime.setChecked(false);
        mcheckBox_threeTime.setChecked(false);
        mcheckBox_twoTime.setChecked(false);
    }

    private void pairM1() {
        mcheckBox_oneMin.setChecked(true);
        mcheckBox_threeMin.setChecked(false);
        mcheckBox_fiveMin.setChecked(false);
        mcheckBox_sevenMin.setChecked(false);
        mcheckBox_tenMin.setChecked(false);
        mcheckBox_fifteenMin.setChecked(false);
    }

    private void pairM3() {
        mcheckBox_oneMin.setChecked(false);
        mcheckBox_threeMin.setChecked(true);
        mcheckBox_fiveMin.setChecked(false);
        mcheckBox_sevenMin.setChecked(false);
        mcheckBox_tenMin.setChecked(false);
        mcheckBox_fifteenMin.setChecked(false);
    }

    private void pairM5() {
        mcheckBox_oneMin.setChecked(false);
        mcheckBox_threeMin.setChecked(false);
        mcheckBox_fiveMin.setChecked(true);
        mcheckBox_sevenMin.setChecked(false);
        mcheckBox_tenMin.setChecked(false);
        mcheckBox_fifteenMin.setChecked(false);
    }

    private void pairM7() {
        mcheckBox_oneMin.setChecked(false);
        mcheckBox_threeMin.setChecked(false);
        mcheckBox_fiveMin.setChecked(false);
        mcheckBox_sevenMin.setChecked(true);
        mcheckBox_tenMin.setChecked(false);
        mcheckBox_fifteenMin.setChecked(false);
    }

    private void pairM10() {
        mcheckBox_oneMin.setChecked(false);
        mcheckBox_threeMin.setChecked(false);
        mcheckBox_fiveMin.setChecked(false);
        mcheckBox_sevenMin.setChecked(false);
        mcheckBox_tenMin.setChecked(true);
        mcheckBox_fifteenMin.setChecked(false);
    }

    private void pairM15() {
        mcheckBox_oneMin.setChecked(false);
        mcheckBox_threeMin.setChecked(false);
        mcheckBox_fiveMin.setChecked(false);
        mcheckBox_sevenMin.setChecked(false);
        mcheckBox_tenMin.setChecked(false);
        mcheckBox_fifteenMin.setChecked(true);
    }

    private boolean checkCondition_oneTime() {
        if(mcheckBox_oneTime.isChecked()) {
            return true;
        }else {
            return false;
        }
    }

    private boolean checkCondition_twoTime() {
        if(mcheckBox_twoTime.isChecked()) {
            return true;
        }else {
            return false;
        }
    }

    private boolean checkCondition_threeTime() {
        if(mcheckBox_threeTime.isChecked()) {
            return true;
        }else {
            return false;
        }
    }

    private boolean checkCondition_fiveTime() {
        if(mcheckBox_fiveTime.isChecked()) {
            return true;
        }else {
            return false;
        }
    }

    private boolean checkCondition_oneMin() {
        if(mcheckBox_oneMin.isChecked()) {
            return true;
        }else {
            return false;
        }
    }

    private boolean checkCondition_threeMin() {
        if(mcheckBox_threeMin.isChecked()) {
            return true;
        }else {
            return false;
        }
    }

    private boolean checkCondition_fiveMin() {
        if(mcheckBox_fiveMin.isChecked()) {
            return true;
        }else {
            return false;
        }
    }

    private boolean checkCondition_sevenMin() {
        if(mcheckBox_sevenMin.isChecked()) {
            return true;
        }else {
            return false;
        }
    }

    private boolean checkCondition_tenMin() {
        if(mcheckBox_tenMin.isChecked()) {
            return true;
        }else {
            return false;
        }
    }

    private boolean checkCondition_fifteenMin() {
        if(mcheckBox_fifteenMin.isChecked()) {
            return true;
        }else {
            return false;
        }
    }

    private void saveStates() {
        if(checkCondition_oneTime()) {
            state[0]=1;
        }

        if(checkCondition_twoTime()) {
            state[0]=2;
        }

        if(checkCondition_threeTime()) {
            state[0]=3;
        }

        if(checkCondition_fiveTime()) {
            state[0]=5;
        }

        if(checkCondition_oneMin()) {
            state[1]=1;
        }

        if(checkCondition_threeMin()) {
            state[1]=3;
        }

        if(checkCondition_fiveMin()) {
            state[1]=5;
        }

        if(checkCondition_sevenMin()) {
            state[1]=7;
        }

        if(checkCondition_tenMin()) {
            state[1]=10;
        }

        if(checkCondition_fifteenMin()) {
            state[1]=15;
        }
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.menu_activityrepeatalarm,menu);
        return true;
    }*/
}
