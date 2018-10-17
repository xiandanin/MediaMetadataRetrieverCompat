package com.dyhdyh.compat.mmrc.impl;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.text.TextUtils;

import com.dyhdyh.compat.mmrc.IMediaMetadataRetriever;
import com.dyhdyh.compat.mmrc.transform.MetadataTransform;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.util.Map;

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
    public void setDataSource(File inputFile) throws FileNotFoundException {
        this.mRetriever.setDataSource(inputFile.getAbsolutePath());
    }

    @Override
    public void setDataSource(Context context, Uri uri) {
        this.mRetriever.setDataSource(context, uri);
    }

    @Override
    public void setDataSource(String uri, Map<String, String> headers) {
        this.mRetriever.setDataSource(uri, headers);
    }

    @Override
    public void setDataSource(FileDescriptor fd, long offset, long length) {
        this.mRetriever.setDataSource(fd, offset, length);
    }

    @Override
    public void setDataSource(FileDescriptor fd) {
        this.mRetriever.setDataSource(fd);
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
        final int[] size = makeRotateSize(width, height);
        return Bitmap.createScaledBitmap(atTime, size[0], size[1], true);
    }

    @Override
    public Bitmap getScaledFrameAtTime(long timeUs, int option, int width, int height) {
        Bitmap atTime = this.mRetriever.getFrameAtTime(timeUs, option);
        if (atTime == null) {
            return null;
        }
        final int[] size = makeRotateSize(width, height);
        return Bitmap.createScaledBitmap(atTime, size[0], size[1], true);
    }

    @Override
    public byte[] getEmbeddedPicture() {
        return this.mRetriever.getEmbeddedPicture();
    }

    @Override
    public String extractMetadata(int keyCode) {
        String keyCodeString = MetadataTransform.transform(getClass(), keyCode);
        if (TextUtils.isEmpty(keyCodeString)) {
            return null;
        }
        return this.mRetriever.extractMetadata(Integer.parseInt(keyCodeString));
    }

    @Override
    public void release() {
        this.mRetriever.release();
    }


    private int[] makeRotateSize(int sourceWidth, int sourceHeight) {
        final String rotationString = mRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION);
        if (!TextUtils.isEmpty(rotationString)) {
            try {
                final int rotation = Integer.parseInt(rotationString);
                if (isVertical(rotation)) {
                    return new int[]{sourceHeight, sourceWidth};
                }
            } catch (NumberFormatException ignored) {

            }
        }
        return new int[]{sourceWidth, sourceHeight};
    }

    private boolean isVertical(float rotate) {
        float abs = Math.abs(rotate);
        return abs == 90 || abs == 270;
    }
}