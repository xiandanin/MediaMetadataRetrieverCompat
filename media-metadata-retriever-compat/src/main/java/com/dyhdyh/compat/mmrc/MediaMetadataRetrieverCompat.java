package com.dyhdyh.compat.mmrc;

import android.graphics.Bitmap;
import android.text.TextUtils;

import com.dyhdyh.compat.mmrc.impl.FFmpegMediaMetadataRetrieverImpl;
import com.dyhdyh.compat.mmrc.impl.MediaMetadataRetrieverImpl;
import com.dyhdyh.compat.mmrc.transform.BitmapRotateTransform;
import com.dyhdyh.compat.mmrc.transform.MetadataTransform;

/**
 * author  dengyuhan
 * created 2017/5/26 14:48
 */
public class MediaMetadataRetrieverCompat {

    private IMediaMetadataRetriever impl;
    private MediaMetadataRetrieverImpl androidImpl;
    private String mPath;

    public static final int OPTION_PREVIOUS_SYNC = 0x00;
    public static final int OPTION_NEXT_SYNC = 0x01;
    public static final int OPTION_CLOSEST_SYNC = 0x02;
    public static final int OPTION_CLOSEST = 0x03;

    public static final int METADATA_KEY_ALBUM = 1;
    public static final int METADATA_KEY_ARTIST = 2;
    public static final int METADATA_KEY_AUTHOR = 3;
    public static final int METADATA_KEY_COMPOSER = 4;
    public static final int METADATA_KEY_DATE = 5;
    public static final int METADATA_KEY_TITLE = 7;
    public static final int METADATA_KEY_DURATION = 9;
    public static final int METADATA_KEY_NUM_TRACKS = 10;
    public static final int METADATA_KEY_ALBUMARTIST = 13;
    public static final int METADATA_KEY_DISC_NUMBER = 14;
    public static final int METADATA_KEY_VIDEO_WIDTH = 18;
    public static final int METADATA_KEY_VIDEO_HEIGHT = 19;
    public static final int METADATA_KEY_VIDEO_ROTATION = 24;
    public static final int METADATA_KEY_CAPTURE_FRAMERATE = 25;

    public static final int RETRIEVER_FFMPEG = 0;
    public static final int RETRIEVER_ANDROID = 1;

    public MediaMetadataRetrieverCompat() {
        this(RETRIEVER_FFMPEG);
    }

    public MediaMetadataRetrieverCompat(int type) {
        if (type == RETRIEVER_FFMPEG) {
            try {
                //优先ffmpeg
                this.impl = new FFmpegMediaMetadataRetrieverImpl();
            } catch (Exception e) {
                //不行就自带的
                this.impl = new MediaMetadataRetrieverImpl();
                e.printStackTrace();
            }
        } else {
            this.impl = new MediaMetadataRetrieverImpl();
        }
    }

    public IMediaMetadataRetriever getMediaMetadataRetriever() {
        return impl;
    }

    public void setDataSource(String path) {
        this.mPath = path;
        this.impl.setDataSource(this.mPath);
        if (this.androidImpl != null) {
            this.androidImpl.setDataSource(this.mPath);
        }
    }

    public String extractMetadata(int keyCode) {
        String keyCodeString = MetadataTransform.transform(this.impl.getClass(), keyCode);
        if (TextUtils.isEmpty(keyCodeString)) {
            return null;
        }
        String metadata = this.impl.extractMetadata(keyCodeString);
        if (metadata == null) {
            //如果ffmpeg失败,自带api替代
            String androidKeyCodeString = MetadataTransform.transform(MediaMetadataRetrieverImpl.class, keyCode);
            metadata = getAndroidMediaMetadataRetriever().extractMetadata(androidKeyCodeString);
        }
        return metadata;
    }

    public Bitmap getFrameAtTime() {
        Bitmap frame = this.impl.getFrameAtTime();
        if (frame == null) {
            return getAndroidMediaMetadataRetriever().getFrameAtTime();
        } else {
            return frame;
        }
    }

    public Bitmap getFrameAtTime(long timeUs, int option) {
        Bitmap frame = this.impl.getFrameAtTime(timeUs, option);
        if (frame == null) {
            return getAndroidMediaMetadataRetriever().getFrameAtTime(timeUs, option);
        } else {
            return frame;
        }
    }

    public Bitmap getScaledFrameAtTime(long timeUs, int width, int height) {
        Bitmap frame = this.impl.getScaledFrameAtTime(timeUs, width, height);
        if (frame == null) {
            return getAndroidMediaMetadataRetriever().getScaledFrameAtTime(timeUs, width, height);
        } else {
            return frame;
        }
    }

    public Bitmap getScaledFrameAtTime(long timeUs, int option, int width, int height) {
        Bitmap frame = this.impl.getScaledFrameAtTime(timeUs, option, width, height);
        if (frame == null) {
            return getAndroidMediaMetadataRetriever().getScaledFrameAtTime(timeUs, option, width, height);
        } else {
            return frame;
        }
    }

    public Bitmap getScaledFrameAtTime(long timeUs, int option, int width, int height, float rotate) {
        boolean isRotate = isRotate(rotate);
        Bitmap frame = getScaledFrameAtTime(timeUs, option,
                isRotate ? height : width, isRotate ? width : height);
        if (isRotate) {
            return BitmapRotateTransform.transform(frame, rotate);
        }
        return frame;
    }

    public byte[] getEmbeddedPicture() {
        return impl.getEmbeddedPicture();
    }


    public void release() {
        impl.release();
    }


    private MediaMetadataRetrieverImpl getAndroidMediaMetadataRetriever() {
        //如果用的是自带的
        if (impl instanceof MediaMetadataRetrieverImpl) {
            return (MediaMetadataRetrieverImpl) impl;
        }
        if (androidImpl == null) {
            this.androidImpl = new MediaMetadataRetrieverImpl();
            if (!TextUtils.isEmpty(this.mPath)) {
                this.androidImpl.setDataSource(this.mPath);
            }
        }
        return this.androidImpl;
    }


    private boolean isRotate(float rotate) {
        float abs = Math.abs(rotate);
        return abs == 90 || abs == 270;
    }
}
