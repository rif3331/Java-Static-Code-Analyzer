package ex5.main;

/**
 * The {@code BadParhtness} class is a
 * custom exception that extends {@code BadCommandException}.
 * This exception is thrown when there is an invalid or malformed parenthesis in the code.
 * It provides a custom error message to indicate where the issue occurred.
 */
public class BadParhtness extends BadCommandException {

    /**
     * Constructs a new {@code BadParhtness} exception with the specified detail message.
     *
     * @param message The detail message which is saved for
     later retrieval by the {@link Throwable#getMessage()} method.
     */
    public BadParhtness(String message) {
        super(message);
        System.out.println(1);
        System.err.println(message);
        System.exit(1);
    }
}
