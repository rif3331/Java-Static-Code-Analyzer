package ex5.variable;

/**
 * The {@code BadVarAssigmentException} class is a custom
 runtime exception that extends {@code RuntimeException}.
 * This exception is thrown when there is an invalid assignment
 to a variable, such as assigning an incompatible value
 * to a variable or performing an illegal operation on the variable.
 */
public class BadVarAssigmentException extends RuntimeException {

    /**
     * Constructs a new {@code BadVarAssigmentException} with the specified detail message.
     *
     * @param message The detail message which is saved for
     later retrieval by the {@link Throwable#getMessage()} method.
     */
    public BadVarAssigmentException(String message) {
        super(message);
    }
}
