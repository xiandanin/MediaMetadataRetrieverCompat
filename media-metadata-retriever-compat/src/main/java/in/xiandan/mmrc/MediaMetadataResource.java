package in.xiandan.mmrc;

import android.media.MediaMetadataRetriever;
import android.os.Build;

import java.util.LinkedHashMap;
import java.util.Map;

import wseemann.media.FFmpegMediaMetadataRetriever;

/**
 * @author dengyuhan
 * created 2019-06-27 20:32
 */
public final class MediaMetadataResource {
    private static Map<String, KeyCompat> mKeyMap;
    private static MediaMetadataConfig mGlobalConfig;

    static void applyGlobalConfig(MediaMetadataConfig config) {
        mGlobalConfig = config;
    }

    /**
     * 获取全局配置
     *
     * @return
     */
    public static MediaMetadataConfig globalConfig() {
        if (mGlobalConfig == null) {
            mGlobalConfig = MediaMetadataConfig.newBuilder().build();
        }
        return mGlobalConfig;
    }

    public static KeyCompat getKey(String keyCode) {
        if (mKeyMap == null) {
            mKeyMap = new LinkedHashMap<>();
            mKeyMap.put(MediaMetadataKey.WIDTH, new KeyCompat(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH, FFmpegMediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH));
            mKeyMap.put(MediaMetadataKey.HEIGHT, new KeyCompat(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT, FFmpegMediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT));
            mKeyMap.put(MediaMetadataKey.DURATION, new KeyCompat(MediaMetadataRetriever.METADATA_KEY_DURATION, FFmpegMediaMetadataRetriever.METADATA_KEY_DURATION));
            mKeyMap.put(MediaMetadataKey.ROTATION, new KeyCompat(MediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION, FFmpegMediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION));
            mKeyMap.put(MediaMetadataKey.FRAMERATE, new KeyCompat(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ? MediaMetadataRetriever.METADATA_KEY_CAPTURE_FRAMERATE : null, FFmpegMediaMetadataRetriever.METADATA_KEY_FRAMERATE));
            mKeyMap.put(MediaMetadataKey.ALBUM, new KeyCompat(MediaMetadataRetriever.METADATA_KEY_ALBUM, FFmpegMediaMetadataRetriever.METADATA_KEY_ALBUM));
            mKeyMap.put(MediaMetadataKey.ARTIST, new KeyCompat(MediaMetadataRetriever.METADATA_KEY_ARTIST, FFmpegMediaMetadataRetriever.METADATA_KEY_ARTIST));
            mKeyMap.put(MediaMetadataKey.ALBUMARTIST, new KeyCompat(MediaMetadataRetriever.METADATA_KEY_ALBUMARTIST, FFmpegMediaMetadataRetriever.METADATA_KEY_ALBUM_ARTIST));
            mKeyMap.put(MediaMetadataKey.COMPOSER, new KeyCompat(MediaMetadataRetriever.METADATA_KEY_COMPOSER, FFmpegMediaMetadataRetriever.METADATA_KEY_COMPOSER));
            mKeyMap.put(MediaMetadataKey.DATE, new KeyCompat(MediaMetadataRetriever.METADATA_KEY_DATE, FFmpegMediaMetadataRetriever.METADATA_KEY_CREATION_TIME));
            mKeyMap.put(MediaMetadataKey.GENRE, new KeyCompat(MediaMetadataRetriever.METADATA_KEY_GENRE, FFmpegMediaMetadataRetriever.METADATA_KEY_GENRE));
            mKeyMap.put(MediaMetadataKey.TITLE, new KeyCompat(MediaMetadataRetriever.METADATA_KEY_TITLE, FFmpegMediaMetadataRetriever.METADATA_KEY_TITLE));
            mKeyMap.put(MediaMetadataKey.NUM_TRACKS, new KeyCompat(MediaMetadataRetriever.METADATA_KEY_NUM_TRACKS, FFmpegMediaMetadataRetriever.METADATA_KEY_TRACK));
            mKeyMap.put(MediaMetadataKey.DISC_NUMBER, new KeyCompat(MediaMetadataRetriever.METADATA_KEY_DISC_NUMBER, FFmpegMediaMetadataRetriever.METADATA_KEY_DISC));
            //android
            mKeyMap.put(MediaMetadataKey.CD_TRACK_NUMBER, new KeyCompat(MediaMetadataRetriever.METADATA_KEY_CD_TRACK_NUMBER, null));
            mKeyMap.put(MediaMetadataKey.AUTHOR, new KeyCompat(MediaMetadataRetriever.METADATA_KEY_AUTHOR, null));
            mKeyMap.put(MediaMetadataKey.YEAR, new KeyCompat(MediaMetadataRetriever.METADATA_KEY_YEAR, null));
            mKeyMap.put(MediaMetadataKey.WRITER, new KeyCompat(MediaMetadataRetriever.METADATA_KEY_WRITER, null));
            mKeyMap.put(MediaMetadataKey.MIMETYPE, new KeyCompat(MediaMetadataRetriever.METADATA_KEY_MIMETYPE, null));
            mKeyMap.put(MediaMetadataKey.COMPILATION, new KeyCompat(MediaMetadataRetriever.METADATA_KEY_COMPILATION, null));
            mKeyMap.put(MediaMetadataKey.HAS_AUDIO, new KeyCompat(MediaMetadataRetriever.METADATA_KEY_HAS_AUDIO, null));
            mKeyMap.put(MediaMetadataKey.HAS_VIDEO, new KeyCompat(MediaMetadataRetriever.METADATA_KEY_HAS_VIDEO, null));
            mKeyMap.put(MediaMetadataKey.BITRATE, new KeyCompat(MediaMetadataRetriever.METADATA_KEY_BITRATE, null));
            mKeyMap.put(MediaMetadataKey.LOCATION, new KeyCompat(MediaMetadataRetriever.METADATA_KEY_LOCATION, null));
            mKeyMap.put(MediaMetadataKey.HAS_IMAGE, new KeyCompat(MediaMetadataRetriever.METADATA_KEY_HAS_IMAGE, null));
            mKeyMap.put(MediaMetadataKey.IMAGE_COUNT, new KeyCompat(MediaMetadataRetriever.METADATA_KEY_IMAGE_COUNT, null));
            mKeyMap.put(MediaMetadataKey.IMAGE_PRIMARY, new KeyCompat(MediaMetadataRetriever.METADATA_KEY_IMAGE_PRIMARY, null));
            mKeyMap.put(MediaMetadataKey.FRAME_COUNT, new KeyCompat(MediaMetadataRetriever.METADATA_KEY_VIDEO_FRAME_COUNT, null));
            //ffmpeg
            mKeyMap.put(MediaMetadataKey.COMMENT, new KeyCompat(FFmpegMediaMetadataRetriever.METADATA_KEY_COMMENT));
            mKeyMap.put(MediaMetadataKey.COPYRIGHT, new KeyCompat(FFmpegMediaMetadataRetriever.METADATA_KEY_COPYRIGHT));
            mKeyMap.put(MediaMetadataKey.ENCODER, new KeyCompat(FFmpegMediaMetadataRetriever.METADATA_KEY_ENCODER));
            mKeyMap.put(MediaMetadataKey.ENCODED_BY, new KeyCompat(FFmpegMediaMetadataRetriever.METADATA_KEY_ENCODED_BY));
            mKeyMap.put(MediaMetadataKey.FILENAME, new KeyCompat(FFmpegMediaMetadataRetriever.METADATA_KEY_FILENAME));
            mKeyMap.put(MediaMetadataKey.LANGUAGE, new KeyCompat(FFmpegMediaMetadataRetriever.METADATA_KEY_LANGUAGE));
            mKeyMap.put(MediaMetadataKey.PERFORMER, new KeyCompat(FFmpegMediaMetadataRetriever.METADATA_KEY_PERFORMER));
            mKeyMap.put(MediaMetadataKey.PUBLISHER, new KeyCompat(FFmpegMediaMetadataRetriever.METADATA_KEY_PUBLISHER));
            mKeyMap.put(MediaMetadataKey.SERVICE_NAME, new KeyCompat(FFmpegMediaMetadataRetriever.METADATA_KEY_SERVICE_NAME));
            mKeyMap.put(MediaMetadataKey.SERVICE_PROVIDER, new KeyCompat(FFmpegMediaMetadataRetriever.METADATA_KEY_SERVICE_PROVIDER));
            mKeyMap.put(MediaMetadataKey.AUDIO_CODEC, new KeyCompat(FFmpegMediaMetadataRetriever.METADATA_KEY_AUDIO_CODEC));
            mKeyMap.put(MediaMetadataKey.VIDEO_CODEC, new KeyCompat(FFmpegMediaMetadataRetriever.METADATA_KEY_VIDEO_CODEC));
            mKeyMap.put(MediaMetadataKey.ICY_METADATA, new KeyCompat(FFmpegMediaMetadataRetriever.METADATA_KEY_ICY_METADATA));
            mKeyMap.put(MediaMetadataKey.CHAPTER_START_TIME, new KeyCompat(FFmpegMediaMetadataRetriever.METADATA_KEY_CHAPTER_START_TIME));
            mKeyMap.put(MediaMetadataKey.CHAPTER_END_TIME, new KeyCompat(FFmpegMediaMetadataRetriever.METADATA_KEY_CHAPTER_END_TIME));
            mKeyMap.put(MediaMetadataKey.CHAPTER_COUNT, new KeyCompat(FFmpegMediaMetadataRetriever.METADATA_CHAPTER_COUNT));
            mKeyMap.put(MediaMetadataKey.FILESIZE, new KeyCompat(FFmpegMediaMetadataRetriever.METADATA_KEY_FILESIZE));
        }
        return mKeyMap.get(keyCode);
    }

    public static final class KeyCompat {

        public Integer android;
        public String ffmpeg;

        public KeyCompat(Integer android, String ffmpeg) {
            this.android = android;
            this.ffmpeg = ffmpeg;
        }

        public KeyCompat(String ffmpeg) {
            this.ffmpeg = ffmpeg;
        }


    }

}
