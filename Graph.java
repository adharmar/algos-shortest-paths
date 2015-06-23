
/**
 *
 * @author Anand Kumar Dharmaraj
 * UNCC ID: 800867560
 */


import java.io.FileReader;
import java.io.IOException;
import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.LinkedList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Scanner;



// Used to signal violations of preconditions for
// various shortest path algorithms.
class GraphException extends RuntimeException
{
    public GraphException( String name )
    {
        super( name );
    }
}

// represents an edge in the graph
class Edge implements Comparable<Edge>
{
    // contains a head vertex to which the edge is pointing to
    public Vertex ver;
    // edgewt has the weight of the edge
    public float edgewt;
    // status of the edge
    public String status;
    // constructor to initialize edges
    public Edge(Vertex in, float inedgewt)
    {
        ver=in; edgewt=inedgewt; status ="UP";
    }

    // comparison between edges with respect to name of the head vertex to which the edge points to
    @Override
    public int compareTo(Edge o)
    {
        return ver.name.compareTo(o.ver.name);   
    }

}
// Represents a vertex in the graph.
class Vertex implements Comparable<Vertex>
{
    public String     name;   // Vertex name
    public List<Edge> adj;    // Adjacent vertices
    public Vertex     prev;   // Previous vertex on shortest path
    public float        dist;  // Distance of path
    public String status; // status of vertex either UP or DOWN
    public int visited;
 
    // initialization constructor for vertex class
    public Vertex( String nm )
    { 
        name = nm; 
        adj = new LinkedList<Edge>( ); 
        reset( ); 
        status="UP";
    }
    // resets the vertex values
    public void reset( )
    { 
        dist = Graph.INFINITY; 
        prev = null; 
        visited=0;
    } 
    
    // compare vertices with name attribute
    public int compareTo(Vertex other)
    {
        
        return name.compareTo(other.name);
    }
      
}

// implemented priority queue class
class PriorQueue extends AbstractCollection
{
    // mySize had the size of the heap
    private int mySize;
    // myList has vertices
    private ArrayList<Vertex>  myList;

    /**
    * The order of the elements returned by the iterator is not specified
    * @return an iterator of all elements in priority queue
    */

    public Iterator iterator()
    {
        return new PQItr();
    }
    
    /**
     * This is a trivial iterator class that returns
     * elements in the PriorQueue ArrayList field
     * one-at-a-time
     */
    private class PQItr implements Iterator
    {
        public Vertex next()
        {
            return myList.get(myCursor);
        }
        
        public boolean hasNext()
        {
            return myCursor <= mySize;
        }

        public void remove()
        {
            throw new UnsupportedOperationException("remove not implemented");
        }
        
        private int myCursor = 1;
    }

    
    
    /**
     * constructs an empty priority queue, when new elements
     * are added their natural order will be used to determine
     * which is minimal. 
     */
    
    public PriorQueue()
    {
        myList = new ArrayList<Vertex>();
        myList.add(null);             // first slot has index 1
        mySize = 0;
    }

    
    /**
     * A new vertex 'v' is added to the priority queue
     * in O(log n) time where n is the size of the priority queue.
     */
    
    public void add(Vertex v)
    {
        myList.add(v);        // stored, but not correct location
        mySize++;             // added element, update count
        int k = mySize;       // location of new element
        
        while (k > 1 && myList.get(k/2).dist > v.dist)
        {
            myList.set(k, myList.get(k/2));
            k /= 2;
        }
        myList.set(k,v);
        
    }
    // addkeymod function checks if the Vertex 'v' is present in the priority queue
    // if it is present, it changes the value of edgewt the vertex holds
    // this value updation is always a decrease key operation
    // else if it is not present, it adds the vertex to the
    // priority queue
    public boolean addkeymod(Vertex v)
    {
        boolean found=false;
        int listsize = myList.size();

        for(int i=1;i<listsize;i++)
        {
            // if vertex found
            if(myList.get(i).compareTo(v)== 0)
            {
                // update its distance , this updation is essentially a decrease key operation
                myList.get(i).dist=v.dist;
                // call decrease key at the vertex index
                decreasekey(i);
                found=true;
            }
        }
        // if not found, add the vertex to the priority queue
        if(found==false)
        {
            add(v); 
        }
        return true;
    }
    // returns the parent index of the given index
    private int parent(int index) {
        int x = ((index) / 2);
        return x;
    }
    // swaps two vertices
    private void swap(int x, int y)
    {
        Vertex temp=myList.get(x);
        myList.set(x,myList.get(y));
        myList.set(y, temp);
    }
    // decrease key operation will get a index and compare it with its parents until a proper location for the element is found
    public void decreasekey(int i)
    {
        // get vertex 
        Vertex v = myList.get(i);
        // get the parent of this vertex
        int parentindex=parent(i);
        //iterate until parent is lesser than or equal to child
        while (true)
        {
            // if vertex position is <= 1 , i.e. either root or null then dont do anything
            if(i<=1)
                break;
            // if parent vertex dist is lesser than considered vertex dist do nothing and break out of the loop
            if (myList.get(parentindex).dist<=v.dist)
            {
                break;
            }
            // in case the loop is not broken, i.e. parent is not less than or equal to vertex
            // then swap the elements
            swap(parentindex,i);
            // to iterate until parent is lesser than or equal to child
            i=parentindex;
            parentindex=parent(i);
        }
    }
    /**
     * @return the number of elements in the priority queue
     */
    public int size()
    {
        return mySize;
    }

