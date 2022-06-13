import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;


/**
 * Graph for storing all of the intersection (vertex) and road (edge) information.
 * Uses your GraphBuildingHandler to convert the XML files into a graph. Your
 * code must include the vertices, adjacent, distance, closest, lat, and lon
 * methods. You'll also need to include instance variables and methods for
 * modifying the graph (e.g. addNode and addEdge).
 *
 * @author Alan Yao, Josh Hug
 */
public class GraphDB {
    /**
     * Your instance variables for storing the graph. You should consider
     * creating helper classes, e.g. Node, Edge, etc.
     */
    public Map<Long, Node> nodes = new HashMap<>();
    private Map<String, ArrayList<Long>> names = new HashMap<>();
    private Map<Long, ArrayList<Long>> adjNodes = new HashMap<>();
    private Map<Long, ArrayList<Edge>> adjEdge = new HashMap<>();
    public Map<Long, Node> location = new HashMap<>();
    private Trie<Long> trie = new Trie();

    public class Node {
        private Long id;
        private double lat;
        private double lon;
        private String name;

        public Node(Long id, double lat, double lon) {
            this.id = id;
            this.lat = lat;
            this.lon = lon;
        }

        public Long getID() {
            return id;
        }

        public double getLat() {
            return lat;
        }

        public double getLon() {
            return lon;
        }

        public String getName() {
            return name;
        }
    }

    public class Edge {
        private Long v;
        private Long w;
        private double distance;
        private String name;

        public Edge(Long v, Long w, double distance, String name) {
            this.v = v;
            this.w = w;
            this.distance = distance;
            this.name = name;
        }

        public Long getOne() {
            return v;
        }

        public Long getOther(Long vertex) {
            return vertex.equals(v) ? w : v;
        }

        public double getDistance() {
            return distance;
        }

        public String getName() {
            return name;
        }

    }

    Iterable<Edge> neighbors(Long v) {
        return adjEdge.get(v);
    }

    /**
     * Example constructor shows how to create and start an XML parser.
     * You do not need to modify this constructor, but you're welcome to do so.
     *
     * @param dbPath Path to the XML file to be parsed.
     */
    public GraphDB(String dbPath) {
        try {
            File inputFile = new File(dbPath);
            FileInputStream inputStream = new FileInputStream(inputFile);
            // GZIPInputStream stream = new GZIPInputStream(inputStream);

            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            GraphBuildingHandler gbh = new GraphBuildingHandler(this);
            saxParser.parse(inputStream, gbh);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        clean();
    }

    /**
     * Helper to process strings into their "cleaned" form, ignoring punctuation and capitalization.
     *
     * @param s Input string.
     * @return Cleaned string.
     */
    static String cleanString(String s) {
        return s.replaceAll("[^a-zA-Z ]", "").toLowerCase();
    }

    /**
     * Remove nodes with no connections from the graph.
     * While this does not guarantee that any two nodes in the remaining graph are connected,
     * we can reasonably assume this since typically roads are connected.
     */
    private void clean() {
        // TODO: Your code here.
        Iterator<Map.Entry<Long, ArrayList<Long>>> it = adjNodes.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Long, ArrayList<Long>> entry = it.next();
            if (entry.getValue().isEmpty()) {
                nodes.remove(entry.getKey());
                it.remove();
            }
        }
    }

    /**
     * Returns an iterable of all vertex IDs in the graph.
     *
     * @return An iterable of id's of all vertices in the graph.
     */
    Iterable<Long> vertices() {
        //YOUR CODE HERE, this currently returns only an empty list.
        return nodes.keySet();
    }

    /**
     * Returns ids of all vertices adjacent to v.
     *
     * @param v The id of the vertex we are looking adjacent to.
     * @return An iterable of the ids of the neighbors of v.
     */
    Iterable<Long> adjacent(long v) {
        validateVertex(nodes.get(v));
        return adjNodes.get(v);
    }

    /**
     * Returns the great-circle distance between vertices v and w in miles.
     * Assumes the lon/lat methods are implemented properly.
     * <a href="https://www.movable-type.co.uk/scripts/latlong.html">Source</a>.
     *
     * @param v The id of the first vertex.
     * @param w The id of the second vertex.
     * @return The great-circle distance between the two locations from the graph.
     */
    double distance(long v, long w) {
        return distance(lon(v), lat(v), lon(w), lat(w));
    }

