package ex5.main;

import ex5.function.CallFunction;
import ex5.function.Function;
import ex5.ifAndWhile.IfAndWhile;
import ex5.variable.DataTypes;
import ex5.variable.Variable;
import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The Sjavac class is a program that processes a given code file and verifies
 various aspects of its structure and syntax.
 * It checks for correct function declarations, variable usage,
 and the proper order of statements in the code.
 * It also ensures that parentheses are balanced and that functions return values where necessary.
 */
public class Sjavac {
    private Parser parserOfBegin;
    private Parser parser;

    /**
     * The number of error messages encountered during the execution.
     */
    public static int countErrorMsg = 0;
    private static final String NAME_FIRST_SCOPE = "global";
    private static final String CONSTANT_VAR_KEYWORD = "final";
    private static final String REGX_END_TOKEN = "\\s*}\\s*";
    private static final String END_TOKEN = "}";
    private static final String BEGIN_TOKEN = "{";
    private static final String REGX_EMPTY_LINE = "^(:?(\\s*//\\s*.*)| (\\s*)|( \\t*))";
    private static final String REGX_LEAST_ONE_SPACE = "\\s+";
    private static final String REGX_RETURN_LINE ="\\s*return\\s*;\\s*";
    private static final String REGX_FUNCTION_1 = "\\s*=\\s*.*";
    private static final String REGX_FUNCTION_2 = "\\s*=\\s*.";
    private static final String REGX_FUNCTION_3 = "\\s*=\\s*";
    private static final String TYPE_VAR_TO_IF_OR_WHILE = "boolean";
    private static final String SIGN_BEFORE_VALUE = "=";
    private static final String SEPERATE_SCOPE_AND_NUMBER = "_";
    private static final String TYPE_VAR_TO_NEW_VALUE_1 = "char";
    private static final String TYPE_VAR_TO_NEW_VALUE_2 = "String";
    private static final String NEW_VALID_VALUE = "=0;";
    private static final String NEW_VALID_VALUE_CHAR = "='0';";
    private static final String NEW_VALID_VALUE_STRING = "=\"0\";";
    private static final String STRING_BAD_ACTION_IN_LINE_MSG_1 =
            "dont have format of order statement (line ";
    private static final String STRING_BAD_ACTION_IN_LINE_MSG_2 = ")";
    private static final String DECLARE_FUNCTION_IN_FUNCTION = "declare function in function(line ";
    private static final String PARHTNESS_PROBLEM_MSG = "parhtness problem";
    private static final String FUNCTION_RETURN_MSG = " function not have return statement";
    private static final String STRING_BAD_ACTION_IF_OR_WHILE_MSG = "dont have format if or while" +
            " order statement (line ";
    private static final String STRING_BAD_ACTION_CALL_FUNC_MSG ="dont have format of call" +
            " function order statement (line ";
    private static final String DONT_MATCH_COUNT_VAR_FUNCTION_MSG =
            "dont match count of variables to variables function ";
    private static final String DONT_MATCH_COUNT_VAR_FUNCTION_MSG_2 = " to variables function " ;
    private static final String NOT_VALID_ASSIGNMENT_MSG = "not valid assignment in function ";
    private static final String NOT_VALID_RETURN_MSG = "return is not in the end of function ";
    private static final String FUNCTION_DECLARE_THAT_EXSIST_MSG = "you try to declare ";
    private static final String FUNCTION_DECLARE_THAT_EXSIST_MSG_2 = " and the function is exsist";
    private static final String IO_ERR_MSG_1 = "Illegal number of arguments. Please provide" +
            " exactly one argument for the file path.";
    private static final String IO_ERR_MSG_2 = "Wrong file format. The file must have a .sjava extension.";
    private static final String IO_ERR_MSG_3 = "Invalid file name. The specified" +
            " file does not exist or is not a valid file.";
    private static final String IO_ERR_MSG_4 = "File not found: ";
    private static final String IO_ERR_MSG_5 = "Error while reading or writing: ";
    private static final String TYPE_OF_FILE = ".sjava";
    private static final int COUNT_ARG_VALID = 1;
    private static final int IO_OUTPUT = 2;
    /**
     * The entry point of the program. It accepts the file path as a command line argument,
     * processes the file, and exits with an appropriate status code based
     on the success or failure of the checks.
     *
     * @param args Command line arguments, expects a single argument for the file path.
     */
    public static void main(String[] args) throws IOException {
        if(args.length!=COUNT_ARG_VALID){
            System.out.println(IO_OUTPUT);
            throw new IOException(IO_ERR_MSG_1);
        }
        String inputFilePath = args[0];
        if (!inputFilePath.endsWith(TYPE_OF_FILE)) {
            System.out.println(IO_OUTPUT);
            throw new IOException(IO_ERR_MSG_2);
        }
        File inputFile = new File(inputFilePath);
        if (!inputFile.exists() || !inputFile.isFile()) {
            System.out.println(IO_OUTPUT);
            throw new IOException(IO_ERR_MSG_3);
        }
        Sjavac sjavac = new Sjavac();
        int returnValue = sjavac.testCode(args[0]);
        System.out.println(returnValue);
    }

