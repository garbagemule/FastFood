package garbagemule.util.syml.parser.token;

public class IndentToken extends StringToken {
    public IndentToken(String s, String line, int lineNumber, int columnNumber) {
        super(s, line, lineNumber, columnNumber);
    }

    @Override
    public TokenType getType() {
        return TokenType.INDENT;
    }
    
    @Override
    public String toString() {
        return "INDENT[amount=" + value().length() + "]";
    }

    @Override
    public <T> T accept(TokenVisitor<T> visitor) {
        return visitor.visitIndent(this);
    }
}
