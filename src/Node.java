public class Node<K extends Comparable, V> {

    public Color color;
    public K key;
    public V value;
    public static int size = 0;
    public boolean visited;
    public Node<K, V> parent;
    public Node<K, V> left;
    public Node<K, V> right;


    public Node(){
        // set RED as default
        this.color = Color.RED;
        this.key = null;
        this.value= null;
        this.left = null;
        this.right = null;
    }
    public Node(Color color, K key, V val) {
        this.color = color;
        this.key = key;
        this.value = val;
        this.left = null;
        this.right = null;
    }

    public void sizeDown(){
        size--;
    }

    public void sizeUp(){
        size++;
    }

    Node getLeftMost() {
        Node n = this;
        while (n.left != null) {
            n = n.left;
        }
        return n;
    }
    Node getNext() {
        if (right != null) {
            return right.getLeftMost();
        } else {
            Node n = this;
            while (n.parent != null && n == n.parent.right) {
                n = n.parent;
            }
            return n.parent;
        }
    }




}

