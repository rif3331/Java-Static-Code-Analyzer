package ex5.variable;

/**
 * The {@code BadVarUseException} class is a
 custom runtime exception that extends {@code RuntimeException}.
 * This exception is thrown when there is an invalid
 use of a variable, such as using a variable before it is initialized,
 * or trying to perform an operation on a variable that is not valid.
 */
public class BadVarUseException extends RuntimeException {

    /**
     * Constructs a new {@code BadVarUseException} with the specified detail message.
     *
     * @param message The detail message which is saved for
     later retrieval by the {@link Throwable#getMessage()} method.
     */
    public BadVarUseException(String message) {
        super(message);
    }
}
