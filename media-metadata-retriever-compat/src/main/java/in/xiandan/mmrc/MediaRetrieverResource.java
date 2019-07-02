package in.xiandan.mmrc;

import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.Build;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import in.xiandan.mmrc.fileformat.FileFormat;
import in.xiandan.mmrc.fileformat.ImageFormatChecker;
import in.xiandan.mmrc.retriever.AndroidMediaMetadataRetrieverFactory;
import in.xiandan.mmrc.retriever.ffmpeg.FFmpegMediaMetadataRetrieverFactory;
import in.xiandan.mmrc.retriever.image.BitmapMediaMetadataRetrieverFactory;
import in.xiandan.mmrc.retriever.image.GIFMediaMetadataRetrieverFactory;
import in.xiandan.mmrc.retriever.image.JPEGMediaMetadataRetrieverFactory;
import wseemann.media.FFmpegMediaMetadataRetriever;

/**
 * @author dengyuhan
 * created 2019-06-27 20:32
 */
public final class MediaRetrieverResource {
    private static Map<String, KeyCompat> mKeyMap;

    public static KeyCompat getKey(String keyCode) {
        if (mKeyMap == null) {
            mKeyMap = new LinkedHashMap<>();
            mKeyMap.put(Key.METADATA_KEY_WIDTH, new KeyCompat(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH, FFmpegMediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH));
            mKeyMap.put(Key.METADATA_KEY_HEIGHT, new KeyCompat(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT, FFmpegMediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT));
            mKeyMap.put(Key.METADATA_KEY_DURATION, new KeyCompat(MediaMetadataRetriever.METADATA_KEY_DURATION, FFmpegMediaMetadataRetriever.METADATA_KEY_DURATION));
            mKeyMap.put(Key.METADATA_KEY_ROTATION, new KeyCompat(MediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION, FFmpegMediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION));
            mKeyMap.put(Key.METADATA_KEY_FRAMERATE, new KeyCompat(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ? MediaMetadataRetriever.METADATA_KEY_CAPTURE_FRAMERATE : KeyCompat.NULL_KEY, FFmpegMediaMetadataRetriever.METADATA_KEY_FRAMERATE));
            mKeyMap.put(Key.METADATA_KEY_ALBUM, new KeyCompat(MediaMetadataRetriever.METADATA_KEY_ALBUM, FFmpegMediaMetadataRetriever.METADATA_KEY_ALBUM));
            mKeyMap.put(Key.METADATA_KEY_ARTIST, new KeyCompat(MediaMetadataRetriever.METADATA_KEY_ARTIST, FFmpegMediaMetadataRetriever.METADATA_KEY_ARTIST));
            mKeyMap.put(Key.METADATA_KEY_ALBUMARTIST, new KeyCompat(MediaMetadataRetriever.METADATA_KEY_ALBUMARTIST, FFmpegMediaMetadataRetriever.METADATA_KEY_ALBUM_ARTIST));
            mKeyMap.put(Key.METADATA_KEY_COMPOSER, new KeyCompat(MediaMetadataRetriever.METADATA_KEY_COMPOSER, FFmpegMediaMetadataRetriever.METADATA_KEY_COMPOSER));
            mKeyMap.put(Key.METADATA_KEY_DATE, new KeyCompat(MediaMetadataRetriever.METADATA_KEY_DATE, FFmpegMediaMetadataRetriever.METADATA_KEY_CREATION_TIME));
            mKeyMap.put(Key.METADATA_KEY_GENRE, new KeyCompat(MediaMetadataRetriever.METADATA_KEY_GENRE, FFmpegMediaMetadataRetriever.METADATA_KEY_GENRE));
            mKeyMap.put(Key.METADATA_KEY_TITLE, new KeyCompat(MediaMetadataRetriever.METADATA_KEY_TITLE, FFmpegMediaMetadataRetriever.METADATA_KEY_TITLE));
            mKeyMap.put(Key.METADATA_KEY_NUM_TRACKS, new KeyCompat(MediaMetadataRetriever.METADATA_KEY_NUM_TRACKS, FFmpegMediaMetadataRetriever.METADATA_KEY_TRACK));
            mKeyMap.put(Key.METADATA_KEY_DISC_NUMBER, new KeyCompat(MediaMetadataRetriever.METADATA_KEY_DISC_NUMBER, FFmpegMediaMetadataRetriever.METADATA_KEY_DISC));
            //android
            mKeyMap.put(Key.METADATA_KEY_CD_TRACK_NUMBER, new KeyCompat(MediaMetadataRetriever.METADATA_KEY_CD_TRACK_NUMBER, null));
            mKeyMap.put(Key.METADATA_KEY_AUTHOR, new KeyCompat(MediaMetadataRetriever.METADATA_KEY_AUTHOR, null));
            mKeyMap.put(Key.METADATA_KEY_YEAR, new KeyCompat(MediaMetadataRetriever.METADATA_KEY_YEAR, null));
            mKeyMap.put(Key.METADATA_KEY_WRITER, new KeyCompat(MediaMetadataRetriever.METADATA_KEY_WRITER, null));
            mKeyMap.put(Key.METADATA_KEY_MIMETYPE, new KeyCompat(MediaMetadataRetriever.METADATA_KEY_MIMETYPE, null));
            mKeyMap.put(Key.METADATA_KEY_COMPILATION, new KeyCompat(MediaMetadataRetriever.METADATA_KEY_COMPILATION, null));
            mKeyMap.put(Key.METADATA_KEY_HAS_AUDIO, new KeyCompat(MediaMetadataRetriever.METADATA_KEY_HAS_AUDIO, null));
            mKeyMap.put(Key.METADATA_KEY_HAS_VIDEO, new KeyCompat(MediaMetadataRetriever.METADATA_KEY_HAS_VIDEO, null));
            mKeyMap.put(Key.METADATA_KEY_BITRATE, new KeyCompat(MediaMetadataRetriever.METADATA_KEY_BITRATE, null));
            mKeyMap.put(Key.METADATA_KEY_LOCATION, new KeyCompat(MediaMetadataRetriever.METADATA_KEY_LOCATION, null));
            mKeyMap.put(Key.METADATA_KEY_HAS_IMAGE, new KeyCompat(MediaMetadataRetriever.METADATA_KEY_HAS_IMAGE, null));
            mKeyMap.put(Key.METADATA_KEY_IMAGE_COUNT, new KeyCompat(MediaMetadataRetriever.METADATA_KEY_IMAGE_COUNT, null));
            mKeyMap.put(Key.METADATA_KEY_IMAGE_PRIMARY, new KeyCompat(MediaMetadataRetriever.METADATA_KEY_IMAGE_PRIMARY, null));
            mKeyMap.put(Key.METADATA_KEY_FRAME_COUNT, new KeyCompat(MediaMetadataRetriever.METADATA_KEY_VIDEO_FRAME_COUNT, null));
            //ffmpeg
            mKeyMap.put(Key.METADATA_KEY_COMMENT, new KeyCompat(FFmpegMediaMetadataRetriever.METADATA_KEY_COMMENT));
            mKeyMap.put(Key.METADATA_KEY_COPYRIGHT, new KeyCompat(FFmpegMediaMetadataRetriever.METADATA_KEY_COPYRIGHT));
            mKeyMap.put(Key.METADATA_KEY_ENCODER, new KeyCompat(FFmpegMediaMetadataRetriever.METADATA_KEY_ENCODER));
            mKeyMap.put(Key.METADATA_KEY_ENCODED_BY, new KeyCompat(FFmpegMediaMetadataRetriever.METADATA_KEY_ENCODED_BY));
            mKeyMap.put(Key.METADATA_KEY_FILENAME, new KeyCompat(FFmpegMediaMetadataRetriever.METADATA_KEY_FILENAME));
            mKeyMap.put(Key.METADATA_KEY_LANGUAGE, new KeyCompat(FFmpegMediaMetadataRetriever.METADATA_KEY_LANGUAGE));
            mKeyMap.put(Key.METADATA_KEY_PERFORMER, new KeyCompat(FFmpegMediaMetadataRetriever.METADATA_KEY_PERFORMER));
            mKeyMap.put(Key.METADATA_KEY_PUBLISHER, new KeyCompat(FFmpegMediaMetadataRetriever.METADATA_KEY_PUBLISHER));
            mKeyMap.put(Key.METADATA_KEY_SERVICE_NAME, new KeyCompat(FFmpegMediaMetadataRetriever.METADATA_KEY_SERVICE_NAME));
            mKeyMap.put(Key.METADATA_KEY_SERVICE_PROVIDER, new KeyCompat(FFmpegMediaMetadataRetriever.METADATA_KEY_SERVICE_PROVIDER));
            mKeyMap.put(Key.METADATA_KEY_AUDIO_CODEC, new KeyCompat(FFmpegMediaMetadataRetriever.METADATA_KEY_AUDIO_CODEC));
            mKeyMap.put(Key.METADATA_KEY_VIDEO_CODEC, new KeyCompat(FFmpegMediaMetadataRetriever.METADATA_KEY_VIDEO_CODEC));
            mKeyMap.put(Key.METADATA_KEY_ICY_METADATA, new KeyCompat(FFmpegMediaMetadataRetriever.METADATA_KEY_ICY_METADATA));
            mKeyMap.put(Key.METADATA_KEY_CHAPTER_START_TIME, new KeyCompat(FFmpegMediaMetadataRetriever.METADATA_KEY_CHAPTER_START_TIME));
            mKeyMap.put(Key.METADATA_KEY_CHAPTER_END_TIME, new KeyCompat(FFmpegMediaMetadataRetriever.METADATA_KEY_CHAPTER_END_TIME));
            mKeyMap.put(Key.METADATA_CHAPTER_COUNT, new KeyCompat(FFmpegMediaMetadataRetriever.METADATA_CHAPTER_COUNT));
            mKeyMap.put(Key.METADATA_KEY_FILESIZE, new KeyCompat(FFmpegMediaMetadataRetriever.METADATA_KEY_FILESIZE));

        }
        final KeyCompat keyCompat = mKeyMap.get(keyCode);
        return keyCompat == null ? new KeyCompat(keyCode) : keyCompat;
    }

