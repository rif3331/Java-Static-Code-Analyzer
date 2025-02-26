package ex5.main;

/**
 * The {@code NoReturnException} class is a
 custom exception that extends {@code BadCommandException}.
 * This exception is thrown when a function
 or method is expected to return a value but does not provide one.
 * It helps signal the absence of a return value where one is required.
 */
public class NoReturnException extends BadCommandException {

    /**
     * Constructs a new {@code NoReturnException} exception with the specified detail message.
     *
     * @param message The detail message which is
     saved for later retrieval by the {@link Throwable#getMessage()} method.
     */
    public NoReturnException(String message) {
        super(message);
        System.out.println(1);
        System.err.println(message);
        System.exit(1);
    }
}
