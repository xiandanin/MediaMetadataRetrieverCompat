package com.dyhdyh.compat.mmrc.image;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

/**
 * Interface for the ImageHeaderParser.
 * * @see <a href="https://github.com/bumptech/glide/blob/master/library/src/main/java/com/bumptech/glide/load/ImageHeaderParser.java"></a>
 */
public interface ImageHeaderParser {
    /**
     * A constant indicating we were unable to parse the orientation from the image either because no
     * exif segment containing orientation data existed, or because of an I/O error attempting to read
     * the exif segment.
     */
    int UNKNOWN_ORIENTATION = -1;

    ImageType getType(InputStream is) throws IOException;

    ImageType getType(ByteBuffer byteBuffer) throws IOException;

    /**
     * Parse the orientation from the image header. If it doesn't handle this image type (or this is
     * not an image) it will return a default value rather than throwing an exception.
     *
     * @return The exif orientation if present or -1 if the header couldn't be parsed or doesn't
     * contain an orientation
     */
    int getOrientation(InputStream is) throws IOException;

}