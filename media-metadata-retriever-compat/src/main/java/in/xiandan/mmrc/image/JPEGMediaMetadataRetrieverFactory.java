package in.xiandan.mmrc.image;

import in.xiandan.mmrc.IMediaMetadataRetriever;
import in.xiandan.mmrc.MediaMetadataRetrieverFactory;
import in.xiandan.mmrc.fileformat.DefaultImageFormats;
import in.xiandan.mmrc.fileformat.FileFormat;
import in.xiandan.mmrc.utils.ClassUtils;

/**
 * @author dengyuhan
 * created 2019-06-24 17:03
 */
public class JPEGMediaMetadataRetrieverFactory implements MediaMetadataRetrieverFactory {

    @Override
    public boolean supportsFileFormat(FileFormat format) {
        return DefaultImageFormats.JPEG == format
                && ClassUtils.hasOneClass("android.support.media.ExifInterface"
                , "androidx.exifinterface.media.ExifInterface");
    }

    @Override
    public IMediaMetadataRetriever create() {
        return new JPEGMediaMetadataRetriever();
    }
}
