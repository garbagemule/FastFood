package garbagemule.util.syml.parser.token;

public class ValueToken extends StringToken {
    public ValueToken(String value, String line, int lineNumber, int column) {
        super(value, line, lineNumber, column);
    }
    
    @Override
    public TokenType getType() {
        return TokenType.VALUE;
    }

    @Override
    public <T> T accept(TokenVisitor<T> visitor) {
        return visitor.visitValue(this);
    }
}
