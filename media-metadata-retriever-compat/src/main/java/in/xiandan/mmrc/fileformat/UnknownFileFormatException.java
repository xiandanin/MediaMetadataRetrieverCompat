package in.xiandan.mmrc.fileformat;

import java.security.InvalidParameterException;

/**
 * @author dengyuhan
 * created 2019-06-25 10:41
 */
public class UnknownFileFormatException extends InvalidParameterException {

    public UnknownFileFormatException() {
    }

    public UnknownFileFormatException(String msg) {
        super(msg);
    }
}
