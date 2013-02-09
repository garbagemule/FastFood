package garbagemule.util.syml;

/**
 * The SymlVisitor interface realizes the visitor role of the Visitor
 * pattern, with {@link SymlNode} realizing both the node and leaf
 * roles. A SymlNode with children is considered a node, and a SymlNode
 * with data is considered a leaf.
 * 
 * @author garbagemule
 * @version 0.1
 * @param <T> the return type for the visit methods
 */
public interface SymlVisitor<T> {
    /**
     * Visit a SymlNode with no data, and possibly some children.
     * @param node the node to visit
     * @return whatever the implementing visitor decides to return
     */
    public T visitNode(SymlNode node);
    
    /**
     * Visit a SymlNode with no children, and possibly some data.
     * @param node the node to visit
     * @return whatever the implementing visitor decides to return
     */
    public T visitLeaf(SymlNode node);
}
