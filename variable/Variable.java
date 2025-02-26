package ex5.variable;

import ex5.main.Sjavac;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class is responsible for managing variables in the programming code.
 * It contains functions that handle variable declarations, assignments, types,
 * and the relationship between different variables in various scopes.
 */
public class Variable {

    /**
     * A regular expression pattern that matches zero or more whitespace characters.
     * This is used throughout the code to handle and ignore
     spaces when processing variable declarations and other code elements.
     */
    public static final String IGNORE_SPACES = "\\s*";
    private static final HashMap<String, ValueDataStructure> scopesMap = new HashMap<>();
    private static final String OPEN_PARHTNESS_IN_REGX = "(:?";
    private static final String CLOSE_PARHTNESS_IN_REGX = ")?";
    private static final String CONSTANT_VAR_KEYWORD = "final";
    private static final String REGX_LEAST_ONE_SPACE = "\\s+";
    private static final String OR_REGX_SIGN = "|";
    private static final String DATA_TYPE_END = "1";
    private static final String SEPERATE_ELEMENTS = ",";
    private static final String SEPERATE_ASSIGNMENTS_OF_VARS = "[=;]";
    private static final String SIGN_BEFORE_VALUE = "=";
    private static final String SIGN_END_LINE = ";";
    private static final String REGX_TEST_OLD_VAR = "[\"\\d+-.']";
    private static final String TYPE_VAR_BOOLEAN_1 = "true";
    private static final String TYPE_VAR_BOOLEAN_2 = "false";
    private static final String SEPERATE_SCOPE_AND_NUMBER = "_";
    private static final String NAME_FIRST_SCOPE = "global";
    private static final String FINAL_WITHOUT_ASSIGNMENT = " is final, cannot declare without assignment";
    private static final String NO_MATCH_VAR_MSG = " doesn't match any accessible variable";
    private static final String ASSIGN_NULL_MSG = "you tried to assign a null value to ";
    private static final String INVALID_ASSIGN_VALUE_MSG ="invalid value assignment to variable ";
    private static final String ILLEGAL_ASSIGN_MSG =" illegal assignment statement.";
    private static final String CHANGE_FINAL_VAR_MSG ="cannot change final variable ";
    private static final String TRY_ADD_SAME_VAR_MSG_1 ="You tried to initialize variable '";
    private static final String TRY_ADD_SAME_VAR_MSG_2 ="' that already exists.";
    private static final char SIGN_BEFORE_VALUE_2 = '=';


    /**
     * Wraps the provided text in a non-mandatory regular expression pattern.
     *
     * @param text the text to be wrapped as non-mandatory.
     * @return the text wrapped as a non-mandatory pattern.
     */
    public static String notMust (String text){
        text = OPEN_PARHTNESS_IN_REGX + text + CLOSE_PARHTNESS_IN_REGX; return text;
    }

    /**
     * Removes the scope from the map.
     *
     * @param scope the scope to be removed from the map.
     */
    public static void removeScope(String scope){
        scopesMap.remove(scope);
    }

    /**
     * Creates a regular expression pattern that matches variable declarations with various types.
     *
     * @param patternNameVariable the regular expression pattern for the variable name.
     * @param patternTypeValue the regular expression pattern for the variable's value type.
     * @param typeVariable the type of the variable.
     * @return the regular expression pattern for matching variable declarations.
     */
    public static String variableActionFormat(String patternNameVariable,
                                              String patternTypeValue, String typeVariable){
        return IGNORE_SPACES +"(:?"+CONSTANT_VAR_KEYWORD+" )?"+
                IGNORE_SPACES + "(:?"+typeVariable+" )?" + IGNORE_SPACES +
                "(("+patternNameVariable+","+IGNORE_SPACES+")*+"+IGNORE_SPACES+ patternNameVariable +
                IGNORE_SPACES + notMust(patternTypeValue)+IGNORE_SPACES+",)*+"+ IGNORE_SPACES +
                "("+patternNameVariable+","+IGNORE_SPACES+")*+"+IGNORE_SPACES+ patternNameVariable +
                IGNORE_SPACES + notMust(patternTypeValue)+ IGNORE_SPACES+ ";" + IGNORE_SPACES;
    }

    /**
     * Creates a pattern to match variable declarations for all supported data types.
     *
     * @return the compiled pattern for variable declarations.
     */
    public static Pattern createPatternVar(){
        StringBuilder allPattern = new StringBuilder();
        for (DataTypes.DataType type : DataTypes.DataType.values()) {
            allPattern.append(Variable.variableActionFormat(DataTypes.PATTERN_NAME_VARIABLE,
                    type.getValue(), DataTypes.DataType.removeLastCharacter
                            (type.name()))).append(OR_REGX_SIGN);
        }
        String pattern = allPattern.substring(0, allPattern.length() - 1);
        return Pattern.compile(pattern);
    }

