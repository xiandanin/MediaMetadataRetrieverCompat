package in.xiandan.mmrc.datasource;

import android.support.annotation.NonNull;

import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * FileDescriptor数据源
 * @author dengyuhan
 * created 2019-06-28 17:08
 */
public class FileDescriptorSource implements DataSource<FileDescriptor> {
    private final FileDescriptor mFD;
    private final int mOffset;
    private final int mLength;

    private FileDescriptorSource(@NonNull FileDescriptor fd, int offset, int length) {
        mFD = fd;
        mOffset = offset;
        mLength = length;
    }

    @NonNull
    @Override
    public FileDescriptor source() {
        return mFD;
    }

    public int getOffset() {
        return mOffset;
    }

    public int getLength() {
        return mLength;
    }

    @NonNull
    @Override
    public InputStream toStream() throws IOException {
        return new FileInputStream(mFD);
    }
}
