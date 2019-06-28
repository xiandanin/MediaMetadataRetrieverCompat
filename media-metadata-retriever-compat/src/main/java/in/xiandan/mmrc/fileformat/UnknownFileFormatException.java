package in.xiandan.mmrc.fileformat;

/**
 * @author dengyuhan
 * created 2019-06-25 10:41
 */
public class UnknownFileFormatException extends Exception {

    public UnknownFileFormatException(String s) {
        super(s);
    }

    public UnknownFileFormatException(String message, Throwable cause) {
        super(message, cause);
    }
}
