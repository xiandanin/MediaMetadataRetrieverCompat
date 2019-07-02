package in.xiandan.mmrc.datasource;

import android.content.Context;
import android.net.Uri;

import java.io.File;

/**
 * @author dengyuhan
 * created 2019-07-01 17:42
 */
public final class URLSource {
    private URLSource() {
    }

    public static DataSource create(Context context, String url) {
        if (url.startsWith("http")) {
            return new HTTPSource(url, null);
        } else if (new File(url).exists()) {
            return new FileSource(url);
        } else {
            return new UriSource(context, Uri.parse(url));
        }
    }
}
