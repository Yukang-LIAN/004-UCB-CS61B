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

    public Value get(String key, Value value) {

    }

    private Node get(String key, Node x, int index) {

    }

    public void put(String key, Value value) {

    }

    private Node put(String key, Value value, Node x, int index) {

    }

    public Iterable<String> keys() {

    }

    public Iterable<String> keysWithPrefix(String s) {

    }

    private void collect(Node x, String pre, Queue<String> queue) {

    }

    public String longestPrefixOf(String s) {

    }

    private int search(Node x, String s, int index, int length) {

    }

    public void delete(String key) {

    }

    private Node delete(Node x, String key, int index) {

    }


}
