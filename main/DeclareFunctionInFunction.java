package ex5.main;

/**
 * The {@code DeclareFunctionInFunction} class
 is a custom exception that extends {@code BadCommandException}.
 * This exception is thrown when a function is
 declared inside another function, which is not allowed.
 * It provides a custom error message to indicate where the issue occurred.
 */
public class DeclareFunctionInFunction extends BadCommandException {

    /**
     * Constructs a new {@code DeclareFunctionInFunction} exception with the specified detail message.
     *
     * @param message The detail message which is saved for
     later retrieval by the {@link Throwable#getMessage()} method.
     */
    public DeclareFunctionInFunction(String message) {
        super(message);
        System.out.println(1);
        System.err.println(message);
        System.exit(1);
    }
}
