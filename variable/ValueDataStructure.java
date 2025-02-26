package ex5.variable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * The {@code ValueDataStructure} class stores information about variables within a specific scope.
 * It holds lists of variable names, values, types, final flags, and whether the values are old variables.
 * This structure allows tracking of variable states within a given scope during the program's execution.
 */
public class ValueDataStructure {

    private final List<String> names;
    private final List<String> values;
    private final List<String> types;
    private final List<Integer> finals;
    private final List<String> valuesIsOldVariable;

    /**
     * Constructs a new {@code ValueDataStructure} object with the specified values.
     *
     * @param names A list of variable names.
     * @param values A list of variable values.
     * @param finals A list of flags indicating if a variable is final.
     * @param nameOfScope The name of the scope in which these variables exist (not directly used here).
     * @param types A list of variable types (e.g., int, String, etc.).
     * @param valuesIsOldVariable A list indicating if the value of a variable is that of an old variable.
     */
    public ValueDataStructure(List<String> names, List<String> values,
                              List<Integer> finals, String nameOfScope, List<String> types,
                              List<String> valuesIsOldVariable) {
        this.names = names;
        this.values = values;
        this.finals = finals;
        this.types = types;
        this.valuesIsOldVariable = valuesIsOldVariable;
    }

    /**
     * Returns the list of variable names in this data structure.
     *
     * @return The list of variable names.
     */
    public List<String> getNames() {
        return names;
    }

    /**
     * Returns the list of variable values in this data structure.
     *
     * @return The list of variable values.
     */
    public List<String> getValues() {
        return values;
    }

    /**
     * Returns the list of variable types in this data structure.
     *
     * @return The list of variable types.
     */
    public List<String> getTypes() {
        return types;
    }

    /**
     * Returns the list indicating if the values of variables are old variables.
     *
     * @return The list of flags for old variables.
     */
    public List<String> getValuesIsOldVariable() {
        return valuesIsOldVariable;
    }

    /**
     * Returns the list of flags indicating if a variable is final.
     *
     * @return The list of flags indicating if variables are final.
     */
    public List<Integer> getFinals() {
        return finals;
    }
}
