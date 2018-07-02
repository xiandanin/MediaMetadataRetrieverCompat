package com.dyhdyh.compat.mmrc;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.dyhdyh.compat.mmrc.impl.FFmpegMediaMetadataRetrieverImpl;
import com.dyhdyh.compat.mmrc.impl.MediaMetadataRetrieverImpl;
import com.dyhdyh.compat.mmrc.transform.BitmapRotateTransform;
import com.dyhdyh.compat.mmrc.transform.MetadataTransform;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

/**
 * author  dengyuhan
 * created 2017/5/26 14:48
 */
public class MediaMetadataRetrieverCompat {
    private final String TAG = "MediaMetadataRetriever";

    private IMediaMetadataRetriever mImpl;
    private MediaMetadataRetrieverImpl mAndroidImpl;

    public static final int VALUE_EMPTY = -1;

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

    public static MediaMetadataRetrieverCompat create() {
        return new MediaMetadataRetrieverCompat(RETRIEVER_FFMPEG);
    }

    public static MediaMetadataRetrieverCompat create(int type) {
        return new MediaMetadataRetrieverCompat(type);
    }

    /**
     * @deprecated {@link MediaMetadataRetrieverCompat#create()}
     */
    @Deprecated
    public MediaMetadataRetrieverCompat() {
        this(RETRIEVER_FFMPEG);
    }

    /**
     * @deprecated {@link MediaMetadataRetrieverCompat#create(int)}
     */
    @Deprecated
    public MediaMetadataRetrieverCompat(int type) {
        if (type == RETRIEVER_FFMPEG) {
            try {
                //创建实例前先检查是否引入FFmpegMediaMetadataRetriever
                Class.forName("wseemann.media.FFmpegMediaMetadataRetriever");
                //优先ffmpeg
                this.mImpl = new FFmpegMediaMetadataRetrieverImpl();
            } catch (Exception e) {
                //不行就自带的
                this.mImpl = new MediaMetadataRetrieverImpl();
                Log.d(TAG, "FFmpegMediaMetadataRetrieverImpl初始化失败，使用原生API");
                e.printStackTrace();
            }
            this.mAndroidImpl = new MediaMetadataRetrieverImpl();
        } else {
            this.mImpl = new MediaMetadataRetrieverImpl();
        }
    }

    public IMediaMetadataRetriever getMediaMetadataRetriever() {
        return mImpl;
    }