    /**
     * Checks if the first word in the input is "final".
     *
     * @param input the input string to be checked.
     * @return true if the first word is "final", false otherwise.
     */
    private static boolean isFirstWord(String input) {
        String[] words = input.trim().split(REGX_LEAST_ONE_SPACE);
        return words.length > 0 && words[0].equals(CONSTANT_VAR_KEYWORD);
    }

    /**
     * Retrieves the data type if it exists in the first or second word of the input.
     *
     * @param allVar the array of words from the input.
     * @return the data type if found, otherwise null.
     */
    private static String getDataTypeIfExists(String[] allVar) {
        String[] words = allVar[0].trim().split(REGX_LEAST_ONE_SPACE);
        String wordToCheck1 = words[0] + DATA_TYPE_END;
        String wordToCheck2 = null;
        if (words.length > 1) { wordToCheck2 = words[1] + DATA_TYPE_END; }
        for (DataTypes.DataType type : DataTypes.DataType.values()) {
            if (type.name().equals(wordToCheck1)) {
                return type.name().trim().substring(0, type.name().length()-1); }
            if (type.name().equals(wordToCheck2)) {
                return type.name().trim().substring(0, type.name().length()-1); }
        }
        return null;
    }

    /**
     * Handles formatting for final variables and their types in the input line.
     *
     * @param line the line of code containing the variable declarations.
     * @return an array of formatted variable declarations.
     */
    private static String[] handleFinalAndTypeFormat(String line){
        String[] allAssignment = line.split
                (SEPERATE_ELEMENTS); boolean isFinal = isFirstWord(allAssignment[0]);
        String type = getDataTypeIfExists(allAssignment); allAssignment[0] = allAssignment[0].trim();
        for (int i = 1; i < allAssignment.length; i++) {
            if(type != null) { allAssignment[i] = type+" " +  allAssignment[i].trim(); }
            if(isFinal) { allAssignment[i] = CONSTANT_VAR_KEYWORD + " " +  allAssignment[i].trim(); }
            allAssignment[i] = allAssignment[i].trim();
            if(i == allAssignment.length - 1) {
                allAssignment[i] = allAssignment[i].substring(0, allAssignment[i].length() - 1);
            }
        }
        return allAssignment;
    }

    /**
     * Decodes a variable declaration using the provided matcher and line, and assigns it to the given scope.
     *
     * @param matcher the regular expression matcher for the variable declaration.
     * @param line the line of code containing the variable declaration.
     * @param scope the scope where the variable is being declared.
     * @return true if the variable was successfully decoded, false otherwise.
     */
    public static boolean dechiperVariableMatcher(Matcher matcher, String line, String scope) {
        if(matcher.matches()) {
            for (String s : handleFinalAndTypeFormat(line)) {
                String copyVariable = null; String[] nameOfNewVariable
                        = new String[1]; String typeOfNewVariable = null;
                String valueOfNewVariable = null; int initalizeOfNewVariable = 0; int variableFinal = 0;
                String[] wordsInLine = s.split(REGX_LEAST_ONE_SPACE);
                if (wordsInLine[0].equals(CONSTANT_VAR_KEYWORD)) {
                    variableFinal = 1; if (!DataTypes.DataType.contains(wordsInLine[1])) { return false; }
                }
                if ((wordsInLine.length >= 2 && !DataTypes.DataType.contains(wordsInLine[1]) &&
                        !DataTypes.DataType.contains(wordsInLine[0])) || (wordsInLine.length == 1 &&
                        !DataTypes.DataType.contains(wordsInLine[0]))) { initalizeOfNewVariable = 1; }
                else { typeOfNewVariable = wordsInLine[variableFinal]; }
                nameOfNewVariable[0] = wordsInLine[1 - initalizeOfNewVariable + variableFinal];
                nameOfNewVariable[0] = nameOfNewVariable[0].split
                        (SEPERATE_ASSIGNMENTS_OF_VARS)[0].trim(); addScope(scope);
                if(initalizeOfNewVariable == 1) {
                    for (int i = 0; i <= nameOfNewVariable.length - 1; i++) {
                        ValueDataStructure scopeData = scopesMap.get(scope);
                        if (scopeData.getNames().contains(nameOfNewVariable[i])) {
                            int index = scopeData.getNames().indexOf(nameOfNewVariable[i]);
                            typeOfNewVariable = scopeData.getTypes().get(index); }
                    }
                }
                if(!s.contains(SIGN_BEFORE_VALUE) && variableFinal == 1) {
                    throw new NoMatchVarException(nameOfNewVariable[0]+FINAL_WITHOUT_ASSIGNMENT);
                }
                if (s.contains(SIGN_BEFORE_VALUE)) {
                    valueOfNewVariable = s.substring(s.indexOf(SIGN_BEFORE_VALUE_2) + 1).trim();
                    if (valueOfNewVariable.endsWith(SIGN_END_LINE)) {
                        valueOfNewVariable = valueOfNewVariable.substring(0, valueOfNewVariable.length() - 1);
                    }
                    String valueWithoutSpaces = valueOfNewVariable.replace(" ", "");
                    if (!String.valueOf(valueWithoutSpaces.charAt(0)).matches(REGX_TEST_OLD_VAR) &&
                            !valueWithoutSpaces.equals(TYPE_VAR_BOOLEAN_2) &&
                            !valueWithoutSpaces.equals(TYPE_VAR_BOOLEAN_1)) {
                        copyVariable = valueOfNewVariable;
                        String temp =  findAcsessVariable(scope, copyVariable, typeOfNewVariable,
                                Arrays.toString(nameOfNewVariable));
                        if(temp == null){
                            return false;
                        }
                        valueOfNewVariable = temp;
                    }
                }

                if(initalizeOfNewVariable == 0) {
                    boolean AssignmentValid = initalizeAndAssginAllNewVar(nameOfNewVariable,
                            scope, valueOfNewVariable, variableFinal, typeOfNewVariable, copyVariable);
                    if(!AssignmentValid) { return false; }
                }
                else {
                    boolean AssignmentValid = allAssginVaribale(scope, valueOfNewVariable, nameOfNewVariable);
                    if(!AssignmentValid) { return false; }
                }
            }
        }
        else { return false; }
        return true;
    }

