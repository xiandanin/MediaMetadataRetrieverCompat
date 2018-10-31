package com.dyhdyh.compat.mmrc;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;

import com.dyhdyh.compat.mmrc.impl.FFmpegMediaMetadataRetrieverImpl;
import com.dyhdyh.compat.mmrc.impl.ImageMediaMetadataRetrieverImpl;
import com.dyhdyh.compat.mmrc.impl.MediaMetadataRetrieverImpl;
import com.dyhdyh.compat.mmrc.transform.BitmapRotateTransform;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * author  dengyuhan
 * created 2017/5/26 14:48
 */
public class MediaMetadataRetrieverCompat {
    private final String TAG = "MediaMetadataRetriever";

    private IMediaMetadataRetriever mImpl;
    private IMediaMetadataRetriever mOriginalImpl;
    private IMediaMetadataRetriever mImageImpl;

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

    /**
     * @deprecated {@link #METADATA_KEY_WIDTH}
     */
    @Deprecated
    public static final int METADATA_KEY_VIDEO_WIDTH = 18;
    /**
     * @deprecated {@link #METADATA_KEY_HEIGHT}
     */
    @Deprecated
    public static final int METADATA_KEY_VIDEO_HEIGHT = 19;
    /**
     * @deprecated {@link #METADATA_KEY_ROTATION}
     */
    @Deprecated
    public static final int METADATA_KEY_VIDEO_ROTATION = 24;

    public static final int METADATA_KEY_WIDTH = 18;
    public static final int METADATA_KEY_HEIGHT = 19;
    public static final int METADATA_KEY_ROTATION = 24;
    public static final int METADATA_KEY_CAPTURE_FRAMERATE = 25;

    public static final int RETRIEVER_AUTOMATIC = 0;
    public static final int RETRIEVER_ANDROID = 1;
    public static final int RETRIEVER_FFMPEG = 2;

    public static MediaMetadataRetrieverCompat create() {
        return new MediaMetadataRetrieverCompat(RETRIEVER_AUTOMATIC);
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
        if (type == RETRIEVER_AUTOMATIC || type == RETRIEVER_FFMPEG) {
            try {
                //创建实例前先检查FFmpegMediaMetadataRetriever是否可用
                Class.forName("wseemann.media.FFmpegMediaMetadataRetriever");
                this.mOriginalImpl = new FFmpegMediaMetadataRetrieverImpl(type == RETRIEVER_AUTOMATIC);
            } catch (Exception e) {
                //如果不可用 就创建原生实例
                this.mOriginalImpl = new MediaMetadataRetrieverImpl();
                Log.e(TAG, "FFmpegMediaMetadataRetrieverImpl初始化失败，已切换至原生API");
                if (!(e instanceof ClassNotFoundException)) {
                    e.printStackTrace();
                }
            }
        } else {
            this.mOriginalImpl = new MediaMetadataRetrieverImpl();
        }
        this.mImpl = mOriginalImpl;
        this.mImageImpl = new ImageMediaMetadataRetrieverImpl();
    }

    public IMediaMetadataRetriever getMediaMetadataRetriever() {
        return mImpl;
    }

    /**
     * @param path
     * @deprecated {@link #setDataSource(File)}
     */
    @Deprecated
    public void setMediaDataSource(String path) throws IOException {
        setMediaDataSource(new File(path));
    }

    /**
     * @param file
     * @deprecated {@link #setDataSource(File)}
     */
    @Deprecated
    public void setMediaDataSource(File file) throws IOException {
        setDataSource(file);
    }


