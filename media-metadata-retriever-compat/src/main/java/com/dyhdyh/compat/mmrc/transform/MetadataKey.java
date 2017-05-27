package com.dyhdyh.compat.mmrc.transform;

/**
 * author  dengyuhan
 * created 2017/5/27 10:51
 */
public class MetadataKey {
    private String ffmpegMetadatakey;
    private String metadatakey;

    public MetadataKey(String ffmpegMetadatakey, String metadatakey) {
        this.ffmpegMetadatakey = ffmpegMetadatakey;
        this.metadatakey = metadatakey;
    }

    public String getFfmpegMetadatakey() {
        return ffmpegMetadatakey;
    }

    public void setFfmpegMetadatakey(String ffmpegMetadatakey) {
        this.ffmpegMetadatakey = ffmpegMetadatakey;
    }

    public String getMetadatakey() {
        return metadatakey;
    }

    public void setMetadatakey(String metadatakey) {
        this.metadatakey = metadatakey;
    }
}
