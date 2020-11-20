## Ex1 OOP Ariel University 2020
## @author Jewgah
_______________________________________________________________________________________________________________________
### NodeInfo implements node_info
### WGraph_DS implements weighted_graph
### WGraph_Algo implements weighted_graph_algorithms

## Those three implementations represent a weighted graph of nodes and its algorithms.
_______________________________________________________________________________________________________________________
# 1/ NodeInfo

## The NodeInfo class implements the node_info and the Serializable Interfaces.
## It represents the set of operations applicable on a node (vertex) in an (undirectional) weighted graph.
## NodeInfo is an inner class of WGraph_DS.


### NodeInfo class attributes are:

** private int key: ** A unique key(id) associated with this node.
** private HashMap<Integer, node_data> neighbours: ** A Hashmap were the keys are the node keys and the values are all the Neighbour nodes of this node_info.
** private String info: ** Remark (meta data) associated with this node.
** private double tag: ** Temporal data which can be used be algorithms.

### NodeInfo uses the following methods:

### Getter and Setters
** int getKey(): ** Returns the unique key (id) associated with this node.

** String getInfo(): ** Getter for info

** void setInfo(String s): ** Setter for info

** double getTag(): ** Getter for tag

** public void setTag(double t): ** Setter for Tag

### Methods
** void addNi(node_data t): ** This method adds the node_info (t) to this node_info.

** boolean hasNi(int key): ** Returns true iff this<==>key are adjacent, as an edge between them.

** void removeNei(node_info node): ** Removes the edge between this and node.

** boolean equals(Object o): ** Overriding the equals method in order to compare between two NodeInfos.

NodeInfo has a ** Constructor ** and a ** CopyConstructor. **
In the CopyConstructor we call the putAll() method from the Hashmap library - O(1) - to copy all the nodes.
_______________________________________________________________________________________________________________________
# 2/ WGraph_DS

## The WGraph_DS class implements the weighted_graph and the Serializable Interfaces.
## It represents an undirectional weighted graph.


### Graph_DS class attributes are:

** private HashMap<Integer, node_data> nodeMap: ** A Hashmap representing the graph with all its nodes.
** private HashMap<Integer, HashMap<Integer, Double>> edgeMap: ** A Hashmap representing all the edges of all the nodes.
** private int MC: ** Mode Count: Any change in the inner state of the graph increments it.
** private int num_of_edge: ** Number of edges (undirectional graph).


### WGraph_DS uses the following methods:


** node_data getNode(int key): ** Returns the node_info by the node_id.

** boolean hasEdge(int node1, int node2): ** Returns true iff (if and only if) there is an edge between node1 and node2. - O(1)

** double getEdge(int node1, int node2): ** Returns the weight of the edge bewtween node1 and node2. If there is no edge between them returns -1. O(1)

** void addNode(node_info n): ** Adds a new node to the graph with the given node_info. - O(1)

** void connect(int node1, int node2, double w): ** Connects an edge between node1 and node2 and gives it a weight of w. - O(1)

** Collection<node_info> getV(): ** Returns a pointer (shallow copy) for the collection representing all the nodes in the graph. - O(1)

** Collection<node_info> getV(int node_id): ** Returns a collection containing all the nodes connected to node_id - O(1)

** node_info removeNode(int key): ** Delete the node (with the given ID) from the graph and removes all edges which starts or ends at this node - O(n), |V|=n

** void removeEdge(int node1, int node2): ** Deletes the edge from the graph - O(1)

** int nodeSize(): ** Returns the number of vertices (nodes) in the graph. - O(1)

** int edgeSize(): ** Returns the number of edges (undirectional graph) - O(1)

** int getMC(): ** Returns the Mode Count - for testing changes in the graph. Any change in the inner state of the graph increments it.

### WGraph_DS has a Constructor and a CopyConstructor.
It's Constructor is a regular one, but it's CopyConstructor is special: it allows a deep copy of the WGraph_DS received as parameter using an Iterator
by calling the CopyConstructor of NodeData for each node for the nodeMap, and deepcopying all the edges by updating the inners Hashmaps of the edgeMap using two Iterators.


_______________________________________________________________________________________________________________________
# 3/ WGraph_Algo

## The WGraph_Algo class implements the weighted_graph_algorithms Interface.
## It represents the "regular" Graph Theory algorithms.


### Graph_Algo class attributes are:

** private weighted_graph myGraph: ** The (weighted) graph on which we will use all or algorithms.
** private HashSet<node_info> visited_set: ** A Hashset containing all the visited nodes.

### WGraph_Algo uses the following methods:


** Comparator<node_info> comparator: ** Overriding the compare() method to compare between two node_infos's weights

** void init(weighted_graph g): ** Init the graph on which this set of algorithms operates on.

** public weighted_graph copy(): ** The copy() method calls the WGraph_DS CopyConstructor which calls itself the NodeInfo CopyConstructor for each node, and deepcopying
the edgeMap iterating over all inner Hashmaps of the edgeMap Hashmap, thus performing a deep copy.

** boolean isConnected(): ** Returns true if and only if (iff) there is a valid path from EVERY node to each other node using BFS algorithm.

** double shortestPathDist(int src, int dest): ** Returns the weight of the shortest path between src to dest using Djikstra's algorithm by updating tags.
Returns -1 if there isn't any path.

** List<node_info> shortestPath(int src, int dest): ** returns the shortest path between src to dest - as an ordered List of nodes: src--> n1-->n2-->...dest
using the Djikstra's algorithm by updating the info String of each node. This way, the info String of the final destination node will contain
the shortest path from src to dest, and we convert it from String to List of node_info using split method and parseInt method.

** boolean save(String file): ** Saves this weighted (undirected) graph to the given file name

** boolean load(String file): ** This method loads a graph to this graph algorithm. If the file was successfully loaded - the underlying graph
of this class will be changed (to the loaded one).

** void init_Tag(Collection<node_info> nodeList): ** Sets all the nodes tags to 0

** static boolean checkTags(Collection<node_info> nodeList): ** Checks if all the tags = 1 (visited)

** static private boolean BFS(Collection<node_info> myNodeList, weighted_graph g): **
BFS is a graph traversal algorithm that starts traversing the graph from a random node and explores all the neighbours nodes,
then do the same with all it's unexplored nearest nodes.The algorithm first initiates all tags to 0 by calling the init_Tag() method.
Then it sets tag to 1 as visited each time it visits a node.The algorithm follows the same process by using a queue stocking all explored nodes,
until the queue is empty. Then it uses a method called checksTag() to check if all tags are marked as visited (==1)
meaning that all the neighbours are connected by an existing path.

** private void Dijkstra (weighted_graph g, node_info source): **
for each iteration the top off the PriorityQueue node will represents the shortest path from source by weight. The algorithm works as a greedy algorithm
which will move forwards in the least "expensive" path until reach the Destination node, updating info String and tag of each node.


### WGraph_Algo has a Constructor initializing myGraph as a new WGraph_DS using WGraph_DS constructor, and initializing its Hashset by a new one.
_________________________________________________________________________________________________________