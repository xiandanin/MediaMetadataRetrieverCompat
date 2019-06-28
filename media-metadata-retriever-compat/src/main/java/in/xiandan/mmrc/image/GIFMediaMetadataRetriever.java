package in.xiandan.mmrc.image;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Movie;
import android.support.annotation.NonNull;

import java.io.IOException;

import in.xiandan.mmrc.MediaRetriever;
import in.xiandan.mmrc.datasource.DataSource;
import in.xiandan.mmrc.utils.BitmapTransformation;
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

    @Override
    public Bitmap getFrameAtTime(long timeUs, int option) {
        mMovie.setTime((int) (timeUs / 1000));
        Bitmap bitmap = Bitmap.createBitmap(mMovie.width(), mMovie.height(), MediaRetriever.Config.get().getConfig());
        final Canvas canvas = new Canvas(bitmap);
        mMovie.draw(canvas, 0, 0);
        return bitmap;
    }

    @Override
    public Bitmap getScaledFrameAtTime(long timeUs, int option, int dstWidth, int dstHeight) {
        return BitmapTransformation.centerCrop(getFrameAtTime(timeUs, option), dstWidth, dstHeight);
    }

    @Override
    public String extractMetadata(String keyCode) {
        if (MediaRetriever.Key.METADATA_KEY_DURATION.equals(keyCode)) {
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
