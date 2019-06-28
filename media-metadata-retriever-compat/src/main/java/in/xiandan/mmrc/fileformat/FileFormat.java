package in.xiandan.mmrc.fileformat;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * From Fresco
 * Class representing all used image formats.
 * @see <a href="https://github.com/facebook/fresco/blob/master/imagepipeline-base/src/main/java/com/facebook/imageformat/ImageFormat.java">ImageFormat</a>
 */
public class FileFormat {

    public interface FormatChecker {

        /**
         * Get the number of header bytes the format checker requires
         * @return the number of header bytes needed
         */
        int getHeaderSize();

        /**
         * Returns an {@link FileFormat} if the checker is able to determine the format
         * or null otherwise.
         * @param headerBytes the header bytes to check
         * @param headerSize the size of the header in bytes
         * @return the image format or null if unknown
         */
        @Nullable
        FileFormat determineFormat(byte[] headerBytes, int headerSize);
    }

    // Unknown image format
    public static final FileFormat UNKNOWN = new FileFormat("UNKNOWN", null);

    private final @Nullable String mFileExtension;
    private final @NonNull String mName;

    public FileFormat(@NonNull String name, @Nullable String fileExtension) {
        mName = name;
        mFileExtension = fileExtension;
    }

    /**
     * Get the default file extension for the given image format.
     * @return file extension for the image format
     */
    @Nullable
    public String getFileExtension() {
        return mFileExtension;
    }

    @Override
    public String toString() {
        return getName();
    }

    @NonNull
    public String getName() {
        return mName;
    }
}
