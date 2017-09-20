package com.dyhdyh.compat.mmrc;

import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.Log;

import com.dyhdyh.compat.mmrc.impl.FFmpegMediaMetadataRetrieverImpl;
import com.dyhdyh.compat.mmrc.impl.MediaMetadataRetrieverImpl;
import com.dyhdyh.compat.mmrc.transform.BitmapRotateTransform;
import com.dyhdyh.compat.mmrc.transform.MetadataTransform;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * author  dengyuhan
 * created 2017/5/26 14:48
 */
public class MediaMetadataRetrieverCompat {
    private final String TAG = "MediaMetadataRetriever";

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
                //创建实例前先检查是否引入FFmpegMediaMetadataRetriever
                Class.forName("wseemann.media.FFmpegMediaMetadataRetriever");
                //优先ffmpeg
                this.impl = new FFmpegMediaMetadataRetrieverImpl();
            } catch (Exception e) {
                //不行就自带的
                this.impl = new MediaMetadataRetrieverImpl();
                Log.d(TAG, "FFmpegMediaMetadataRetrieverImpl初始化失败，使用原生API");
                e.printStackTrace();
            }
        } else {
            this.impl = new MediaMetadataRetrieverImpl();
        }
    }

    public IMediaMetadataRetriever getMediaMetadataRetriever() {
        return impl;
    }

    /**
     * @param path
     * @deprecated Use {@link #setMediaDataSource(String)} instead.
     */
    @Deprecated
    public void setDataSource(String path) {
        try {
            setMediaDataSource(path);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void setMediaDataSource(String path) throws FileNotFoundException {
        setMediaDataSource(new File(path));
    }

    public void setMediaDataSource(File file) throws FileNotFoundException {
        if (!file.exists()) {
            throw new FileNotFoundException();
        }
        this.mPath = file.getAbsolutePath();
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
        boolean isVertical = isVertical(rotate);
        Bitmap frame = getScaledFrameAtTime(timeUs, option,
                isVertical ? height : width, isVertical ? width : height);
        if (isRotate(rotate)) {
            return BitmapRotateTransform.transform(frame, rotate);
        }
        return frame;
    }

    public Bitmap getScaledFrameAtTime(long timeUs, int option, float widthScale, float heightScale, float rotate) {
        String widthText = extractMetadata(METADATA_KEY_VIDEO_WIDTH);
        String heightText = extractMetadata(METADATA_KEY_VIDEO_HEIGHT);
        int width = TextUtils.isEmpty(widthText) ? 0 : (int) (Integer.parseInt(widthText) * widthScale);
        int height = TextUtils.isEmpty(heightText) ? 0 : (int) (Integer.parseInt(heightText) * heightScale);
        return getScaledFrameAtTime(timeUs, option, width, height, rotate);
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


    private boolean isVertical(float rotate) {
        float abs = Math.abs(rotate);
        return abs == 90 || abs == 270;
    }


    /**
     * 是否有角度
     * @param rotate
     * @return
     */
    public static boolean isRotate(float rotate) {
        float abs = Math.abs(rotate);
        return abs % 360 != 0;
    }
}
