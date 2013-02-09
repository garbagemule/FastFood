package garbagemule.util.syml;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * The SymlNode class is the main element of the data structure for an
 * internal representation of a SYML document.
 * 
 * <p>Provided are methods for creating and manipulating nodes and their
 * associated comments, and children or data values. 
 * 
 * @author garbagemule
 * @version 0.1
 */
public class SymlNode {
    private String name;
    private Object data;
    private String comment;
    private SymlNode parent;
    private Map<String,SymlNode> children;

    /**
     * Create a new SymlNode with the given name.
     * <p>The name cannot contain periods/dots, as these are used as
     * delimiters for nested getting and setting.
     * @param name
     * @throws IllegalArgumentException if the name contains periods/dots
     */
    public SymlNode(String name) {
        if (name.contains(".")) {
            throw new IllegalArgumentException("Name cannot contain periods.");
        }
        this.name = name;
    }

    /**
     * Get the name (key) of this node.
     * @return the name of the node
     */
    public String getName() {
        return name;
    }

    /**
     * Set the comment of this node.
     * <p>For multiline comments, use linebreak characters ("\n").
     * @param comment the comment string
     */
    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * Get the comment of this node.
     * <p>Multiline comments will contain linebreak characters ("\n").
     * @return the comment of this node
     */
    public String getComment() {
        return comment;
    }

    /**
     * Set the data value of this node.
     * <p>A SymlNode is always either a leaf (no children, may have data)
     * or a parent (no data, may have children), and this is guaranteed
     * by the following:
     * <ul>
     * <li>If the given object is a SymlNode or an instance thereof, it
     * will be inserted as a child of this node, nulling any pre-existing
     * data value. If this SymlNode exists as a descendant of the given
     * node, an IllegalStateException is thrown.
     * <li>If the object is not a SymlNode or an instance thereof, any
     * pre-existing children will be unlinked.
     * </ul> 
     * <p>Note that if the object is a numeric type (int, long, double)
     * or a boolean, it will still be possible to get a String value
     * using the getString() method. However, if the object is a String,
     * it is not possible to get numeric values using e.g. getInt(),
     * even if the String would be able to convert via Integer.parseInt().
     * @param data a data object (or SymlNode child)
     * @throws IllegalStateException if the given object is a SymlNode,
     * and this SymlNode is a descendant of the given SymlNode.
     */
    public void set(Object data) {
        if (data instanceof SymlNode) {
            SymlNode node = (SymlNode) data;
            if (isDescendantOf(node)) {
                throw new IllegalStateException("Cycle detected!");
            }
            addChild(node);
        } else {
            convertToLeaf();
            this.data = data;
        }
    }

    /**
     * Set the data value of the given child node, creating the child if
     * it doesn't exist. However, if the given value is null, the final
     * child node of the key string will be removed.
     * <p>Note that this method is the same as calling createChild(key)
     * and then set(data) on the resulting SymlNode, given the value is
     * not null. Refer to these methods for further documentation.
     * @param key the key of the child node to set the data for, may not
     * be null
     * @param data a data object (or SymlNode child)
     * @throws NullPointerException if the key is null
     * @throws IllegalStateException if the given object is a SymlNode,
     * and this SymlNode is a descendant of the given SymlNode.
     */
    public void set(String key, Object data) {
        if (data != null) {
            SymlNode node = createChild(key);
            node.set(data);
        } else {
            SymlNode node = getChild(key);
            if (node != null) {
                node.parent.removeChild(node.getName());
            }
        }
    }
    
    /**
     * Get the data value of this node.
     * @return the data value of this node, may be null
     */
    public Object get() {
        return data;
    }
    
    /**
     * Get the data value of the given child node.
     * <p>If the child does not exist, null is returned. Note that even
     * if the child does exist, it might still have a null data value.
     * @param key the key of the child node to get the data for
     * @return the data value of the child node, or null if the child
     * does not exist, may be null
     */
    public Object get(String key) {
        SymlNode node = getChild(key);
        if (node == null) {
            return null;
        }
        return node.get();
    }
    
