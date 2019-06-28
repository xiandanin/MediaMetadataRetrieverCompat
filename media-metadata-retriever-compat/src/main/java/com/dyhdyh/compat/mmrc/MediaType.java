package com.dyhdyh.compat.mmrc;

import com.dyhdyh.compat.mmrc.image.DefaultImageHeaderParser;
import com.dyhdyh.compat.mmrc.image.ImageType;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author dengyuhan
 * created 2019/3/15 16:26
 */
public class MediaType {
    private DefaultImageHeaderParser mImageParser;

    public MediaType() {
        this.mImageParser = new DefaultImageHeaderParser();
    }

    public String getType(InputStream is) throws IOException {
        final ImageType imageType = mImageParser.getType(is);
        if (ImageType.UNKNOWN == imageType) {
            return "";
        } else {
            return imageType.name();
        }
    }
}