    static double distance(double lonV, double latV, double lonW, double latW) {
        double phi1 = Math.toRadians(latV);
        double phi2 = Math.toRadians(latW);
        double dphi = Math.toRadians(latW - latV);
        double dlambda = Math.toRadians(lonW - lonV);

        double a = Math.sin(dphi / 2.0) * Math.sin(dphi / 2.0);
        a += Math.cos(phi1) * Math.cos(phi2) * Math.sin(dlambda / 2.0) * Math.sin(dlambda / 2.0);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return 3963 * c;
    }

    /**
     * Returns the initial bearing (angle) between vertices v and w in degrees.
     * The initial bearing is the angle that, if followed in a straight line
     * along a great-circle arc from the starting point, would take you to the
     * end point.
     * Assumes the lon/lat methods are implemented properly.
     * <a href="https://www.movable-type.co.uk/scripts/latlong.html">Source</a>.
     *
     * @param v The id of the first vertex.
     * @param w The id of the second vertex.
     * @return The initial bearing between the vertices.
     */
    double bearing(long v, long w) {
        return bearing(lon(v), lat(v), lon(w), lat(w));
    }

    static double bearing(double lonV, double latV, double lonW, double latW) {
        double phi1 = Math.toRadians(latV);
        double phi2 = Math.toRadians(latW);
        double lambda1 = Math.toRadians(lonV);
        double lambda2 = Math.toRadians(lonW);

        double y = Math.sin(lambda2 - lambda1) * Math.cos(phi2);
        double x = Math.cos(phi1) * Math.sin(phi2);
        x -= Math.sin(phi1) * Math.cos(phi2) * Math.cos(lambda2 - lambda1);
        return Math.toDegrees(Math.atan2(y, x));
    }

    /**
     * Returns the vertex closest to the given longitude and latitude.
     *
     * @param lon The target longitude.
     * @param lat The target latitude.
     * @return The id of the node in the graph closest to the target.
     */
    long closest(double lon, double lat) {
        double minDistance = Double.MAX_VALUE;
        Long minID = 0L;
        for (Long id : vertices()) {
            double distance = distance(lon, lat, lon(id), lat(id));
            if (distance < minDistance) {
                minDistance = distance;
                minID = id;
            }
        }
        return minID;
    }

    /**
     * Gets the longitude of a vertex.
     *
     * @param v The id of the vertex.
     * @return The longitude of the vertex.
     */
    double lon(long v) {
        validateVertex(nodes.get(v));
        return nodes.get(v).lon;
    }

    /**
     * Gets the latitude of a vertex.
     *
     * @param v The id of the vertex.
     * @return The latitude of the vertex.
     */
    double lat(long v) {
        validateVertex(nodes.get(v));
        return nodes.get(v).lat;
    }

    String getName(Long id) {
        if (nodes.get(id).name == null) {
            throw new IllegalArgumentException();
        }
        return nodes.get(id).name;
    }


    ArrayList<Long> getLocation(String name) {
        return names.get(cleanString(name));
    }

    void addName(Long id, double lat, double lon, String locationName) {
        String cleanedName = cleanString(locationName);
        if (!names.containsKey(cleanedName)) {
            names.put(cleanedName, new ArrayList<>());
        }
        names.get(cleanedName).add(id);
        nodes.get(id).name = locationName;
        location.get(id).name = locationName;
        trie.put(cleanedName, id);
    }

    void addNode(Long id, double lon, double lat) {
        Node newNode = new Node(id, lat, lon);
        nodes.put(id, newNode);
        location.put(id, newNode);
        adjEdge.put(id, new ArrayList<>());
        adjNodes.put(id, new ArrayList<>());
    }

    void addEdge(Long v, Long w, String edgeName) {
        validateVertex(nodes.get(v));
        validateVertex(nodes.get(w));

        adjNodes.get(v).add(w);
        adjNodes.get(w).add(v);
        adjEdge.get(w).add(new Edge(v, w, distance(v, w), edgeName));
        adjEdge.get(v).add(new Edge(v, w, distance(v, w), edgeName));
    }

    void addWay(ArrayList<Long> ways, String wayName) {
        for (int i = 1; i < ways.size(); i++) {
            addEdge(ways.get(i - 1), ways.get(i), wayName);
        }
    }


    void validateVertex(Node v) {
        if (!nodes.containsKey(v.id)) {
            throw new IllegalArgumentException("Vertex" + v + "is not in the graph");
        }
    }

    List<String> keysWithPrefixOf(String prefix) {
        List<String> result = new ArrayList<String>();
        for (String key : trie.keysWithPrefix(cleanString(prefix))) {
            for (Long id : names.get(key)) {
                result.add(location.get(id).name);
            }
        }
        System.out.println(result);
        return result;
    }
}