    /**
     * Processes the input file, performing a series of validation checks in two runs:
     one for the initial structure and
     * one for a deeper analysis of function calls, variable declarations, and control structures.
     *
     * @param inputFilePath The file path of the code to be processed.
     * @return 0 if no errors were found, 1 if errors were encountered.
     */
    private int testCode(String inputFilePath) {

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFilePath))) {
            parserOfBegin = new Parser(reader);
            parser = Parser.createParserCopy(parserOfBegin);
        } catch (FileNotFoundException e) {
            System.out.println(IO_ERR_MSG_4 + e.getMessage());
        } catch (IOException e) {
            System.out.println(IO_ERR_MSG_5 + e.getMessage());
        }
        Pattern patternVar = Variable.createPatternVar();
        int countOfOpenParhtness = 0; String functionName = NAME_FIRST_SCOPE;
        firstRunOfCode(patternVar, countOfOpenParhtness, functionName);
        testEndCodeParhtness(countOfOpenParhtness);
        secondRunOnCode(patternVar, countOfOpenParhtness, functionName);
        return (countErrorMsg > 0) ? 1 : 0;
    }

    /**
     * Validates that a line of code contains one of
     the allowed types of statements: variable declaration, function call,
     * or control structures like if/while. If none of these are found, it reports an error.
     *
     * @param variableDeclareLine Whether the line contains a valid variable declaration.
     * @param callFunctionDeclareLine Whether the line contains a valid function call.
     * @param ifAndWhileDeclareLine Whether the line contains a valid if/while statement.
     * @param parser The parser that is used to get the current line.
     */
    private void testValidActionDoInLine(boolean variableDeclareLine, boolean callFunctionDeclareLine,
                                         boolean ifAndWhileDeclareLine, Parser parser) {
        if (!(variableDeclareLine || callFunctionDeclareLine ||
                ifAndWhileDeclareLine) && countErrorMsg == 0) {
            throw new BadActionInLIne(STRING_BAD_ACTION_IN_LINE_MSG_1 + parser.getNumberLine()
                    + STRING_BAD_ACTION_IN_LINE_MSG_2);
        }
    }

    /**
     * Checks that a function is not declared inside another function.
     *
     * @param functionDeclareLine Whether the current line is a function declaration.
     * @param countOfOpenParhtness The current count of open parentheses.
     * @param parser The parser that is used to get the current line.
     */
    private void testDeclareFunctionInFunction(boolean functionDeclareLine,
                                               int countOfOpenParhtness, Parser parser){
        if ((functionDeclareLine) && countOfOpenParhtness > 1) {
            throw new DeclareFunctionInFunction(DECLARE_FUNCTION_IN_FUNCTION + parser.getNumberLine()
                    + STRING_BAD_ACTION_IN_LINE_MSG_2);
        }
    }

    /**
     * Checks for any parentheses issues. Specifically,
     if the parentheses count goes below 0, it reports an error.
     *
     * @param countOfOpenParhtness The current count of open parentheses.
     */
    private void testParhtness(int countOfOpenParhtness) {
        if (countOfOpenParhtness < 0) {
            throw new BadParhtness(PARHTNESS_PROBLEM_MSG);
        }
    }

    /**
     * Verifies that the code ends with balanced parentheses.
     *
     * @param countOfOpenParhtness The final count of open parentheses after processing the code.
     */
    private void testEndCodeParhtness(int countOfOpenParhtness) {
        if (countOfOpenParhtness != 0) {
            throw new BadParhtness(PARHTNESS_PROBLEM_MSG);
        }
    }

    /**
     * Checks that a function contains a return statement.
     *
     * @param countOfOpenParhtness The current count of open parentheses.
     * @param parser The parser that is used to get the current line.
     */
    private void testFunctionReturn(int countOfOpenParhtness, Parser parser) {
        if (parser.getCurrentLine().matches(REGX_END_TOKEN) && countOfOpenParhtness == 0) {
            throw new NoReturnException(FUNCTION_RETURN_MSG);
        }
    }

    /**
     * Calculates the number of open parentheses based on the current line's content.
     *
     * @param countOfOpenParhtness The current count of open parentheses.
     * @param parser The parser that is used to get the current line.
     * @return The updated count of open parentheses.
     */
    private int calculateParhtness(int countOfOpenParhtness, Parser parser) {
        if (parser.getCurrentLine().contains(BEGIN_TOKEN)) { countOfOpenParhtness++; }
        if (parser.getCurrentLine().contains(END_TOKEN)) { countOfOpenParhtness--; }
        return countOfOpenParhtness;
    }

    /**
     * Determines if the current line is irrelevant for processing in the second run.
     * Irrelevant lines include comments, empty lines, and closing brackets for the global scope.
     *
     * @param parser The parser that is used to get the current line.
     * @param functionName The name of the current function.
     * @param functionDeclareLine Whether the line declares a function.
     * @return True if the line is irrelevant, false otherwise.
     */
    private boolean notRelvantLineSecondRun(Parser parser, String functionName, boolean functionDeclareLine) {
        boolean noteOrEmptyLine = parser.getCurrentLine().matches(REGX_EMPTY_LINE) ||
                parser.getCurrentLine().isEmpty();
        if ((noteOrEmptyLine) || !((functionName.equals(NAME_FIRST_SCOPE) && (noteOrEmptyLine ||
                parser.getCurrentLine().matches(REGX_END_TOKEN))) ||
                (!functionName.equals(NAME_FIRST_SCOPE) && !functionDeclareLine))
                || (noteOrEmptyLine || parser.getCurrentLine().matches(REGX_END_TOKEN))) {
            return true;
        }
        return false;
    }

    /**
     * Determines if the current line is irrelevant for processing in the first run.
     *
     * @param functionName The name of the current function.
     * @param functionDeclareLine Whether the line declares a function.
     * @return True if the line is irrelevant, false otherwise.
     */
    private boolean notRelvantLineFirstRun(String functionName, boolean functionDeclareLine) {
        boolean noteOrEmptyLine = parserOfBegin.getCurrentLine().matches(REGX_EMPTY_LINE)
                || parserOfBegin.getCurrentLine().isEmpty();
        if ((functionName.equals(NAME_FIRST_SCOPE) &&
                (noteOrEmptyLine || parserOfBegin.getCurrentLine().matches(REGX_END_TOKEN)))
                || (!functionName.equals(NAME_FIRST_SCOPE) && !functionDeclareLine)) {
            return true;
        }
        return false;
    }

    /**
     * Validates the conditions within if and while statements.
     * Ensures that the conditions are boolean expressions.
     *
     * @param ifAndWhileStatmentTest The IfAndWhile object containing the statement to check.
     * @param scope The current scope of the program (e.g., global or function).
     */
    private void testValidIfWhileStatement(IfAndWhile ifAndWhileStatmentTest, String scope) {
        String[] arrayOfConditionStatement = ifAndWhileStatmentTest.getVariables();
        for (int i = 0; i <= arrayOfConditionStatement.length - 1; i++) {
            String value = arrayOfConditionStatement[i];
            if (value.matches(DataTypes.PATTERN_NAME_VARIABLE)) {
                value = Variable.findAcsessVariable(scope, value, TYPE_VAR_TO_IF_OR_WHILE, value);
            }
            String temp = SIGN_BEFORE_VALUE + value;
            if (!temp.matches(DataTypes.DataType.getVariableValue(TYPE_VAR_TO_IF_OR_WHILE))) {
                throw new BadIfOrWhileStatment(STRING_BAD_ACTION_IF_OR_WHILE_MSG + parser.getNumberLine()
                        + STRING_BAD_ACTION_IN_LINE_MSG_2);
            }
        }
    }

    /**
     * Validates the parameters in a function call.
     * Ensures that the number of arguments matches the expected number and that the types are correct.
     *
     * @param testCallFunction The CallFunction object containing the function call to check.
     * @param scope The current scope of the program (e.g., global or function).
     */
    private void testValidCallFunctionStatement(CallFunction testCallFunction, String scope) {
        String[] arrayToAssign = testCallFunction.getLinesOfVariable();
        if (arrayToAssign == null) {
            throw new CallFunctionBadCountVars(DONT_MATCH_COUNT_VAR_FUNCTION_MSG +
                    DONT_MATCH_COUNT_VAR_FUNCTION_MSG_2 + testCallFunction.getFunctionName());
        }
        for (String s : arrayToAssign) {
            if (s.matches(REGX_FUNCTION_1) || s.split(SIGN_BEFORE_VALUE).length <= 1) {
                if ((s.matches(REGX_FUNCTION_2)) || ((s.split(SIGN_BEFORE_VALUE).length <= 1) &&
                        !s.matches(REGX_FUNCTION_3))) {
                    throw new BadAssigmentInFunc(NOT_VALID_ASSIGNMENT_MSG +
                            testCallFunction.getFunctionName());
                }
                if (s.length() > 1 && s.split(SIGN_BEFORE_VALUE)[0].isEmpty()
                        && !s.split(SIGN_BEFORE_VALUE)[1].isEmpty()) {
                    throw new BadAssigmentInFunc(NOT_VALID_ASSIGNMENT_MSG
                            + testCallFunction.getFunctionName());
                }
                continue;
            }
            String type = getFirstNonFinalWord(s); String value = s.split(SIGN_BEFORE_VALUE)[1];
            if (value.matches(DataTypes.PATTERN_NAME_VARIABLE)) {
                value = Variable.findAcsessVariable(scope, value, type, s.split(SIGN_BEFORE_VALUE)[0]);
            }
            String temp = SIGN_BEFORE_VALUE + value;
            if (!temp.matches(DataTypes.DataType.getVariableValue(type))) {
                throw new BadFormatCallFunc(STRING_BAD_ACTION_CALL_FUNC_MSG + parser.getNumberLine()
                        + STRING_BAD_ACTION_IN_LINE_MSG_2);
            }
        }
    }

    /**
     * Checks if the return statement in a function is correctly placed (at the end of the function).
     *
     * @param functionName The name of the current function.
     * @param scope The current scope of the program.
     * @return True if the return statement is correctly placed, false otherwise.
     */
    private boolean testReturnCorrectPlace(String functionName, String scope) {
        if (parser.getCurrentLine().matches(REGX_RETURN_LINE)) {
            parser.next();
            while (parser.getCurrentLine().matches(Variable.IGNORE_SPACES)) {
                parser.next();
            }
            if (parser.getCurrentLine().matches(REGX_END_TOKEN)) {
                parser.next();
                return true;
            } else {
                throw new WrongPlaceReturn(NOT_VALID_RETURN_MSG + functionName);
            }
        }
        return false;
    }



    /**
     * This method represents the second pass through the code, where more detailed validation is performed.
     * The method processes each line, checking for function declarations, variable declarations,
     * correct syntax for 'if' and 'while' statements,
     function calls, and the correct usage of return statements.*
     * @param patternVar The pattern to match variable declarations.
     * @param countOfOpenParhtness The current count of open parentheses (braces).
     * @param functionName The name of the function currently being processed.
     */
    private void secondRunOnCode(Pattern patternVar, int countOfOpenParhtness, String functionName) {
        while (parser.getCurrentLine() != null) {
            String line = parser.getCurrentLine();
            Matcher matcherVar = patternVar.matcher(line);
            countOfOpenParhtness = calculateParhtness(countOfOpenParhtness, parser);

            if (countOfOpenParhtness == 0) {
                functionName = NAME_FIRST_SCOPE;
            }

            Function tempFunction = new Function(line);
            boolean functionDeclareLine = tempFunction.getIsValid();

            // Update the function name if a new function is declared.
            if (functionDeclareLine) {
                functionName = tempFunction.getFunctionName();
            }

            String scope = functionName + SEPERATE_SCOPE_AND_NUMBER + String.valueOf(countOfOpenParhtness);
            testParhtness(countOfOpenParhtness);

            // If we encounter a closing brace '}', remove the scope.
            if (parser.getCurrentLine().matches(REGX_END_TOKEN) && countOfOpenParhtness >= 1) {
                int temp = countOfOpenParhtness + 1;
                Variable.removeScope(functionName + SEPERATE_SCOPE_AND_NUMBER + temp);
            }

            // Check if function declaration is valid in a nested context.
            testDeclareFunctionInFunction(functionDeclareLine, countOfOpenParhtness, parser);

            // Check if the function has a return statement in the correct place.
            testFunctionReturn(countOfOpenParhtness, parser);

            // Skip irrelevant lines such as comments or empty lines.
            if (notRelvantLineSecondRun(parser, functionName, functionDeclareLine)) {
                parser.next();
                continue;
            }

            // Check if the line represents an 'if' or 'while' statement and validate it.
            IfAndWhile ifAndWhileStatmentTest = new IfAndWhile(line);
            boolean ifAndWhileDeclareLine = ifAndWhileStatmentTest.getIsValid();
            if (ifAndWhileDeclareLine) {
                testValidIfWhileStatement(ifAndWhileStatmentTest, scope);
            }

            // Check if the line represents a function call and validate it.
            CallFunction testCallFunction = new CallFunction(line);
            boolean callFunctionDeclareLine = testCallFunction.getIsValid() &&
                    testCallFunction.getFunctionDeclareData() != null;
            if (callFunctionDeclareLine && !ifAndWhileDeclareLine) {
                testValidCallFunctionStatement(testCallFunction, scope);
            }

            // Check if the return statement is correctly placed.
            boolean passTest = testReturnCorrectPlace(functionName, scope);
            if (passTest) {
                countOfOpenParhtness--;
                continue;
            }

            // Check if the line represents a variable declaration and validate it.
            boolean variableDeclareLine = Variable.dechiperVariableMatcher(matcherVar, line, scope);
            testValidActionDoInLine(variableDeclareLine, callFunctionDeclareLine,
                    ifAndWhileDeclareLine, parser);

            // Move to the next line.
            parser.next();
        }
    }


    /**
     * Checks if a function already exists before declaring it.
     * If a function with the same name already exists, an error is reported.
     *
     * @param tempFunction The Function object representing the function to check.
     */
    private void testFunctionExsist(Function tempFunction){
        if (tempFunction.getIsExsist()) {
            throw new ExistFunctionException(FUNCTION_DECLARE_THAT_EXSIST_MSG + tempFunction.getFunctionName()
                    + FUNCTION_DECLARE_THAT_EXSIST_MSG_2);
        }
    }


    /**
     * Handles the declaration and initialization of function variables.
     * It creates the function declaration line and processes variable initialization.
     *
     * @param functionName The name of the function in which the variables are declared.
     * @param patternVar The pattern to match variable declarations.
     * @param countOfOpenParhtness The current count of open parentheses.
     * @param varLine The variable declaration line to process.
     */
    private void functionsRowDeclareActions(String functionName,
                                            Pattern patternVar, int countOfOpenParhtness, String varLine) {
        String value = NEW_VALID_VALUE;
        if (varLine.contains(TYPE_VAR_TO_NEW_VALUE_1)) {
            value = NEW_VALID_VALUE_CHAR;
        }
        if (varLine.contains(TYPE_VAR_TO_NEW_VALUE_2)) {
            value = NEW_VALID_VALUE_STRING;
        }
        String lineOfFunction = varLine + value;
        Matcher matcherLineFunction = patternVar.matcher(lineOfFunction);
        Variable.dechiperVariableMatcher(matcherLineFunction,
                lineOfFunction, functionName + SEPERATE_SCOPE_AND_NUMBER
                        + String.valueOf(countOfOpenParhtness));
    }

    /**
     * Extracts the first non-final word from a string.
     * This is used to detect the type of a variable or function argument.
     *
     * @param input The input string to parse.
     * @return The first non-final word found in the string.
     */
    private static String getFirstNonFinalWord(String input) {
        String[] words = input.trim().split(REGX_LEAST_ONE_SPACE);
        for (String word : words) {
            if (!word.equals(CONSTANT_VAR_KEYWORD)) {
                return word;
            }
        }
        return null;
    }

    /**
     * The first pass through the code to parse function declarations,
     variable declarations, and to check for basic syntax.
     * It processes each line and applies the necessary checks for valid structure.
     *
     * @param patternVar The pattern to match variable declarations.
     * @param countOfOpenParhtness The initial count of open parentheses.
     * @param functionName The name of the current function being processed.
     */
    private void firstRunOfCode(Pattern patternVar, int countOfOpenParhtness, String functionName){
        while (parserOfBegin.getCurrentLine() != null) {
            String line = parserOfBegin.getCurrentLine();
            Matcher matcherVar = patternVar.matcher(line);
            countOfOpenParhtness = calculateParhtness(countOfOpenParhtness, parserOfBegin);
            testParhtness(countOfOpenParhtness);
            if (countOfOpenParhtness == 0) {
                functionName = NAME_FIRST_SCOPE;
            }
            Function tempFunction = new Function(line);
            boolean functionDeclareLine = tempFunction.getIsValid();
            testFunctionExsist(tempFunction);
            if (functionDeclareLine) {
                functionName = tempFunction.getFunctionName();
                for (String varLine : tempFunction.getVariables()) {
                    functionsRowDeclareActions(functionName, patternVar, countOfOpenParhtness, varLine);
                }
            }
            String scope = functionName + SEPERATE_SCOPE_AND_NUMBER + String.valueOf(countOfOpenParhtness);
            if (notRelvantLineFirstRun(functionName, functionDeclareLine)) {
                parserOfBegin.next();
                continue;
            }
            boolean variableDeclareLine = Variable.dechiperVariableMatcher(matcherVar, line, scope);
            testValidActionDoInLine(variableDeclareLine, functionDeclareLine,
                    functionDeclareLine, parserOfBegin);
            parserOfBegin.next();
        }
    }
}