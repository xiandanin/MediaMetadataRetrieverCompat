package in.xiandan.mmrc.example.custom;

import android.support.annotation.Nullable;

import in.xiandan.mmrc.fileformat.FileFormat;
import in.xiandan.mmrc.fileformat.FileFormatCheckerUtils;

/**
 * @author dengyuhan
 * created 2019-07-02 18:21
 */
public class CustomFormatChecker implements FileFormat.FormatChecker {
    public static final FileFormat SVG = new FileFormat("SVG", "svg");

    private final byte[] SVG_BYTES = FileFormatCheckerUtils.asciiBytes("svg");
    private final int SVG_HEADER_LENGTH = "<?xml version=\"1.0\" standalone=\"no\"?><!DOCTYPE ".length() + SVG_BYTES.length;

    @Override
    public int getHeaderSize() {
        return SVG_HEADER_LENGTH;
    }

    @Nullable
    @Override
    public FileFormat determineFormat(byte[] headerBytes, int headerSize) {
        if (FileFormatCheckerUtils.indexOfPattern(headerBytes, headerSize, SVG_BYTES, SVG_BYTES.length) != -1) {
            return SVG;
        } else {
            return FileFormat.UNKNOWN;
        }
    }
}
