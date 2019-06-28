package in.xiandan.mmrc.ffmpeg;

import in.xiandan.mmrc.IMediaMetadataRetriever;
import in.xiandan.mmrc.MediaMetadataRetrieverFactory;
import in.xiandan.mmrc.fileformat.FileFormat;
import in.xiandan.mmrc.utils.ClassUtils;

/**
 * @author dengyuhan
 * created 2019-06-24 17:03
 */
public class FFmpegMediaMetadataRetrieverFactory implements MediaMetadataRetrieverFactory {

    @Override
    public boolean supportsFileFormat(FileFormat format) {
        return FileFormat.UNKNOWN == format && ClassUtils.hasClass("wseemann.media.FFmpegMediaMetadataRetriever");
    }

    @Override
    public IMediaMetadataRetriever create() {
        return new FFmpegMediaMetadataRetrieverImpl();
    }
}
