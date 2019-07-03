package in.xiandan.mmrc.datasource;

import android.content.Context;
import android.content.res.AssetManager;
import android.support.annotation.NonNull;

import java.io.IOException;
import java.io.InputStream;

/**
 * Asset数据源
 *
 * @author dengyuhan
 * created 2019-06-28 16:45
 */
public class AssetSource implements DataSource<String> {
    private AssetManager mAssetManager;
    private String mAssetPath;

    public AssetSource(@NonNull Context context, @NonNull String assetPath) {
        this.mAssetManager = context.getAssets();
        this.mAssetPath = assetPath;
    }

    @NonNull
    @Override
    public String source() {
        return mAssetPath;
    }

    @NonNull
    @Override
    public InputStream toStream() throws IOException {
        return mAssetManager.open(mAssetPath);
    }
}
