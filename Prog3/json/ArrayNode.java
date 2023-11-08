import java.util.ArrayList;
import java.util.Iterator;

public class ArrayNode extends Node implements Iterable<Node> {
    private final ArrayList<Node> nodeList;

    @Override
    public Iterator<Node> iterator() {
        return nodeList.iterator();
    }

    ArrayNode() {
        nodeList = new ArrayList<>();
    }

    public void add(Node node) {
        nodeList.add(node);
    }

    public int size() {
        return nodeList.size();
    }
}