    /**
     * Get the boolean data value of this node.
     * <p>If the data is null or non-boolean, the default value is returned.
     * @param def the value to return, if the data is null or non-boolean
     * @return the boolean data value of this node, or def
     */
    public boolean getBoolean(boolean def) {
        if (data != null && data instanceof Boolean) {
            return ((Boolean) data).booleanValue();
        }
        return def;
    }
    
    /**
     * Get the boolean data value of the node with the given name.
     * <p>If the child does not exist or the data is null or non-boolean,
     * the default value is returned.
     * @param key the name of the child to get the value from
     * @param def the value to return, if the child does not exist, or
     * the data is null or non-boolean
     * @return the boolean data value of the child, or def
     */
    public boolean getBoolean(String key, boolean def) {
        SymlNode node = getChild(key);
        if (node != null) {
            return node.getBoolean(def);
        }
        return def;
    }
    
    /**
     * Get the boolean data value of the node with the given name.
     * <p>If the child does not exist or the data is null or non-boolean,
     * a default value of false is returned.
     * @param key the name of the child to get the value from
     * @return the boolean data value of the child, or false
     */
    public boolean getBoolean(String key) {
        return getBoolean(key, false);
    }
    
    /**
     * Get the int data value of this node.
     * <p>If the data is null or non-numeric, the default value is returned.
     * <p>Note that the numeric getters are subject to both widening,
     * narrowing and primitive casting.
     * @param def the value to return, if the data is null or non-numeric
     * @return the int data value of this node, or def
     */
    public int getInt(int def) {
        if (data != null && data instanceof Number) {
            return ((Number) data).intValue();
        }
        return def;
    }
    
    /**
     * Get the int data value of the node with the given name.
     * <p>If the child does not exist or the data is null or non-numeric,
     * the default value is returned.
     * <p>Note that the numeric getters are subject to both widening,
     * narrowing and primitive casting.
     * @param key the name of the child to get the value from
     * @param def the value to return, if the child does not exist, or
     * the data is null or non-numeric
     * @return the int data value of the child, or def
     */
    public int getInt(String key, int def) {
        SymlNode node = getChild(key);
        if (node != null) {
            return node.getInt(def);
        }
        return def;
    }
    
    /**
     * Get the int data value of the node with the given name.
     * <p>If the child does not exist or the data is null or non-numeric,
     * a default value of 0 is returned.
     * <p>Note that the numeric getters are subject to both widening,
     * narrowing and primitive casting.
     * @param key the name of the child to get the value from
     * @return the int data value of the child, or 0
     */
    public int getInt(String key) {
        return getInt(key, 0);
    }

    /**
     * Get the long data value of this node.
     * <p>If the data is null or non-numeric, the default value is returned.
     * <p>Note that the numeric getters are subject to both widening,
     * narrowing and primitive casting.
     * @param def the value to return, if the data is null or non-numeric
     * @return the long data value of this node, or def
     */
    public long getLong(long def) {
        if (data != null && data instanceof Number) {
            return ((Number) data).longValue();
        }
        return def;
    }
    
    /**
     * Get the long data value of the node with the given name.
     * <p>If the child does not exist or the data is null or non-numeric,
     * the default value is returned.
     * <p>Note that the numeric getters are subject to both widening,
     * narrowing and primitive casting.
     * @param key the name of the child to get the value from
     * @param def the value to return, if the child does not exist, or
     * the data is null or non-numeric
     * @return the long data value of the child, or def
     */
    public long getLong(String key, long def) {
        SymlNode node = getChild(key);
        if (node != null) {
            return node.getLong(def);
        }
        return def;
    }
    
    /**
     * Get the long data value of the node with the given name.
     * <p>If the child does not exist or the data is null or non-numeric,
     * a default value of 0 is returned.
     * <p>Note that the numeric getters are subject to both widening,
     * narrowing and primitive casting.
     * @param key the name of the child to get the value from
     * @return the long data value of the child, or 0
     */
    public long getLong(String key) {
        return getLong(key, 0L);
    }

    /**
     * Get the boolean data value of this node.
     * <p>If the data is null or non-numeric, the default value is returned.
     * @param def the value to return, if the data is not boolean-valued
     * @return the boolean data value of this node, or def
     */
    public double getDouble(double def) {
        if (data != null && data instanceof Number) {
            return ((Number) data).doubleValue();
        }
        return def;
    }
    
