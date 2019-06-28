package in.xiandan.mmrc;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import in.xiandan.mmrc.ffmpeg.FFmpegMediaMetadataRetrieverFactory;
import in.xiandan.mmrc.image.BitmapMediaMetadataRetrieverFactory;
import in.xiandan.mmrc.image.GIFMediaMetadataRetrieverFactory;
import in.xiandan.mmrc.image.JPEGMediaMetadataRetrieverFactory;

/**
 * @author dengyuhan
 * created 2019-06-27 20:32
 */
public final class MediaRetriever {

    public interface Key {
        int OPTION_PREVIOUS_SYNC = 0x00;
        int OPTION_NEXT_SYNC = 0x01;
        int OPTION_CLOSEST_SYNC = 0x02;
        int OPTION_CLOSEST = 0x03;

        String METADATA_KEY_WIDTH = "width";
        String METADATA_KEY_HEIGHT = "height";
        String METADATA_KEY_DURATION = "duration";
        String METADATA_KEY_ROTATION = "rotation";
    }

    public static final class Config {
        private final List<MediaMetadataRetrieverFactory> mFactories;
        private Bitmap.Config mConfig;
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
        public Bitmap.Config getConfig() {
            return mConfig;
        }

        /**
         * 供IMediaMetadataRetriever调用 当setDataSource回调callback
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

        public Config addCustomRetrieverFactory(MediaMetadataRetrieverFactory factory) {
            mFactories.add(factory);
            return this;
        }

        public Config addCustomRetrieverFactory(int index, MediaMetadataRetrieverFactory factory) {
            mFactories.add(index, factory);
            return this;
        }

    }

    public interface DataSourceCallback {
        void setCustomDataSource(IMediaMetadataRetriever retriever);
    }
}