    /**
     * @return true if and only if the priority queue is empty
     */
    public boolean isEmpty()
    {
        return mySize == 0;
    }

    /**
     * The smallest/minimal element is removed and returned
     * in O(log n) time where n is the size of the priority queue.
     *
     * @return the smallest element (and removes it)
     */
    
    public Vertex poll()
    {
        if (! isEmpty())
        {
            Vertex hold = myList.get(1);
            
            myList.set(1, myList.get(mySize));  // move last to top
            myList.remove(mySize);              // pop last off
            mySize--;
            if (mySize > 1)
            {
                // heapify at root element
                heapify(1);
            }
            return hold;
        }
        return null;
    }


 

    /**
     * works in O(log(size()-vroot)) time
     * @param vroot is the index at which re-heaping occurs
     * @precondition: subheaps of index vroot are heaps
     * @postcondition: heap rooted at index vroot is a heap
     */
    
    private void heapify(int vroot)
    {
        Vertex last = myList.get(vroot);
        int child, k = vroot;
        while (2*k <= mySize)
        {
            child = 2*k;
            if (child < mySize && myList.get(child).dist>myList.get(child+1).dist)
            {
                child++;
            }
            if (last.dist<= myList.get(child).dist)
            {
                break;
            }
            else
            {
                myList.set(k, myList.get(child));
                k = child;
            }
        }
        myList.set(k, last);
    }
}


// Graph class
//
// CONSTRUCTION: with no parameters.
//
// ******************PUBLIC OPERATIONS**********************
// void addEdge( String v, String w ) --> Add undirected edge, i.e. directed edge in both directions of vertices under consideration
// void printPath( String w )   --> Prints path after algorithm is run
// void dijkstra( String s )  --> Single-source dijkstra
// ******************ERRORS*********************************
// Some error checking is performed to make sure graph is ok,
// and to make sure graph satisfies properties needed by each
// algorithm.  Exceptions are thrown if errors are detected.

public class Graph
{
    public static final float INFINITY = Float.MAX_VALUE;
    // vertexMap to store vertices of the Graph
    private Map<String,Vertex> vertexMap = new HashMap<String,Vertex>( );
    

    /**
     * If vertexName is not present, add it to vertexMap.
     * In either case, return the Vertex.
     */
    private Vertex getVertex( String vertexName )
    {
        Vertex v = vertexMap.get( vertexName );
        // if vertex not present, add it
        if( v == null )
        {
            v = new Vertex( vertexName );
            vertexMap.put( vertexName, v );
        }
        return v;
    }
    
