package com.dyhdyh.compat.mmrc.transform;

import android.graphics.Bitmap;
import android.graphics.Matrix;

/**
 * author  dengyuhan
 * created 2017/5/27 13:13
 */
public class BitmapRotateTransform {

    public static Bitmap transform(Bitmap bitmap, float rotate) {
        Matrix matrix = new Matrix();
        matrix.postRotate(rotate);
        int frameWidth = bitmap.getWidth();
        int frameHeight = bitmap.getHeight();
        return Bitmap.createBitmap(bitmap, 0, 0, frameWidth, frameHeight, matrix, true);
    }


}
