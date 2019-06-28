package in.xiandan.mmrc.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * author  dengyuhan
 * created 2019-06-26 17:16
 */
public class BitmapTransformation {

    @NonNull
    public static Bitmap rotate(@NonNull Bitmap bitmap, float rotate) {
        if (rotate != 0f) {
            Matrix matrix = new Matrix();
            matrix.postRotate(rotate);
            return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        } else {
            return bitmap;
        }
    }

    @NonNull
    public static Bitmap scale(@NonNull Bitmap bitmap, float scale) {
        if (scale != 1f) {
            Matrix matrix = new Matrix();
            matrix.setScale(scale, scale);
            return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        } else {
            return bitmap;
        }
    }

    /**
     * reference glide centerCrop
     *
     * @param bitmap    源bitmap
     * @param dstWidth  目标宽度
     * @param dstHeight 目标高度
     * @return 缩放裁剪后的bitmap
     * @see <a href="https://github.com/bumptech/glide/blob/master/library/src/main/java/com/bumptech/glide/load/resource/bitmap/TransformationUtils.java#L111">TransformationUtils</a>
     */
    @Nullable
    public static Bitmap centerCrop(@Nullable Bitmap bitmap, int dstWidth, int dstHeight) {
        if (bitmap == null || (bitmap.getWidth() == dstWidth && bitmap.getHeight() == dstHeight)) {
            return bitmap;
        }
        // From ImageView/Bitmap.createScaledBitmap.
        final float scale;
        final float dx;
        final float dy;
        Matrix matrix = new Matrix();
        if (bitmap.getWidth() * dstHeight > dstWidth * bitmap.getHeight()) {
            scale = (float) dstHeight / (float) bitmap.getHeight();
            dx = (dstWidth - bitmap.getWidth() * scale) * 0.5f;
            dy = 0;
        } else {
            scale = (float) dstWidth / (float) bitmap.getWidth();
            dx = 0;
            dy = (dstHeight - bitmap.getHeight() * scale) * 0.5f;
        }

        matrix.setScale(scale, scale);
        matrix.postTranslate((int) (dx + 0.5f), (int) (dy + 0.5f));

        final Bitmap targetBitmap = Bitmap.createBitmap(dstWidth, dstHeight, bitmap.getConfig());
        Canvas canvas = new Canvas(targetBitmap);
        canvas.drawBitmap(bitmap, matrix, new Paint(Paint.ANTI_ALIAS_FLAG));
        return targetBitmap;
    }

    public static int calculateSampleSize(int srcWidth, int srcHeight, int outWidth, int outHeight) {
        return (int) Math.ceil((double) Math.min(srcWidth, srcHeight) / Math.min(outWidth, outHeight));
    }

}
