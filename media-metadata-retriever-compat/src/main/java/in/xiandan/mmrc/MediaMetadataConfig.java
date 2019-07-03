package in.xiandan.mmrc;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import in.xiandan.mmrc.datasource.DataSource;
import in.xiandan.mmrc.fileformat.FileFormat;
import in.xiandan.mmrc.fileformat.FileFormatChecker;
import in.xiandan.mmrc.retriever.AndroidMediaMetadataRetrieverFactory;
import in.xiandan.mmrc.retriever.ffmpeg.FFmpegMediaMetadataRetrieverFactory;
import in.xiandan.mmrc.retriever.image.BitmapMediaMetadataRetrieverFactory;
import in.xiandan.mmrc.retriever.image.GIFMediaMetadataRetrieverFactory;
import in.xiandan.mmrc.retriever.image.JPEGMediaMetadataRetrieverFactory;

/**
 * @author dengyuhan
 * created 2019-07-03 17:59
 */
public class MediaMetadataConfig {
    private final List<MediaMetadataRetrieverFactory> mFactories;
    private CustomDataSourceCallback mSourceCallback;

    private MediaMetadataConfig(Builder builder) {
        mFactories = builder.factories;
        mSourceCallback = builder.sourceCallback;
        if (builder.formatCheckers != null) {
            FileFormatChecker.getInstance().setCustomImageFormatCheckers(builder.formatCheckers);
        }
    }


    /**
     * 创建新的配置
     *
     * @return
     */
    public static Builder newBuilder() {
        return new Builder();
    }

    @NonNull
    public List<MediaMetadataRetrieverFactory> getFactories() {
        return mFactories;
    }

    public CustomDataSourceCallback getSourceCallback() {
        return mSourceCallback;
    }

    public void apply() {
        MediaMetadataResource.applyGlobalConfig(this);
    }


    /**
     * 配置构造器
     */
    public static final class Builder {
        private final List<MediaMetadataRetrieverFactory> factories = new ArrayList<>();
        private List<FileFormat.FormatChecker> formatCheckers;
        private CustomDataSourceCallback sourceCallback;

        private Builder() {
            addCustomRetrieverFactory(new JPEGMediaMetadataRetrieverFactory())
                    .addCustomRetrieverFactory(new GIFMediaMetadataRetrieverFactory())
                    .addCustomRetrieverFactory(new BitmapMediaMetadataRetrieverFactory())
                    .addCustomRetrieverFactory(new FFmpegMediaMetadataRetrieverFactory())
                    .addCustomRetrieverFactory(new AndroidMediaMetadataRetrieverFactory());
        }

        public Builder addCustomRetrieverFactory(@NonNull MediaMetadataRetrieverFactory factory) {
            this.factories.add(factory);
            return this;
        }

        public Builder addCustomRetrieverFactory(int index, @NonNull MediaMetadataRetrieverFactory factory) {
            this.factories.add(index, factory);
            return this;
        }

        /**
         * 设置自定义格式检查器
         *
         * @param formatChecker
         * @return
         */
        public Builder addFileFormatChecker(@NonNull FileFormat.FormatChecker formatChecker) {
            if (formatCheckers == null) {
                formatCheckers = new ArrayList<>();
            }
            formatCheckers.add(formatChecker);
            return this;
        }

        public Builder setCustomDataSourceCallback(@Nullable CustomDataSourceCallback callback) {
            this.sourceCallback = callback;
            return this;
        }


        public MediaMetadataConfig build() {
            return new MediaMetadataConfig(this);
        }

    }

    public interface CustomDataSourceCallback {
        void setCustomDataSource(IMediaMetadataRetriever retriever, DataSource source);
    }
}