    /**
     * Get the double data value of the node with the given name.
     * <p>If the child does not exist or the data is null or non-numeric,
     * the default value is returned.
     * <p>Note that the numeric getters are subject to both widening,
     * narrowing and primitive casting.
     * @param key the name of the child to get the value from
     * @param def the value to return, if the child does not exist, or
     * the data is null or non-numeric
     * @return the double data value of the child, or def
     */
    public double getDouble(String key, double def) {
        SymlNode node = getChild(key);
        if (node != null) {
            return node.getDouble(def);
        }
        return def;
    }

    /**
     * Get the double data value of the node with the given name.
     * <p>If the child does not exist or the data is null or non-numeric,
     * a default value of 0 is returned.
     * <p>Note that the numeric getters are subject to both widening,
     * narrowing and primitive casting.
     * @param key the name of the child to get the value from
     * @return the double data value of the child, or 0
     */
    public double getDouble(String key) {
        return getDouble(key, 0D);
    }

    /**
     * Get the String data value of this node.
     * <p>If the data is null, the default value is returned.
     * <p>Note that the toString() method is invoked on the data
     * value if it isn't null, and thus any kind of non-null data
     * can be fetched as a String.
     * @param def the value to return, if the data is null
     * @return the String data value of this node, or def
     */
    public String getString(String def) {
        return (data != null ? data.toString() : def.toString());
    }

    /**
     * Get the String data value of the node with the given name.
     * <p>If the child does not exist or the data is null, the
     * default value is returned.
     * <p>Note that the toString() method is invoked on the data
     * value if it isn't null, and thus any kind of non-null data
     * can be fetched as a String.
     * @param key the name of the child to get the value from
     * @param def the value to return, if the child does not exist,
     * or the data is null
     * @return the String data value of the child, or def
     */
    public String getString(String key, String def) {
        SymlNode node = getChild(key);
        if (node != null) {
            return node.getString(def);
        }
        return def;
    }

    /**
     * Get the String list data value of this node.
     * <p>If the data is null or not a list, the default value is returned.
     * <p>If the data is a list, but not a valid String list, all of the
     * objects will be converted to Strings and inserted into a new list,
     * which will be returned.
     * <p>Note that the list is a copy of the data, not a direct reference.
     * @param def the value to return, if the data is not a list
     * @return the String list data value of this node, or def
     */
    public List<String> getStringList(List<String> def) {
        if (data != null && data instanceof List<?>) {
            List<?> list = (List<?>) data;
            List<String> result = new ArrayList<String>();
            for (Object o : list) {
                result.add(String.valueOf(o));
            }
            return result;
        }
        return def;
    }

    /**
     * Get the String list data value of the node with the given name.
     * <p>If the child does not exist or the data is null, the
     * default value is returned.
     * <p>If the data is a list, but not a valid String list, all of the
     * objects will be converted to Strings and inserted into a new list,
     * which will be returned.
     * <p>Note that the list is a copy of the data, not a direct reference.
     * @param key the name of the child to get the value from
     * @param def the value to return, if the child does not exist, or the
     * data is null or not a list
     * @return the String list data value of this node, or def
     */
    public List<String> getStringList(String key, List<String> def) {
        SymlNode node = getChild(key);
        if (node != null) {
            return node.getStringList(def);
        }
        return def;
    }

    /**
     * Get a set of the names of all children of this node, or an empty
     * set if the node has no children.
     * <p>Note that the set is a copy of the data, not a direct reference.
     * @return a set of key names for this node's children, or null
     */
    public Set<String> getKeys() {
        return (children != null ? children.keySet() : new HashSet<String>());
    }
    
    /**
     * Get a set of the names of all children of the given node, or null
     * if the child does not exist.
     * @param key the name of the child to get the keys from
     * @return a set of key names for the given child, or null
     */
    public Set<String> getKeys(String key) {
        SymlNode node = getChild(key);
        if (node != null) {
            return node.getKeys();
        }
        return null;
    }
    
    /**
     * Get the root node for this tree.
     * <p>The root node is the first ancestor of this node which
     * has no parent.
     * @return the root node of the tree
     */
    public SymlNode getRoot() {
        if (parent == null) {
            return this;
        }
        return parent.getRoot();
    }
    
