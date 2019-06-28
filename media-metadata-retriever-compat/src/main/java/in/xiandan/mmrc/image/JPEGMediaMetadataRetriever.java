package in.xiandan.mmrc.image;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.media.ExifInterface;

import java.io.IOException;

import in.xiandan.mmrc.MediaRetriever;
import in.xiandan.mmrc.datasource.DataSource;
import in.xiandan.mmrc.utils.BitmapTransformation;

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
        if (MediaRetriever.Key.METADATA_KEY_ROTATION.equals(keyCode)) {
            return String.valueOf(mExif.getRotationDegrees());
        } else {
            final String exifAttribute = mExif.getAttribute(keyCode);
            return exifAttribute != null ? exifAttribute : super.extractMetadata(keyCode);
        }
    }

    @Override
    public Bitmap getFrameAtTime(long timeUs, int option) {
        final Bitmap source = super.getFrameAtTime(timeUs, option);
        return source != null ? BitmapTransformation.rotate(source, mExif.getRotationDegrees()) : null;
    }

    @Override
    public Bitmap getScaledFrameAtTime(long timeUs, int option, int dstWidth, int dstHeight) {
        final Bitmap source = super.getScaledFrameAtTime(timeUs, option, dstWidth, dstHeight);
        return source != null ? BitmapTransformation.rotate(source, mExif.getRotationDegrees()) : null;
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
