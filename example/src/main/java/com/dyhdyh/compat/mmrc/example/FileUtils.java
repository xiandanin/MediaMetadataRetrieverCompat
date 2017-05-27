package com.dyhdyh.compat.mmrc.example;

import android.app.Activity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * author  dengyuhan
 * created 2017/5/27 13:40
 */
public class FileUtils {

    public static void copyAssetFile(Activity activity,String assetFileName, File newFile) {
        try {
            InputStream is = activity.getAssets().open(assetFileName);
            int byteread = 0;
            FileOutputStream fs = new FileOutputStream(newFile);
            byte[] buffer = new byte[1024];
            while ((byteread = is.read(buffer)) != -1) {
                fs.write(buffer, 0, byteread);
            }
            fs.close();
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
