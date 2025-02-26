package ex5.main;

/**
 * The {@code BadCommandException}
 class is a custom exception that extends {@code RuntimeException}.
 * This exception is used to signal an error related to a bad or invalid command.
 * It allows the passing of a custom error message that can be retrieved later.
 */
public class BadCommandException extends RuntimeException {

    /**
     * Constructs a new {@code BadCommandException} with the specified detail message.
     *
     * @param message The detail message which is
     saved for later retrieval by the {@link Throwable#getMessage()} method.
     */
    public BadCommandException(String message) {
        super(message);
    }
}
