package com.dyhdyh.compat.mmrc;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.MediaMetadataRetriever;
import android.text.TextUtils;

import com.dyhdyh.compat.mmrc.impl.FFmpegMediaMetadataRetrieverImpl;
import com.dyhdyh.compat.mmrc.impl.MediaMetadataRetrieverImpl;

import wseemann.media.FFmpegMediaMetadataRetriever;

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
        this.impl.setDataSource(path);
    }

    public String extractMetadata(int keyCode) {
        String keyCodeString = null;
        switch (keyCode) {
            case METADATA_KEY_ALBUM:
                keyCodeString = impl instanceof FFmpegMediaMetadataRetrieverImpl ?
                        FFmpegMediaMetadataRetriever.METADATA_KEY_ALBUM :
                        String.valueOf(MediaMetadataRetriever.METADATA_KEY_ALBUM);
                break;
            case METADATA_KEY_ARTIST:
                keyCodeString = impl instanceof FFmpegMediaMetadataRetrieverImpl ?
                        FFmpegMediaMetadataRetriever.METADATA_KEY_ARTIST :
                        String.valueOf(MediaMetadataRetriever.METADATA_KEY_ARTIST);
                break;
            case METADATA_KEY_AUTHOR:
                keyCodeString = impl instanceof FFmpegMediaMetadataRetrieverImpl ?
                        FFmpegMediaMetadataRetriever.METADATA_KEY_ALBUM_ARTIST :
                        String.valueOf(MediaMetadataRetriever.METADATA_KEY_AUTHOR);
                break;
            case METADATA_KEY_COMPOSER:
                keyCodeString = impl instanceof FFmpegMediaMetadataRetrieverImpl ?
                        FFmpegMediaMetadataRetriever.METADATA_KEY_COMPOSER :
                        String.valueOf(MediaMetadataRetriever.METADATA_KEY_COMPOSER);
                break;
            case METADATA_KEY_DATE:
                keyCodeString = impl instanceof FFmpegMediaMetadataRetrieverImpl ?
                        FFmpegMediaMetadataRetriever.METADATA_KEY_DATE :
                        String.valueOf(MediaMetadataRetriever.METADATA_KEY_DATE);
                break;
            case METADATA_KEY_TITLE:
                keyCodeString = impl instanceof FFmpegMediaMetadataRetrieverImpl ?
                        FFmpegMediaMetadataRetriever.METADATA_KEY_TITLE :
                        String.valueOf(MediaMetadataRetriever.METADATA_KEY_TITLE);
                break;
            case METADATA_KEY_DURATION:
                keyCodeString = impl instanceof FFmpegMediaMetadataRetrieverImpl ?
                        FFmpegMediaMetadataRetriever.METADATA_KEY_DURATION :
                        String.valueOf(MediaMetadataRetriever.METADATA_KEY_DURATION);
                break;
            case METADATA_KEY_NUM_TRACKS:
                keyCodeString = impl instanceof FFmpegMediaMetadataRetrieverImpl ?
                        FFmpegMediaMetadataRetriever.METADATA_KEY_TRACK :
                        String.valueOf(MediaMetadataRetriever.METADATA_KEY_NUM_TRACKS);
                break;
            case METADATA_KEY_ALBUMARTIST:
                keyCodeString = impl instanceof FFmpegMediaMetadataRetrieverImpl ?
                        FFmpegMediaMetadataRetriever.METADATA_KEY_ALBUM_ARTIST :
                        String.valueOf(MediaMetadataRetriever.METADATA_KEY_ALBUMARTIST);
                break;
            case METADATA_KEY_DISC_NUMBER:
                keyCodeString = impl instanceof FFmpegMediaMetadataRetrieverImpl ?
                        FFmpegMediaMetadataRetriever.METADATA_KEY_DISC :
                        String.valueOf(MediaMetadataRetriever.METADATA_KEY_DISC_NUMBER);
                break;
            case METADATA_KEY_VIDEO_WIDTH:
                keyCodeString = impl instanceof FFmpegMediaMetadataRetrieverImpl ?
                        FFmpegMediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH :
                        String.valueOf(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH);
                break;
            case METADATA_KEY_VIDEO_HEIGHT:
                keyCodeString = impl instanceof FFmpegMediaMetadataRetrieverImpl ?
                        FFmpegMediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT :
                        String.valueOf(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT);
                break;
            case METADATA_KEY_VIDEO_ROTATION:
                keyCodeString = impl instanceof FFmpegMediaMetadataRetrieverImpl ?
                        FFmpegMediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION :
                        String.valueOf(MediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION);
                break;
            case METADATA_KEY_CAPTURE_FRAMERATE:
                keyCodeString = impl instanceof FFmpegMediaMetadataRetrieverImpl ?
                        FFmpegMediaMetadataRetriever.METADATA_KEY_FRAMERATE :
                        String.valueOf(MediaMetadataRetriever.METADATA_KEY_CAPTURE_FRAMERATE);
                break;
        }
        if (TextUtils.isEmpty(keyCodeString)) {
            return null;
        }
        return this.impl.extractMetadata(keyCodeString);
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
        Bitmap frame = this.impl.getFrameAtTime(timeUs, getOption(this.impl, option));
        if (frame == null) {
            return getAndroidMediaMetadataRetriever().getFrameAtTime(timeUs, getOption(this.androidImpl, option));
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
        Bitmap frame = this.impl.getScaledFrameAtTime(timeUs, getOption(this.impl, option), width, height);
        if (frame == null) {
            return getAndroidMediaMetadataRetriever().getScaledFrameAtTime(timeUs, getOption(this.androidImpl, option), width, height);
        } else {
            return frame;
        }
    }

    public Bitmap getScaledFrameAtTime(long timeUs, int option, int width, int height, float rotate) {
        boolean isRotate = isRotate(rotate);
        Bitmap frame = getScaledFrameAtTime(timeUs, getOption(this.impl, option),
                isRotate ? height : width, isRotate ? width : height);
        if (isRotate) {
            Matrix matrix = new Matrix();
            matrix.postRotate(rotate);
            int frameWidth = frame.getWidth();
            int frameHeight = frame.getHeight();
            frame = Bitmap.createBitmap(frame, 0, 0, frameWidth, frameHeight, matrix, true);
        }
        return frame;
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

    public byte[] getEmbeddedPicture() {
        return impl.getEmbeddedPicture();
    }

    private int getOption(IMediaMetadataRetriever retriever, int option) {
        int tmpOption = option;
        switch (option) {
            case OPTION_PREVIOUS_SYNC:
                tmpOption = retriever instanceof FFmpegMediaMetadataRetrieverImpl ?
                        FFmpegMediaMetadataRetriever.OPTION_PREVIOUS_SYNC :
                        MediaMetadataRetriever.OPTION_PREVIOUS_SYNC;
                break;
            case OPTION_NEXT_SYNC:
                tmpOption = retriever instanceof FFmpegMediaMetadataRetrieverImpl ?
                        FFmpegMediaMetadataRetriever.OPTION_NEXT_SYNC :
                        MediaMetadataRetriever.OPTION_NEXT_SYNC;
                break;
            case OPTION_CLOSEST_SYNC:
                tmpOption = retriever instanceof FFmpegMediaMetadataRetrieverImpl ?
                        FFmpegMediaMetadataRetriever.OPTION_CLOSEST_SYNC :
                        MediaMetadataRetriever.OPTION_CLOSEST_SYNC;
                break;
            case OPTION_CLOSEST:
                tmpOption = retriever instanceof FFmpegMediaMetadataRetrieverImpl ?
                        FFmpegMediaMetadataRetriever.OPTION_CLOSEST :
                        MediaMetadataRetriever.OPTION_CLOSEST;
                break;
        }
        return tmpOption;
    }

    private boolean isRotate(float rotate) {
        float abs = Math.abs(rotate);
        return abs == 90 || abs == 270;
    }
}
