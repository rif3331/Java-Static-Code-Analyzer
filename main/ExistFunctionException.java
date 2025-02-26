package ex5.main;

/**
 * The {@code ExistFunctionException} class
 is a custom exception that extends {@code BadCommandException}.
 * This exception is thrown when an attempt
 is made to declare a function that already exists in the current context.
 * It provides a custom error message to indicate the conflict.
 */
public class ExistFunctionException extends BadCommandException {

    /**
     * Constructs a new {@code ExistFunctionException} exception with the specified detail message.
     *
     * @param message The detail message which is
     saved for later retrieval by the {@link Throwable#getMessage()} method.
     */
    public ExistFunctionException(String message) {
        super(message);
        System.out.println(1);
        System.err.println(message);
        System.exit(1);
    }
}
