package ex5.function;

import ex5.variable.DataTypes;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The {@code Function} class represents a function declaration.
 * It validates the syntax of the function and extracts its name and variables.
 * Additionally, it manages a map of declared functions to check for duplicates.
 */
public class Function {

    /** Regular expression pattern for function names.
     It ensures the function name is valid and not a reserved keyword. */
    public static final String PATTERN_NAME_FUNCTION =
            "(:?(?!\\b("+ DataTypes.RESERVED_LIST+")" +
            "\\b)[a-zA-Z][a-zA-Z0-9]*(_?[a-zA-Z0-9]*)+)";

    /** The original row value representing the function declaration. */
    private final String rowValue;

    /** The name of the function. */
    private String functionName;

    /** A flag indicating whether the function declaration is valid. */
    private final boolean isValid;

    /** A flag indicating whether the function already exists in the function map. */
    private boolean isExsist;

    /** The list of variables used in the function's parameters. */
    private String[] variablesOfFunction;

    /** A map of function names to {@code Function} objects, used to track declared functions. */
    private static final HashMap<String, Function> functionMap = new HashMap<>();

    /** Regular expression for matching variable declarations within the function's parameters. */
    private static final String inParhtness ="\\s*(:?final)?\\s*\\b" +
            "("+DataTypes.DataType.getEnumNamesAsString()+")" +
            "\\b\\s*"+DataTypes.PATTERN_NAME_VARIABLE+"\\s*";

    /** Regular expression for matching the complete function declaration format. */
    private static final String FORMAT_FUNCTION = "\\s*void \\s*"+PATTERN_NAME_FUNCTION+"\\s*\\" +
            "(((:?"+inParhtness+"\\s*,\\s*)*(:?"+inParhtness+"))*\\)\\s*\\{\\s*";

    private static final String REGX_PARTHNESS_END = "\\)";
    private static final String REGX_PARTHNESS_BEGIN = "\\(";
    private static final String REGX_RECOGNIZE_FUNCTION = "void";
    private static final int LAST_PART = 1;


    /**
     * Constructs a new {@code Function} object by parsing and validating the given function declaration.
     * If the function is valid, it extracts the function's name and parameters, and checks if the function
     * already exists in the function map.
     *
     * @param rowValue The function declaration as a string.
     */
    public Function(String rowValue) {
        this.rowValue = rowValue;
        this.isValid = this.isNonValid();
        if(isValid){
            this.functionName = findFunctionName();
            if(functionMap.containsKey(functionName)){
                isExsist = true;
            } else {
                isExsist = false;
                functionMap.put(functionName, this);
            }
            this.variablesOfFunction = this.findVariables();
        }
    }

    /**
     * Checks if a function with the given name exists in the function map.
     *
     * @param functionName The name of the function to search for.
     * @return The {@code Function} object if found, {@code null} otherwise.
     */
    public static Function isFunctionInMap(String functionName){
        if(functionMap.containsKey(functionName)){
            return functionMap.get(functionName);
        }
        return null;
    }

    /**
     * Returns the name of the function.
     *
     * @return The function's name.
     */
    public String getFunctionName(){
        return functionName;
    }

    /**
     * Returns whether the function already exists in the function map.
     *
     * @return {@code true} if the function exists in the map, {@code false} otherwise.
     */
    public boolean getIsExsist(){
        return isExsist;
    }

    /**
     * Returns whether the function declaration is valid.
     *
     * @return {@code true} if the function declaration is valid, {@code false} otherwise.
     */
    public boolean getIsValid(){
        return isValid;
    }

    /**
     * Returns an array of the variables used in the function's parameters.
     *
     * @return An array of variable names used in the function's parameters.
     */
    public String[] getVariables(){
        return variablesOfFunction;
    }

    /**
     * Extracts the name of the function from the function declaration string.
     *
     * @return The name of the function.
     */
    private String findFunctionName(){
        String tempString = rowValue.split(REGX_RECOGNIZE_FUNCTION)[LAST_PART];
        return tempString.split(REGX_PARTHNESS_BEGIN)[0].trim();
    }

    /**
     * Extracts the variables used in the function's parameters from the function declaration string.
     *
     * @return An array of variable names used in the function's parameters.
     */
    private String[] findVariables(){
        String tempString = rowValue.split(REGX_PARTHNESS_BEGIN)[LAST_PART];
        tempString = tempString.split(REGX_PARTHNESS_END)[0].trim();
        String[] variables = tempString.split("\\s*,\\s*");
        for (int i = 0; i < variables.length; i++) {
            variables[i] = variables[i].replaceAll("\\s+", " ");
        }
        return variables;
    }

    /**
     * Validates whether the function declaration follows the correct format.
     *
     * @return {@code true} if the function declaration matches the valid format, {@code false} otherwise.
     */
    private Boolean isNonValid(){
        Pattern patternFunction = Pattern.compile(FORMAT_FUNCTION);
        Matcher functionMatcher = patternFunction.matcher(rowValue);
        return functionMatcher.matches();
    }
}
