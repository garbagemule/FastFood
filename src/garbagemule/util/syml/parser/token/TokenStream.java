package garbagemule.util.syml.parser.token;

import java.util.ArrayList;
import java.util.List;

public class TokenStream {
    private List<Token> tokens;
    private int position;
    
    public TokenStream() {
        tokens = new ArrayList<Token>();
        position = -1;
    }
    
    public void add(Token token) {
        tokens.add(token);
    }
    
    public boolean hasNext() {
        return position < tokens.size() - 1;
    }
    
    public Token next() {
        return tokens.get(++position);
    }
    
    public Token peek() {
        return tokens.get(position + 1);
    }
    
    public Token current() {
        return tokens.get(position);
    }
    
    @Override
    public String toString() {
        StringBuilder buffy = new StringBuilder();
        for (Token token : tokens) {
            buffy.append(token.toString()).append("\n");
        }
        return buffy.toString();
    }
}