    // search for an edge, if present change the edgewt of the edge
    // else add the edge to graph
    private void searchandadd(Vertex v, Edge x)
    {
        // get list of edges for the vertex under consideration
        List<Edge> veredges = v.adj;
        // variables to note edge finding
        int found=0;
        int index=0;
        // search the adjacency list of the vertex for the edge
        for(int i=0;i<veredges.size();i++)
        {
            if(veredges.get(i).ver.equals(x.ver))
            {
                found=1;
                index=i;
            }
        }
        // if edge found, change its edgewt
        if(found==1)
        {
            v.adj.get(index).edgewt=x.edgewt;
        }
        // else add the edge to the adjacency list of the vertex
        else
        {
            v.adj.add(x);
        }
    }
    // search and delete an edge in graph
    private void searchanddelete(Vertex u, Vertex v)
    {
        // get adjacency list for the vertex u (tail/source vertex)
        List<Edge> veredges = u.adj;
        // if edge is present, delete it, else do nothing
        for(int i=0;i<veredges.size();i++)
        {
            if(veredges.get(i).ver.equals(v))
            {
                // edge found, delete it
                u.adj.remove(i);
            }
        }
    }
    // search and DOWN an edge in graph
    private void edgedownfn(Vertex u, Vertex v)
    {
        // get the adjacency list of the vertex u (tail/source vertex)
        List<Edge> veredges = u.adj;
        // search for the edge, if found, update its status to DOWN
        for(int i=0;i<veredges.size();i++)
        {
            if(veredges.get(i).ver.equals(v))
            {
                // edge found, update status
                u.adj.get(i).status="DOWN";
            }
        }
    }
    // search and UP an edge in graph
    private void edgeupfn(Vertex u, Vertex v)
    {
         // get the adjacency list of the vertex u (tail/source vertex)
        List<Edge> veredges = u.adj;
        // search for the edge, if found, update its status to UP
        for(int i=0;i<veredges.size();i++)
        {
            if(veredges.get(i).ver.equals(v))
            {
                // edge found, update status
                u.adj.get(i).status="UP";
            }
        }
    }
    /**
     * Add a new undirected edge to the graph.
     * This method effectively adds two directed edges in each direction for the given two vertices
     */
    public void addUndirectedEdge( String sourceName, String destName, float ewt )
    {
        // get the vertex variables
        Vertex v = getVertex( sourceName );
        Vertex w = getVertex( destName);
        // form edges
        Edge x= new Edge(w, ewt);
        Edge y=new Edge(v, ewt);
        
        // adding two directed edges to the graph for one line of input
        // search if edge is present, if present update , else add
        searchandadd(v,x);
        searchandadd(w,y);
    }
    /**
     * Add a new directed edge to the graph.
     * Adds one directed edge from source to destination
     * 
     */
    public void addDirectedEdge( String sourceName, String destName, float ewt )
    {
        // get vertex variables
        Vertex v = getVertex( sourceName );
        Vertex w = getVertex( destName);
        // form the edge
        Edge x= new Edge(w, ewt);
        // adding one directed edge to the graph for one line of input
        // search if edge is present, if present update , else add
        searchandadd(v,x);        
    }
    /**
     * Delete directed edge from the graph.
     */
    public void deleteDirectedEdge( String sourceName, String destName)
    {
        // get vertex variables
        Vertex v = getVertex( sourceName );
        Vertex w = getVertex( destName);
       
        // search and delete the directed edge between vertices if present in the graph
        searchanddelete(v,w);        
    }
    // set status as DOWN for the edge between source and destination
    public void downDirectedEdge( String sourceName, String destName)
    {
        // get vertex variables
        Vertex v = getVertex( sourceName );
        Vertex w = getVertex( destName);
        
        // set status DOWN for the edge between given vertices
        edgedownfn(v,w);        
    }
    // set status as UP for the edge between source and destination
    public void upDirectedEdge( String sourceName, String destName)
    {
        // get vertex variables
        Vertex v = getVertex( sourceName );
        Vertex w = getVertex( destName);
        
        // set status UP for the edge between given vertices
        edgeupfn(v,w);
    }
    // set status as UP for the given vertex
    public void upVertex( String vertoup)
    {
        Vertex v = getVertex( vertoup );
        // set status UP for the given vertex
        v.status="UP";     
    }
    // set status as DOWN for the given vertex
    public void downVertex( String vertodown)
    {
        Vertex v = getVertex( vertodown );
         // set status DOWN for the given vertex
        v.status="DOWN";      
    }
    /**
     * Driver routine to print path.
     * It calls recursive routine to print shortest path to
     * destNode after dijkstra shortest path algorithm has run.
     */
    public void printPath( String destName )
    {
        Vertex w = vertexMap.get( destName );
        if( w == null )
            throw new NoSuchElementException( "Destination vertex not found" );
        else if( w.dist == INFINITY )
            System.out.println( "Either "+destName + " is unreachable or source vertex is down" );
        else
        {
            //System.out.print( "(Distance is: " + w.dist + ") " );
            printPath( w );
            System.out.print(" "+w.dist);
            System.out.println( );
        }
    }

    

