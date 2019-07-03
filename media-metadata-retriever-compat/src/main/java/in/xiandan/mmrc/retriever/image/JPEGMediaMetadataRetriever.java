package in.xiandan.mmrc.retriever.image;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.media.ExifInterface;

import java.io.IOException;

import in.xiandan.mmrc.MediaMetadataKey;
import in.xiandan.mmrc.datasource.DataSource;
import in.xiandan.mmrc.utils.BitmapProcessor;
import in.xiandan.mmrc.utils.MetadataRetrieverUtils;

/**
 * 支持Exif的Retriever
 *
 * @author dengyuhan
 * created 2019-06-24 16:58
 */
public class JPEGMediaMetadataRetriever extends BitmapMediaMetadataRetriever {
    private ExifInterface mExif;

    @Override
    public void setDataSource(@NonNull DataSource source) throws IOException {
        super.setDataSource(source);
        mExif = new ExifInterface(getStream(false));
    }

    @Override
    public String extractMetadata(String keyCode) {
        final String value = super.extractMetadata(keyCode);
        if (value != null) {
            return value;
        } else if (MediaMetadataKey.ROTATION.equals(keyCode)) {
            return String.valueOf(mExif.getRotationDegrees());
        } else if (MediaMetadataKey.LOCATION.equals(keyCode)) {
            return MetadataRetrieverUtils.formatLatLong(mExif.getLatLong());
        } else if (MediaMetadataKey.DATE.equals(keyCode)) {
            return String.valueOf(mExif.getDateTime());
        } else {
            return mExif.getAttribute(keyCode);
        }
    }


    @Nullable
    @Override
    public Bitmap getFrameAtTime() {
        final Bitmap source = super.getFrameAtTime();
        return source != null ? BitmapProcessor.rotate(source, mExif.getRotationDegrees()) : null;
    }

    @Nullable
    @Override
    public Bitmap getFrameAtTime(long millis, int option) {
        final Bitmap source = super.getFrameAtTime(millis, option);
        return source != null ? BitmapProcessor.rotate(source, mExif.getRotationDegrees()) : null;
    }

    @Nullable
    @Override
    public Bitmap getScaledFrameAtTime(long millis, int option, int dstWidth, int dstHeight) {
        final Bitmap source = super.getScaledFrameAtTime(millis, option, dstWidth, dstHeight);
        return source != null ? BitmapProcessor.rotate(source, mExif.getRotationDegrees()) : null;
    }

    @Override
    protected int getWidth() {
        return mExif.getAttributeInt(ExifInterface.TAG_IMAGE_WIDTH, 0);
    }

    @Override
    protected int getHeight() {
        return mExif.getAttributeInt(ExifInterface.TAG_IMAGE_LENGTH, 0);
    }

}
