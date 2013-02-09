package garbagemule.util.syml.parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import garbagemule.util.syml.parser.token.*;

public class SymlLexer {
    private TokenStream stream;
    private List<String> input;
    private Iterator<String> iterator;
    private String line;
    private int position, lineCount;
    
    /**
     * Generate a TokenStream from an input String list.
     * <p>Each string in the list represents a line in a SYML file.
     * <p>The input list must be valid, i.e. it must be non-null and
     * must constitute a valid SYML document, for the method not to
     * throw any exceptions.
     * @param lines the input String list
     * @return a stream of tokens generated from the input list
     * @throws NullPointerException if the list is null
     */
    public TokenStream lex(List<String> lines) {
        stream    = new TokenStream();
        input     = lines;
        lineCount = 0;
        
        // Grab the iterator on the list
        iterator = input.iterator();
        
        // Lex all the lines
        while (iterator.hasNext()) {
            line();
        }
        
        // Return the stream
        return stream;
    }

    /**
     * Generate a TokenStream from an input String.
     * <p>The input String must be valid, i.e. it must be non-null and
     * must constitute a valid SYML document, for the method not to
     * throw any exceptions.
     * @param input the input String
     * @return a stream of tokens generated from the input String
     * @throws NullPointerException if the String is null
     */
    public TokenStream lex(String input) {
        List<String> lines = new ArrayList<String>();
        BufferedReader br = new BufferedReader(new StringReader(input));
        String line;
        try {
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
            return lex(lines);
        }
        catch (IOException e) {
            return null;
        }
    }
    
    /**
     * precondition: the iterator has more elements
     * postcondition: the iterator has one fewer elements
     */
    private void line() {
        // Grab the next line and reset the position
        line = iterator.next();
        position = -1;
        lineCount++;
        
        // Grab the indent first
        indent();
        
        // Special case - completely empty line -> null comment
        if (position == -1 || isRestWhitespace()) {
            stream.add(new CommentToken(null, line, lineCount, (position >= 0 ? position : 0)));
            stream.add(new LineBreakToken(line, lineCount, position));
            return;
        }
        
        // Then the comment, listitem or node
        switch (current()) {
            case '#': comment();  break;
            case '-': listitem(); break;
            default:  node();     break;
        }
        
        // Finally, the actually linebreak
        stream.add(new LineBreakToken(line, lineCount, position));
    }
    
    /**
     * precondition: current position is before the start of a new line (-1)
     * postcondition: current char is not a space or a tab
     */
    private void indent() {
        StringBuilder buffy = new StringBuilder();
        
        while (hasNext()) {
            char c = next();
            if (c == ' ') {
                buffy.append(c);
            } else if (c == '\t') {
                int length = 4 - (buffy.length() % 4);
                for (int i = 0; i < length; i++) {
                    buffy.append(' ');
                }
            } else {
                break;
            }
        }
        
        stream.add(new IndentToken(buffy.toString(), line, lineCount, position));
    }
    
    /**
     * precondition: current position is at a hash tag
     * postcondition: the line will have been completely consumed
     */
    private void comment() {
        // In case of empty comment lines, just add and return
        if (!hasNext()) {
            stream.add(new CommentToken("", line, lineCount, 0));
            return;
        }
        
        // Otherwise, skip the hash tag
        int start = position;
        next();
        
        // Consume the rest of the line
        String comment = consume();
        
        // Add the comment token
        stream.add(new CommentToken(comment, line, lineCount, start));
    }
    
    /**
     * precondition: current position is at a hyphen
     * postcondition: the line will have been completey consumed
     */
    private void listitem() {
        int start = position;
        
        // Skip the hyphen, and require a space before the first symbol
        if (next() != ' ') {
            error("List items must have a space between the hyphen and the first symbol.");
        }
        
        // Advance past the space
        next();
        
        // Consume the rest of the line
        String listitem = consume();
        
        // Add the listitem token
        stream.add(new ListItemToken(listitem, line, lineCount, start));
    }
    
    /**
     * precondition: current position is at a symbol (not # or -)
     * postcondition: the line will have been completely consumed
     */
    private void node() {
        // Grab the string up to the colon
        int start = position;
        for (char c = current(); hasNext(); c = next()) {
            if (c == ':') break;
            if (c == '#') error("Illegal character in key. The pound sign # is reserved for comments.");
        }
        
        // Verify that there is, in fact, a colon now
        if (current() != ':') {
            position = line.replaceAll("\\s+$", "").length(); // Trim trailing whitespace
            error("A key must always be followed by a colon.");
        }
        
        // If so, extract the key string.
        String key = line.substring(start, position);
        
        // Add the tokens
        stream.add(new KeyToken(key, line, lineCount, start));
        stream.add(new ColonToken(line, lineCount, position));
        
        // If no more symbols, or only whitespace, return
        if (!hasNext() || isRestWhitespace()) {
            return;
        }
        
        // Otherwise, advance past the colon, and require a space
        if (next() != ' ') {
            error("There must be a space between the colon and the value.");
        }
        
        // Advance past the space
        next();
        
        // Consume the remaining part of the line, and trim off whitespace
        start = position;
        String value = consume().trim();
        
        // If it's just whitespace, ignore it
        if (value.length() == 0) {
            return;
        }
        
        // Otherwise, make a value token
        stream.add(new ValueToken(value, line, lineCount, start));
    }
    
    private String consume() {
        String rest = line.substring(position);
        position = rest.length();
        return rest;
    }
    
    private boolean hasNext() {
        return position < (line.length() - 1);
    }
    
    private char next() {
        return line.charAt(++position);
    }
    
    private char current() {
        return line.charAt(position);
    }
    
    private boolean isRestWhitespace() {
        return line.substring(position).trim().length() == 0;
    }
    
    private void error(String msg) {
        throw new LexException(msg, input, line, lineCount, position + 1);
    }
}
