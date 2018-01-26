package com.rumshaaamir.alarm;

import android.os.Build;
import android.os.Environment;
import android.os.StatFs;

import java.io.File;

/**
 * Created by Rumsha Aamir on 1/24/2018.
 */

public class CheckInternalStorageSpace {

    public static long getAvailableInternalMemorySize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize, availableBlocks;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            blockSize = stat.getBlockSizeLong();
            availableBlocks = stat.getAvailableBlocksLong();
        } else {
            blockSize = stat.getBlockSize();
            availableBlocks = stat.getAvailableBlocks();
        }
        return availableBlocks * blockSize;
    }

}
