package garbagemule.util.syml.parser.token;

public abstract class StringToken extends AbstractToken {
    private String s;
    
    public StringToken(String s, String line, int lineNumber, int columnNumber) {
        super(line, lineNumber, columnNumber);
        this.s = s;
    }
    
    @Override
    public String value() {
        return s;
    }
    
    @Override
    public String toString() {
        return getType() + "[\"" + s + "\"]";
    }
}
