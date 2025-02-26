package ex5.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

/**
 * The {@code Parser} class is responsible for reading and parsing a file line by line.
 * It maintains the current line being processed,
 tracks the remaining lines, and provides methods to navigate through them.
 */
public class Parser {
    private String currentLine;
    private final Queue<String> remainingLines;
    private int numberLine = 1;

    /**
     * Constructs a new {@code Parser} object that reads lines from the provided {@code BufferedReader}.
     *
     * @param allText The {@code BufferedReader} from which lines are read.
     * @throws IOException If an error occurs while reading lines from the {@code BufferedReader}.
     */
    public Parser(BufferedReader allText) throws IOException {
        remainingLines = new LinkedList<>();
        String line;
        while ((line = allText.readLine()) != null) {
            remainingLines.add(line);
        }
        currentLine = remainingLines.poll();
    }

    /**
     * Constructs a new {@code Parser} object as a copy of another {@code Parser} object.
     *
     * @param otherParser The {@code Parser} object to copy.
     */
    public Parser(Parser otherParser) {
        this.remainingLines = new LinkedList<>(otherParser.remainingLines);
        this.currentLine = otherParser.currentLine;
    }

    /**
     * Returns the current line being processed by the parser.
     *
     * @return The current line.
     */
    public String getCurrentLine() {
        return currentLine;
    }

    /**
     * Returns the number of the current line being processed.
     *
     * @return The current line number.
     */
    public int getNumberLine() {
        return numberLine;
    }

    /**
     * Moves to the next line in the queue. If there are no more lines,
     the current line is set to {@code null}.
     */
    public void next() {
        if (hasNext()) {
            currentLine = remainingLines.poll();
            numberLine++;
        } else {
            currentLine = null;
        }
    }

    /**
     * Creates a copy of the given {@code Parser} object.
     *
     * @param parser The {@code Parser} object to copy.
     * @return A new {@code Parser} object that is a copy of the provided one.
     */
    public static Parser createParserCopy(Parser parser) {
        return new Parser(parser);
    }

    /**
     * Checks if there are more lines to process.
     *
     * @return {@code true} if there are more lines; {@code false} otherwise.
     */
    private boolean hasNext() {
        return !remainingLines.isEmpty();
    }
}
