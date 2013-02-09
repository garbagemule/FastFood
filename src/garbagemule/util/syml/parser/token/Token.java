package garbagemule.util.syml.parser.token;

public interface Token {
    public String value();
    public TokenType getType();
    public <T> T accept(TokenVisitor<T> visitor);
    
    public String getLine();
    public int getLineNumber();
    public int getColumnNumber();
}
