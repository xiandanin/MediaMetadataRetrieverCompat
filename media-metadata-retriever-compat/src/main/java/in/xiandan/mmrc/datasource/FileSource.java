package in.xiandan.mmrc.datasource;

import android.support.annotation.NonNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author dengyuhan
 * created 2019-06-28 16:45
 */
public class FileSource implements DataSource<File> {
    private File mFile;

    public FileSource(@NonNull String path) {
        this.mFile = new File(path);
    }

    public FileSource(@NonNull File file) {
        this.mFile = file;
    }

    @NonNull
    @Override
    public File source() {
        return mFile;
    }

    @NonNull
    @Override
    public InputStream toStream() throws IOException {
        return new FileInputStream(mFile);
    }
}
