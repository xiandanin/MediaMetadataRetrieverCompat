package in.xiandan.mmrc.example.custom;

import in.xiandan.mmrc.IMediaMetadataRetriever;
import in.xiandan.mmrc.MediaMetadataRetrieverFactory;
import in.xiandan.mmrc.fileformat.FileFormat;

/**
 * @author dengyuhan
 * created 2019-07-02 18:09
 */
public class CustomMediaMetadataRetrieverFactory implements MediaMetadataRetrieverFactory {

    @Override
    public boolean supportsFileFormat(FileFormat format) {
        return false;
    }

    @Override
    public IMediaMetadataRetriever create() {
        return new CustomMediaMetadataRetriever();
    }
}
