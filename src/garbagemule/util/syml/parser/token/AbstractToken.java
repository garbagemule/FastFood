package garbagemule.util.syml.parser.token;

public abstract class AbstractToken implements Token {
    private String line;
    private int lineNumber;
    private int columnNumber;
    
    public AbstractToken(String line, int lineNumber, int columnNumber) {
        this.line = line;
        this.lineNumber = lineNumber;
        this.columnNumber = columnNumber;
    }
    
    @Override
    public String getLine() {
        return line;
    }
    
    @Override
    public int getLineNumber() {
        return lineNumber;
    }
    
    @Override
    public int getColumnNumber() {
        return columnNumber;
    }
}
