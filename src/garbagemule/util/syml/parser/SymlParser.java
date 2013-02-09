package garbagemule.util.syml.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import garbagemule.util.syml.SymlNode;
import garbagemule.util.syml.parser.token.*;

public class SymlParser implements TokenVisitor<Void> {
    private TokenStream tokens;
    private List<TokenType> expected;
    
    private StringBuilder comment;
    private SymlNode node;
    private SymlNode parent;
    
    private int level;
    private Map<Integer,Integer> indents;

    /**
     * Populate a given SymlNode from a given TokenStream.
     * The input stream must be valid, i.e. it must be non-null and
     * must constitute a valid SYML token sequence, for the method
     * not to throw any exceptions.
     * @param stream a TokenStream
     * @param root a SymlNode which will act as a root node
     * @throws NullPointerException if any parameter is null
     */
    public void parse(TokenStream stream, SymlNode root) {
        if (stream == null || root == null) {
            throw new NullPointerException();
        }
        tokens   = stream;
        parent   = root;
        expected = new ArrayList<TokenType>();

        level = 0;
        indents = new TreeMap<Integer,Integer>();
        indents.put(0, 0);
        
        setExpected(TokenType.INDENT);
        
        while (tokens.hasNext()) {
            tokens.next().accept(this);
        }
    }

    @Override
    public Void visitIndent(IndentToken token) {
        assertExpected(token);
        
        // If we're dealing with a comment, just smile and wave
        if (tokens.peek().getType() == TokenType.COMMENT) {
            setExpected(TokenType.COMMENT);
            return null;
        }
        
        // If we're dealing with list items, don't do peep!
        if (tokens.peek().getType() == TokenType.LISTITEM) {
            setExpected(TokenType.LISTITEM);
            return null;
        }
        
        // Get the indent amount of the token
        int newIndent = token.value().length();
        
        // And the current indent amount
        int currentIndent = indents.get(level);
        
        // Higher value -> deeper indent level
        if (newIndent > currentIndent) {
            if (node == null || node.get() != null) {
                error("Indent mismatch 1! Expected " + currentIndent + " spaces, but found " + newIndent, token);
            }
            level++;
            indents.put(level, newIndent);
            parent = node;
        } else if (newIndent < currentIndent) {
            int originalIndent = currentIndent;
            while (newIndent < currentIndent) {
                parent = parent.getParent();
                level--;
                currentIndent = indents.get(level);
            }
            if (newIndent != currentIndent) {
                error("Indent mismatch 2! Expected " + originalIndent + " spaces, but found only " + newIndent, token);
            }
        }
        
        setExpected(TokenType.KEY, TokenType.COMMENT, TokenType.LISTITEM);
        return null;
    }

    @Override
    public Void visitComment(CommentToken token) {
        assertExpected(token);
        
        if (comment == null) {
            comment = new StringBuilder();
        }
        
        // Extract the actual comment
        String str = token.value();
        
        // Distinguish between empty lines and empty comment-lines
        if (str != null) {
            comment.append(str.equals("") ? " " : str);
        }
        comment.append('\n');
        
        setExpected(TokenType.LINEBREAK);
        return null;
    }

    @Override
    public Void visitKey(KeyToken token) {
        assertExpected(token);
        
        // Create the node as a child of the current parent
        node = parent.createChild(token.value());
        
        // Consume comment, if any
        if (comment != null) {
            node.setComment(comment.toString());
            comment = null;
        }
        
        setExpected(TokenType.COLON);
        return null;
    }

    @Override
    public Void visitValue(ValueToken token) {
        assertExpected(token);
        
        // Trim the whitespace off
        String trim = token.value().trim();
        
        // Prepare the data object
        Object data = null;
        
        // The following blob is okay because FUCK YOU, that's why!
        if ((data = tryBoolean(trim)) != null) ;
        else if ((data = tryNumber(trim)) != null) ;
        else data = trim;
        
        // Set the data
        node.set(data);
        
        setExpected(TokenType.LINEBREAK);
        return null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Void visitListItem(ListItemToken token) {
        assertExpected(token);
        
        Object data = node.get();
        if (data == null) {
            List<String> list = new ArrayList<String>();
            list.add(token.value());
            node.set(list);
        } else {
            if (!(data instanceof List<?>)) {
                error("Unexpected data object in node.", token);
            }
            List<String> list = (List<String>) data;
            list.add(token.value());
        }
        
        setExpected(TokenType.LINEBREAK);
        return null;
    }

    @Override
    public Void visitLineBreak(LineBreakToken token) {
        assertExpected(token);
        
        setExpected(TokenType.INDENT);
        return null;
    }

    @Override
    public Void visitColon(ColonToken token) {
        assertExpected(token);
        
        setExpected(TokenType.VALUE, TokenType.LINEBREAK);
        return null;
    }
    
    private Boolean tryBoolean(String s) {
        s = s.toLowerCase();
        if (s.equals("true")) {
            return true;
        } else if (s.equals("false")) {
            return false;
        }
        return null;
    }
    
    private Number tryNumber(String s) {
        // Allow the minus, but skip it
        int start = (s.startsWith("-") ? 1 : 0);
        
        // Set to true if a period is found
        boolean seenPeriod = false;
        
        // True, if we may need a long
        boolean useLong = (s.length() - start) > 9;
        
        for (int i = start; i < s.length(); i++) {
            char c = s.charAt(i);
            
            if (c == '.') {
                if (seenPeriod) {
                    return null;
                }
                seenPeriod = true;
                continue;
            }
            if (!Character.isDigit(s.charAt(i))) {
                return null;
            }
        }
        if (seenPeriod) return Double.parseDouble(s);
        if (useLong)    return Long.parseLong(s);
        return Integer.parseInt(s);
    }
    
    private void setExpected(TokenType... types) {
        expected.clear();
        for (TokenType type : types) {
            expected.add(type);
        }
    }
    
    private void assertExpected(Token token) {
        TokenType type = token.getType();
        if (!expected.contains(type)) {
            if (expected.size() == 1) {
                error("Expected " + expected.get(0) + ", but found " + type, token);
            } else {
                String all = "";
                for (TokenType t : expected) all += t;
                error("Expected one of " + all + ", but found " + type, token);
            }
        }
    }
    
    private void error(String msg, Token token) {
        throw new ParseException(msg, token);
    }
}
