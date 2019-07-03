package in.xiandan.mmrc.example;

import android.content.Context;
import android.net.Uri;

import java.io.File;

import in.xiandan.mmrc.datasource.DataSource;
import in.xiandan.mmrc.datasource.FileSource;
import in.xiandan.mmrc.datasource.OkHttpSource;
import in.xiandan.mmrc.datasource.UriSource;

/**
 * @author dengyuhan
 * created 2019-07-01 17:42
 */
public final class URLSource {
    private URLSource() {
    }

    public static DataSource create(Context context, String url) {
        if (url.startsWith("http")) {
            return new OkHttpSource(url, null);
        } else if (new File(url).exists()) {
            return new FileSource(url);
        } else {
            return new UriSource(context, Uri.parse(url));
        }
    }
}
