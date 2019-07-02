package in.xiandan.mmrc.retriever.image;

import in.xiandan.mmrc.MediaMetadataRetrieverFactory;
import in.xiandan.mmrc.fileformat.DefaultImageFormats;
import in.xiandan.mmrc.fileformat.FileFormat;
import in.xiandan.mmrc.IMediaMetadataRetriever;

/**
 * @author dengyuhan
 * created 2019-06-24 17:03
 */
public class BitmapMediaMetadataRetrieverFactory implements MediaMetadataRetrieverFactory {

    @Override
    public boolean supportsFileFormat(FileFormat format) {
        return DefaultImageFormats.getDefaultFormats().contains(format);
    }

    @Override
    public IMediaMetadataRetriever create() {
        return new BitmapMediaMetadataRetriever();
    }
}