    /**
     * Recursive routine to print shortest path to dest
     * after running shortest path algorithm. The path
     * is known to exist.
     */
    private void printPath( Vertex dest )
    {
        if( dest.prev != null )
        {
            printPath( dest.prev );
            System.out.print( " " );
        }
        System.out.print( dest.name );
    }
    // method to print entire graph with edges, edgewt and status of vertices and edges
    public void printGraph()
    {
        // get all vertices from vertex map
        List<Vertex> verlist=new ArrayList<Vertex>(vertexMap.values());
        // sort the vertices in O(n logn) time Collections.sort algorithm
        Collections.sort(verlist);
        // for each vertex print its edges
        for(Vertex v: verlist)
        {
            System.out.print(v.name);
            if(v.status.equals("DOWN"))
            {
                System.out.print(" ");
                System.out.println(v.status);
            }
            else
                    System.out.println();
            // get the edge adjacency list
            List<Edge> edges=v.adj;
            // sort the edges and print them with their edgewt and status
            Collections.sort(edges);
            for(int i=0;i<edges.size();i++)
            {
                System.out.print(" ");
                System.out.print(edges.get(i).ver.name);
                System.out.print(" ");
                System.out.print(edges.get(i).edgewt);
                if(edges.get(i).status.equals("DOWN"))
                {
                    System.out.print(" ");
                    System.out.println(edges.get(i).status);
                }
                else
                    System.out.println();
            }
        }
    }   
    // code to print reachable vertices
    public void reachableGraph()
    {
        // get all the vertices
        List<Vertex> verlist=new ArrayList<Vertex>(vertexMap.values());
        // sort them for alphabetical ordering
        Collections.sort(verlist);
        // for each vertex, do a recursive DFSVisit search to get all the reachable vertices
        for(Vertex start: verlist)
        {
            clearAll();
            // list to store reachable vertices
            List<String> temp=new LinkedList<String>();
            
            if(start.status.equals("UP"))
            {
                System.out.print(start.name);
                System.out.println();
                // DFSVisit with vertex start
                DFSVisit(start,temp);
            }
            // sort the reachable vertices and print them
            Collections.sort(temp);
            for(int i=0;i<temp.size();i++)
            {
                System.out.println(" "+temp.get(i));
            }
        }         
    }
    // DFSVisit does a recursive DFS search
    private void DFSVisit(Vertex start,List<String> temp)
    {
        // mark the vertex visited
        start.visited=1;
        // for each edge, get the vertex and add it to list if not visited
        for( Edge v : start.adj )
        {
            if(v.status.equals("UP"))
            {
                Vertex ver = v.ver;

                if( ver.status.equals("UP") )
                {
                    if(ver.visited!=1)
                    {
                        temp.add(ver.name);
                        // recursive call to newly discovered vertex
                        DFSVisit(ver,temp);
                    }
                }
            }                    
        }
    }  
    /**
     * Initializes the vertex output info prior to running
     * any shortest path algorithm or reachable vertices algorithm.
     */
    private void clearAll( )
    {
        for( Vertex v : vertexMap.values( ) )
            v.reset( );
    }

    /**
     * Single-source dijkstra shortest-path algorithm.
     */
    public void dijkstra( String startName )
    {
        clearAll( ); 

        Vertex start = vertexMap.get( startName );
        if( start == null )
            throw new NoSuchElementException( "Start vertex not found" );

        // implemented PriorQueue (Min Heap)
        PriorQueue q = new PriorQueue();
        q.add( start ); start.dist = 0;
        
        // until q is empty
        while( !q.isEmpty( ) )
        {
            // get the min vertex
            Vertex u = q.poll( );
            
            if(u.status.equals("UP"))
            {
                // for the vertex in each edge of the vertex, calculate distance
                for( Edge v : u.adj )
                {
                    if(v.status.equals("UP"))
                    {
                        Vertex ver = v.ver;
                        float temp= v.edgewt;
                        // if distance is lesser than previously stored value, update dist and prev values
                        if( ver.dist > u.dist+temp )
                        {
                            ver.dist = u.dist + temp;

                            ver.prev = u;
                            // add key mod will check if the vertex is present, if present, will change dist
                            // else will add vertex to priority queue
                            q.addkeymod(ver);
                        }
                    }
                }
            }
        }
    }



