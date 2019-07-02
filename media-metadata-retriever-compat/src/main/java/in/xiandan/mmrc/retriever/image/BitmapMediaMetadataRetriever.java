package in.xiandan.mmrc.retriever.image;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import in.xiandan.mmrc.IMediaMetadataRetriever;
import in.xiandan.mmrc.MediaRetrieverResource;
import in.xiandan.mmrc.datasource.DataSource;
import in.xiandan.mmrc.utils.BitmapProcessor;
import in.xiandan.mmrc.utils.Closeables;

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

    @Override
    public Bitmap getFrameAtTime() {
        return getFrameAtTime(0, MediaRetrieverResource.Key.OPTION_CLOSEST_SYNC);
    }

    @Override
    public Bitmap getFrameAtTime(long timeUs, int option) {
        return BitmapFactory.decodeStream(getStream(true), null, createOptions());
    }

    @Override
    public Bitmap getScaledFrameAtTime(long timeUs, int option, int dstWidth, int dstHeight) {
        final BitmapFactory.Options sampling = createOptions();
        sampling.inSampleSize = BitmapProcessor.calculateSampleSize(getWidth(), getHeight(), dstWidth, dstHeight);
        return BitmapFactory.decodeStream(getStream(true), null, sampling);
    }

    @Override
    public byte[] getEmbeddedPicture() {
        try {
            final InputStream stream = getStream(true);
            ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
            byte[] buff = new byte[1024];
            int buffer;
            while ((buffer = stream.read(buff)) > 0) {
                swapStream.write(buff, 0, buffer);
            }
            return swapStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String extractMetadata(String keyCode) {
        if (MediaRetrieverResource.Key.METADATA_KEY_WIDTH.equals(keyCode)) {
            return String.valueOf(getWidth());
        } else if (MediaRetrieverResource.Key.METADATA_KEY_HEIGHT.equals(keyCode)) {
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
            mOptions = createOptions();
            mOptions.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(getStream(true), null, mOptions);
        }
        return mOptions;
    }


    /**
     * 根据配置创建Options
     *
     * @return
     */
    protected BitmapFactory.Options createOptions() {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = MediaRetrieverResource.Config.get().getBitmapConfig();
        return options;
    }
}