    /**
     * @param path
     */
    public void setDataSource(String path) {
        try {
            setDataSource(new File(path));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void setDataSource(File inputFile) throws FileNotFoundException {
        if (inputFile == null || !inputFile.exists()) {
            throw new FileNotFoundException("文件不存在 " + (inputFile != null ? inputFile.getAbsolutePath() : ""));
        }
        runDynamicSetDataSource(new Runnable() {
            @Override
            public void run() {
                try {
                    mImpl.setDataSource(inputFile);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    public void setDataSource(Context context, Uri uri) {
        runDynamicSetDataSource(new Runnable() {
            @Override
            public void run() {
                mImpl.setDataSource(context, uri);
            }
        });
    }

    /**
     * 网络媒体资源
     *
     * @param uri
     * @param headers
     */
    public void setDataSource(String uri, Map<String, String> headers) {
        Map<String, String> newHeaders = headers == null ? new HashMap<>() : headers;
        runDynamicSetDataSource(new Runnable() {
            @Override
            public void run() {
                mImpl.setDataSource(uri, newHeaders);
            }
        });
    }

    public void setDataSource(FileDescriptor fd, long offset, long length) {
        runDynamicSetDataSource(new Runnable() {
            @Override
            public void run() {
                mImpl.setDataSource(fd, offset, length);
            }
        });
    }

    public void setDataSource(FileDescriptor fd) {
        runDynamicSetDataSource(new Runnable() {
            @Override
            public void run() {
                mImpl.setDataSource(fd);
            }
        });
    }


    public String extractMetadata(int keyCode) {
        String metadata = mImpl.extractMetadata(keyCode);
        Log.d(TAG, "extractMetadata : " + keyCode + " = " + metadata);
        return metadata;
    }

    public long extractMetadataLong(int keyCode) {
        return extractMetadataLong(keyCode, (long) VALUE_EMPTY);
    }

    public Long extractMetadataLong(int keyCode, Long defaultValue) {
        try {
            return Long.parseLong(extractMetadata(keyCode));
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public int extractMetadataInt(int keyCode) {
        return extractMetadataInteger(keyCode, VALUE_EMPTY);
    }

    public Integer extractMetadataInteger(int keyCode, Integer defaultValue) {
        try {
            return Integer.parseInt(extractMetadata(keyCode));
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public float extractMetadataFloat(int keyCode) {
        return extractMetadataFloat(keyCode, (float) VALUE_EMPTY);
    }

    public Float extractMetadataFloat(int keyCode, Float defaultValue) {
        try {
            return Float.parseFloat(extractMetadata(keyCode));
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * 获取第一帧bitmap
     *
     * @return
     */
    public Bitmap getFrameAtTime() {
        return this.mImpl.getFrameAtTime();
    }

    /**
     * 获取指定时间的bitmap
     *
     * @param timeUs 微秒
     * @param option {@link #OPTION_PREVIOUS_SYNC} 早于timeUs的同步帧;
     *               {@link #OPTION_NEXT_SYNC} 晚于timeUs的同步帧;
     *               {@link #OPTION_CLOSEST_SYNC} 最接近timeUs的同步帧;
     *               {@link #OPTION_CLOSEST} 最接近timeUs的帧，但可能不是同步帧(性能开销较大).
     * @return
     */
    public Bitmap getFrameAtTime(long timeUs, int option) {
        return this.mImpl.getFrameAtTime(timeUs, option);
    }

    /**
     * 获取指定时间指定宽高的bitmap
     *
     * @param timeUs
     * @param width
     * @param height
     * @return
     */
    public Bitmap getScaledFrameAtTime(long timeUs, int width, int height) {
        return this.mImpl.getScaledFrameAtTime(timeUs, width, height);
    }

    /**
     * 指定获取方式获取指定时间指定宽高的bitmap
     *
     * @param timeUs
     * @param width
     * @param height
     * @return
     */
    public Bitmap getScaledFrameAtTime(long timeUs, int option, int width, int height) {
        return this.mImpl.getScaledFrameAtTime(timeUs, option, width, height);
    }

    /**
     * 指定获取方式获取指定时间指定宽高的bitmap并根据角度旋转
     *
     * @param timeUs
     * @param option
     * @param width
     * @param height
     * @param rotate
     * @return
     * @deprecated 在其它方法已开启自动旋转
     */
    @Deprecated
    public Bitmap getScaledFrameAtTime(long timeUs, int option, int width, int height, float rotate) {
        Bitmap frame = getScaledFrameAtTime(timeUs, option, width, height);
        if (frame != null && isRotate(rotate)) {
            return BitmapRotateTransform.transform(frame, rotate);
        }
        return frame;
    }

    @Deprecated
    public Bitmap getScaledFrameAtTime(long timeUs, int option, float scale, float rotate) {
        int widthInt = extractMetadataInt(METADATA_KEY_WIDTH);
        int heightInt = extractMetadataInt(METADATA_KEY_HEIGHT);
        int width = widthInt <= 0 ? 0 : (int) (widthInt * scale);
        int height = heightInt <= 0 ? 0 : (int) (heightInt * scale);
        return getScaledFrameAtTime(timeUs, option, width, height, rotate);
    }

    public byte[] getEmbeddedPicture() {
        return mImpl.getEmbeddedPicture();
    }


    public void release() {
        mImpl.release();
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


    /**
     * 根据输入源动态切换引擎
     *
     * @param runnable
     */
    private void runDynamicSetDataSource(Runnable runnable) {
        try {
            //设置输入源前先还原实例
            this.mImpl = this.mOriginalImpl;
            runnable.run();
        } catch (RuntimeException e) {
            final Pattern pattern = Pattern.compile("(?=.*status)(?=.*0x80000000)^.*$");
            if (pattern.matcher(e.getMessage()).matches()) {
                //如果发现输入源的是图片 就切换成图片实例
                this.mImpl = this.mImageImpl;
                runnable.run();
            } else {
                e.printStackTrace();
            }
        }
    }
}