    /**
     * Finds a variable's value in the current or parent scopes.
     *
     * @param currentScope the current scope in which the variable is being searched for.
     * @param varName the name of the variable.
     * @param typeVar the type of the variable.
     * @param varToAssignName the name of the variable to be assigned a value.
     * @return the value of the variable if found, null otherwise.
     */
    public static String findAcsessVariable(String currentScope, String varName,
                                            String typeVar, String varToAssignName){
        String[] parts = currentScope.split(SEPERATE_SCOPE_AND_NUMBER);
        int number = Integer.parseInt(parts[1]); String functionName = parts[0];
        for (int i = number; i >= 0; i--) {
            if (i == 0) { functionName = NAME_FIRST_SCOPE; }
            ValueDataStructure scopeData = scopesMap.get(functionName + SEPERATE_SCOPE_AND_NUMBER + i);
            while (scopeData == null && i > 0) {
                i--; if (i == 0) { functionName = NAME_FIRST_SCOPE; }
                scopeData = scopesMap.get(functionName + SEPERATE_SCOPE_AND_NUMBER + i);
                if(scopeData == null && i == 0) {
                    throw new NoMatchVarException(varToAssignName + NO_MATCH_VAR_MSG);
                }
            }
            if (scopeData != null && scopeData.getNames().contains(varName)) {
                int index = scopeData.getNames().indexOf(varName);
                String newValue = scopeData.getValues().get(index);
                if(newValue == null) {
                    throw new AssigmentNullException(ASSIGN_NULL_MSG + varToAssignName);
                }
                String temp = " "+SIGN_BEFORE_VALUE+" " + newValue;
                if (!temp.matches(DataTypes.DataType.getVariableValue(typeVar))) {
                    throw new BadValueAssigmentException(INVALID_ASSIGN_VALUE_MSG + varToAssignName);
                }
                return newValue;
            }
        }
        throw new NoMatchVarException(varToAssignName + NO_MATCH_VAR_MSG);
    }


    /**
     * Assigns values to multiple variables within the same scope.
     *
     * @param scope the scope in which the variables are being assigned.
     * @param valueOfNewVariable the value to assign to the variables.
     * @param nameOfNewVariable the names of the variables to be assigned the value.
     * @return true if the assignment is valid for all variables, false otherwise.
     */
    private static boolean allAssginVaribale(String scope, String valueOfNewVariable,
                                             String[] nameOfNewVariable){
        for (String name : nameOfNewVariable) {
            String[] parts = scope.split(SEPERATE_SCOPE_AND_NUMBER);
            int number = Integer.parseInt(parts[1]);
            String functionName = parts[0];
            boolean AssignmentValid = onlyAssginOneVaribale(scope,
                    number, functionName, name, valueOfNewVariable);
            if(!AssignmentValid) { return false; }
        }
        return true;
    }

