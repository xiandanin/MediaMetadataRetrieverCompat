package in.xiandan.mmrc;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import in.xiandan.mmrc.datasource.DataSource;
import in.xiandan.mmrc.fileformat.FileFormat;
import in.xiandan.mmrc.fileformat.ImageFormatChecker;
import in.xiandan.mmrc.fileformat.UnknownFileFormatException;
import in.xiandan.mmrc.retriever.NullMediaMetadataRetriever;
import in.xiandan.mmrc.utils.BitmapProcessor;
import in.xiandan.mmrc.utils.Closeables;

/**
 * @author dengyuhan
 * created 2019-06-24 16:43
 */
public final class MediaMetadataRetrieverCompat {
    private IMediaMetadataRetriever mImpl;

    public void setDataSource(@NonNull DataSource source) {
        try {
            this.setDataSourceOrThrow(source);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 自动根据文件 创建MediaMetadataRetrieverCompat
     *
     * @param source 数据源
     * @return
     * @throws IOException
     * @throws UnknownFileFormatException
     */
    public void setDataSourceOrThrow(@NonNull DataSource source) throws IOException {
        this.setDataSourceOrThrow(source, null);
    }


    /**
     * 创建MediaMetadataRetrieverCompat
     *
     * @param source 数据源
     * @param cls    指定的检索器，null则自动选择
     * @return
     * @throws IOException
     * @throws UnknownFileFormatException
     */
    public void setDataSourceOrThrow(@NonNull DataSource source, @Nullable Class<? extends MediaMetadataRetrieverFactory> cls) throws IOException {
        final List<MediaMetadataRetrieverFactory> factories = MediaRetrieverResource.Config.get().getFactories();
        MediaMetadataRetrieverFactory applyFactory = null;
        if (cls != null) {
            //如果指定了先检查是否有此类型的检索器
            for (MediaMetadataRetrieverFactory factory : factories) {
                if (cls == factory.getClass()) {
                    applyFactory = factory;
                    break;
                }
            }
        }
        //没有指定或没有找到指定的就自动根据格式创建检索器
        if (applyFactory == null) {
            //不指定检索器 自动根据格式创建检索器
            final InputStream stream = source.toStream();
            final FileFormat fileFormat = ImageFormatChecker.getImageFormat(stream);
            Closeables.closeQuietly(stream);
            for (MediaMetadataRetrieverFactory factory : factories) {
                if (factory.supportsFileFormat(fileFormat)) {
                    applyFactory = factory;
                    break;
                }
            }
        }

        //如果没有支持的Factory
        if (applyFactory == null) {
            this.mImpl = new NullMediaMetadataRetriever();
            throw new UnknownFileFormatException("there are no retriever available for this file.");
        } else {
            //如果有合适的Factory 就直接创建
            this.mImpl = applyFactory.create();
        }
        this.mImpl.setDataSource(source);
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

    public Bitmap getCenterCropFrameAtTime(long timeUs, int option, int dstWidth, int dstHeight) {
        return BitmapProcessor.centerCrop(this.getScaledFrameAtTime(timeUs, option, dstWidth, dstHeight), MediaRetrieverResource.Config.get().getBitmapConfig(), dstWidth, dstHeight);
    }

    public byte[] getEmbeddedPicture() {
        return mImpl.getEmbeddedPicture();
    }

    public String extractMetadata(String keyCode) {
        return mImpl.extractMetadata(keyCode);
    }

    public int extractMetadataInt(String keyCode, int defaultValue) {
        try {
            return Integer.parseInt(this.extractMetadata(keyCode));
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public float extractMetadataFloat(String keyCode, float defaultValue) {
        try {
            return Float.parseFloat(this.extractMetadata(keyCode));
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public long extractMetadataLong(String keyCode, long defaultValue) {
        try {
            return Long.parseLong(this.extractMetadata(keyCode));
        } catch (Exception e) {
            return defaultValue;
        }
    }


    public void release() {
        mImpl.release();
    }

    @NonNull
    public IMediaMetadataRetriever getRetriever() {
        return mImpl;
    }


}
