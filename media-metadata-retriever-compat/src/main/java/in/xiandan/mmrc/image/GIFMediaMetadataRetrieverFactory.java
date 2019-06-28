package in.xiandan.mmrc.image;

import in.xiandan.mmrc.MediaMetadataRetrieverFactory;
import in.xiandan.mmrc.fileformat.DefaultImageFormats;
import in.xiandan.mmrc.fileformat.FileFormat;
import in.xiandan.mmrc.IMediaMetadataRetriever;

/**
 * @author dengyuhan
 * created 2019-06-24 17:03
 */
public class GIFMediaMetadataRetrieverFactory implements MediaMetadataRetrieverFactory {

    @Override
    public boolean supportsFileFormat(FileFormat format) {
        return format == DefaultImageFormats.GIF;
    }

    @Override
    public IMediaMetadataRetriever create() {
        return new GIFMediaMetadataRetriever();
    }
}
