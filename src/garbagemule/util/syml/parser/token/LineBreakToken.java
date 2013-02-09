package garbagemule.util.syml.parser.token;

public class LineBreakToken extends NullToken {
    public LineBreakToken(String line, int lineNumber, int columnNumber) {
        super(line, lineNumber, columnNumber);
    }

    @Override
    public TokenType getType() {
        return TokenType.LINEBREAK;
    }

    @Override
    public <T> T accept(TokenVisitor<T> visitor) {
        return visitor.visitLineBreak(this);
    }
}
