package in.xiandan.mmrc.retriever;

import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.io.IOException;

import in.xiandan.mmrc.IMediaMetadataRetriever;
import in.xiandan.mmrc.MediaMetadataConfig;
import in.xiandan.mmrc.MediaMetadataKey;
import in.xiandan.mmrc.MediaMetadataResource;
import in.xiandan.mmrc.datasource.DataSource;
import in.xiandan.mmrc.datasource.FileDescriptorSource;
import in.xiandan.mmrc.datasource.FileSource;
import in.xiandan.mmrc.datasource.HTTPSource;
import in.xiandan.mmrc.datasource.UriSource;
import in.xiandan.mmrc.utils.BitmapProcessor;
import in.xiandan.mmrc.utils.MetadataRetrieverUtils;

/**
 * 原生的Retriever
 *
 * @author dengyuhan
 * created 2019-06-25 11:26
 */
public class AndroidMediaMetadataRetriever implements IMediaMetadataRetriever {
    private MediaMetadataRetriever mRetriever;

    private Integer mWidth;
    private Integer mHeight;

    public AndroidMediaMetadataRetriever() {
        this.mRetriever = new MediaMetadataRetriever();
    }

    @Override
    public void setDataSource(@NonNull DataSource source) throws IOException {
        mWidth = null;
        mHeight = null;
        if (source instanceof FileSource) {
            //File
            this.mRetriever.setDataSource(((FileSource) source).source().getAbsolutePath());
        } else if (source instanceof HTTPSource) {
            //HTTP
            final HTTPSource httpSource = ((HTTPSource) source);
            this.mRetriever.setDataSource(httpSource.source(), httpSource.getHeaders());
        } else if (source instanceof FileDescriptorSource) {
            //FileDescriptor
            final FileDescriptorSource fdSource = ((FileDescriptorSource) source);
            this.mRetriever.setDataSource(fdSource.source(), fdSource.getOffset(), fdSource.getLength());
        } else if (source instanceof UriSource) {
            //Uri
            final UriSource uriSource = ((UriSource) source);
            this.mRetriever.setDataSource(uriSource.getContext(), uriSource.source());
        } else {
            final MediaMetadataConfig.CustomDataSourceCallback callback = MediaMetadataResource.globalConfig().getSourceCallback();
            if (callback != null) {
                callback.setCustomDataSource(this, source);
            }
        }
    }

    @Nullable
    @Override
    public Bitmap getFrameAtTime() {
        return this.mRetriever.getFrameAtTime();
    }

    @Nullable
    @Override
    public Bitmap getFrameAtTime(long millis, int option) {
        return this.mRetriever.getFrameAtTime(millis * 1000, option);
    }

    @Nullable
    @Override
    public Bitmap getScaledFrameAtTime(long millis, int option, int dstWidth, int dstHeight) {
        long timeUs = millis * 1000;
        final int srcWidth = getWidth();
        final int srcHeight = getHeight();
        if (srcWidth > 0 && srcHeight > 0) {
            final float scale = BitmapProcessor.calculateScale(mWidth, mHeight, dstWidth, dstHeight);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
                return this.mRetriever.getScaledFrameAtTime(timeUs, option, (int) (mWidth * scale), (int) (mHeight * scale));
            } else {
                return BitmapProcessor.floorScale(this.mRetriever.getFrameAtTime(timeUs, option), scale);
            }
        }
        return this.mRetriever.getFrameAtTime(timeUs, option);
    }

    @Override
    public byte[] getEmbeddedPicture() {
        return this.mRetriever.getEmbeddedPicture();
    }


    @Override
    public String extractMetadata(String keyCode) {
        final MediaMetadataResource.KeyCompat key = MediaMetadataResource.getKey(keyCode);
        String value = key == null || key.android == null ? null : this.mRetriever.extractMetadata(key.android);
        //统一格式
        if (MediaMetadataKey.DATE.equals(keyCode) && !TextUtils.isEmpty(value)) {
            return MetadataRetrieverUtils.parseTimeString("yyyyMMdd'T'HHmmss.SSS'Z'", value);
        }
        return value;
    }

    @Override
    public void release() {
        this.mRetriever.release();
    }

    protected int getWidth() {
        try {
            if (mWidth == null) {
                mWidth = Integer.parseInt(mRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH));
            }
            return mWidth;
        } catch (NumberFormatException ignored) {
            return 0;
        }
    }

    protected int getHeight() {
        try {
            if (mHeight == null) {
                mHeight = Integer.parseInt(mRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT));
            }
            return mHeight;
        } catch (NumberFormatException ignored) {
            return 0;
        }
    }

}
