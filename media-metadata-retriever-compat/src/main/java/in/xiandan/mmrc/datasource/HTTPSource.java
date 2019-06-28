package in.xiandan.mmrc.datasource;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.Map;

/**
 * @author dengyuhan
 * created 2019-06-28 16:45
 */
public class HTTPSource implements DataSource<String> {
    private String mUrl;
    private Map<String, String> mHeaders;

    public HTTPSource(@NonNull String url, @Nullable Map<String, String> headers) {
        this.mUrl = url;
        this.mHeaders = headers;
    }

    @NonNull
    @Override
    public String source() {
        return mUrl;
    }

    public Map<String, String> getHeaders() {
        return mHeaders;
    }

    @NonNull
    @Override
    public InputStream toStream() throws IOException {
        if (TextUtils.isEmpty(mUrl)) {
            throw new UnknownHostException(mUrl);
        }
        final URL url = new URL(mUrl);
        final HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        Iterator<Map.Entry<String, String>> entries = mHeaders.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry<String, String> entry = entries.next();
            connection.addRequestProperty(entry.getKey(), entry.getValue());
        }
        connection.connect();
        return connection.getInputStream();
    }
}
