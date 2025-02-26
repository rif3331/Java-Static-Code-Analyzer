package ex5.main;

/**
 * The {@code BadFormatCallFunc} class is
 a custom exception that extends {@code BadCommandException}.
 * This exception is thrown when there is
 an invalid or incorrectly formatted function call in the code.
 * It provides a custom error message that can be retrieved for debugging purposes.
 */
public class BadFormatCallFunc extends BadCommandException {

    /**
     * Constructs a new {@code BadFormatCallFunc} with the specified detail message.
     *
     * @param message The detail message which
     is saved for later retrieval by the {@link Throwable#getMessage()} method.
     */
    public BadFormatCallFunc(String message) {
        super(message);
        System.out.println(1);
        System.err.println(message);
        System.exit(1);
    }
}
