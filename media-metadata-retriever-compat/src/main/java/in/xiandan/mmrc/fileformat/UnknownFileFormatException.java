package in.xiandan.mmrc.fileformat;

import java.io.IOException;

/**
 * @author dengyuhan
 * created 2019-06-25 10:41
 */
public class UnknownFileFormatException extends IOException {

    public UnknownFileFormatException(String s) {
        super(s);
    }

    public UnknownFileFormatException(String message, Throwable cause) {
        super(message, cause);
    }
}
