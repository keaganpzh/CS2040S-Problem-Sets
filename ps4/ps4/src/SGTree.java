import com.sun.source.tree.Tree;

/**
 * ScapeGoat Tree class
 *
 * This class contains some of the basic code for implementing a ScapeGoat tree.
 * This version does not include any of the functionality for choosing which node
 * to scapegoat.  It includes only code for inserting a node, and the code for rebuilding
 * a subtree.
 */

public class SGTree {

    // Designates which child in a binary tree
    enum Child {LEFT, RIGHT}

    /**
     * TreeNode class.
     *
     * This class holds the data for a node in a binary tree.
     *
     * Note: we have made things public here to facilitate problem set grading/testing.
     * In general, making everything public like this is a bad idea!
     *
     */
    public static class TreeNode {
        int key;
        public TreeNode left = null;
        public TreeNode right = null;

        TreeNode(int k) {
            key = k;
        }
    }

    // Root of the binary tree
    public TreeNode root = null;

    /**
     * Counts the number of nodes in the specified subtree
     *
     * @param node  the parent node, not to be counted
     * @param child the specified subtree
     * @return number of nodes
     */
    public int countNodes(TreeNode node, Child child) {
        // TODO: Implement this
        TreeNode nodeToCheck = child == Child.LEFT ? node.left : node.right;
        return countNodesHelper(nodeToCheck);
    }

    /**
     * Helper method for countNodes
     *
     * @param node  the root node for a tree to be counted
     * @return number of nodes in the tree
     */
    public int countNodesHelper(TreeNode node) {
        if (node == null) {
            return 0;
        } else if (node.left == null) {
            // if no left subtree, traverse right subtree and count itself
            return countNodesHelper(node.right) + 1;
        } else if (node.right == null) {
            // if no right subtree, traverse left subtree and count itself
            return countNodesHelper(node.left) + 1;
        } else if (node.right != null & node.left != null){
            // if there are both left and right subtrees, traverse both and count itself
            return countNodesHelper(node.right) + countNodesHelper(node.left) + 1;
        } else {
            // if no left or right subtree, count itself
            return 1;
        }
    }

    /**
     * An array of nodes arranged based on in-order traversal of a subtree.
     */
    TreeNode[] inOrderSubtree;

    /**
     * Builds an array of nodes in the specified subtree
     *
     * @param node  the parent node, not to be included in returned array
     * @param child the specified subtree
     * @return array of nodes
     */
    public TreeNode[] enumerateNodes(TreeNode node, Child child) {
        // TODO: Implement this
        int numOfNodes = countNodes(node, child);
        inOrderSubtree = new TreeNode[numOfNodes];
        TreeNode nodeToCheck = child == Child.LEFT ? node.left : node.right;
        for (int i = 0; i < numOfNodes; i++) {
            inOrderSubtree[i] = traverseSubtreeInOrder(nodeToCheck);
        }
        return inOrderSubtree;

    }

    public int[] nodesToKeys(TreeNode[] nodes) {
        int[] result = new int[nodes.length];
        for (int i = 0; i < nodes.length; i++) {
            result[i] = nodes[i].key;
        }
        return result;
    }


    /**
     * Method to check if a node has been traversed in the subtree
     * @param node the node to check
     * @return true if node has been traversed, false otherwise.
     */
    public boolean isTraversed(TreeNode node) {
        if (node == null) {
            return true;
        }
        for (TreeNode curr : inOrderSubtree) {
            if (curr != null) {
                if (curr == node) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Method to check if a subtree is traversed, given its root.
     * @param node the specified node.
     * @return true if the subtree has been traversed.
     */
    public boolean isTraversedDeep(TreeNode node) {
        if (node == null) {
            return true;
        }
        return isTraversed(node) && isTraversedDeep(node.left) && isTraversedDeep(node.right);
    }


    /**
     *  Traverses the specified subtree and returns a leaf
     * @param node the root node of a subtree
     * @return lowest node (leaf) of a subtree
     */
    public TreeNode traverseSubtreeInOrder (TreeNode node) {
        if (!isTraversed(node) && node.left == null && node.right == null) {
            return node;
        } else if (isTraversedDeep(node.left) && isTraversed(node) && !isTraversedDeep(node.right)) {
            return traverseSubtreeInOrder(node.right);
        } else if (isTraversedDeep(node.left)) {
            return node;
        } else if (!isTraversed(node) && !isTraversedDeep(node.left)) {
            return traverseSubtreeInOrder(node.left);
        } else {
            // should not reach here
            return node;
        }
    }

    /**
     * Builds a tree from the list of nodes
     * Returns the node that is the new root of the subtree
     *
     * @param nodeList ordered array of nodes
     * @return the new root node
     */
    public TreeNode buildTree(TreeNode[] nodeList) {
        // TODO: Implement this
        if (nodeList == null) {
            return new TreeNode(0);
        } else {
            return buildTreeHelper(nodeList, 0, nodeList.length - 1);
        }
    }

    public TreeNode buildTreeHelper(TreeNode[] nodeList, int start, int end) {
        if (start > end) {
            return null;
        }
        int mid = start + (end-start)/2;
        TreeNode root = nodeList[mid];
        root.left = buildTreeHelper(nodeList, start, mid - 1);
        root.right = buildTreeHelper(nodeList, mid + 1, end);
        return root;
    }

    /**
    * Rebuilds the specified subtree of a node
    * 
    * @param node the part of the subtree to rebuild
    * @param child specifies which child is the root of the subtree to rebuild
    */
    public void rebuild(TreeNode node, Child child) {
        // Error checking: cannot rebuild null tree
        if (node == null) return;
        // First, retrieve a list of all the nodes of the subtree rooted at child
        TreeNode[] nodeList = enumerateNodes(node, child);
        // Then, build a new subtree from that list
        TreeNode newChild = buildTree(nodeList);
        // Finally, replace the specified child with the new subtree
        if (child == Child.LEFT) {
            node.left = newChild;
        } else if (child == Child.RIGHT) {
            node.right = newChild;
        }
    }

    /**
    * Inserts a key into the tree
    *
    * @param key the key to insert
    */
    public void insert(int key) {
        if (root == null) {
            root = new TreeNode(key);
            return;
        }

        TreeNode node = root;

        while (true) {
            if (key <= node.key) {
                if (node.left == null) break;
                node = node.left;
            } else {
                if (node.right == null) break;
                node = node.right;
            }
        }

        if (key <= node.key) {
            node.left = new TreeNode(key);
        } else {
            node.right = new TreeNode(key);
        }
    }


    // Simple main function for debugging purposes
    public static void main(String[] args) {
        SGTree tree = new SGTree();
        for (int i = 0; i < 100; i++) {
            tree.insert(i);
        }
        tree.rebuild(tree.root, Child.RIGHT);

    }
}
