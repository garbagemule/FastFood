package garbagemule.util.syml.parser.token;

public class CommentToken extends StringToken {
    public CommentToken(String comment, String line, int lineNumber, int columnNumber) {
        super(comment, line, lineNumber, columnNumber);
    }
    
    @Override
    public TokenType getType() {
        return TokenType.COMMENT;
    }

    @Override
    public <T> T accept(TokenVisitor<T> visitor) {
        return visitor.visitComment(this);
    }
}
