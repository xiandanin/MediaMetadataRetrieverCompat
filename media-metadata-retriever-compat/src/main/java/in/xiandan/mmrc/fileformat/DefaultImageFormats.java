/*
 * Copyright (c) Facebook, Inc. and its affiliates.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */
package in.xiandan.mmrc.fileformat;

import java.util.ArrayList;
import java.util.List;

/**
 * Default image formats that Fresco supports.
 */
public final class DefaultImageFormats {

    public static final FileFormat JPEG = new FileFormat("JPEG", "jpeg");
    public static final FileFormat PNG = new FileFormat("PNG", "png");
    public static final FileFormat PNG_ANIMATED = new FileFormat("PNG_ANIMATED", "png");
    public static final FileFormat GIF = new FileFormat("GIF", "gif");
    public static final FileFormat BMP = new FileFormat("BMP", "bmp");
    public static final FileFormat ICO = new FileFormat("ICO", "ico");
    public static final FileFormat WEBP_SIMPLE = new FileFormat("WEBP_SIMPLE", "webp");
    public static final FileFormat WEBP_LOSSLESS = new FileFormat("WEBP_LOSSLESS", "webp");
    public static final FileFormat WEBP_EXTENDED = new FileFormat("WEBP_EXTENDED", "webp");
    public static final FileFormat WEBP_EXTENDED_WITH_ALPHA =
            new FileFormat("WEBP_EXTENDED_WITH_ALPHA", "webp");
    public static final FileFormat WEBP_ANIMATED = new FileFormat("WEBP_ANIMATED", "webp");
    public static final FileFormat HEIF = new FileFormat("HEIF", "heif");

    private static List<FileFormat> sAllDefaultFormats;

    /**
     * Check if the given image format is a WebP image format (static or animated).
     *
     * @param fileFormat the image format to check
     * @return true if WebP format
     */
    public static boolean isWebpFormat(FileFormat fileFormat) {
        return isStaticWebpFormat(fileFormat) ||
                fileFormat == WEBP_ANIMATED;
    }

    /**
     * Check if the given image format is static WebP (not animated).
     *
     * @param fileFormat the image format to check
     * @return true if static WebP
     */
    public static boolean isStaticWebpFormat(FileFormat fileFormat) {
        return fileFormat == WEBP_SIMPLE ||
                fileFormat == WEBP_LOSSLESS ||
                fileFormat == WEBP_EXTENDED ||
                fileFormat == WEBP_EXTENDED_WITH_ALPHA;
    }

    /**
     * Get all default formats supported by Fresco.
     * Does not include {@link FileFormat#UNKNOWN}.
     *
     * @return all supported default formats
     */
    public static List<FileFormat> getDefaultFormats() {
        if (sAllDefaultFormats == null) {
            List<FileFormat> mDefaultFormats = new ArrayList<>(9);
            mDefaultFormats.add(JPEG);
            mDefaultFormats.add(PNG);
            mDefaultFormats.add(PNG_ANIMATED);
            mDefaultFormats.add(GIF);
            mDefaultFormats.add(BMP);
            mDefaultFormats.add(ICO);
            mDefaultFormats.add(WEBP_SIMPLE);
            mDefaultFormats.add(WEBP_LOSSLESS);
            mDefaultFormats.add(WEBP_EXTENDED);
            mDefaultFormats.add(WEBP_EXTENDED_WITH_ALPHA);
            mDefaultFormats.add(WEBP_ANIMATED);
            mDefaultFormats.add(HEIF);
            sAllDefaultFormats = mDefaultFormats;
        }
        return sAllDefaultFormats;
    }

    private DefaultImageFormats() {
    }
}
