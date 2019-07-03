package in.xiandan.mmrc.example.custom;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVGParseException;

import java.io.IOException;

import in.xiandan.mmrc.IMediaMetadataRetriever;
import in.xiandan.mmrc.MediaMetadataKey;
import in.xiandan.mmrc.datasource.DataSource;

/**
 * @author dengyuhan
 * created 2019-07-02 18:09
 */
public class SVGMediaMetadataRetriever implements IMediaMetadataRetriever {
    private SVG mSVG;
    private final int mDefaultSize = (int) (200 * Resources.getSystem().getDisplayMetrics().density);

    @Override
    public void setDataSource(@NonNull DataSource source) throws IOException {
        try {
            mSVG = SVG.getFromInputStream(source.toStream());
        } catch (SVGParseException e) {
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public Bitmap getFrameAtTime() {
        return this.getFrameAtTime(0, MediaMetadataKey.OPTION_CLOSEST_SYNC);
    }

    @Nullable
    @Override
    public Bitmap getFrameAtTime(long millis, int option) {
        return this.getScaledFrameAtTime(millis, option, mDefaultSize, mDefaultSize);
    }

    @Nullable
    @Override
    public Bitmap getScaledFrameAtTime(long millis, int option, int dstWidth, int dstHeight) {
        final Bitmap targetBitmap = Bitmap.createBitmap(dstWidth, dstHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(targetBitmap);
        mSVG.setDocumentWidth(dstWidth);
        mSVG.setDocumentHeight(dstHeight);
        mSVG.renderToCanvas(canvas);
        return targetBitmap;
    }

    @Nullable
    @Override
    public byte[] getEmbeddedPicture() {
        return null;
    }

    @Nullable
    @Override
    public String extractMetadata(String keyCode) {
        if (MediaMetadataKey.WIDTH.equals(keyCode)) {
            return String.valueOf(mSVG.getDocumentWidth());
        } else if (MediaMetadataKey.HEIGHT.equals(keyCode)) {
            return String.valueOf(mSVG.getDocumentHeight());
        } else if (CustomKey.METADATA_KEY_SVG_VERSION.equals(keyCode)) {
            return mSVG.getDocumentSVGVersion();
        } else if (CustomKey.METADATA_KEY_SVG_TITLE.equals(keyCode)) {
            return mSVG.getDocumentTitle();
        } else if (CustomKey.METADATA_KEY_ASPECT_RATIO.equals(keyCode)) {
            return String.valueOf(mSVG.getDocumentAspectRatio());
        } else if (CustomKey.METADATA_KEY_DESCRIPTION.equals(keyCode)) {
            return mSVG.getDocumentDescription();
        } else if (CustomKey.METADATA_KEY_RENDER_DPI.equals(keyCode)) {
            return String.valueOf(mSVG.getRenderDPI());
        }
        return null;
    }

    @Override
    public void release() {

    }
}
