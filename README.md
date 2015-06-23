# algos-shortest-paths
This project aims at building a initial graph, updating the graph to reﬂect changes, ﬁnding the shortest path between any two vertices in the graph based on its current state, printing the graph, and ﬁnding reachable sets of vertices.


Compilation:
The input file “network.txt” is to be given as argument to the main function through the console. The entire file path needs to be specified if file is not in parent/working directory. Files are easily identified if the entire path is specified. The input is obtained from standard input as commands/queries. The output is displayed in the standard output and can be piped to any file for reviewing the program’s functionality. The program exits with a ‘quit’ command or any exception thrown through the program.

What works?
As instructed, the initial network/graph is formed with two directed edges one in each direction between vertices specified in the input file. After this, any edge can be deleted/added between vertices. Vertices’ can be marked down/up. Edges can also be marked down/up.

What does not work?
Vertex deletion or insertion is not supported as of now. The only way to add a vertex to the graph is to add an edge from new vertex to some other vertex currently present in the graph. Deletion of vertices are also not supported. The graph supports disjoint vertices, i.e. one or more vertices can be separated from the graph and can thrive alone. The path distances are sometimes in displayed as 1.4000001 instead of 1.4 due to the usage of floating point numbers to hold distance measurements. This can easily be rectified by rounding off the digits to any precision required.

Environment:
Netbeans 8.0.2 IDE along with Java JDK 1.7 was used to develop the code.

Input and Output:
•	Initial network is formed from input file. Input strings in the file are undirected edges in the graph. The file locations are specified as the path for the strings to be populated.
•	Queries/Commands are given as console/standard input from users.
•	Output is written to standard output and can be piped for comparison if necessary.
•	A sample input and output file is also attached with this project folder.

Algorithm & Code Description:
•	The main function will take an input file containing undirected edges between vertices specified with edge weights.
•	processRequest function processes commands/queries from user given via standard input.
•	getVertex searches if vertex is present, if present returns it, else adds new vertex and returns it.
•	addUndirectedEdge function adds two directed edges from source to destination with given edgewt.
•	addDirectedEdge function adds a directed edge between given vertices with given edgewt.
•	searchandadd searches for the directed edge, if present, updates its edgewt, else, adds it to the graph.
•	deleteDirectedEdge function deletes a particular directed edge from the graph.
•	searchanddelete function searches for the directed edge, if found, removes it from the graph.
•	downDirecetdEdge function marks an edge with status DOWN.
•	edgedownfn searches for an edge, if found, updates its status to DOWN.
•	upDirectedEdge function marks an edge with status UP.
•	edgeupfn searches for an edge, if found, updates its status to UP.
•	upVertex function marks an vertex with status UP.
•	downVertex function marks an vertex with status DOWN
•	path prints the shortest path calculated by dijkstra’s shortest path algorithm
•	print prints the entire graph with vertices, edges, edgewts and statuses.
•	reachable prints the reachable vertices from each vertex in alphabetical order.
o	Complexity – O (V2+E). 
o	Analysis: reachable executes for each vertex to find the reachable vertices from it. To find the reachable vertices, DFSVisit is used. DFSVisit recursively performs a DFS search with source as the origin vertex. DFSVisit takes O(E) time. For each vertex DFSVisit executes accounting to O(V) time. But all vertices are reset before DFSVisit is performed accounting to O(V2) time. Hence total running time of reachable function is O(V2+E).
o	A more efficient algorithm can be formulated by using a memorized version of dynamic programming which stores the reachbility between vertices throughout its execution.
•	DFSVisit does a recursive DFS search with origin vertex given as argument.
•	quit command exits the program.
•	PriorQueue class is a tailored implementation of priority queue with necessary changes made to suit the given programming problem.
•	Edge class denotes an edge in graph with a target vertex name, edgewt and status.
•	Vertex class denotes a vertex in the graph with vertex name, adjacency list of edges, prev parent, dist to store shortest distance from source, status and visited variable to denote discovery.

Data Structure Design:
•	PriorQueue
o	Contains variables mySize and myList (ArrayList of vertices)
o	Constructor initializes an empty priority queue (min heap). Constant time execution.
o	iterator method is a overrided method to return order of elements.
o	PQItr is a submethod for iterator method
o	add method adds a vertex into priority queue in O(log n) time
o	addkeymod method checks if the vertex is present in the priority queue by searching through the array list. If present, it changes the edgewt of the vertex and performs decrease key operation at that index. Else, adds the vertex to the priority queue. Runs in O(log n) time. decreasekey takes lesser time, but time is overbound by adding a node to priority queue.
o	decreasekey function gets the index of the element under consideration. It checks if the parents are lesser than the element. If lesser, it breaks out of operation and returns control, else, it swaps the element with the parent and performs the decreasekey function iteratively on the new position of the element. Runs in less than O(log n) time where n is the number of nodes/vertices in priority queue. Actual running time is O(log (n-k)) where k is the height of the node from the leaf node of min heap.
o	parent and swap methods are supplementary methods for proper working of decrease key operation. Runs in constant time.
o	size function returns the size of the priority queue. Runs in constant time.
o	isEmpty function returns true if priority queue is empty. Runs in constant time.
o	poll function returns the smallest element of the priority queue, i.e. the root element of the min heap and removes it by replacing it with the last element of the priority queue and running heapify at root node. Runs in O(log n) where n is number of nodes/vertices.
o	heapify function takes a input index. It compares the element with its children. If the elements are smaller, then a swap occurs with the smallest child and the process is iteratively carries out until a suitable position is found for the element under consideration. Runs in O(log n) where n is number of nodes/vertices.
•	Edge
o	Contains target vertex, edgewt and status
o	Constructor assigns target vertex, edgewt and status
o	compareTo method compares two edges with respect to name of the target vertex
•	Vertex
o	Contains vertex name, adjacency list of edges, prev variable to hold parent of the vertex and dist variable to hold shortest distance from source vertex during shortest path algorithm execution, status , and visited variable to mark discovery during DFSVisit.
o	Constructor assigns name, initializes adjacency list, status, dist, and visited variables.
o	compareTo method comapres two vertices with their names.
•	Graph
o	Contains a vertex map to hold vertex values
o	clearAll function to reset vertices
o	Several functions to perform operations on the network through user commands from main method.