    public interface Key {

        //早于timeUs的关键帧 - 仅视频
        int OPTION_PREVIOUS_SYNC = MediaMetadataRetriever.OPTION_PREVIOUS_SYNC;
        //晚于timeUs的关键帧 - 仅视频
        int OPTION_NEXT_SYNC = MediaMetadataRetriever.OPTION_NEXT_SYNC;
        //最接近timeUs的关键帧 - 仅视频
        int OPTION_CLOSEST_SYNC = MediaMetadataRetriever.OPTION_CLOSEST_SYNC;
        //最接近timeUs的帧，但可能不是关键(性能开销较大) - 仅视频
        int OPTION_CLOSEST = MediaMetadataRetriever.OPTION_CLOSEST;

        String METADATA_KEY_WIDTH = "width";
        String METADATA_KEY_HEIGHT = "height";
        String METADATA_KEY_DURATION = "duration";
        String METADATA_KEY_ROTATION = "rotation";
        String METADATA_KEY_FRAMERATE = "frame_rate";
        String METADATA_KEY_ALBUM = "album";
        String METADATA_KEY_ARTIST = "artist";
        String METADATA_KEY_ALBUMARTIST = "album_artist";
        String METADATA_KEY_COMPOSER = "composer";
        String METADATA_KEY_DATE = "date";
        String METADATA_KEY_GENRE = "genre";
        String METADATA_KEY_TITLE = "title";
        String METADATA_KEY_NUM_TRACKS = "num_tracks";
        String METADATA_KEY_DISC_NUMBER = "disc_number";
        //android
        String METADATA_KEY_CD_TRACK_NUMBER = "cd_track_number";
        String METADATA_KEY_AUTHOR = "author";
        String METADATA_KEY_YEAR = "year";
        String METADATA_KEY_WRITER = "writer";
        String METADATA_KEY_MIMETYPE = "mime_type";
        String METADATA_KEY_COMPILATION = "compilation";
        String METADATA_KEY_HAS_AUDIO = "has_audio";
        String METADATA_KEY_HAS_VIDEO = "has_video";
        String METADATA_KEY_BITRATE = "bit_rate";
        String METADATA_KEY_LOCATION = "location";
        String METADATA_KEY_HAS_IMAGE = "has_imag";
        String METADATA_KEY_IMAGE_COUNT = "image_count";
        String METADATA_KEY_IMAGE_PRIMARY = "image_primary";
        String METADATA_KEY_FRAME_COUNT = "frame_count";
        //ffmpeg
        String METADATA_KEY_COMMENT = "comment";
        String METADATA_KEY_COPYRIGHT = "copyright";
        String METADATA_KEY_ENCODER = "encoder";
        String METADATA_KEY_ENCODED_BY = "encoded_by";
        String METADATA_KEY_FILENAME = "file_name";
        String METADATA_KEY_LANGUAGE = "language";
        String METADATA_KEY_PERFORMER = "performer";
        String METADATA_KEY_PUBLISHER = "publisher";
        String METADATA_KEY_SERVICE_NAME = "service_name";
        String METADATA_KEY_SERVICE_PROVIDER = "service_provider";
        String METADATA_KEY_AUDIO_CODEC = "audio_codec";
        String METADATA_KEY_VIDEO_CODEC = "video_codec";
        String METADATA_KEY_ICY_METADATA = "icy_metadata";
        String METADATA_KEY_CHAPTER_START_TIME = "chapter_start_time";
        String METADATA_KEY_CHAPTER_END_TIME = "chapter_end_time";
        String METADATA_CHAPTER_COUNT = "chapter_count";
        String METADATA_KEY_FILESIZE = "file_size";
    }

