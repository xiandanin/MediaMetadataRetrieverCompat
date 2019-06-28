package com.dyhdyh.compat.mmrc.example;

import android.graphics.Bitmap;

/**
 * author  dengyuhan
 * created 2017/5/27 14:39
 */
public class ThumbnailBitmap {
    private String retrieverName;
    private int index;
    private Bitmap bitmap;

    public ThumbnailBitmap(String retrieverName, int index, Bitmap bitmap) {
        this.retrieverName = retrieverName;
        this.index = index;
        this.bitmap = bitmap;
    }

    public String getRetrieverName() {
        return retrieverName;
    }

    public void setRetrieverName(String retrieverName) {
        this.retrieverName = retrieverName;
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
