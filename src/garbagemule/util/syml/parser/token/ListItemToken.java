package garbagemule.util.syml.parser.token;

public class ListItemToken extends StringToken {
    public ListItemToken(String item, String line, int lineNumber, int columnNumber) {
        super(item, line, lineNumber, columnNumber);
    }

    @Override
    public TokenType getType() {
        return TokenType.LISTITEM;
    }

    @Override
    public <T> T accept(TokenVisitor<T> visitor) {
        return visitor.visitListItem(this);
    }
}
