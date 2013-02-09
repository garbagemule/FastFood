package garbagemule.util.syml.parser.token;

public class KeyToken extends StringToken {
    public KeyToken(String key, String line, int lineNumber, int columnNumber) {
        super(key, line, lineNumber, columnNumber);
    }
    
    @Override
    public TokenType getType() {
        return TokenType.KEY;
    }

    @Override
    public <T> T accept(TokenVisitor<T> visitor) {
        return visitor.visitKey(this);
    }
}
