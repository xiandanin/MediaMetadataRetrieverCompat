package in.xiandan.mmrc.datasource;

import android.support.annotation.NonNull;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author dengyuhan
 * created 2019-06-28 17:08
 */
public class StreamSource implements DataSource<InputStream> {
    private InputStream mStream;

    public StreamSource(@NonNull InputStream stream) {
        this.mStream = stream;
    }


    @NonNull
    @Override
    public InputStream source() {
        return mStream;
    }

    @NonNull
    @Override
    public InputStream toStream() throws IOException {
        return mStream;
    }
}
