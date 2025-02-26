package ex5.variable;

/**
 * The {@code DataTypes} class holds constants and enum definitions related to various data types.
 * It provides a structure for managing the types and their corresponding patterns and behaviors.
 * <p>
 * This class includes a list of reserved keywords and data type
 patterns used throughout the code to match variable names and assignments.
 */
public class DataTypes {

    private static final String OR_REGX_SIGN = "|";
    private static final String DATA_TYPE_END = "1";

    /**
     * A string of reserved keywords in the code. These include basic language keywords like {@code void},
     * {@code true}, {@code false}, and primitive types like {@code int}, {@code double}, etc.
     */
    public static final String RESERVED_LIST = "void|final|true|false|return|" +
            "if|while|String|char|int|double|boolean";

    /**
     * A regular expression pattern for matching valid variable names. The pattern ensures that variable names
     * don't conflict with reserved keywords and must start with a letter or underscore.
     */
    public static final String PATTERN_NAME_VARIABLE =
            "(:?(?!\\b("+RESERVED_LIST+")\\b)[a-zA-Z][a-zA-Z0-9]*(_?[a-zA-Z0-9]*)|_([a-zA-Z0-9])+)";

    /**
     * Enum to represent various data types used in the code. Each data type has a corresponding pattern
     * for assignment matching (e.g., for primitive data types like {@code String}, {@code int}, etc.).
     */
    public enum DataType {
        /**
         * Pattern for matching assignments of type String.
         */
        String1(Variable.IGNORE_SPACES + "=" +Variable.IGNORE_SPACES+ "(:?\".*\"" +
                "|(" +PATTERN_NAME_VARIABLE+ "))\\s*"),

        /**
         * Pattern for matching assignments of type char.
         */
        char1(Variable.IGNORE_SPACES + "=" +Variable.IGNORE_SPACES+ "(:?'.?'" +
                "|(" +PATTERN_NAME_VARIABLE+ "))\\s*"),

        /**
         * Pattern for matching assignments of type int.
         */
        int1(Variable.IGNORE_SPACES + "=" +Variable.IGNORE_SPACES+ "(:?[-+]?[0-9]+" +
                "|(" +PATTERN_NAME_VARIABLE+ "))\\s*"),

        /**
         * Pattern for matching assignments of type double.
         */
        double1(Variable.IGNORE_SPACES + "=" +Variable.IGNORE_SPACES+
                "(:?[-+]?([0-9]*.?[0-9]+|[0-9]+.?[0-9]*)" + "|(" +PATTERN_NAME_VARIABLE+ "))\\s*"),

        /**
         * Pattern for matching assignments of type boolean.
         */
        boolean1(Variable.IGNORE_SPACES + "=" +Variable.IGNORE_SPACES+
                "(:?true|false|[-+]?([0-9]*.?[0-9]+|[0-9]+.?[0-9]*)" + "|(" +PATTERN_NAME_VARIABLE+ "))\\s*");

        private final String value;

        /**
         * Constructor for creating a {@code DataType} enum instance with a specific assignment pattern.
         *
         * @param value The assignment pattern for the data type.
         */
        DataType(String value) {
            this.value = value;
        }

        /**
         * Retrieves the assignment pattern associated with the data type.
         *
         * @return The assignment pattern as a string.
         */
        public String getValue() {
            return value;
        }

        /**
         * Checks if the provided string matches any of the data type names (excluding the last character).
         *
         * @param input The string to be checked.
         * @return {@code true} if the string matches any data type name, {@code false} otherwise.
         */
        public static boolean contains(String input) {
            for (DataType type : values()) {
                if (removeLastCharacter(type.name()).equals(input.trim())) {
                    return true;
                }
            }
            return false;
        }

        /**
         * Removes the last character from the given string.
         *
         * @param input The string from which to remove the last character.
         * @return The input string without the last character.
         */
        public static String removeLastCharacter(String input) {
            if (input == null || input.isEmpty()) {
                return input;
            }
            return input.substring(0, input.length() - 1);
        }

        /**
         * Returns a string of all enum names, excluding their last character, joined by a pipe ("|").
         *
         * @return A string containing all data type names separated by a pipe ("|").
         */
        public static String getEnumNamesAsString() {
            StringBuilder result = new StringBuilder();
            for (DataType type : values()) {
                if (result.length() > 0) {
                    result.append(OR_REGX_SIGN);
                }
                result.append(removeLastCharacter(type.name()));
            }
            return result.toString();
        }

        /**
         * Returns the assignment pattern associated with the provided variable name.
         *
         * @param variableName The name of the variable whose pattern is to be retrieved.
         * @return The assignment pattern for the variable type, or {@code null} if no match is found.
         */
        public static String getVariableValue(String variableName) {
            String enumName = variableName + DATA_TYPE_END;
            try {
                DataTypes.DataType dataType = DataTypes.DataType.valueOf(enumName);
                return dataType.getValue();
            } catch (IllegalArgumentException e) {
                return null;
            }
        }
    }
}
