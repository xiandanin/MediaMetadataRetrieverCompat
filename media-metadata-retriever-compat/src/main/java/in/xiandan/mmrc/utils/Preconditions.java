package in.xiandan.mmrc.utils;

/**
 * From Glide
 * Contains common assertions.
 * @see <a href="https://github.com/bumptech/glide/blob/master/library/src/main/java/com/bumptech/glide/util/Preconditions.java"></a>
 */
public class Preconditions {
    private Preconditions() {}

    /**
     * Ensures the truth of an expression involving one or more parameters to the calling method.
     *
     * @param expression a boolean expression
     * @throws IllegalArgumentException if {@code expression} is false
     */
    public static void checkArgument(boolean expression) {
        if (!expression) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Ensures that an object reference passed as a parameter to the calling method is not null.
     *
     * @param reference an object reference
     * @return the non-null reference that was validated
     * @throws NullPointerException if {@code reference} is null
     */
    public static <T> T checkNotNull(T reference) {
        if (reference == null) {
            throw new NullPointerException();
        }
        return reference;
    }
}