    /**
     * @param path
     * @deprecated {@link #setDataSource(File)}
     */
    @Deprecated
    public void setDataSource(String path) {
        try {
            setMediaDataSource(path);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param path
     * @deprecated {@link #setDataSource(File)}
     */
    @Deprecated
    public void setMediaDataSource(String path) throws FileNotFoundException {
        setMediaDataSource(new File(path));
    }

    /**
     * @param file
     * @deprecated {@link #setDataSource(File)}
     */
    @Deprecated
    public void setMediaDataSource(File file) throws FileNotFoundException {
        setDataSource(file);
    }

    public void setDataSource(File inputFile) throws FileNotFoundException {
        if (inputFile == null || !inputFile.exists()) {
            throw new FileNotFoundException("文件不存在 " + (inputFile != null ? inputFile.getAbsolutePath() : ""));
        }
        String inputPath = inputFile.getAbsolutePath();
        mImpl.setDataSource(inputPath);
        if (mAndroidImpl != null) {
            mAndroidImpl.setDataSource(inputPath);
        }
    }

    public void setDataSource(Context context, Uri uri) {
        mImpl.setDataSource(context, uri);
        if (mAndroidImpl != null) {
            mAndroidImpl.setDataSource(context, uri);
        }
    }

    /**
     * 网络媒体资源
     *
     * @param uri
     * @param headers
     */
    public void setDataSource(String uri, Map<String, String> headers) {
        if (headers == null) {
            headers = new HashMap<>();
        }
        mImpl.setDataSource(uri, headers);
        if (mAndroidImpl != null) {
            mAndroidImpl.setDataSource(uri, headers);
        }
    }

    public void setDataSource(FileDescriptor fd, long offset, long length) {
        mImpl.setDataSource(fd, offset, length);
        if (mAndroidImpl != null) {
            mAndroidImpl.setDataSource(fd, offset, length);
        }
    }

    public void setDataSource(FileDescriptor fd) {
        mImpl.setDataSource(fd);
        if (mAndroidImpl != null) {
            mAndroidImpl.setDataSource(fd);
        }
    }


    public String extractMetadata(int keyCode) {
        String keyCodeString = MetadataTransform.transform(this.mImpl.getClass(), keyCode);
        if (TextUtils.isEmpty(keyCodeString)) {
            return null;
        }
        String metadata = mImpl.extractMetadata(keyCodeString);
        if (metadata == null && mAndroidImpl != null) {
            //如果ffmpeg失败,自带api替代
            String androidKeyCodeString = MetadataTransform.transform(mAndroidImpl.getClass(), keyCode);
            metadata = mAndroidImpl.extractMetadata(androidKeyCodeString);
        }
        Log.d(TAG, "extractMetadata : " + keyCode + " = " + metadata);
        return metadata;
    }

    public long extractMetadataLong(int keyCode) {
        try {
            return Long.parseLong(extractMetadata(keyCode));
        } catch (Exception e) {
            return VALUE_EMPTY;
        }
    }

    public int extractMetadataInt(int keyCode) {
        try {
            return Integer.parseInt(extractMetadata(keyCode));
        } catch (Exception e) {
            return VALUE_EMPTY;
        }
    }


    public float extractMetadataFloat(int keyCode) {
        try {
            return Float.parseFloat(extractMetadata(keyCode));
        } catch (Exception e) {
            return VALUE_EMPTY;
        }
    }

    public Bitmap getFrameAtTime() {
        Bitmap frame = this.mImpl.getFrameAtTime();
        if (frame == null && mAndroidImpl != null) {
            return mAndroidImpl.getFrameAtTime();
        }
        return frame;
    }

    public Bitmap getFrameAtTime(long timeUs, int option) {
        Bitmap frame = this.mImpl.getFrameAtTime(timeUs, option);
        if (frame == null && mAndroidImpl != null) {
            return mAndroidImpl.getFrameAtTime(timeUs, option);
        }
        return frame;
    }

    public Bitmap getScaledFrameAtTime(long timeUs, int width, int height) {
        Bitmap frame = this.mImpl.getScaledFrameAtTime(timeUs, width, height);
        if (frame == null && mAndroidImpl != null) {
            return mAndroidImpl.getScaledFrameAtTime(timeUs, width, height);
        }
        return frame;
    }

    public Bitmap getScaledFrameAtTime(long timeUs, int option, int width, int height) {
        Bitmap frame = this.mImpl.getScaledFrameAtTime(timeUs, option, width, height);
        if (frame == null && mAndroidImpl != null) {
            return mAndroidImpl.getScaledFrameAtTime(timeUs, option, width, height);
        }
        return frame;
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
        int widthInt = extractMetadataInt(METADATA_KEY_VIDEO_WIDTH);
        int heightInt = extractMetadataInt(METADATA_KEY_VIDEO_HEIGHT);
        int width = widthInt <= 0 ? 0 : (int) (widthInt * widthScale);
        int height = heightInt <= 0 ? 0 : (int) (heightInt * heightScale);
        return getScaledFrameAtTime(timeUs, option, width, height, rotate);
    }

    public byte[] getEmbeddedPicture() {
        byte[] embeddedPicture = mImpl.getEmbeddedPicture();
        if (embeddedPicture == null && mAndroidImpl != null) {
            return mAndroidImpl.getEmbeddedPicture();
        }
        return embeddedPicture;
    }


    public void release() {
        mImpl.release();
        if (mAndroidImpl != null) {
            mAndroidImpl.release();
        }
    }


    private boolean isVertical(float rotate) {
        float abs = Math.abs(rotate);
        return abs == 90 || abs == 270;
    }


    /**
     * 是否有角度
     *
     * @param rotate
     * @return
     */
    public static boolean isRotate(float rotate) {
        float abs = Math.abs(rotate);
        return abs % 360 != 0;
    }
}
