package garbagemule.util.syml.parser.token;

public interface TokenVisitor<T> {
    public T visitIndent(IndentToken token);
    
    public T visitLineBreak(LineBreakToken token);
    
    public T visitComment(CommentToken token);
    
    public T visitKey(KeyToken token);
    
    public T visitValue(ValueToken token);
    
    public T visitListItem(ListItemToken token);
    
    public T visitColon(ColonToken token);
}
