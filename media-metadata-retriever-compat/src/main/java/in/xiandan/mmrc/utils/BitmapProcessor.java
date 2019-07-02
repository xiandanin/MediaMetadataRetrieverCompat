package in.xiandan.mmrc.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.support.annotation.Nullable;

/**
 * author  dengyuhan
 * created 2019-06-26 17:16
 */
public class BitmapProcessor {

    @Nullable
    public static Bitmap rotate(@Nullable Bitmap bitmap, float rotate) {
        if (bitmap != null && rotate != 0f) {
            Matrix matrix = new Matrix();
            matrix.postRotate(rotate);
            return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        } else {
            return bitmap;
        }
    }

    @Nullable
    public static Bitmap scale(@Nullable Bitmap bitmap, float scale) {
        if (bitmap != null && scale != 1f) {
            Matrix matrix = new Matrix();
            matrix.setScale(scale, scale);
            return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        } else {
            return bitmap;
        }
    }

    @Nullable
    public static Bitmap centerCrop(@Nullable Bitmap bitmap, Bitmap.Config config, int dstWidth, int dstHeight) {
        return rotateCenterCrop(bitmap, config, 0, dstWidth, dstHeight);
    }

    /**
     * 旋转并缩放裁剪
     * reference glide centerCrop
     *
     * @param rotate    旋转角度
     * @param bitmap    源bitmap
     * @param dstWidth  目标宽度
     * @param dstHeight 目标高度
     * @return 缩放裁剪后的bitmap
     * @see <a href="https://github.com/bumptech/glide/blob/master/library/src/main/java/com/bumptech/glide/load/resource/bitmap/TransformationUtils.java#L111">TransformationUtils</a>
     */
    @Nullable
    public static Bitmap rotateCenterCrop(@Nullable Bitmap bitmap, Bitmap.Config config, int rotate, int dstWidth, int dstHeight) {
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

        matrix.postScale(scale, scale);

        int translateX = (int) (dx + 0.5f);
        int translateY = (int) (dy + 0.5f);
        matrix.postTranslate(translateX, translateY);
        if (rotate != 0) {
            float px = translateX + scale * bitmap.getWidth() / 2;
            float py = translateY + scale * bitmap.getHeight() / 2;
            matrix.postRotate(rotate, px, py);
        }

        final Bitmap targetBitmap = Bitmap.createBitmap(dstWidth, dstHeight, config);
        Canvas canvas = new Canvas(targetBitmap);
        canvas.drawBitmap(bitmap, matrix, new Paint(Paint.ANTI_ALIAS_FLAG));
        return targetBitmap;
    }

    public static int calculateSampleSize(int srcWidth, int srcHeight, int outWidth, int outHeight) {
        return (int) Math.ceil((double) Math.min(srcWidth, srcHeight) / Math.min(outWidth, outHeight));
    }

    public static float calculateScale(int srcWidth, int srcHeight, int outWidth, int outHeight) {
        return (float) Math.min(outWidth, outHeight) / Math.min(srcWidth, srcHeight);
    }

    public static int[] calculateScaleSize(int srcWidth, int srcHeight, int outWidth, int outHeight) {
        final float scale = calculateScale(srcWidth, srcHeight, outWidth, outHeight);
        return new int[]{(int) (srcWidth * scale), (int) (srcHeight * scale)};
    }

}
