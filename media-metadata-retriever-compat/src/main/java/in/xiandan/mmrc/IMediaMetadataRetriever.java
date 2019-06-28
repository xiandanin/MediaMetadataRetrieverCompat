package in.xiandan.mmrc;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.IOException;

import in.xiandan.mmrc.datasource.DataSource;

/**
 * @author dengyuhan
 * created 2019-06-24 16:53
 */
public interface IMediaMetadataRetriever {

    void setDataSource(@NonNull DataSource source) throws IOException;

    @Nullable
    Bitmap getFrameAtTime();

    /**
     * 获取指定时间帧
     *
     * @param timeUs 微秒
     * @param option {@link MediaRetriever.Key#OPTION_PREVIOUS_SYNC} 早于timeUs的同步帧;
     *               {@link MediaRetriever.Key#OPTION_NEXT_SYNC} 晚于timeUs的同步帧;
     *               {@link MediaRetriever.Key#OPTION_CLOSEST_SYNC} 最接近timeUs的同步帧;
     *               {@link MediaRetriever.Key#OPTION_CLOSEST} 最接近timeUs的帧，但可能不是同步帧(性能开销较大).
     * @return 时间帧
     */
    @Nullable
    Bitmap getFrameAtTime(long timeUs, int option);


    /**
     * 获取指定时间帧并缩放裁剪
     *
     * @param timeUs    微秒
     * @param option
     * @param dstWidth  输出宽度
     * @param dstHeight 输出高度
     * @return
     */
    @Nullable
    Bitmap getScaledFrameAtTime(long timeUs, int option, int dstWidth, int dstHeight);

    @Nullable
    byte[] getEmbeddedPicture();

    /**
     * 获取文件元数据
     *
     * @param keyCode
     * @return
     */
    @Nullable
    String extractMetadata(String keyCode);

    void release();
}
