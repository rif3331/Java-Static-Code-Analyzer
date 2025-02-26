package ex5.ifAndWhile;

import ex5.variable.DataTypes;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The {@code IfAndWhile} class is used to represent
 and validate {@code if} and {@code while} statements.
 * It checks whether the given statement follows the correct
 syntax and extracts the variables used in the condition.
 */
public class IfAndWhile {

    /** The original row value (line) representing the {@code if} or {@code while} statement. */
    private final String rowValue;

    /** A flag indicating whether the statement is valid. */
    private final boolean isValid;

    /** A constant representing the regular expression format for boolean values in the condition. */
    private static final String FORMAT_BOOLEAN = "(:?true|false|[-+]?([0-9]*.?[0-9]+|[0-9]+.?[0-9]*)" +
            "|(" + DataTypes.PATTERN_NAME_VARIABLE + "))";

    /** A constant representing the regular expression format for {@code if} and {@code while} statements. */
    private static final String FORMAT_IF_OR_WHILE = "\\s*(:?if|while)\\s*\\(\\s*" + FORMAT_BOOLEAN +
            "\\s*(:?(:?&\\s*&|\\|\\s*\\|)\\s*" + FORMAT_BOOLEAN + "\\s*)*\\)\\s*\\{\\s*";

    /** An array holding the variables used in the condition of the {@code if} or {@code while} statement. */
    private String[] variables;

    /**
     * Constructs a new {@code IfAndWhile} object and validates the given row value.
     * If the statement is valid, it extracts the variables used in the condition.
     *
     * @param rowValue The {@code if} or {@code while} statement as a string.
     */
    public IfAndWhile(String rowValue) {
        this.rowValue = rowValue;
        this.isValid = !this.isNonValid();
        if (isValid) {
            this.variables = this.findVariables();
        }
    }

    /**
     * Returns whether the {@code if} or {@code while} statement is valid.
     *
     * @return {@code true} if the statement is valid, {@code false} otherwise.
     */
    public boolean getIsValid() {
        return isValid;
    }

    /**
     * Returns an array of variables used in the condition of the {@code if} or {@code while} statement.
     *
     * @return An array of variable names.
     */
    public String[] getVariables() {
        return variables;
    }

    /**
     * Extracts the variables used in the condition of the {@code if} or {@code while} statement.
     *
     * @return An array of variable names.
     */
    private String[] findVariables() {
        String tempString = rowValue.split("\\(")[1];
        tempString = tempString.split("\\)")[0].trim();
        String[] variables = tempString.split("\\s*(?:&\\s*&|\\|\\s*\\|)\\s*");
        for (int i = 0; i < variables.length; i++) {
            variables[i] = variables[i].replaceAll("\\s+", " ");
        }
        return variables;
    }

    /**
     * Validates whether the given {@code if} or {@code while} statement is non-valid.
     *
     * @return {@code true} if the statement does not match the valid {@code if} or {@code while} format,
     *         {@code false} otherwise.
     */
    private Boolean isNonValid() {
        Pattern patternFunction = Pattern.compile(FORMAT_IF_OR_WHILE);
        Matcher functionMatcher = patternFunction.matcher(rowValue);
        return !functionMatcher.matches();
    }
}
