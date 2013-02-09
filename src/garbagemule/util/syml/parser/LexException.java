package garbagemule.util.syml.parser;

import java.util.List;

@SuppressWarnings("serial")
public class LexException extends RuntimeException {
    private List<String> input;
    private String line;
    private int lineNumber, columnNumber;
    
    public LexException(String msg, List<String> input, String snip, int line, int column) {
        super(msg);
        this.input        = input;
        this.line         = snip;
        this.lineNumber   = line;
        this.columnNumber = column;
    }

    /**
     * Get the full input text given to the lexer.
     * @return the full input text
     */
    public List<String> getInput() {
        return input;
    }

    /**
     * Get the line snip in the input text.
     * @return the line on which the problem exists
     */
    public String getLine() {
        return line;
    }

    /**
     * Get the line number.
     * @return the line number
     */
    public int getLineNumber() {
        return lineNumber;
    }

    /**
     * Get the column number.
     * @return the column number
     */
    public int getColumn() {
        return columnNumber;
    }
    
    @Override
    public String getLocalizedMessage() {
        // Create the info string
        String info = "on line " + lineNumber + ", column " + columnNumber + ":";
        
        // Create the arrow (from 1 to avoid column-1)
        StringBuilder buffy = new StringBuilder();
        for (int i = 1; i < columnNumber; i++) {
            buffy.append(' ');
        }
        buffy.append('^');
        
        // Create the error message
        return super.getMessage() + "\n" + info + "\n" + line + "\n" + buffy.toString();
    }
}
