package com.dyhdyh.compat.mmrc.engine;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;

import com.dyhdyh.compat.mmrc.MediaMetadataRetrieverEngine;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.util.Map;

/**
 * webp引擎
 * @author dengyuhan
 * created 2019/3/15 16:19
 */
public class WebPEngineImpl implements MediaMetadataRetrieverEngine {
    @Override
    public String getName() {
        return getClass().getSimpleName();
    }

    @Override
    public void setDataSource(File inputFile) throws FileNotFoundException {

    }

    @Override
    public void setDataSource(String uri, Map<String, String> headers) {

    }

    @Override
    public void setDataSource(FileDescriptor fd, long offset, long length) {

    }

    @Override
    public void setDataSource(FileDescriptor fd) {

    }

    @Override
    public void setDataSource(Context context, Uri uri) {

    }

    @Override
    public Bitmap getFrameAtTime() {
        return null;
    }

    @Override
    public Bitmap getFrameAtTime(long timeUs, int option) {
        return null;
    }

    @Override
    public Bitmap getScaledFrameAtTime(long timeUs, int width, int height) {
        return null;
    }

    @Override
    public Bitmap getScaledFrameAtTime(long timeUs, int option, int width, int height) {
        return null;
    }

    @Override
    public byte[] getEmbeddedPicture() {
        return new byte[0];
    }

    @Override
    public String extractMetadata(int keyCode) {
        return null;
    }

    @Override
    public void release() {

    }
}
