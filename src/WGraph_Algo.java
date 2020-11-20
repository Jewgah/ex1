package ex1.src;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.util.*;

public class WGraph_Algo implements weighted_graph_algorithms {

    private weighted_graph myGraph;
    private HashSet<node_info> visited_set = new HashSet<>(); // Hashset containing all the visited nodes

    //Constructor
    public WGraph_Algo(){
        this.myGraph = new WGraph_DS();
        this.visited_set = new HashSet<>();
    }

    Comparator<node_info> comparator = new Comparator<node_info>() {
        @Override
        public int compare(node_info node1, node_info node2) {
            return (int) (node1.getTag() - node2.getTag());
        }
    };

    @Override
    public void init(weighted_graph g) {
        this.myGraph = g;
    }

    @Override
    public weighted_graph getGraph() {
        return this.myGraph;
    }

    @Override
    public weighted_graph copy() {
        weighted_graph myCopy = new WGraph_DS((WGraph_DS) this.myGraph);
        return myCopy;
    }

    @Override
    public boolean isConnected() {

        if (this.myGraph == null) {
            return false;
        }
        if (this.myGraph.nodeSize() == 0 || this.myGraph.nodeSize() == 1) {
            return true;
        }

        Collection<node_info> nodeList = this.myGraph.getV();
        if (BFS(nodeList, this.myGraph)) return true;
        else return false;
    }

    @Override
    //if path exists returns total weight of the shortest path, else return -1
    public double shortestPathDist(int src, int dest) {

            node_info start = myGraph.getNode(src);
            node_info end  = myGraph.getNode(dest);

            if(getGraph().getV().contains(start) && getGraph().getV().contains(end)){// if src and dest node are both in graph

                Dijkstra(myGraph,start);
                if(end.getTag() < Double.POSITIVE_INFINITY){
                    return end.getTag();
                }
            }
        return - 1;
    }


    @Override
    public List<node_info> shortestPath(int src, int dest) {

        node_info start = myGraph.getNode(src);
        node_info end  = myGraph.getNode(dest);

        if(shortestPathDist(src,dest) != -1){ // if there is a shortest path

            String[] finalInfo = end.getInfo().split(",");
            int[] arr = new int [finalInfo.length];

            for(int i=0; i<arr.length; i++) {
                arr[i] = Integer.parseInt(finalInfo[i]);
            }


            List<node_info> nodePath = new ArrayList<>();

            for(int i=0; i<arr.length ; i++){
                nodePath.add(myGraph.getNode(arr[i]));
            }

            return nodePath;
        }

        return null; //no path found
    }

