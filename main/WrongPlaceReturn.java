package ex5.main;

/**
 * The {@code WrongPlaceReturn} class is a custom
 exception that extends {@code BadCommandException}.
 * This exception is thrown when a {@code return}
 statement appears in an invalid location within the code.
 * It helps signal that a {@code return} statement
 has been used incorrectly, such as outside a function or
 * in an inappropriate part of the program flow.
 */
public class WrongPlaceReturn extends BadCommandException {

    /**
     * Constructs a new {@code WrongPlaceReturn} exception with the specified detail message.
     *
     * @param message The detail message which is saved
     for later retrieval by the {@link Throwable#getMessage()} method.
     */
    public WrongPlaceReturn(String message) {
        super(message);
        System.out.println(1);
        System.err.println(message);
        System.exit(1);
    }
}
