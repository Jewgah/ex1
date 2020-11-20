package ex1.src;


import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Objects;

public class WGraph_DS implements weighted_graph, Serializable {

    private HashMap<Integer, node_info> nodeMap;
    private HashMap<Integer, HashMap<Integer, Double>> edgeMap;
    private int MC;
    private int num_of_edge;

    //Constructor
    public WGraph_DS() {
        this.nodeMap = new HashMap<>();
        this.edgeMap = new HashMap<>();
        this.MC = 0;
        this.num_of_edge = 0;
    }

    //CopyConstructor
    public WGraph_DS(WGraph_DS g) {
        this.nodeMap = new HashMap<>();
        this.edgeMap = new HashMap<>();

        //deep copying nodeMap
        if (g.nodeMap != null) {

            //itr iterates over the Collection of nodeMap's nodes
            Iterator itr = g.nodeMap.values().iterator();

            while (itr.hasNext()) {
                NodeInfo copyNode = (NodeInfo) itr.next();
                this.nodeMap.put(copyNode.getKey(), new NodeInfo(copyNode));
            }
        }

        //deep copying edgeMap
        if (g.edgeMap != null) {

            //itr2 iterates over all the keys of the outer g's Hashmap
            Iterator itr2 = g.edgeMap.keySet().iterator();
            while (itr2.hasNext()) {

                int outerIndex = (int) itr2.next();

                //itr3 iterates over all the keys of the inner g's Hashmap
                Iterator itr3 = g.edgeMap.get(outerIndex).keySet().iterator();
                while (itr3.hasNext()) {
                    int innerIndex = (int) itr3.next();

                    //putAll all the inners g's Hashmaps in this.edgeMap inners Hashmaps
                    HashMap<Integer, Double> newInnerMap = new HashMap<>(); // create new innerMap
                    Double value = g.edgeMap.get(outerIndex).get(innerIndex); // get weight

                    newInnerMap.put(innerIndex,value);//put weight in new innerMap

                    this.edgeMap.put(outerIndex,newInnerMap);//put newInnerMap in edgeMap at outerIndex
                }
            }
        }
        this.MC = g.getMC();
        this.num_of_edge = g.edgeSize();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WGraph_DS wGraph_ds = (WGraph_DS) o;
        return num_of_edge == wGraph_ds.num_of_edge &&
                Objects.equals(nodeMap, wGraph_ds.nodeMap) &&
                Objects.equals(edgeMap, wGraph_ds.edgeMap);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nodeMap, edgeMap, num_of_edge);
    }


    ///////////////////////////////////////////////////////////////////////////////
    ////////////////////// NodeInfo INNER CLASS ///////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////

    private class NodeInfo implements node_info, Serializable {

        private int key;
        private HashMap<Integer, node_info> neighbours;
        private String info;
        private double tag;


        //constructor needed?
        public NodeInfo(int key) {
            this.key = key;
            this.neighbours = new HashMap<>();
            this.info = "";
            this.tag = -1;
        }

        //constructor needed?
        public NodeInfo(NodeInfo node) {
            this.key = node.getKey();
            this.neighbours = new HashMap<>();
            this.neighbours.putAll(node.neighbours);
            this.info = node.getInfo();
            this.tag = node.getTag();
        }

        ///////////////Getters & Setters//////////////////////

        @Override
        public int getKey() {
            return this.key;
        }

        @Override
        public String getInfo() {
            return this.info;
        }

        @Override
        public void setInfo(String s) {
            this.info = s;
        }

        @Override
        public double getTag() {
            return this.tag;
        }

        @Override
        public void setTag(double t) {
            this.tag = t;
        }

        ///////////////Methods//////////////////////

        //Adds node t to the map O(1)
        public void addNi(node_info t) {
            this.neighbours.put(t.getKey(), t);
        }

        //returns true iff this node and key node are neighbours - Used for hasEdge
        public boolean hasNi(int key) {
            return this.neighbours.containsKey(key); //O(1)
        }

        //Removes the edge between this and node
        public void removeNei(node_info node) {
            if (this.neighbours.containsValue(node)) {
                this.neighbours.remove(node.getKey());
            }
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            NodeInfo nodeInfo = (NodeInfo) o;
            return key == nodeInfo.key &&
                    Double.compare(nodeInfo.tag, tag) == 0 &&
                    Objects.equals(info, nodeInfo.info);
        }
    }//End of NodeInfo inner class


    ///////////////////////////////////////////////////////////////////////////////
    ////////////////////// WGraph_DS methods //////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////


    @Override
    public node_info getNode(int key) {
        if(nodeMap.containsKey(key)){
            return this.nodeMap.get(key);
        }
        else return null;
    }

    @Override
    //returns true iff (if and only if) there is an edge between node1 and node2
    public boolean hasEdge(int node1, int node2) {
        //  O(1) since containsKey() method for Hashmaps used by hasNi has a O(1) complexity
        NodeInfo node = (NodeInfo) this.nodeMap.get(node1);
        return node.hasNi(node2);
    }

    @Override
    public double getEdge(int node1, int node2) {
        //if there is an edge between node1 and node2
        if (hasEdge(node1, node2)) {
            return edgeMap.get(node1).get(node2); //returns node1-node2 edge's weight
        }
        if (node1 == node2) return 0;

        return -1; //no edge found
    }

    @Override
    public void addNode(int key) {
        //if key isn't already in
        if (!nodeMap.containsKey(key)) {
            this.nodeMap.put(key, new NodeInfo(key));
            this.edgeMap.put(key, new HashMap<>());
            MC++;
        }
        //else do nothing
    }

    @Override
    // We can suppose valid weight (positive)
    // Connect an edge between node1 and node2
    public void connect(int node1, int node2, double w) {

        //Updating neighbours Hashmap:

        if(!hasEdge(node1,node2)) num_of_edge++; // only increments num_of_edge if there isn't an edge already

        if (nodeMap.containsKey(node1)                  //if node1 exists in nodeMap
                && (nodeMap.containsKey(node2))        //if node2 exists in nodeMap
                && (node1 != node2)) {                 //this is to avoid adding a connection between a node and itself

                if(hasEdge(node1,node2)                                     //if there is already an edge
                        && (getEdge(node1,node2) != w)){                    // and its weight isn't the same as w
                    MC++;
                }

                //add edge between each other
                this.edgeMap.get(node1).put(node2, w); //O(1)
                this.edgeMap.get(node2).put(node1, w); //O(1)

                //add each of them in each other's neighbour's list
                NodeInfo node_1 = (NodeInfo) this.nodeMap.get(node1);
                node_1.addNi(this.nodeMap.get(node2)); //O(1)
                NodeInfo node_2 = (NodeInfo) this.nodeMap.get(node2);
                node_2.addNi(this.nodeMap.get(node1)); //O(1)
        }
    }