    /**
     * Assigns a value to a single variable within the specified scope and function.
     *
     * @param scope the scope in which the variable is being assigned.
     * @param number the number representing the depth of the scope.
     * @param functionName the name of the function within the scope.
     * @param name the name of the variable being assigned the value.
     * @param valueOfNewVariable the value to assign to the variable.
     * @return true if the assignment is valid, false otherwise.
     */
    private static boolean onlyAssginOneVaribale(String scope, int number, String functionName,
                                                 String name, String valueOfNewVariable){
        for (int i = number; i >= 0; i--) {
            if(i == 0) { functionName = NAME_FIRST_SCOPE; }
            ValueDataStructure scopeData = scopesMap.get(functionName + SEPERATE_SCOPE_AND_NUMBER + i);
            if (scopeData.getNames().contains(name)) {
                if(valueOfNewVariable == null) {
                    throw new AssigmentNullException(name + ILLEGAL_ASSIGN_MSG);
                }
                int index = scopeData.getNames().indexOf(name);
                if(scopeData.getFinals().get(index) == 1 && valueOfNewVariable != null) {
                    throw new AssigmentFinalException(CHANGE_FINAL_VAR_MSG + name);
                }
                String temp = " = " + valueOfNewVariable;
                if (!temp.matches(DataTypes.DataType.getVariableValue(scopeData.getTypes().get(index)))) {
                    throw new BadValueAssigmentException(INVALID_ASSIGN_VALUE_MSG + name);
                }
                if(!scope.equals(functionName + SEPERATE_SCOPE_AND_NUMBER + i)) {
                    String value = valueOfNewVariable;
                    if (valueOfNewVariable == null) {
                        value = scopeData.getValues().get(index);
                    }
                    addVariable(scope, scopeData.getNames().get(index), value,
                            scopeData.getFinals().get(index), scopeData.getTypes().get(index),
                            scopeData.getValuesIsOldVariable().get(index), false);
                    return true;
                } else {
                    if (valueOfNewVariable != null) {
                        scopeData.getValues().set(index, valueOfNewVariable);
                        return true;
                    }
                }
            }
        }
        throw new NoMatchVarException(name + NO_MATCH_VAR_MSG);
    }

    /**
     * Initializes and assigns values to all new variables declared in the scope.
     *
     * @param nameOfNewVariable the names of the new variables.
     * @param scope the scope in which the variables are declared.
     * @param valueOfNewVariable the value to assign to the new variables.
     * @param variableFinal the final status of the variable (whether it's final or not).
     * @param typeOfNewVariable the type of the new variable.
     * @param copyVariable an optional reference to another variable's value.
     * @return true if the assignment is valid for all variables, false otherwise.
     */
    private static boolean initalizeAndAssginAllNewVar(
            String[] nameOfNewVariable, String scope, String valueOfNewVariable,
            int variableFinal, String typeOfNewVariable, String copyVariable){
        for (String name : nameOfNewVariable) {
            boolean AssignmentValid = addVariable(scope, name, valueOfNewVariable, variableFinal,
                    typeOfNewVariable, copyVariable, true);
            if(!AssignmentValid) {
                return false;
            }
        }
        return true;
    }

    /**
     * Adds a new scope to the map if it does not already exist.
     *
     * @param scopeName the name of the scope to be added.
     */
    private static void addScope(String scopeName) {
        if (!scopesMap.containsKey(scopeName)) {
            scopesMap.put(scopeName, new ValueDataStructure(new ArrayList<>(), new ArrayList<>(),
                    new ArrayList<>(), scopeName, new ArrayList<>(), new ArrayList<>()));
        }
    }


    /**
     * Adds a new variable to the specified scope.
     *
     * @param scopeName the name of the scope to which the variable is being added.
     * @param variableName the name of the variable.
     * @param valueOfNewVariable the value to assign to the variable.
     * @param variableFinal the final status of the variable (whether it's final or not).
     * @param typeOfNewVariable the type of the variable.
     * @param copyVariable an optional reference to another variable's value.
     * @param test flag to indicate whether the variable is being tested for duplication.
     * @return true if the variable was successfully added, false if it already exists.
     */
    private static boolean addVariable(String scopeName, String variableName,
                                       String valueOfNewVariable, int variableFinal,
                                       String typeOfNewVariable, String copyVariable, boolean test) {
        ValueDataStructure scope = scopesMap.get(scopeName);
        if(scope.getNames().contains(variableName) && test){
            throw new DoubleNameVarDeclarition(TRY_ADD_SAME_VAR_MSG_1 +
                    variableName + TRY_ADD_SAME_VAR_MSG_2);
        }
        if (scope != null) {
            scope.getNames().add(variableName);
            scope.getFinals().add(variableFinal);
            scope.getValues().add(valueOfNewVariable);
            scope.getTypes().add(typeOfNewVariable);
            scope.getValuesIsOldVariable().add(copyVariable);
        }
        return true;
    }
}