    @Override
    public boolean save(String file) {

        //Serialization
        try {

            //Saving object in a file
            File f = new File(file);

            FileOutputStream in = new FileOutputStream(f);
            ObjectOutputStream out = new ObjectOutputStream(in);

            //Method for serialization of an Object
            out.writeObject(this.myGraph);

            out.close();
            in.close();

            return true;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean load(String file) {

       // WGraph_DS obj = null;

        try {

            FileInputStream fi = new FileInputStream(file);
            ObjectInputStream in = new ObjectInputStream(fi);

            weighted_graph obj = (weighted_graph) in.readObject(); //loading the graph from file

            this.myGraph = obj; //loading the graph in myGraph

            in.close();
            fi.close();

            return true;

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Breath First Search algorithm:
     * <p>
     * <p>
     * Given a Collection of nodes_info from a given graph,
     * the BFS algorithm checks if the graph KACHIR.
     * First we initiate all tags to 0 by calling init_Tag() method.
     * BFS is a graph traversal algorithm that starts traversing the graph from a random node and explores
     * all the neighbours nodes, then do the same with all it's unexplored nearest nodes.
     * Each time we visit a node we set its tag to 1, as visited.
     * The algorithm follows the same process by using a queue stocking all explored nodes, until
     * the queue is empty which means all the neighbours are connected by an existing path
     * Then we use a method called checksTag() to check if all tags are marked as visited (==1).
     *
     * @param myNodeList collections of node_info.
     * @param g          given graph.
     * @return boolean , true: if all nodes have been visited, false if not;
     */
    static private boolean BFS(Collection<node_info> myNodeList, weighted_graph g) {

        Queue<node_info> q = new LinkedList<>();

        if (myNodeList.isEmpty()) {
            return false;
        }

        //initializes all nodes tag to 0 (unvisited)
        init_Tag(g.getV());

        Iterator hit = myNodeList.iterator();
        node_info a = (node_info) hit.next();
        a.setTag(1); //set first node to visited
        q.add(a);    //adds it to queue


        while (!q.isEmpty()) {

            //dequeue the first in queue.
            node_info first = q.remove();
            //getting all the neighbours
            Collection<node_info> neigh = g.getV(first.getKey());
            if (neigh == null) {
                //if there are no neighbours then there are no edges
                // meaning the graph cannot be connected. then return false
                return false;
            }

            Iterator bgu = neigh.iterator();
            while (bgu.hasNext()) {  //iterating over the neighbours nodes
                node_info ed = (node_info) bgu.next();
                int next = ed.getKey();    //gets the next neighbour.
                if (g.getV().contains(ed)) {
                    if (g.getNode(next).getTag() == 0) {    //checks if already visited.
                        g.getNode(next).setTag(1);    //if not visited set to visited.
                        q.add(g.getNode(next));    //adds the node to the queue.
                    }
                }
            }
        }
        return checkTags(myNodeList); //checks the tag on all vertexes.
    }

    /**
     * sets every node's tag to 0 (unvisited)
     *
     * @param nodeList
     */
    static private void init_Tag(Collection<node_info> nodeList) {
        Iterator itr = nodeList.iterator();
        while (itr.hasNext()) {
            node_info node = (node_info) itr.next();
            node.setTag(0);
        }
    }


    /**
     * Iterates on all the nodes and checks if every tag = 1 (visited). If they're all visited the graph is kachir
     *
     * @param nodeList
     * @return true: all tags == 1 , false: if at least one tag == 0 ;
     */
    private static boolean checkTags(Collection<node_info> nodeList) {

        Iterator cavs = nodeList.iterator();
        while (cavs.hasNext()) {
            node_info check = (node_info) cavs.next();
            if (check.getTag() == 0) {
                return false;
            }
        }
        return true;
    }

    //we will use the tag of a given node as the shortest path value to get there from a source node
    private void Dijkstra (weighted_graph g, node_info source){


        visited_set.clear();       //Clears Hashset (visited nodes)

        //initializes all nodes info to null and all nodes tags to +INF
        Collection<node_info> nodelist = myGraph.getV();
        for(node_info node: nodelist){
            node.setInfo(null);
            node.setTag(Double.POSITIVE_INFINITY);
        }

        source.setTag(0); // initialize only the source tag to 0
        source.setInfo(source.getKey()+""); //initialize Info

        PriorityQueue<node_info> pq  = new PriorityQueue<>(3,comparator);
        pq.add(source);

        while (!pq.isEmpty()){
            node_info u = pq.poll(); //dequeue from front of queue

            //if u hasn't been visited yet
                visited_set.add(u); // set him as visited

                /*visit the unvisited neighbours nodes, starting from
			    the nearest node(smallest shortestDistance)*/
                for (node_info e : myGraph.getV(u.getKey())) { //for each u neighbour

                    e.setInfo(u.getInfo());

                    if(!visited_set.contains(e)){
                        node_info v = e;
                        double weight = g.getEdge(e.getKey(), u.getKey());
                        double distanceFromU = u.getTag() + weight;


                        if (distanceFromU < v.getTag()) { //if we found a shortest path from u to neighbour e

                            /*remove v from queue for updating
                            the shortestDistance value*/
                            pq.remove(v);
                            v.setTag(distanceFromU); // v's tag now stores the shortest path from u to v
                            pq.add(v); // put it back in pq updated


                            e.setInfo(e.getInfo()+","+v.getKey()); //update shortestpath in info

                            visited_set.add(e);
                        }
                    }
                }
        } // if we get there there is no path

        //print all nodes tags and infos
//        for (node_info node: nodelist) {
//            System.out.println("key: "+node.getKey()+" final_tag: "+node.getTag()+" final_info: "+node.getInfo());
//        }
//        System.out.println("\nfinal_info node 10 expected: 0,1,5,7,10");
    }
}
