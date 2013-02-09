package garbagemule.util.syml.parser;

import garbagemule.util.syml.parser.token.Token;

@SuppressWarnings("serial")
public class ParseException extends RuntimeException {
    private Token token;
    
    public ParseException(String msg, Token token) {
        super(msg);
        this.token = token;
    }

    /**
     * Get the line snip in the input text.
     * @return the line on which the problem exists
     */
    public String getLine() {
        return token.getLine();
    }

    /**
     * Get the line number.
     * @return the line number
     */
    public int getLineNumber() {
        return token.getLineNumber();
    }

    /**
     * Get the column number.
     * @return the column number
     */
    public int getColumn() {
        return token.getColumnNumber();
    }
    
    @Override
    public String getLocalizedMessage() {
        // Create the info string
        String info = "on line " + token.getLineNumber() + ", column " + token.getColumnNumber() + ":";
        
        // Create the arrow (from 1 to avoid column-1)
        StringBuilder buffy = new StringBuilder();
        for (int i = 1; i < token.getColumnNumber(); i++) {
            buffy.append(' ');
        }
        buffy.append('^');
        
        // Create the error message
        return super.getMessage() + "\n" + info + "\n" + token.getLine() + "\n" + buffy.toString();
    }
}
