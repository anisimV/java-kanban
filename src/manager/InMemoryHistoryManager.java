package manager;

import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {
    private final Map<Integer, Node<Task>> taskNodeMap = new HashMap<>();
    private Node<Task> head;
    private Node<Task> tail;

    @Override
    public void add(Task task) {
        removeNode(taskNodeMap.get(task.getId()));
        Node<Task> newNode = new Node<>(task);
        linkLast(newNode);
        taskNodeMap.put(task.getId(), newNode);
    }

    @Override
    public void remove(int id) {
        removeNode(taskNodeMap.remove(id));
    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }

    private void linkLast(Node<Task> node) {
        if (tail == null) {
            head = tail = node;
        } else {
            tail.next = node;
            node.prev = tail;
            tail = node;
        }
    }

    private void removeNode(Node<Task> node) {
        if (node == null) return;

        final Node<Task> previousNode = node.prev;
        final Node<Task> nextNode = node.next;

        if (previousNode != null) {
            previousNode.next = nextNode;
        } else {
            head = nextNode;
        }

        if (nextNode != null) {
            nextNode.prev = previousNode;
        } else {
            tail = previousNode;
        }
    }

    private List<Task> getTasks() {
        List<Task> tasks = new ArrayList<>(taskNodeMap.size());
        Node<Task> current = head;
        while (current != null) {
            tasks.add(current.item);
            current = current.next;
        }
        return tasks;
    }

    private static class Node<T> {
        private final T item;
        private Node<T> prev;
        private Node<T> next;

        public Node(T item) {
            this.item = item;
        }
    }
}
