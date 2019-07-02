package in.xiandan.mmrc.example;

import android.graphics.Bitmap;

/**
 * author  dengyuhan
 * created 2017/5/27 14:39
 */
public class ThumbnailBitmap {
    private int index;
    private long millis;
    private Bitmap bitmap;

    public ThumbnailBitmap(int index, long millis, Bitmap bitmap) {
        this.index = index;
        this.millis = millis;
        this.bitmap = bitmap;
    }

    public long getMillis() {
        return millis;
    }

    public void setMillis(long millis) {
        this.millis = millis;
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
