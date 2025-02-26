package ex5.variable;

/**
 * The {@code DoubleNameVarDeclarition} class is a
 custom runtime exception that extends {@code RuntimeException}.
 * This exception is thrown when a variable is
 declared with a name that has already been used within the same scope.
 * It indicates that the variable name being used has already been declared, which is not allowed.
 */
public class DoubleNameVarDeclarition extends RuntimeException {

  /**
   * Constructs a new {@code DoubleNameVarDeclarition} with the specified detail message.
   *
   * @param message The detail message which is saved for
   later retrieval by the {@link Throwable#getMessage()} method.
   */
  public DoubleNameVarDeclarition(String message) {
    super(message);
    System.out.println(1);
    System.err.println(message);
    System.exit(1);
  }
}
