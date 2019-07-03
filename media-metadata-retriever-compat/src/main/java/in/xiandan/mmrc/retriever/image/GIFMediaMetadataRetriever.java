package in.xiandan.mmrc.retriever.image;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Movie;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.IOException;

import in.xiandan.mmrc.MediaMetadataKey;
import in.xiandan.mmrc.datasource.DataSource;
import in.xiandan.mmrc.utils.BitmapProcessor;

/**
 * 支持GIF动画的Retriever
 *
 * @author dengyuhan
 * created 2019-06-24 16:58
 */
public class GIFMediaMetadataRetriever extends BitmapMediaMetadataRetriever {
    private Movie mMovie;

    @Override
    public void setDataSource(@NonNull DataSource source) throws IOException {
        super.setDataSource(source);
        mMovie = Movie.decodeStream(getStream(false));
    }

    @Nullable
    @Override
    public Bitmap getFrameAtTime() {
        return this.getFrameAtTime(0, MediaMetadataKey.OPTION_CLOSEST_SYNC);
    }

    @Nullable
    @Override
    public Bitmap getFrameAtTime(long millis, int option) {
        mMovie.setTime((int) millis);
        Bitmap bitmap = Bitmap.createBitmap(mMovie.width(), mMovie.height(), Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(bitmap);
        mMovie.draw(canvas, 0, 0,new Paint(Paint.ANTI_ALIAS_FLAG));
        return bitmap;
    }

    @Nullable
    @Override
    public Bitmap getScaledFrameAtTime(long millis, int option, int dstWidth, int dstHeight) {
        final float scale = BitmapProcessor.calculateScale(mMovie.width(), mMovie.height(), dstWidth, dstHeight);
        return BitmapProcessor.floorScale(this.getFrameAtTime(millis, option), scale);
    }


    @Override
    public String extractMetadata(String keyCode) {
        if (MediaMetadataKey.DURATION.equals(keyCode)) {
            return String.valueOf(mMovie.duration());
        } else {
            return super.extractMetadata(keyCode);
        }
    }

    @Override
    protected int getWidth() {
        return mMovie.width();
    }

    @Override
    protected int getHeight() {
        return mMovie.height();
    }

    @Override
    public void release() {
        super.release();
        mMovie = null;
    }
}
