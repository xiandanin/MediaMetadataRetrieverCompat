package com.dyhdyh.compat.mmrc.impl;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Movie;
import android.media.ExifInterface;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;

import com.dyhdyh.compat.mmrc.IMediaMetadataRetriever;
import com.dyhdyh.compat.mmrc.image.DefaultImageHeaderParser;
import com.dyhdyh.compat.mmrc.image.ImageType;
import com.dyhdyh.compat.mmrc.transform.BitmapRotateTransform;
import com.dyhdyh.compat.mmrc.transform.MetadataTransform;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;

/**
 * 图片引擎
 *
 * @author dengyuhan
 * created 2018/10/15 19:36
 */
public class ImageMediaMetadataRetrieverImpl implements IMediaMetadataRetriever {

    public static final int METADATA_KEY_WIDTH = MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH;
    public static final int METADATA_KEY_HEIGHT = MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT;
    public static final int METADATA_KEY_ROTATION = MediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION;
    public static final int METADATA_KEY_DURATION = MediaMetadataRetriever.METADATA_KEY_DURATION;

    private DefaultImageHeaderParser mImageHeaderParser;

    private File mInputFile;
    private ImageType mImageType;
    private Movie mMovie;
    private BitmapFactory.Options mOptions;

    public ImageMediaMetadataRetrieverImpl() {
        mImageHeaderParser = new DefaultImageHeaderParser();
    }

    @Override
    public void setDataSource(File inputFile) throws FileNotFoundException {
        mInputFile = inputFile;
        mImageType = null;
        mMovie = null;
        mOptions = null;
    }

    @Override
    public void setDataSource(String uri, Map<String, String> headers) {
        try {
            final URL url = new URL(uri);
            final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            Iterator<Map.Entry<String, String>> entries = headers.entrySet().iterator();
            while (entries.hasNext()) {
                Map.Entry<String, String> entry = entries.next();
                connection.addRequestProperty(entry.getKey(), entry.getValue());
            }
            connection.connect();

            String fileUrl = url.getFile();
            String filename = fileUrl.substring(fileUrl.lastIndexOf(File.separator) + File.separator.length(), fileUrl.length());
            File outputFile = new File(Environment.getDownloadCacheDirectory(), filename);

            BufferedInputStream ins = new BufferedInputStream(connection.getInputStream());
            inputStream2File(ins, outputFile);

            setDataSource(outputFile);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setDataSource(FileDescriptor fd, long offset, long length) {

    }

    @Override
    public void setDataSource(FileDescriptor fd) {
        try {
            FileInputStream in = new FileInputStream(fd);
            File outputFile = new File(Environment.getDownloadCacheDirectory(), String.valueOf(System.currentTimeMillis()));
            inputStream2File(in, outputFile);

            setDataSource(outputFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setDataSource(Context context, Uri uri) {
        try {
            String[] projection = {MediaStore.Images.Media.DATA};
            Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
            if (cursor.moveToFirst()) {
                String filepath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                setDataSource(new File(filepath));
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Bitmap getFrameAtTime() {
        return getScaledFrameAtTime(0, -1, -1);
    }

    @Override
    public Bitmap getFrameAtTime(long timeUs, int option) {
        return getScaledFrameAtTime(timeUs, option, -1, -1);
    }

    @Override
    public Bitmap getScaledFrameAtTime(long timeUs, int width, int height) {
        return getScaledFrameAtTime(timeUs, Bitmap.Config.ARGB_8888.ordinal(), width, height);
    }

    @Override
    public Bitmap getScaledFrameAtTime(long timeUs, int option, int width, int height) {
        if (ImageType.GIF == getImageType()) {
            final Movie movie = getMovie();
            movie.setTime((int) (timeUs * 1000));
            Bitmap bitmap = Bitmap.createBitmap(movie.width(), movie.height(), Bitmap.Config.ARGB_8888);
            final Canvas canvas = new Canvas(bitmap);
            movie.draw(canvas, 0, 0);
            return bitmap;
        } else {
            //原图
            Bitmap atTime = BitmapFactory.decodeFile(mInputFile.getAbsolutePath());
            if (atTime == null) {
                return null;
            }
            //如果需要缩放
            if (width > 0 && height > 0) {
                atTime = Bitmap.createScaledBitmap(atTime, width, height, true);
            }
            //如果需要旋转
            if (ImageType.JPEG == getImageType()) {
                final int rotation = getJPGRotate();
                if (rotation != 0) {
                    return BitmapRotateTransform.transform(atTime, rotation);
                }
            }
            return atTime;
        }
    }

    @Override
    public byte[] getEmbeddedPicture() {
        try {
            final FileInputStream inputStream = new FileInputStream(mInputFile);
            ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
            byte[] buff = new byte[1024];
            int buffer;
            while ((buffer = inputStream.read(buff, 0, 100)) > 0) {
                swapStream.write(buff, 0, buffer);
            }
            return swapStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String extractMetadata(int keyCode) {
        try {
            String keyCodeString = MetadataTransform.transform(getClass(), keyCode);
            if (TextUtils.isEmpty(keyCodeString)) {
                return null;
            }
            int keyCodeInt = Integer.parseInt(keyCodeString);
            if (METADATA_KEY_WIDTH == keyCodeInt) {
                return String.valueOf(getOptions().outWidth);
            } else if (METADATA_KEY_HEIGHT == keyCodeInt) {
                return String.valueOf(getOptions().outHeight);
            } else if (METADATA_KEY_DURATION == keyCodeInt && isImageType(ImageType.GIF, ImageType.WEBP_A, ImageType.PNG_A)) {
                //gif/webp/apng才有时长
                return String.valueOf(getMovie().duration());
            } else if (METADATA_KEY_ROTATION == keyCodeInt && isImageType(ImageType.JPEG)) {
                //jpg才有Exif
                return String.valueOf(getJPGRotate());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取jpg旋转角度
     *
     * @return
     */
    private int getJPGRotate() {
        try {
            final int orientation = mImageHeaderParser.getOrientation(new FileInputStream(mInputFile));
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    return 90;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    return 180;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    return 270;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public void release() {

    }

    private Movie getMovie() {
        try {
            if (mMovie == null) {
                mMovie = Movie.decodeStream(new FileInputStream(mInputFile));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return mMovie;
    }

    /**
     * 获取输入源的图片类型
     *
     * @return
     */
    public ImageType getImageType() {
        try {
            if (mImageType == null) {
                return mImageHeaderParser.getType(new FileInputStream(mInputFile));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (mImageType == null) {
            mImageType = ImageType.UNKNOWN;
        }
        return mImageType;
    }

    /**
     * 获取宽高信息
     *
     * @return
     */
    private BitmapFactory.Options getOptions() {
        if (mOptions == null) {
            mOptions = new BitmapFactory.Options();
            mOptions.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(mInputFile.getAbsolutePath(), mOptions);
            return mOptions;
        }
        return mOptions;
    }

    /**
     * 判断输入源是否传入的type其中一种
     *
     * @param targetImageType
     * @return
     */
    public boolean isImageType(ImageType... targetImageType) {
        final ImageType imageType = getImageType();
        for (ImageType type : targetImageType) {
            if (imageType == type) {
                return true;
            }
        }
        return false;
    }


    private void inputStream2File(InputStream is, File outputFile) throws IOException {
        OutputStream os = new FileOutputStream(outputFile);
        int bytesRead;
        byte[] buffer = new byte[8192];
        while ((bytesRead = is.read(buffer, 0, 8192)) != -1) {
            os.write(buffer, 0, bytesRead);
        }
        os.close();
        is.close();
    }

}
