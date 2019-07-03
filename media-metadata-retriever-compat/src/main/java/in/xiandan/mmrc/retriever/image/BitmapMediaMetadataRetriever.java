package in.xiandan.mmrc.retriever.image;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import in.xiandan.mmrc.IMediaMetadataRetriever;
import in.xiandan.mmrc.MediaMetadataKey;
import in.xiandan.mmrc.datasource.DataSource;
import in.xiandan.mmrc.utils.BitmapProcessor;
import in.xiandan.mmrc.utils.Closeables;
import in.xiandan.mmrc.utils.MetadataRetrieverUtils;

/**
 * @author dengyuhan
 * created 2019-06-24 16:53
 */
public class BitmapMediaMetadataRetriever implements IMediaMetadataRetriever {
    private BufferedInputStream mStream;
    private BitmapFactory.Options mOptions;

    @Override
    public void setDataSource(@NonNull DataSource source) throws IOException {
        mStream = new BufferedInputStream(source.toStream());
        mStream.mark(Integer.MAX_VALUE);
    }

    @Nullable
    @Override
    public Bitmap getFrameAtTime() {
        return this.getFrameAtTime(0, MediaMetadataKey.OPTION_CLOSEST_SYNC);
    }

    @Nullable
    @Override
    public Bitmap getFrameAtTime(long millis, int option) {
        return BitmapFactory.decodeStream(getStream(true));
    }

    @Nullable
    @Override
    public Bitmap getScaledFrameAtTime(long millis, int option, int dstWidth, int dstHeight) {
        final BitmapFactory.Options sampling = new BitmapFactory.Options();
        sampling.inSampleSize = BitmapProcessor.calculateSampleSize(getWidth(), getHeight(), dstWidth, dstHeight);
        return BitmapFactory.decodeStream(getStream(true), null, sampling);

    }

    @Override
    public byte[] getEmbeddedPicture() {
        return MetadataRetrieverUtils.getEmbeddedPicture(getStream(true));
    }

    @Override
    public String extractMetadata(String keyCode) {
        if (MediaMetadataKey.WIDTH.equals(keyCode)) {
            return String.valueOf(getWidth());
        } else if (MediaMetadataKey.HEIGHT.equals(keyCode)) {
            return String.valueOf(getHeight());
        }
        return null;
    }

    protected int getWidth() {
        return obtainDecodeOptions().outWidth;
    }

    protected int getHeight() {
        return obtainDecodeOptions().outHeight;
    }

    @Override
    public void release() {
        Closeables.closeQuietly(mStream);
    }


    /**
     * 获取流之前重置
     *
     * @return
     */
    protected InputStream getStream(boolean reset) {
        try {
            if (reset) {
                mStream.reset();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mStream;
    }

    /**
     * 创建并获取Options
     *
     * @return 图片的Options
     */
    protected BitmapFactory.Options obtainDecodeOptions() {
        if (mOptions == null) {
            mOptions = new BitmapFactory.Options();
            mOptions.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(getStream(true), null, mOptions);
        }
        return mOptions;
    }

}
