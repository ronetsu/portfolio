import java.util.Iterator;
import java.util.TreeMap;

public class ObjectNode extends Node implements Iterable<String> {
    private final TreeMap<String, Node> map;

    @Override
    public Iterator<String> iterator() {
        return map.keySet().iterator();
    }

    ObjectNode() {
        map = new TreeMap<>();
    }

    public Node get(String key) {
        if(map.containsKey(key)) {
            return map.get(key);
        }
        return null;
    }

    public void set(String key, Node node) {
        map.put(key, node);
    }

    public int size() {
        return map.size();
    }
}
