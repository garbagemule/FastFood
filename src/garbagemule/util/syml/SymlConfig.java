package garbagemule.util.syml;

import garbagemule.util.syml.parser.SymlLexer;
import garbagemule.util.syml.parser.SymlParser;
import garbagemule.util.syml.parser.token.TokenStream;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Scanner;

/**
 * The SymlConfig class realizes an internal representation of a config
 * file, and acts as both an entry into the root {@link SymlNode} of the
 * document, and a front end for loading documents from, and saving them
 * to, disk.
 * 
 * @author garbagemule
 * @version 0.1
 */
public class SymlConfig extends SymlNode {
    private File file;
    private String encoding;
    
    /**
     * Create a new SymlConfig with no associated file.
     * <p>Note that until the setFile(File) method has been called with a
     * non-null File object, the parameterless load() and save() methods
     * will throw IllegalStateExceptions. 
     * save() and load() will 
     */
    public SymlConfig() {
        super("");
        this.encoding = "UTF-8";
    }
    
    /**
     * Create a new SymlConfig with the given file.
     * <p>Note that the SymlConfig is not populated until either of the
     * load() methods have been called.
     * @param file the File to load from and/or save to
     */
    public SymlConfig(File file) {
        this();
        this.file = file;
    }
    
    /**
     * Set the internal File object of the SymlConfig.
     * <p>The file is read when calling the load() method, and overwritten
     * when calling the save() method.
     * @param file the File to load from and/or save to
     */
    public synchronized void setFile(File file) {
        this.file = file;
    }
    
    /**
     * Set the encoding to use when loading and saving the SymlConfig.
     * <p>The default encoding for a SymlConfig is UTF-8.
     * @param encoding the character encoding to use
     * @return true, if the encoding is supported, false otherwise 
     */
    public synchronized boolean setEncoding(String encoding) {
        if (!Charset.isSupported(encoding)) {
            return false;
        }
        this.encoding = encoding;
        return true;
    }
    
    /**
     * Load the contents of the File object into memory.
     * <p>Any existing data in memory will be erased, such that calling
     * the save(File) method immediately after the load(File) method
     * should yield the exact same file, given the same File object.
     * @param file the file to load
     * @return true, if the file was loaded successfully, false if the
     * file does not exist, or if any error occurred while loading
     * @throws NullPointerException if the File object is null
     * @throws LexException if the data is not well-formed SYML
     * @throws ParseException if the SYML is semantically incorrect
     */
    public synchronized boolean load(File file) {
        if (file == null) {
            throw new NullPointerException("Cannot load from a null file.");
        }
        Scanner scan = null;
        try {
            scan = new Scanner(file, encoding);
            scan.useDelimiter("\\A");

            String text = scan.next();

            SymlLexer lexer = new SymlLexer();
            TokenStream stream = lexer.lex(text);
            
            SymlParser parser = new SymlParser();
            parser.parse(stream, this);
            return true;
        } catch (IOException e) {
            return false;
        } finally {
            if (scan != null) scan.close();
        }
    }
    
    /**
     * Load the contents of the File object into memory.
     * <p>Any existing data in memory will be erased, such that calling
     * the save() method immediately after the load() method should yield
     * the exact same file.   
     * @return true, if the file was loaded successfully, false if the
     * file does not exist, or if any error occurred while loading
     * @throws NullPointerException if the File object is null
     * @throws LexException if the data is not well-formed SYML
     * @throws ParseException if the SYML is semantically incorrect
     */
    public synchronized boolean load() {
        return load(file);
    }
    
    /**
     * Save the current memory contents to the given File object.
     * <p>Any existing data in the file will be erased, such that calling
     * the load(File) method immediately after the save(File) method should
     * yield the exact same SymlConfig, given the same File object.
     * <p>Note that the internal File object of the SymlConfig is not changed
     * by a call to this method. To change the File object, a call to
     * setFile(File) is required.
     * @param file the file to save to
     * @return true, if the data were successfully saved, false otherwise,
     * e.g. if an error occurred while saving
     * @throws NullPointerException if the File object is null
     */
    public synchronized boolean save(File file) {
        if (file == null) {
            throw new NullPointerException("Cannot save to a null file.");
        }
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                return false;
            }
        }
        SymlEmitter visitor = new SymlEmitter(this);
        String text = visitor.getText();
        try {
            byte[] bytes = text.getBytes(encoding);
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bytes);
            fos.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }
    
    /**
     * Save the current memory contents to the File object.
     * <p>Any existing data in the file will be erased, such that calling
     * the load() method immediately after the save() method should yield
     * the exact same SymlConfig.
     * @return true, if the data were successfully saved, false otherwise,
     * e.g. if an error occurred while saving
     * @throws NullPointerException if the File object is null
     */
    public synchronized boolean save() {
        return save(file);
    }
}
