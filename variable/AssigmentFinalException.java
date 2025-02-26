package ex5.variable;

/**
 * The {@code AssigmentFinalException} class is
 a custom exception that extends {@code BadVarUseException}.
 * This exception is thrown when an attempt is made to assign a
 value to a final variable, which is not allowed
 * in most programming languages as final variables cannot be reassigned after their initial assignment.
 */
public class AssigmentFinalException extends BadVarUseException {

    /**
     * Constructs a new {@code AssigmentFinalException} with the specified detail message.
     *
     * @param message The detail message which is saved for
     later retrieval by the {@link Throwable#getMessage()} method.
     */
    public AssigmentFinalException(String message) {
        super(message);
        System.out.println(1);
        System.err.println(message);
        System.exit(1);
    }
}
