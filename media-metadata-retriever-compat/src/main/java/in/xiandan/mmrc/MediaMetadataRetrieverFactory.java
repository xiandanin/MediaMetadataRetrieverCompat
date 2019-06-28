package in.xiandan.mmrc;

import in.xiandan.mmrc.fileformat.FileFormat;

/**
 * @author dengyuhan
 * created 2019-06-24 16:40
 */
public interface MediaMetadataRetrieverFactory {
    /**
     * 是否支持此格式
     *
     * @param format
     * @return
     */
    boolean supportsFileFormat(FileFormat format);


    /**
     * 创建MediaMetadataRetriever
     * @return
     */
    IMediaMetadataRetriever create();

}
