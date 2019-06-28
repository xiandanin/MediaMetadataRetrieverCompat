package in.xiandan.mmrc;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import in.xiandan.mmrc.datasource.DataSource;
import in.xiandan.mmrc.fileformat.FileFormat;
import in.xiandan.mmrc.fileformat.ImageFormatChecker;
import in.xiandan.mmrc.fileformat.UnknownFileFormatException;
import in.xiandan.mmrc.utils.Closeables;

/**
 * @author dengyuhan
 * created 2019-06-24 16:43
 */
public final class MediaMetadataRetrieverCompat {
    private IMediaMetadataRetriever mImpl;

    private MediaMetadataRetrieverCompat() {
    }

    public static MediaMetadataRetrieverCompat create(@NonNull DataSource source) throws IOException, UnknownFileFormatException {
        final MediaMetadataRetrieverCompat retriever = new MediaMetadataRetrieverCompat();
        final InputStream stream = source.toStream();
        final FileFormat fileFormat = ImageFormatChecker.getImageFormat(stream);
        Closeables.closeQuietly(stream);
        final List<MediaMetadataRetrieverFactory> factories = MediaRetriever.Config.get().getFactories();
        for (MediaMetadataRetrieverFactory factory : factories) {
            if (factory.supportsFileFormat(fileFormat)) {
                retriever.mImpl = factory.create();
                break;
            }
        }
        //如果没有支持的Retriever
        if (retriever.mImpl == null) {
            throw new UnknownFileFormatException("unsupported file format");
        } else {
            retriever.mImpl.setDataSource(source);
        }
        return retriever;
    }

    public Bitmap getFrameAtTime() {
        return mImpl.getFrameAtTime();
    }

    public Bitmap getFrameAtTime(long timeUs, int option) {
        return mImpl.getFrameAtTime(timeUs, option);
    }

    public Bitmap getScaledFrameAtTime(long timeUs, int option, int dstWidth, int dstHeight) {
        return mImpl.getScaledFrameAtTime(timeUs, option, dstWidth, dstHeight);
    }

    public byte[] getEmbeddedPicture() {
        return mImpl.getEmbeddedPicture();
    }

    public String extractMetadata(String keyCode) {
        return mImpl.extractMetadata(keyCode);
    }

    public void release() {
        mImpl.release();
        mImpl = null;
    }

    public IMediaMetadataRetriever getRetriever() {
        return mImpl;
    }


}
