package com.dyhdyh.compat.mmrc;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.util.Map;

/**
 * 引擎
 * @author dengyuhan
 * created 2019/3/15 16:16
 */
public interface MediaMetadataRetrieverEngine {

    String getName();

    void setDataSource(File inputFile) throws FileNotFoundException;

    void setDataSource(String uri, Map<String, String> headers);

    void setDataSource(FileDescriptor fd, long offset, long length);

    void setDataSource(FileDescriptor fd);

    void setDataSource(Context context, Uri uri);

    Bitmap getFrameAtTime();

    Bitmap getFrameAtTime(long timeUs, int option);

    Bitmap getScaledFrameAtTime(long timeUs, int width, int height);

    Bitmap getScaledFrameAtTime(long timeUs, int option, int width, int height);

    byte[] getEmbeddedPicture();

    String extractMetadata(int keyCode);

    void release();
}
