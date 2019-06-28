package in.xiandan.mmrc.datasource;

import android.support.annotation.NonNull;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author dengyuhan
 * created 2019-06-28 16:43
 */
public interface DataSource<Input> {

    @NonNull
    Input source();

    @NonNull
    InputStream toStream() throws IOException;

}