//@Override
//// We can suppose valid weight (positive)
//// Connect an edge between node1 and node2
//public void connect(int node1, int node2, double w) {
//
//    //Updating neighbours Hashmap:
//
//    if (nodeMap.containsKey(node1)                  //if node1 exists in nodeMap
//            && (nodeMap.containsKey(node2))        //if node2 exists in nodeMap
//            && (node1 != node2)
//            && !hasEdge(node1,node2)) {                 //this is to avoid adding a connection between a node and itself
//
//        //add edge between each other
//        this.edgeMap.get(node1).put(node2, w); //O(1)
//        this.edgeMap.get(node2).put(node1, w); //O(1)
//
//        //add each of them in each other's neighbour's list
//        NodeInfo node_1 = (NodeInfo) this.nodeMap.get(node1);
//        node_1.addNi(this.nodeMap.get(node2)); //O(1)
//        NodeInfo node_2 = (NodeInfo) this.nodeMap.get(node2);
//        node_2.addNi(this.nodeMap.get(node1)); //O(1)
//
//        this.MC++;
//        this.num_of_edge++;
//    }
//    else{
//        this.edgeMap.get(node1).put(node2, w); //O(1)
//        this.edgeMap.get(node2).put(node1, w); //O(1)
//
//        //add each of them in each other's neighbour's list
//        NodeInfo node_1 = (NodeInfo) this.nodeMap.get(node1);
//        node_1.addNi(this.nodeMap.get(node2)); //O(1)
//        NodeInfo node_2 = (NodeInfo) this.nodeMap.get(node2);
//        node_2.addNi(this.nodeMap.get(node1)); //O(1)
//
//        this.MC++;
//    }
//}


    @Override
    public Collection<node_info> getV() {

            return nodeMap.values();

    }

    //if node_id has no neighbours returns 0
    @Override
    public Collection<node_info> getV(int node_id) {
        NodeInfo node = (NodeInfo) this.nodeMap.get(node_id);
        return node.neighbours.values();
    }

    @Override
    public node_info removeNode(int key) {
        //if the node to remove is in the map
        if (this.nodeMap.containsKey(key)) {

            //NEIGHBOURS UPDATE
            //run on all the map - O(n)
            for (Integer i : this.nodeMap.keySet()) { //O(n)

                //If node i has an edge with node key
                if (hasEdge(i, key)) { // O(1)
                    //remove edge between i and key - O(1)
                    ((NodeInfo) this.nodeMap.get(i)).removeNei(this.nodeMap.get(key));
                    this.num_of_edge--;
                }
            }

            // EDGEMAP UPDATE
            for (Integer id : edgeMap.get(key).keySet()) { //for all the key's node edges
                edgeMap.get(id).remove(key); // delete the edge id -> key
            }

            edgeMap.remove(key); // removes all key's node edges
            MC++;

            //returns the node from the graph and deletes it
            return this.nodeMap.remove(key);
        }
        return null; //else return null
    }

    @Override
    public void removeEdge(int node1, int node2) {
        //If node1 has an edge with node2
        if (hasEdge(node1, node2)) {

            //NEIGHBOURS UPDATE
            NodeInfo node = (NodeInfo) this.nodeMap.get(node1);
            node.removeNei(this.nodeMap.get(node2)); // removes node2 from node1
            node = (NodeInfo) this.nodeMap.get(node2);
            node.removeNei(this.nodeMap.get(node1));

            //EDGEMAP UPDATE
            edgeMap.get(node1).remove(node2);
            edgeMap.get(node2).remove(node1);

            num_of_edge--;
            MC++;
        }
    }

    @Override
    public int nodeSize() {
        return this.nodeMap.size();
    }

    @Override
    public int edgeSize() {
        return this.num_of_edge;
    }

    @Override
    public int getMC() {
        return this.MC;
    }
}
