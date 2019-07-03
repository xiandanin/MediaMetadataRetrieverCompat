package in.xiandan.mmrc;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import in.xiandan.mmrc.datasource.DataSource;
import in.xiandan.mmrc.fileformat.FileFormat;
import in.xiandan.mmrc.fileformat.FileFormatChecker;
import in.xiandan.mmrc.retriever.NullMediaMetadataRetriever;
import in.xiandan.mmrc.utils.BitmapProcessor;
import in.xiandan.mmrc.utils.Closeables;

/**
 * @author dengyuhan
 * created 2019-06-24 16:43
 */
public final class MediaMetadataRetrieverCompat {
    private IMediaMetadataRetriever mImpl;

    /**
     * 设置数据源
     *
     * @param source
     */
    public void setDataSource(@NonNull DataSource source) {
        try {
            this.setDataSourceOrThrow(source);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置数据源或抛出异常
     *
     * @param source 数据源
     * @throws IOException
     */
    public void setDataSourceOrThrow(@NonNull DataSource source) throws IOException {
        this.setDataSourceOrThrow(source, null);
    }

    /**
     * 设置数据源或抛出异常 并指定检索器
     *
     * @param source
     * @param cls
     * @throws IOException
     */
    public void setDataSourceOrThrow(@NonNull DataSource source, @Nullable Class<? extends MediaMetadataRetrieverFactory> cls) throws IOException {
        final List<MediaMetadataRetrieverFactory> factories = MediaMetadataResource.globalConfig().getFactories();
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
            //不指定检索器 就根据文件创建检索器
            final InputStream stream = source.toStream();
            final FileFormat fileFormat = FileFormatChecker.getFileFormat(stream);
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
        } else {
            //如果有合适的Factory 就直接创建
            this.mImpl = applyFactory.create();
        }
        this.mImpl.setDataSource(source);
    }

    public Bitmap getFrameAtTime() {
        return mImpl.getFrameAtTime();
    }

    public Bitmap getFrameAtTime(long millis, int option) {
        return mImpl.getFrameAtTime(millis, option);
    }

    /**
     * 获取指定毫秒的缩略图，并基于指定宽高缩放，输出的Bitmap不一定是指定宽高
     *
     * @param millis
     * @param option
     * @param dstWidth
     * @param dstHeight
     * @return
     */
    public Bitmap getScaledFrameAtTime(long millis, int option, int dstWidth, int dstHeight) {
        return mImpl.getScaledFrameAtTime(millis, option, dstWidth, dstHeight);
    }

    /**
     * 获取指定毫秒的缩略图，并按指定宽高缩放裁剪，输出的Bitmap一定是指定宽高
     *
     * @param millis
     * @param option
     * @param dstWidth
     * @param dstHeight
     * @return
     */
    public Bitmap getCenterCropFrameAtTime(long millis, int option, int dstWidth, int dstHeight) {
        return BitmapProcessor.centerCrop(this.getScaledFrameAtTime(millis, option, dstWidth, dstHeight), dstWidth, dstHeight);
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
