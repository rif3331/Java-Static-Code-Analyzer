package ex5.main;

/**
 * The {@code BadActionInLIne} class is a
 custom exception that extends {@code BadCommandException}.
 * This exception is thrown when an invalid
 action is encountered in a specific line of code.
 * It includes a constructor that passes a message
 to the superclass and prints a debug statement.
 */
public class BadActionInLIne extends BadCommandException {

    /**
     * Constructs a new {@code BadActionInLIne} exception with the specified detail message.
     * This constructor also prints a debug message (1) to the console.
     *
     * @param message The detail message which is
     saved for later retrieval by the {@link Throwable#getMessage()} method.
     */
    public BadActionInLIne(String message) {
        super(message);
        System.out.println(1);
        System.err.println(message);
        System.exit(1);
    }
}