    /**
     * Get the parent of this SymlNode.
     * @return the parent of this node, may be null
     */
    public SymlNode getParent() {
        return parent;
    }
    
    /**
     * Get the child with the given key as name.
     * @param key the name of the child to look for
     * @return the child with the given name, or null
     */
    public SymlNode getChild(String key) {
        return getNode(key, false);
    }

    /**
     * Get the child with the given key as name, or create it
     * if it doesn't exist.
     * @param key the name of the child to look for
     * @return the child with the given name
     */
    public SymlNode createChild(String key) {
        return getNode(key, true);
    }
    
    /**
     * Get an unmodifiable collection of the children of this node,
     * or null if the node has no children.
     * @return a collection of children, or null
     */
    public Collection<SymlNode> getChildren() {
        if (children == null) {
            return null;
        }
        return Collections.unmodifiableCollection(children.values());
    }
    
    private SymlNode getNode(String key, boolean create) {
        if (children == null) {
            if (create) {
                convertToNode(); // Note: Removes any current data objects!
            } else {
                return null;
            }
        }
        
        // Grab the first dot
        int dot = key.indexOf('.');
        
        // End of the line?
        if (dot == -1) {
            SymlNode node = children.get(key);
            if (node == null && create) {
                node = addChild(key);
            }
            return node;
        }
        
        // Otherwise, split and recursively call
        String first = key.substring(0, dot);
        SymlNode node = children.get(first);
        if (node == null) {
            if (create) {
                node = addChild(first);
            } else {
                return null;
            }
        }
        return node.getNode(key.substring(dot + 1), create);
    }
    
    private SymlNode addChild(String name) {
        return addChild(new SymlNode(name));
    }
    
    private SymlNode addChild(SymlNode node) {
        convertToNode(); // Note: Removes any current data objects!
        
        // The empty string means a root node, so add its children
        if (node.name.equals("")) {
            if (node.children == null) {
                return null;
            }
            for (SymlNode child : node.children.values()) {
                // Only add children that don't already exist
                if (!children.containsKey(child.getName())) {
                    addChild(child);
                }
            }
            return this;
        }
        node.parent = this;
        children.put(node.name, node);
        return node;
    }
    
    private SymlNode removeChild(String key) {
        if (children != null) {
            SymlNode child = children.remove(key);
            if (child != null) {
                child.parent = null;
            }
            if (children.isEmpty()) {
                convertToLeaf();
            }
            return child;
        }
        return null;
    }
    
    private boolean isDescendantOf(SymlNode node) {
        if (this.equals(node)) return true;
        if (parent == null) return false;
        return parent.isDescendantOf(node);
    }
    
    private void convertToNode() {
        if (children == null) {
            // LinkedHashMap preserves document order
            children = new LinkedHashMap<String,SymlNode>();
            data = null;
        }
    }
    
    private void convertToLeaf() {
        if (children != null) {
            // Do some proper cleanup
            for (SymlNode child : children.values()) {
                child.parent = null;
            }
            children.clear();
            children = null;
        }
    }
    
    /**
     * Visitor accept method for arbitrary traversal.
     * <p>If this node has children, the visitNode(SymlNode) method on
     * the SymlVisitor will be called, otherwise visitLeaf(SymlNode)
     * is called.
     * @param visitor a SymlVisitor, may not be null
     * @return whatever the callback to the given visitor method returns
     * @throws NullPointerException if the visitor is null
     */
    public <T> T accept(SymlVisitor<T> visitor) {
        if (children != null) {
            return visitor.visitNode(this);
        }
        return visitor.visitLeaf(this);
    }
    
    @Override
    public int hashCode() {
        return name.hashCode();
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SymlNode)) return false;
        
        SymlNode other = (SymlNode) o;
        
        if (data != null && other.data != null) {
            return data.equals(other.data);
        }
        
        if (children != null && other.children != null) {
            return children.equals(other.children);
        }
        
        return false;
    }
    
    @Override
    public String toString() {
        StringBuilder buffy = new StringBuilder();
        buffy.append("Node[name='" + name + "'");
        
        if (data != null) {
            buffy.append(",data='").append(data).append("']");
        } else if (children != null) {
            buffy.append(",children=").append(children.size()).append("]");
        } else {
            buffy.append("]");
        }
        return buffy.toString();
    }
}
