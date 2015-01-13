package package1;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Piotr on 2015-01-12.
 */
public class Graph<V> {

    private class Node {
        public V info;
        public boolean visited;
        public int color;
        //wierzchołki z którymi granicza krawędzie
        public List<Node> adj;

        public Node(V info) {
            this.info = info;
            this.visited = false;
            this.adj = new ArrayList<Node>();
            this.color = -1;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            final Node other = (Node) obj;
            if (info == null) {
                if (other.info != null)
                    return false;
            } else if (!info.equals(other.info))
                return false;
            return true;
        }
    }

    private HashMap<V, Node> nodes;

    public Graph() {
        this.nodes = new HashMap<V, Node>();
    }

    public void addVertex(V vertex) {
        if (!nodes.containsKey(vertex)) {
            nodes.put(vertex, new Node(vertex));
        }
    }

    public void addEdge(V v, V w) {
        Node origin = nodes.get(v);
        Node dest = nodes.get(w);
        if (origin != null && dest != null && !origin.equals(dest)) {
            if (!origin.adj.contains(dest)) {
                origin.adj.add(dest);
                dest.adj.add(origin);
            }
        }
    }

    public List<V> neighbors(V v) {
        Node node = nodes.get(v);
        if (node != null) {
            List<V> l = new ArrayList<V>(node.adj.size());
            for (Node neighbor : node.adj) {
                l.add(neighbor.info);
            }
            return l;
        }
        return null;
    }

    public int vertexCount() {
        return nodes.size();
    }

    private List<Node> getNodes() {
        List<Node> l = new ArrayList<Node>(vertexCount());
        Iterator<V> it = nodes.keySet().iterator();
        while (it.hasNext()) {
            l.add(nodes.get(it.next()));
        }
        return l;
    }

    public List<V> DFS() {
        return DFS(getNodes().get(0).info);
    }

    public List<V> DFS(V origin) {
        Node node = nodes.get(origin);
        if (node == null)
            return null;
        clearMarks();
        List<V> l = new ArrayList<V>();
        this.DFS(node, l);
        return l;
    }

    private void clearMarks() {
        for (Node n : getNodes()) {
            n.visited = false;
        }
    }
    // przeszukiwanie grafu w głąb
    private void DFS(Node origin, List<V> l) {
        if (origin.visited)
            return;
        l.add(origin.info);
        origin.visited = true;
        for (Node neighbor : origin.adj)
            DFS(neighbor, l);
    }

    public int getColor(V info) {
        if (nodes.containsKey(info))
            return nodes.get(info).color;
        return -1;
    }

    public void setColor(V info, int color) {
        nodes.get(info).color = color;
    }

    public List<V> neighborsColor(V info) {
        List<V> ans = new ArrayList<V>();
        Node node = nodes.get(info);
        for (Node neighbor : node.adj) {
            if (node.color == neighbor.color)
                ans.add(neighbor.info);
        }
        return ans;
    }


}
