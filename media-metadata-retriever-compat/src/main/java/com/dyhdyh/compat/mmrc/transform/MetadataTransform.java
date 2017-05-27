package com.dyhdyh.compat.mmrc.transform;

import android.media.MediaMetadataRetriever;
import android.util.SparseArray;

import com.dyhdyh.compat.mmrc.IMediaMetadataRetriever;
import com.dyhdyh.compat.mmrc.MediaMetadataRetrieverCompat;
import com.dyhdyh.compat.mmrc.impl.FFmpegMediaMetadataRetrieverImpl;
import com.dyhdyh.compat.mmrc.impl.MediaMetadataRetrieverImpl;

import wseemann.media.FFmpegMediaMetadataRetriever;

/**
 * author  dengyuhan
 * created 2017/5/27 10:39
 */
public class MetadataTransform {
    private static SparseArray<MetadataKey> METADATA_KEYS = new SparseArray<>();

    static {
        METADATA_KEYS.put(MediaMetadataRetrieverCompat.METADATA_KEY_ALBUM,
                new MetadataKey(FFmpegMediaMetadataRetriever.METADATA_KEY_ALBUM,
                        String.valueOf(MediaMetadataRetriever.METADATA_KEY_ALBUM)));

        METADATA_KEYS.put(MediaMetadataRetrieverCompat.METADATA_KEY_ARTIST,
                new MetadataKey(FFmpegMediaMetadataRetriever.METADATA_KEY_ARTIST,
                        String.valueOf(MediaMetadataRetriever.METADATA_KEY_ARTIST)));

        METADATA_KEYS.put(MediaMetadataRetrieverCompat.METADATA_KEY_AUTHOR,
                new MetadataKey(FFmpegMediaMetadataRetriever.METADATA_KEY_ALBUM_ARTIST,
                        String.valueOf(MediaMetadataRetriever.METADATA_KEY_AUTHOR)));

        METADATA_KEYS.put(MediaMetadataRetrieverCompat.METADATA_KEY_COMPOSER,
                new MetadataKey(FFmpegMediaMetadataRetriever.METADATA_KEY_COMPOSER,
                        String.valueOf(MediaMetadataRetriever.METADATA_KEY_COMPOSER)));

        METADATA_KEYS.put(MediaMetadataRetrieverCompat.METADATA_KEY_DATE,
                new MetadataKey(FFmpegMediaMetadataRetriever.METADATA_KEY_DATE,
                        String.valueOf(MediaMetadataRetriever.METADATA_KEY_DATE)));

        METADATA_KEYS.put(MediaMetadataRetrieverCompat.METADATA_KEY_TITLE,
                new MetadataKey(FFmpegMediaMetadataRetriever.METADATA_KEY_TITLE,
                        String.valueOf(MediaMetadataRetriever.METADATA_KEY_TITLE)));

        METADATA_KEYS.put(MediaMetadataRetrieverCompat.METADATA_KEY_DURATION,
                new MetadataKey(FFmpegMediaMetadataRetriever.METADATA_KEY_DURATION,
                        String.valueOf(MediaMetadataRetriever.METADATA_KEY_DURATION)));

        METADATA_KEYS.put(MediaMetadataRetrieverCompat.METADATA_KEY_NUM_TRACKS,
                new MetadataKey(FFmpegMediaMetadataRetriever.METADATA_KEY_TRACK,
                        String.valueOf(MediaMetadataRetriever.METADATA_KEY_NUM_TRACKS)));

        METADATA_KEYS.put(MediaMetadataRetrieverCompat.METADATA_KEY_ALBUMARTIST,
                new MetadataKey(FFmpegMediaMetadataRetriever.METADATA_KEY_ALBUM_ARTIST,
                        String.valueOf(MediaMetadataRetriever.METADATA_KEY_ALBUMARTIST)));

        METADATA_KEYS.put(MediaMetadataRetrieverCompat.METADATA_KEY_DISC_NUMBER,
                new MetadataKey(FFmpegMediaMetadataRetriever.METADATA_KEY_DISC,
                        String.valueOf(MediaMetadataRetriever.METADATA_KEY_DISC_NUMBER)));

        METADATA_KEYS.put(MediaMetadataRetrieverCompat.METADATA_KEY_VIDEO_WIDTH,
                new MetadataKey(FFmpegMediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH,
                        String.valueOf(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH)));

        METADATA_KEYS.put(MediaMetadataRetrieverCompat.METADATA_KEY_VIDEO_HEIGHT,
                new MetadataKey(FFmpegMediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT,
                        String.valueOf(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT)));

        METADATA_KEYS.put(MediaMetadataRetrieverCompat.METADATA_KEY_VIDEO_ROTATION,
                new MetadataKey(FFmpegMediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION,
                        String.valueOf(MediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION)));

        METADATA_KEYS.put(MediaMetadataRetrieverCompat.METADATA_KEY_CAPTURE_FRAMERATE,
                new MetadataKey(FFmpegMediaMetadataRetriever.METADATA_KEY_FRAMERATE,
                        String.valueOf(MediaMetadataRetriever.METADATA_KEY_CAPTURE_FRAMERATE)));

    }

    public static String transform(Class<? extends IMediaMetadataRetriever> clazz, int metadataKeyCode) {
        MetadataKey metadataKey = METADATA_KEYS.get(metadataKeyCode);
        if (clazz.getName().equals(FFmpegMediaMetadataRetrieverImpl.class.getName())) {
            return metadataKey.getFfmpegMetadatakey();
        } else if (clazz.getName().equals(MediaMetadataRetrieverImpl.class.getName())) {
            return metadataKey.getMetadatakey();
        }
        return null;
    }

}
