package com.dyhdyh.compat.mmrc.transform;

/**
 * author  dengyuhan
 * created 2017/5/27 10:51
 */
public class MetadataKey {
    private String ffmpegMetadataKey;
    private String metadataKey;
    private String imageMetadataKey;

    public MetadataKey(String ffmpegMetadataKey, String metadataKey) {
        this.ffmpegMetadataKey = ffmpegMetadataKey;
        this.metadataKey = metadataKey;
        this.imageMetadataKey = metadataKey;
    }

    public MetadataKey(String ffmpegMetadataKey, String metadataKey, String imageMetadataKey) {
        this.ffmpegMetadataKey = ffmpegMetadataKey;
        this.metadataKey = metadataKey;
        this.imageMetadataKey = imageMetadataKey;
    }

    public String getFFmpegMetadataKey() {
        return ffmpegMetadataKey;
    }

    public void setFFmpegMetadataKey(String ffmpegMetadataKey) {
        this.ffmpegMetadataKey = ffmpegMetadataKey;
    }

    public String getMetadataKey() {
        return metadataKey;
    }

    public void setMetadataKey(String metadataKey) {
        this.metadataKey = metadataKey;
    }

    public String getImageMetadataKey() {
        return imageMetadataKey;
    }

    public void setImageMetadataKey(String imageMetadataKey) {
        this.imageMetadataKey = imageMetadataKey;
    }
}
