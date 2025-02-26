package ex5.main;

/**
 * The {@code CallFunctionBadCountVars} class
 is a custom exception that extends {@code BadCommandException}.
 * This exception is thrown when there is an
 incorrect number of variables provided for a function call.
 * It provides a custom error message to indicate where the issue occurred.
 */
public class CallFunctionBadCountVars extends BadCommandException {

    /**
     * Constructs a new {@code CallFunctionBadCountVars} exception with the specified detail message.
     *
     * @param message The detail message which is saved for
     later retrieval by the {@link Throwable#getMessage()} method.
     */
    public CallFunctionBadCountVars(String message) {
        super(message);
        System.out.println(1);
        System.err.println(message);
        System.exit(1);
    }
}
