package ex5.variable;

/**
 * The {@code AssigmentNullException} class is a custom
 exception that extends {@code BadVarAssigmentException}.
 * This exception is thrown when an attempt is made to
 assign a {@code null} value to a variable,
 * which is not allowed under certain conditions,
 such as when the variable type does not permit {@code null} values.
 */
public class AssigmentNullException extends BadVarAssigmentException {

    /**
     * Constructs a new {@code AssigmentNullException} with the specified detail message.
     *
     * @param message The detail message which is saved for
     later retrieval by the {@link Throwable#getMessage()} method.
     */
    public AssigmentNullException(String message) {
        super(message);
        System.out.println(1);
        System.err.println(message);
        System.exit(1);
    }
}
