package in.xiandan.mmrc.datasource;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author dengyuhan
 * created 2019-06-28 17:08
 */
public class UriSource implements DataSource<Uri> {
    private final Context mContext;
    private final Uri mUri;

    public UriSource(@NonNull Context context, @NonNull Uri uri) {
        this.mContext = context;
        this.mUri = uri;
    }

    @NonNull
    @Override
    public Uri source() {
        return mUri;
    }

    public Context getContext() {
        return mContext;
    }

    @NonNull
    @Override
    public InputStream toStream() throws IOException {
        return mContext.getContentResolver().openInputStream(mUri);
    }
}
