import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class Trie<Value> {
    private static int R = 256;
    private Node root;

    private static class Node {
        private Object val;
        private Map<Character, Node> next = new HashMap<>();
    }

    public Value get(String key) {
        Node node = get(key, root, 0);
        if (node == null) {
            return null;
        }
        return (Value) node.val;
    }

    private Node get(String key, Node x, int index) {
        if (x == null) {
            return null;
        }
        if (index == key.length()) {
            return x;
        }
        return get(key, x.next.get(key.charAt(index)), index + 1);
    }

    public void put(String key, Value value) {
        root = put(key, value, root, 0);
    }

    private Node put(String key, Value value, Node x, int index) {
        if (x == null) {
            x=new Node();
        }
        if (index == key.length()) {
            x.val = value;
            return x;
        }
        x.next.put(key.charAt(index), put(key, value, x.next.get(key.charAt(index)), index + 1));
        return x;
    }

    public Iterable<String> keys() {
        return keysWithPrefix("");
    }

    public Iterable<String> keysWithPrefix(String s) {
        Queue<String> queue = new LinkedList<>();
        collect(get(s, root, 0), s, queue);
        return queue;
    }

    private void collect(Node x, String pre, Queue<String> queue) {
        if (x == null) {
            return;
        }
        if (x.val != null) {
            queue.add(pre);
        }
        for (Map.Entry<Character, Node> entry : x.next.entrySet()) {
            collect(entry.getValue(), pre + entry.getKey(), queue);
        }
    }

    public String longestPrefixOf(String s) {
        int length = search(root, s, 0, 0);
        return s.substring(0, length);
    }

    private int search(Node x, String s, int index, int length) {
        if (x == null) {
            return length;
        }
        if (x.val != null) {
            length = index;
        }
        if (index == s.length()) {
            return length;
        }
        return search(x.next.get(s.charAt(index)), s, index + 1, length);
    }

    public void delete(String key) {
        root = delete(root, key, 0);
    }

    private Node delete(Node x, String key, int index) {
        if (x == null) {
            return null;
        }
        if (index == key.length()) {
            x.val = null;
        } else {
            x.next.put(key.charAt(index), delete(x.next.get(key.charAt(index)), key, index + 1));
        }

        if (x.val != null) {
            return x;
        }
        if (!x.next.isEmpty()) {
            return x;
        }
        return null;
    }


}
