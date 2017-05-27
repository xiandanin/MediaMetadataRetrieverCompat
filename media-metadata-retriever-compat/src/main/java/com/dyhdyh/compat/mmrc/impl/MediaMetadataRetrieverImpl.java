package com.dyhdyh.compat.mmrc.impl;

import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;

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
    public Bitmap getFrameAtTime() {
        return this.mRetriever.getFrameAtTime();
    }

    @Override
    public Bitmap getFrameAtTime(long timeUs, int option) {
        return this.mRetriever.getFrameAtTime(timeUs, option);
    }

    @Override
    public Bitmap getScaledFrameAtTime(long timeUs, int width, int height) {
        return Bitmap.createScaledBitmap(this.mRetriever.getFrameAtTime(timeUs), width, height, true);
    }

    @Override
    public Bitmap getScaledFrameAtTime(long timeUs, int option, int width, int height) {
        return Bitmap.createScaledBitmap(this.mRetriever.getFrameAtTime(timeUs, option), width, height, true);
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