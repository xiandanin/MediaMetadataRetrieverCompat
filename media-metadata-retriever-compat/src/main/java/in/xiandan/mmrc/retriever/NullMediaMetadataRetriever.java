package in.xiandan.mmrc.retriever;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.IOException;
import java.util.Locale;

import in.xiandan.mmrc.IMediaMetadataRetriever;
import in.xiandan.mmrc.datasource.DataSource;
import in.xiandan.mmrc.fileformat.UnknownFileFormatException;

/**
 * @author dengyuhan
 * created 2019-07-02 16:09
 */
public class NullMediaMetadataRetriever implements IMediaMetadataRetriever {
    @Override
    public void setDataSource(@NonNull DataSource source) throws IOException {
        throwException();
    }

    @Nullable
    @Override
    public Bitmap getFrameAtTime() {
        throwException();
        return null;
    }

    @Nullable
    @Override
    public Bitmap getFrameAtTime(long millis, int option) {
        throwException();
        return null;
    }

    @Nullable
    @Override
    public Bitmap getScaledFrameAtTime(long millis, int option, int dstWidth, int dstHeight) {
        throwException();
        return null;
    }


    @Nullable
    @Override
    public byte[] getEmbeddedPicture() {
        throwException();
        return null;
    }

    @Nullable
    @Override
    public String extractMetadata(String keyCode) {
        throwException();
        return null;
    }

    @Override
    public void release() {
        throwException();
    }

    private void throwException() throws UnknownFileFormatException {
        if (Locale.getDefault().getLanguage().equals(Locale.CHINESE.getLanguage())) {
            throw new UnknownFileFormatException("此文件没有可用的检索器");
        } else {
            throw new UnknownFileFormatException("there are no retriever available for this file.");
        }
    }
}
