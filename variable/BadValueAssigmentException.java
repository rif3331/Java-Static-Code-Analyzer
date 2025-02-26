package ex5.variable;

/**
 * The {@code BadValueAssigmentException} class is a
 custom exception that extends {@code BadVarAssigmentException}.
 * This exception is thrown when there is an attempt
 to assign an invalid value to a variable. It ensures that
 * values assigned to variables are in the correct
 format or follow the expected rules for variable assignments.
 */
public class BadValueAssigmentException extends BadVarAssigmentException {

    /**
     * Constructs a new {@code BadValueAssigmentException} with the specified detail message.
     *
     * @param message The detail message which is saved
     for later retrieval by the {@link Throwable#getMessage()} method.
     */
    public BadValueAssigmentException(String message) {
        super(message);
        System.out.println(1);
        System.err.println(message);
        System.exit(1);
    }
}
