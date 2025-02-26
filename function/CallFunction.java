package ex5.function;

import ex5.variable.DataTypes;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The {@code CallFunction} class represents a function call in the code.
 * It validates the syntax of the function call, extracts the function name,
 * retrieves the function declaration data from the {@code Function} class,
 * and stores the arguments passed to the function in the call.
 */
public class CallFunction {

    /** The original row value representing the function call. */
    private final String rowValue;

    /** The name of the function being called. */
    private String functionName;

    /** A flag indicating whether the function call is valid. */
    private final boolean isValid;

    /** The {@code Function} object representing the function declaration data. */
    private Function functionDeclareData;

    /** An array of variables passed in the function call. */
    private String[] variablesOfFunction;

    /** Regular expression pattern to validate the format of the function call. */
    private static final String FORMAT_CALL_FUNCTION = "\\s*" + Function.PATTERN_NAME_FUNCTION
            + "\\s*(\\(([^,]+(?:,\\s*[^,]+)*)|(\\s*)\\))\\s*;";

    private static final char SPEARTE_ELEMENTS = ',';
    private static final char END_VARIABLE_CHAR = ')';
    private static final String REGX_PARTHNESS_END = "\\)";
    private static final String REGX_PARTHNESS_BEGIN = "\\(";
    private static final String SIGN_BEFORE_VALUE = "=";
    private static final int LAST_PART = 1;

    /**
     * Constructs a new {@code CallFunction} object by parsing and validating the given function call.
     * It extracts the function name, retrieves the associated function declaration data,
     * and finds the variables passed in the call.
     *
     * @param rowValue The function call as a string.
     */
    public CallFunction(String rowValue) {
        this.rowValue = rowValue;
        this.isValid = !this.isNonValid();
        if(isValid){
            this.functionName = findFunctionName();
            this.functionDeclareData = Function.isFunctionInMap(functionName.trim());
            this.variablesOfFunction = this.findVariables();
        }
    }

    /**
     * Returns the name of the function being called.
     *
     * @return The name of the function.
     */
    public String getFunctionName(){
        return functionName;
    }

    /**
     * Returns the {@code Function} object containing the function's declaration data.
     *
     * @return The function's declaration data.
     */
    public Function getFunctionDeclareData(){
        return functionDeclareData;
    }

    /**
     * Returns whether the function call is valid.
     *
     * @return {@code true} if the function call is valid, {@code false} otherwise.
     */
    public boolean getIsValid(){
        return isValid;
    }

    /**
     * Extracts the name of the function from the function call string.
     *
     * @return The name of the function.
     */
    private String findFunctionName(){
        String tempString = rowValue.split(REGX_PARTHNESS_BEGIN)[0];
        return tempString.trim();
    }

    /**
     * Returns the function call arguments formatted as assignments for the corresponding parameters.
     * Each element in the returned array will be in the form of "parameterName=argument".
     * If the number of arguments doesn't match the number of parameters, {@code null} is returned.
     *
     * @return An array of formatted arguments, or
     {@code null} if the number of arguments doesn't match.
     */
    public String[] getLinesOfVariable(){
        if(functionDeclareData == null){return null;}
        String[] temp = Arrays.copyOf(functionDeclareData.getVariables(),
                functionDeclareData.getVariables().length);
        if(temp.length != variablesOfFunction.length){return null;}
        for(int i = 0; i < variablesOfFunction.length; i++){
            temp[i] = temp[i] + SIGN_BEFORE_VALUE + variablesOfFunction[i];
        }
        return temp;
    }

    /**
     * Extracts the arguments passed in the function call from the row value.
     *
     * @return An array of arguments passed in the function call.
     */
    public String[] findVariables(){
        String tempString = rowValue.split(REGX_PARTHNESS_BEGIN)[LAST_PART];
        tempString = tempString.split(REGX_PARTHNESS_END)[0].trim();
        String[] variables = tempString.split("\\s*,\\s*");
        for (int i = 0; i < variables.length; i++) {
            variables[i] = variables[i].replaceAll("\\s+", " ");
        }
        return variables;
    }

    /**
     * Validates the syntax of the function call using a regular expression.
     * It also checks that the function call's argument list does not end with a comma.
     *
     * @return {@code true} if the function call is invalid, {@code false} if it is valid.
     */
    private Boolean isNonValid(){
        Pattern patternFunction = Pattern.compile(FORMAT_CALL_FUNCTION);
        Matcher functionMatcher = patternFunction.matcher(rowValue);
        if(functionMatcher.matches()){
            if(rowValue.charAt(rowValue.lastIndexOf(END_VARIABLE_CHAR) - LAST_PART) == SPEARTE_ELEMENTS){
                return true;
            }
            return false;
        }
        return true;
    }
}
