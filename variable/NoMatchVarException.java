package ex5.variable;

/**
 * The {@code NoMatchVarException} class is a custom
 runtime exception that extends {@code BadVarAssigmentException}.
 * This exception is thrown when there is a
 mismatch between a variable's value and its expected type or assignment.
 * It indicates that an assignment operation is
 attempting to use a value that does not match the expected type or variable constraints.
 */
public class NoMatchVarException extends BadVarAssigmentException {

    /**
     * Constructs a new {@code NoMatchVarException} with the specified detail message.
     *
     * @param message The detail message which is saved
     for later retrieval by the {@link Throwable#getMessage()} method.
     */
    public NoMatchVarException(String message) {
        super(message);
        System.out.println(1);
        System.err.println(message);
        System.exit(1);
    }
}
