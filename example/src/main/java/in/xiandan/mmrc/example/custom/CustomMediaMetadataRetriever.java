package in.xiandan.mmrc.example.custom;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.IOException;

import in.xiandan.mmrc.IMediaMetadataRetriever;
import in.xiandan.mmrc.datasource.DataSource;

/**
 * @author dengyuhan
 * created 2019-07-02 18:09
 */
public class CustomMediaMetadataRetriever implements IMediaMetadataRetriever {
    @Override
    public void setDataSource(@NonNull DataSource source) throws IOException {

    }

    @Nullable
    @Override
    public Bitmap getFrameAtTime() {
        return null;
    }

    @Nullable
    @Override
    public Bitmap getFrameAtTime(long timeUs, int option) {
        return null;
    }

    @Nullable
    @Override
    public Bitmap getScaledFrameAtTime(long timeUs, int option, int dstWidth, int dstHeight) {
        return null;
    }

    @Nullable
    @Override
    public byte[] getEmbeddedPicture() {
        return new byte[0];
    }

    @Nullable
    @Override
    public String extractMetadata(String keyCode) {
        return null;
    }

    @Override
    public void release() {

    }
}
