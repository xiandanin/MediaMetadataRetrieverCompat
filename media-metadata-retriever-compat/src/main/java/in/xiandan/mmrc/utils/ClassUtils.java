package in.xiandan.mmrc.utils;

/**
 * @author dengyuhan
 * created 2019-06-27 17:22
 */
public class ClassUtils {

    /**
     * 是否存在这些类
     *
     * @param classNames
     * @return
     */
    public static boolean hasClass(String... classNames) {
        for (String cls : classNames) {
            try {
                Class.forName(cls);
            } catch (ClassNotFoundException e) {
                return false;
            }
        }
        return true;
    }


    /**
     * 是否存在这些类中其中一个
     *
     * @param classNames
     * @return
     */
    public static boolean hasOneClass(String... classNames) {
        for (String cls : classNames) {
            try {
                Class.forName(cls);
                return true;
            } catch (ClassNotFoundException ignored) {

            }
        }
        return false;
    }
}