    public static final class KeyCompat {
        public static final int NULL_KEY = -1;

        public int android = NULL_KEY;
        public String ffmpeg;


        public KeyCompat(int android, String ffmpeg) {
            this.android = android;
            this.ffmpeg = ffmpeg;
        }

        public KeyCompat(String ffmpeg) {
            this.ffmpeg = ffmpeg;
        }


    }

    public static final class Config {
        private final List<MediaMetadataRetrieverFactory> mFactories;
        private Bitmap.Config mConfig;
        private boolean mMultipleEnginesEnable;
        private DataSourceCallback mCallback;

        private static Config mInstance;

        private Config() {
            mFactories = new ArrayList<>();
            mConfig = Bitmap.Config.ARGB_4444;
            addCustomRetrieverFactory(new JPEGMediaMetadataRetrieverFactory())
                    .addCustomRetrieverFactory(new GIFMediaMetadataRetrieverFactory())
                    .addCustomRetrieverFactory(new BitmapMediaMetadataRetrieverFactory())
                    .addCustomRetrieverFactory(new FFmpegMediaMetadataRetrieverFactory())
                    .addCustomRetrieverFactory(new AndroidMediaMetadataRetrieverFactory());
        }

        public static Config get() {
            synchronized (Config.class) {
                if (mInstance == null) {
                    mInstance = new Config();
                }
            }
            return mInstance;
        }

