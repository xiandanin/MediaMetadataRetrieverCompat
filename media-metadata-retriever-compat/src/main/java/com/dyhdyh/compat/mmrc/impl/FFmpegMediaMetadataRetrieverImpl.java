package com.dyhdyh.compat.mmrc.impl;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.text.TextUtils;

import com.dyhdyh.compat.mmrc.IMediaMetadataRetriever;
import com.dyhdyh.compat.mmrc.transform.BitmapRotateTransform;
import com.dyhdyh.compat.mmrc.transform.MetadataTransform;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.util.Map;

import wseemann.media.FFmpegMediaMetadataRetriever;

/**
 * 基于ffmpeg实现
 * author  dengyuhan
 * created 2017/5/26 14:51
 */
public class FFmpegMediaMetadataRetrieverImpl implements IMediaMetadataRetriever {
    private FFmpegMediaMetadataRetriever mRetriever;

    //原生实例
    private MediaMetadataRetrieverImpl mAndroidRetriever;


    /**
     * 默认启用备用实例
     */
    public FFmpegMediaMetadataRetrieverImpl() {
        this(true);
    }

    /**
     * 是否启用备用实例
     *
     * @param enableBackupImpl
     */
    public FFmpegMediaMetadataRetrieverImpl(boolean enableBackupImpl) {
        this.mRetriever = new FFmpegMediaMetadataRetriever();
        if (enableBackupImpl) {
            //如果启用备用实例 当ffmpeg获取数据失败的时候 会用备用实例再获取一次
            this.mAndroidRetriever = new MediaMetadataRetrieverImpl();
        }
    }

    @Override
    public void setDataSource(File inputFile) throws FileNotFoundException {
        this.mRetriever.setDataSource(inputFile.getAbsolutePath());

        if (mAndroidRetriever != null) {
            this.mAndroidRetriever.setDataSource(inputFile);
        }
    }

    @Override
    public void setDataSource(Context context, Uri uri) {
        this.mRetriever.setDataSource(context, uri);

        if (mAndroidRetriever != null) {
            this.mAndroidRetriever.setDataSource(context, uri);
        }
    }

    @Override
    public void setDataSource(String uri, Map<String, String> headers) {
        this.mRetriever.setDataSource(uri, headers);

        if (mAndroidRetriever != null) {
            this.mAndroidRetriever.setDataSource(uri, headers);
        }
    }

    @Override
    public void setDataSource(FileDescriptor fd, long offset, long length) {
        this.mRetriever.setDataSource(fd, offset, length);

        if (mAndroidRetriever != null) {
            this.mAndroidRetriever.setDataSource(fd, offset, length);
        }
    }

    @Override
    public void setDataSource(FileDescriptor fd) {
        this.mRetriever.setDataSource(fd);

        if (mAndroidRetriever != null) {
            this.mAndroidRetriever.setDataSource(fd);
        }
    }

    @Override
    public Bitmap getFrameAtTime() {
        final Bitmap frameAtTime = this.mRetriever.getFrameAtTime();

        if (frameAtTime == null && mAndroidRetriever != null) {
            return this.mAndroidRetriever.getFrameAtTime();
        }
        return makeRotateBitmap(frameAtTime);
    }

    @Override
    public Bitmap getFrameAtTime(long timeUs, int option) {
        final Bitmap frameAtTime = this.mRetriever.getFrameAtTime(timeUs, option);

        if (frameAtTime == null && mAndroidRetriever != null) {
            return this.mAndroidRetriever.getFrameAtTime(timeUs, option);
        }
        return makeRotateBitmap(frameAtTime);
    }

    @Override
    public Bitmap getScaledFrameAtTime(long timeUs, int width, int height) {
        final Bitmap scaledFrameAtTime = this.mRetriever.getScaledFrameAtTime(timeUs, width, height);

        if (scaledFrameAtTime == null && mAndroidRetriever != null) {
            return this.mAndroidRetriever.getScaledFrameAtTime(timeUs, width, height);
        }
        return makeRotateBitmap(scaledFrameAtTime);
    }

    @Override
    public Bitmap getScaledFrameAtTime(long timeUs, int option, int width, int height) {
        final Bitmap scaledFrameAtTime = this.mRetriever.getScaledFrameAtTime(timeUs, option, width, height);
        if (scaledFrameAtTime == null && mAndroidRetriever != null) {
            return this.mAndroidRetriever.getScaledFrameAtTime(timeUs, option, width, height);
        }
        return makeRotateBitmap(scaledFrameAtTime);
    }

    @Override
    public byte[] getEmbeddedPicture() {
        final byte[] picture = this.mRetriever.getEmbeddedPicture();
        if (picture == null && mAndroidRetriever != null) {
            return this.mAndroidRetriever.getEmbeddedPicture();
        }
        return picture;
    }

    @Override
    public String extractMetadata(int keyCode) {
        String keyCodeString = MetadataTransform.transform(getClass(), keyCode);
        if (TextUtils.isEmpty(keyCodeString)) {
            return null;
        }
        final String metadata = this.mRetriever.extractMetadata(keyCodeString);

        if (metadata == null && mAndroidRetriever != null) {
            return this.mAndroidRetriever.extractMetadata(keyCode);
        }
        return metadata;
    }

    @Override
    public void release() {
        this.mRetriever.release();

        if (mAndroidRetriever != null) {
            this.mAndroidRetriever.release();
        }
    }

    private Bitmap makeRotateBitmap(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }
        final String rotationString = mRetriever.extractMetadata(FFmpegMediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION);
        if (!TextUtils.isEmpty(rotationString)) {
            try {
                final int rotation = Integer.parseInt(rotationString);
                if (rotation != 0) {
                    return BitmapRotateTransform.transform(bitmap, rotation);
                }
            } catch (NumberFormatException ignored) {

            }
        }
        return bitmap;
    }

}