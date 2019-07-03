package in.xiandan.mmrc.datasource;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Set;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * okhttp数据源 - 仅图片会采用okhttp
 * @author dengyuhan
 * created 2019-07-03 13:50
 */
public class OkHttpSource extends HTTPSource {
    private OkHttpClient mOkHttpClient;


    public OkHttpSource(@NonNull String url, @Nullable Map<String, String> headers) {
        this(new OkHttpClient.Builder().build(), url, headers);
    }

    public OkHttpSource(@NonNull OkHttpClient client, @NonNull String url, @Nullable Map<String, String> headers) {
        super(url, headers);
        this.mOkHttpClient = client;
    }

    @NonNull
    @Override
    public InputStream toStream() throws IOException {
        final Request.Builder builder = new Request.Builder().get().url(super.source());

        final Map<String, String> headers = super.getHeaders();
        final Set<Map.Entry<String, String>> entries = headers.entrySet();
        for (Map.Entry<String, String> entry : entries) {
            builder.header(entry.getKey(), entry.getValue());
        }
        return mOkHttpClient.newCall(builder.build()).execute().body().byteStream();
    }
}
