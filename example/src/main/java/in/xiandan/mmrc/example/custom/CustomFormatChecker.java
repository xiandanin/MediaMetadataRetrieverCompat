package in.xiandan.mmrc.example.custom;

import android.support.annotation.Nullable;

import in.xiandan.mmrc.fileformat.FileFormat;

/**
 * @author dengyuhan
 * created 2019-07-02 18:21
 */
public class CustomFormatChecker implements FileFormat.FormatChecker {
    @Override
    public int getHeaderSize() {
        return 0;
    }

    @Nullable
    @Override
    public FileFormat determineFormat(byte[] headerBytes, int headerSize) {
        return null;
    }
}
