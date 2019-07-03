package in.xiandan.mmrc.retriever.ffmpeg;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.IOException;

import in.xiandan.mmrc.IMediaMetadataRetriever;
import in.xiandan.mmrc.MediaMetadataResource;
import in.xiandan.mmrc.datasource.DataSource;
import in.xiandan.mmrc.retriever.AndroidMediaMetadataRetriever;

/**
 * @author dengyuhan
 * created 2019-07-03 20:05
 */
public class FFmpegAndroidMediaMetadataRetriever implements IMediaMetadataRetriever {
    private FFmpegMediaMetadataRetrieverImpl mFFmpeg;
    private AndroidMediaMetadataRetriever mAndroid;

    public FFmpegAndroidMediaMetadataRetriever() {
        this.mFFmpeg = new FFmpegMediaMetadataRetrieverImpl();
        this.mAndroid = new AndroidMediaMetadataRetriever();
    }

    @Override
    public void setDataSource(@NonNull DataSource source) throws IOException {
        this.mFFmpeg.setDataSource(source);
        this.mAndroid.setDataSource(source);
    }

    @Nullable
    @Override
    public Bitmap getFrameAtTime() {
        return mFFmpeg.getFrameAtTime();
    }

    @Nullable
    @Override
    public Bitmap getFrameAtTime(long millis, int option) {
        return mFFmpeg.getFrameAtTime(millis, option);
    }

    @Nullable
    @Override
    public Bitmap getScaledFrameAtTime(long millis, int option, int dstWidth, int dstHeight) {
        return mFFmpeg.getScaledFrameAtTime(millis, option, dstWidth, dstHeight);
    }

    @Nullable
    @Override
    public byte[] getEmbeddedPicture() {
        return mFFmpeg.getEmbeddedPicture();
    }

    @Nullable
    @Override
    public String extractMetadata(String keyCode) {
        final MediaMetadataResource.KeyCompat key = MediaMetadataResource.getKey(keyCode);
        if (key != null) {
            if (key.ffmpeg != null) {
                return mFFmpeg.extractMetadata(keyCode);
            } else if (key.android != null) {
                return mAndroid.extractMetadata(keyCode);
            }
        }
        return null;
    }

    @Override
    public void release() {
        mFFmpeg.release();
        mAndroid.release();
    }
}
