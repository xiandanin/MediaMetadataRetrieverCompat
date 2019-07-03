package in.xiandan.mmrc.fileformat;


import android.graphics.ImageFormat;
import android.support.annotation.Nullable;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import in.xiandan.mmrc.utils.ByteStreams;
import in.xiandan.mmrc.utils.Closeables;
import in.xiandan.mmrc.utils.Preconditions;

/**
 * Detects the format of an encoded image.
 */
public class FileFormatChecker {

    private static FileFormatChecker sInstance;

    private int mMaxHeaderLength;

    @Nullable
    private List<FileFormat.FormatChecker> mCustomImageFormatCheckers;

    private final FileFormat.FormatChecker mDefaultFormatChecker = new DefaultImageFormatChecker();

    private FileFormatChecker() {
        updateMaxHeaderLength();
    }

    public void setCustomImageFormatCheckers(
            @Nullable List<FileFormat.FormatChecker> customImageFormatCheckers) {
        mCustomImageFormatCheckers = customImageFormatCheckers;
        updateMaxHeaderLength();
    }

    public FileFormat determineImageFormat(final InputStream is) throws IOException {
        Preconditions.checkNotNull(is);
        final byte[] imageHeaderBytes = new byte[mMaxHeaderLength];
        final int headerSize = readHeaderFromStream(mMaxHeaderLength, is, imageHeaderBytes);

        FileFormat format = mDefaultFormatChecker.determineFormat(imageHeaderBytes, headerSize);
        if (format != null && format != FileFormat.UNKNOWN) {
            return format;
        }

        if (mCustomImageFormatCheckers != null) {
            for (FileFormat.FormatChecker formatChecker : mCustomImageFormatCheckers) {
                format = formatChecker.determineFormat(imageHeaderBytes, headerSize);
                if (format != null && format != FileFormat.UNKNOWN) {
                    return format;
                }
            }
        }
        return FileFormat.UNKNOWN;
    }

    private void updateMaxHeaderLength() {
        mMaxHeaderLength = mDefaultFormatChecker.getHeaderSize();
        if (mCustomImageFormatCheckers != null) {
            for (FileFormat.FormatChecker checker : mCustomImageFormatCheckers) {
                mMaxHeaderLength = Math.max(mMaxHeaderLength, checker.getHeaderSize());
            }
        }
    }

    /**
     * Reads up to maxHeaderLength bytes from is InputStream. If mark is supported by is, it is
     * used to restore content of the stream after appropriate amount of data is read.
     * Read bytes are stored in imageHeaderBytes, which should be capable of storing
     * maxHeaderLength bytes.
     * @param maxHeaderLength the maximum header length
     * @param is
     * @param imageHeaderBytes
     * @return number of bytes read from is
     * @throws IOException
     */
    private static int readHeaderFromStream(
            int maxHeaderLength,
            final InputStream is,
            final byte[] imageHeaderBytes)
            throws IOException {
        Preconditions.checkNotNull(is);
        Preconditions.checkNotNull(imageHeaderBytes);
        Preconditions.checkArgument(imageHeaderBytes.length >= maxHeaderLength);

        // If mark is supported by the stream, use it to let the owner of the stream re-read the same
        // data. Otherwise, just consume some data.
        if (is.markSupported()) {
            try {
                is.mark(maxHeaderLength);
                return ByteStreams.read(is, imageHeaderBytes, 0, maxHeaderLength);
            } finally {
                is.reset();
            }
        } else {
            return ByteStreams.read(is, imageHeaderBytes, 0, maxHeaderLength);
        }
    }

    /**
     * Get the currently used instance of the image format checker
     * @return the image format checker to use
     */
    public static synchronized FileFormatChecker getInstance() {
        if (sInstance == null) {
            sInstance = new FileFormatChecker();
        }
        return sInstance;
    }

    /**
     * Tries to read up to MAX_HEADER_LENGTH bytes from InputStream is and use read bytes to
     * determine type of the image contained in is. If provided input stream does not support mark,
     * then this method consumes data from is and it is not safe to read further bytes from is after
     * this method returns. Otherwise, if mark is supported, it will be used to preserve original
     * content of is.
     * @param is
     * @return ImageFormat matching content of is InputStream or UNKNOWN_IMAGE if no type is suitable
     * @throws IOException if exception happens during read
     */
    public static FileFormat getFileFormat(final InputStream is) throws IOException {
        return getInstance().determineImageFormat(is);
    }


    /**
     * Reads image header from a file indicated by provided filename and determines
     * its format. This method does not throw IOException if one occurs. In this case,
     * {@link ImageFormat#UNKNOWN} will be returned.
     * @param filename
     * @return ImageFormat for image stored in filename
     */
    public static FileFormat getFileFormat(String filename) {
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(filename);
            return getFileFormat(fileInputStream);
        } catch (IOException ioe) {
            return FileFormat.UNKNOWN;
        } finally {
            Closeables.closeQuietly(fileInputStream);
        }
    }
}
