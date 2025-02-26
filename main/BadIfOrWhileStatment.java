package ex5.main;

/**
 * The {@code BadIfOrWhileStatment} class is a
 custom exception that extends {@code BadCommandException}.
 * This exception is thrown when there is an invalid {@code if} or {@code while} statement in the code.
 * It provides a custom error message that can be used for debugging purposes.
 */
public class BadIfOrWhileStatment extends BadCommandException {

    /**
     * Constructs a new {@code BadIfOrWhileStatment} with the specified detail message.
     *
     * @param message The detail message which is saved
     for later retrieval by the {@link Throwable#getMessage()} method.
     */
    public BadIfOrWhileStatment(String message) {
        super(message);
        System.out.println(1);
        System.err.println(message);
        System.exit(1);
    }
}
