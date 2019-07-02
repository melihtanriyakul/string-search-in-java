import java.util.ArrayList;

class Node {
    Node left, right;
    ArrayList<Node> duplicates;
    private String key;
    private float value;

    /**
     * Class Constructor specifying key and value of Node.
     */
    Node(String key, float value) {
        this.duplicates = new ArrayList<>();
        this.key = key;
        this.value = value;
        left = right = null;
    }

    Node getLeft() {
        return left;
    }

    void setLeft(Node left) {
        this.left = left;
    }

    Node getRight() {
        return right;
    }

    void setRight(Node right) {
        this.right = right;
    }

    String getKey() {
        return key;
    }

    void setKey(String key) {
        this.key = key;
    }

    float getValue() {
        return value;
    }

    void setValue(float value) {
        this.value = value;
    }
}