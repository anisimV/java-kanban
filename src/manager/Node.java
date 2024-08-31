package manager;

public class Node<T> {
    T item;
    Node<T> prev;
    Node<T> next;

    public Node(T item) {
        this.item = item;
    }
}
