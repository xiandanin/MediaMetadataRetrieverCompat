package com.dyhdyh.compat.mmrc.impl;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;

import com.dyhdyh.compat.mmrc.IMediaMetadataRetriever;

/**
 * 基于原生MediaMetadataRetriever实现
 * author  dengyuhan
 * created 2017/5/26 14:49
 */
public class MediaMetadataRetrieverImpl implements IMediaMetadataRetriever {
    private MediaMetadataRetriever mRetriever;

    public MediaMetadataRetrieverImpl() {
        this.mRetriever = new MediaMetadataRetriever();
    }

    @Override
    public void setDataSource(String path) {
        this.mRetriever.setDataSource(path);
    }

    @Override
    public void setDataSource(Context context, Uri uri) {
        this.mRetriever.setDataSource(context, uri);
    }

    @Override
    public Bitmap getFrameAtTime() {
        return this.mRetriever.getFrameAtTime();
    }

    @Override
    public Bitmap getFrameAtTime(long timeUs, int option) {
        return this.mRetriever.getFrameAtTime(timeUs, option);
    }

    @Override
    public Bitmap getScaledFrameAtTime(long timeUs, int width, int height) {
        Bitmap atTime = this.mRetriever.getFrameAtTime(timeUs);
        if (atTime == null) {
            return null;
        }
        return Bitmap.createScaledBitmap(atTime, width, height, true);
    }

    @Override
    public Bitmap getScaledFrameAtTime(long timeUs, int option, int width, int height) {
        Bitmap atTime = this.mRetriever.getFrameAtTime(timeUs, option);
        if (atTime == null) {
            return null;
        }
        return Bitmap.createScaledBitmap(atTime, width, height, true);
    }

    @Override
    public byte[] getEmbeddedPicture() {
        return this.mRetriever.getEmbeddedPicture();
    }

    @Override
    public String extractMetadata(String keyCode) {
        return this.mRetriever.extractMetadata(Integer.parseInt(keyCode));
    }

    @Override
    public void release() {
        this.mRetriever.release();
    }
}