    /**
     * Process a request/command; return false if command is 'quit' or any exception caught.
     */
    public static boolean processRequest( Scanner in, Graph g )
    {
        try
        {
            // get commands and process
            System.out.print( "Enter command: " );
            String command = in.nextLine( );
            StringTokenizer st = new StringTokenizer( command );
            // first string is the command
            String temp=st.nextToken();
            // for add edge add a directed edge between vertices
            if(temp.equals("addedge"))
            {
                if( st.countTokens( ) != 3 )
                {
                    System.err.println( "Skipping ill-formatted line " + command );
                }
                else
                {
                    String source  = st.nextToken( );
                    String dest    = st.nextToken( );
                    float edgewt = Float.parseFloat(st.nextToken());
                    g.addDirectedEdge( source, dest, edgewt );
                }
            }
            // delete the particular directed edge
            else if(temp.equals("deleteedge"))
            {
                if( st.countTokens( ) != 2 )
                {
                    System.err.println( "Skipping ill-formatted line " + command );
                }
                else
                {
                    String source  = st.nextToken( );
                    String dest    = st.nextToken( );
                
                    g.deleteDirectedEdge( source, dest );
                }                
            }
            // mark edge DOWN
            else if(temp.equals("edgedown"))
            {
                if( st.countTokens( ) != 2 )
                {
                    System.err.println( "Skipping ill-formatted line " + command );
                }
                else
                {
                    String source  = st.nextToken( );
                
                    String dest    = st.nextToken( );
                
                    g.downDirectedEdge( source, dest );
                }
            }
            // mark edge UP
            else if(temp.equals("edgeup"))
            {
                if( st.countTokens( ) != 2 )
                {
                    System.err.println( "Skipping ill-formatted line " + command );
                }
                else
                {
                    String source  = st.nextToken( );
                    String dest    = st.nextToken( );
                
                    g.upDirectedEdge( source, dest );
                }
            }
            // mark vertex UP
            else if(temp.equals("vertexup"))
            {
                if( st.countTokens( ) != 1 )
                {
                    System.err.println( "Skipping ill-formatted line " + command);
                }
                else
                {
                    String vertoup  = st.nextToken( );              
                
                    g.upVertex( vertoup );
                }
            }
            // mark vertex DOWN
            else if(temp.equals("vertexdown"))
            {
                if( st.countTokens( ) != 1 )
                {
                    System.err.println( "Skipping ill-formatted line " + command);
                }
                else
                {
                    String vertodown  = st.nextToken( );              
                
                    g.downVertex( vertodown );
                }
            }
            // get shortest path between vertices
            else if(temp.equals("path"))
            {
                if( st.countTokens( ) != 2 )
                {
                    System.err.println( "Skipping ill-formatted line " + command );
                }
                else
                {
                    String source  = st.nextToken( );
                    String dest    = st.nextToken( );
                    g.dijkstra( source );
                    g.printPath( dest );
                    System.out.println();
                }
            }
            // print the graph with edges and edgewts
            else if(temp.equals("print"))
            {
                //System.out.println("Entering print section..");
                if( st.countTokens( ) != 0 )
                {
                    System.err.println( "Skipping ill-formatted line " + command );
                }
                else
                {
                    g.printGraph();
                    System.out.println();
                }
            }
            // print reachable graph
            else if(temp.equals("reachable"))
            {
                //System.out.println("Entering print section..");
                if( st.countTokens( ) != 0 )
                {
                    System.err.println( "Skipping ill-formatted line " + command );
                }
                else
                {
                    g.reachableGraph();
                    System.out.println();
                }
            }
            // quit the program
            else if(temp.equals("quit"))
            {
                return false;
            }
            // for unkown commands
            else
            {
                System.out.println("Unknown command: ");
            }            
        }
        // catch all exceptions
        catch( Exception e )
        {
            System.err.println(e);
            return false; 
        }
        // return true in case of no exceptions to get next command
        return true;
    }

    /**
     * A main routine that:
     * 1. Reads a file containing edges (supplied as a command-line parameter);
     * 2. Forms the graph with addUndirectedEdge routine repeatedly;
     * 3. Repeatedly prompts for two vertices and
     *    runs the shortest path algorithm.
     * The data file is a sequence of lines of the format
     *    source destination 
     */
    public static void main( String [ ] args )
    {
        // initialize a new graph
        Graph g = new Graph( );
        try
        {
            // read the network file
            FileReader fin = new FileReader( args[0] );
            Scanner graphFile = new Scanner( fin );    

            // Read the edges and insert
            String line;
            while( graphFile.hasNextLine( ) )
            {
                line = graphFile.nextLine( );
                StringTokenizer st = new StringTokenizer( line );

                try
                {
                    if( st.countTokens( ) != 3 )
                    {
                        System.err.println( "Skipping ill-formatted line " + line );
                        continue;
                    }
                    // get the source, destination and edgewt
                    String source  = st.nextToken( );
                    String dest    = st.nextToken( );
                    float edgewt = Float.parseFloat(st.nextToken());
                    // insert undirected, i.e. two edges one in each direction for the specified vertices and given edgewt
                    g.addUndirectedEdge( source, dest, edgewt );
                }
                catch( NumberFormatException e )
                  { System.err.println( "Skipping ill-formatted line " + line ); }
            }
            //System.out.println("Graph Built!");
            //System.out.println("Reading commands..");     
        }
        catch( IOException e )
        { 
            System.err.println( e ); 
        }

        //System.out.println( "File read..." );
        //System.out.println( g.vertexMap.size( ) + " vertices" );
        // to process commands
        Scanner in = new Scanner( System.in );
        while( processRequest( in, g ) );
    }
}
