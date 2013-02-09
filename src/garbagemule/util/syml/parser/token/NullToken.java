package garbagemule.util.syml.parser.token;

public abstract class NullToken extends AbstractToken {
    public NullToken(String line, int lineNumber, int columnNumber) {
        super(line, lineNumber, columnNumber);
    }

    @Override
    public String value() {
        return null;
    }
    
    @Override
    public String toString() {
        return getType().toString();
    }
}
