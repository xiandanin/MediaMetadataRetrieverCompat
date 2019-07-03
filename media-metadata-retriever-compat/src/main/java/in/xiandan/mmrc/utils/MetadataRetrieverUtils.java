package in.xiandan.mmrc.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

/**
 * @author dengyuhan
 * created 2019-07-02 15:11
 */
public class MetadataRetrieverUtils {
    public static long parseTime(String datePattern, String text) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(datePattern, Locale.getDefault());
            dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
            return dateFormat.parse(text).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static String parseTimeString(String datePattern, String text) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(datePattern, Locale.getDefault());
            dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
            return String.valueOf(dateFormat.parse(text).getTime());
        } catch (ParseException e) {
            e.printStackTrace();
            return text;
        }
    }

    /**
     * 统一格式经纬度
     * +31.2368+121.4992
     *
     * @param latLong
     * @return
     */
    public static String formatLatLong(double[] latLong) {
        if (latLong == null) {
            return null;
        } else {
            double longitude = latLong[0];
            double latitude = latLong[1];
            return String.format("%s%s", longitude > 0 ? "+" + longitude : String.valueOf(longitude), latitude > 0 ? "+" + latitude : String.valueOf(latitude));
        }
    }

    /**
     * 解析经纬度
     * +31.2368+121.4992
     *
     * @param latLong
     * @return
     */
    public static double[] parseLatLong(String latLong) {
        try {
            if (latLong != null) {
                final String[] split = latLong.split("\\+");
                if (split.length >= 3) {
                    return new double[]{Double.parseDouble(split[1]), Double.parseDouble(split[2])};
                }
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static byte[] getEmbeddedPicture(InputStream stream) {
        try {
            ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
            byte[] buff = new byte[1024];
            int buffer;
            while ((buffer = stream.read(buff)) > 0) {
                swapStream.write(buff, 0, buffer);
            }
            return swapStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
