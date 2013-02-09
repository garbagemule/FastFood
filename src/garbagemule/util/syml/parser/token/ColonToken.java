package garbagemule.util.syml.parser.token;

public class ColonToken extends NullToken {
    public ColonToken(String line, int lineNumber, int columnNumber) {
        super(line, lineNumber, columnNumber);
    }

    @Override
    public TokenType getType() {
        return TokenType.COLON;
    }

    @Override
    public <T> T accept(TokenVisitor<T> visitor) {
        return visitor.visitColon(this);
    }
}
