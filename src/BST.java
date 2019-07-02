import java.util.ArrayList;

class BST {
    /**
     * Root of Binary Search Tree.
     */
    Node root;

    /**
     * Class constructor.
     */
    BST() {
        root = null;
    }

    void insert(String key, float value) {
        if (value > -1) {
            root = insertNode(root, key, value);
        }
    }

    /**
     * Insert a new key in BST recursively. If the
     * value is the same with the root value then
     * adds the value into the array list of
     * root, which holds the duplicates.
     *
     * @param root  the root of the tree
     * @param key   the key
     * @param value the value of the key
     */
    private Node insertNode(Node root, String key, float value) {
        /** If tree is empty, creates a new node with the given key, value pair and returns it. */
        if (root == null) {
            root = new Node(key, value);
        } else if (value < root.getValue()) {
            root.left = insertNode(root.left, key, value);
        } else if (value > root.getValue()) {
            root.right = insertNode(root.right, key, value);
        } else if (value == root.getValue()) {
            root.duplicates.add(new Node(key, value));
        }

        return root;
    }


    /**
     * Traverses the whole tree preorderly and insert the nodes
     * into an array list.
     */
    void inorderTraverse(Node root, ArrayList<Node> countriesSorted) {
        if (root != null) {
            inorderTraverse(root.left, countriesSorted);
            countriesSorted.add(root);
            if (root.duplicates.size() > 0) {
                countriesSorted.addAll(root.duplicates);
            }
            inorderTraverse(root.right, countriesSorted);
        }
    }
}
