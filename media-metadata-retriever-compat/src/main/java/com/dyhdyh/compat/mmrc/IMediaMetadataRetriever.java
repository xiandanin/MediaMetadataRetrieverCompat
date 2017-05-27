package com.dyhdyh.compat.mmrc;

import android.graphics.Bitmap;

/**
 * author  dengyuhan
 * created 2017/5/26 14:49
 */
public interface IMediaMetadataRetriever {

    void setDataSource(String path);

    Bitmap getFrameAtTime();

    Bitmap getFrameAtTime(long timeUs, int option);

    Bitmap getScaledFrameAtTime(long timeUs, int width, int height);

    Bitmap getScaledFrameAtTime(long timeUs, int option, int width, int height);

    byte[] getEmbeddedPicture();

    String extractMetadata(String keyCode);

    void release();
}