        @NonNull
        public List<MediaMetadataRetrieverFactory> getFactories() {
            return mFactories;
        }

        @NonNull
        public Bitmap.Config getBitmapConfig() {
            return mConfig;
        }

        /**
         * 供IMediaMetadataRetriever调用，setDataSource时回调callback
         *
         * @param retriever
         */
        public void setCustomDataSource(IMediaMetadataRetriever retriever) {
            if (mCallback != null) {
                mCallback.setCustomDataSource(retriever);
            }
        }

        public Config registerDataSource(@NonNull DataSourceCallback callback) {
            this.mCallback = callback;
            return this;
        }

        public Config bitmapConfig(@NonNull Bitmap.Config config) {
            this.mConfig = config;
            return this;
        }

        public Config addCustomRetrieverFactory(@NonNull MediaMetadataRetrieverFactory factory) {
            mFactories.add(factory);
            return this;
        }

        public Config addCustomRetrieverFactory(int index, @NonNull MediaMetadataRetrieverFactory factory) {
            mFactories.add(index, factory);
            return this;
        }

        /**
         * 设置自定义格式检查器
         *
         * @param formatCheckers
         * @return
         */
        public Config setCustomFileFormatCheckers(@NonNull List<FileFormat.FormatChecker> formatCheckers) {
            ImageFormatChecker.getInstance().setCustomImageFormatCheckers(formatCheckers);
            return this;
        }


    }

    public interface DataSourceCallback {
        void setCustomDataSource(IMediaMetadataRetriever retriever);
    }
}
