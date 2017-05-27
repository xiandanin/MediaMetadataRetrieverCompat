package com.dyhdyh.compat.mmrc.example;

import android.graphics.Bitmap;

/**
 * author  dengyuhan
 * created 2017/5/27 14:39
 */
public class ThumbnailBitmap {
    private int index;
    private Bitmap bitmap;

    public ThumbnailBitmap(int index, Bitmap bitmap) {
        this.index = index;
        this.bitmap = bitmap;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
