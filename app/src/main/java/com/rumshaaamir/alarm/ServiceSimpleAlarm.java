package com.rumshaaamir.alarm;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.widget.Toast;

/**
 * Created by Rumsha Aamir on 1/24/2018.
 */

public class ServiceSimpleAlarm extends Service {

    private MediaPlayer mmediaPlayer;

    private String TAG="TAG_ServiceSimpleAlarm";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        int status=intent.getIntExtra(TAG,0);

        mmediaPlayer=MediaPlayer.create(this,R.raw.default_tone);
        mmediaPlayer.setLooping(true);
        mmediaPlayer.start();

        /*if(status==1) {
            Toast.makeText(this, "1", Toast.LENGTH_SHORT).show();
            mmediaPlayer.start();
        }else if(status==0) {
            Toast.makeText(this, "0", Toast.LENGTH_SHORT).show();
            if (mmediaPlayer != null) {
                mmediaPlayer.stop();
                mmediaPlayer.release();
                mmediaPlayer = null;
            }
        }*/
        return START_NOT_STICKY;

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //Toast.makeText(this,"Destroy",Toast.LENGTH_SHORT).show();
        if (mmediaPlayer != null) {
            mmediaPlayer.stop();
            mmediaPlayer.release();
            mmediaPlayer = null;
        }
    }
}
