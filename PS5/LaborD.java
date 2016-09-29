import java.util.*;
import java.io.*;

// year 2015 hash code: JESg5svjYpIsmHmIjabX (do NOT delete this line)

class LaborD {
  private int V; // number of vertices in the graph (number of junctions in Singapore map)
  private int Q; // number of queries
  private ArrayList< ArrayList< IntegerPair > > adjList; // the weighted graph (the Singapore map), the length of each edge (road) is stored here too, as the weight of edge
private int[] dist;
private static final int INF = 1000000000;
 
  public LaborD() {
    

  }

  void preprocess() {
    // initSssp(); // sets the distance info array to INF
   // for (int source=0; source<V; source++) // generate distance info for all possible starting sources
	   // dijkstra(source);
	  
	  
  }

  
  void initSssp(int source) {
	  dist = new int[V];
	  
		  for (int dest=0; dest<V; dest++)
			  dist[dest] = INF;
		  
		  dist[source] = 0;
		  }

  // generates a residual graph with the capacities set at 1
  // convert the adjacency list into matrix because the algorithm requires updating of the graph 
  int[][] generateResidualGraph() {
	  int[][] residualGraph = new int[V][V];
	  	  
	  for (int i=0; i<V; i++)
		  for (int j=0; j<V; j++)
			  residualGraph[i][j] = 0;
	  
	 for (int i=0; i<V; i++)
		 for (IntegerPair edge : adjList.get(i))
		 residualGraph[i][edge.second()] = 1; // ignore the existing weight
	 
		  	  return residualGraph;
	    }
  
 int Query(int source, int sink, int k) {
	      // You have to report the shortest path from Steven and Grace's current position s
    // to reach their chosen hospital t, output -1 if t is not reachable from s
    // with one catch: this path cannot use more than k vertices
    //
    // PS: this query means different thing for the Subtask D (R-option)
    
	 // the number of edge disjoint paths from source to sink is the maximum flow with the edges set to capacity 1
	 int[][] residualGraph = generateResidualGraph();
	  int parentOf[] = new int[V]; // stores path from source to sink

	  int nPaths = 0; // max flow
// using Ford-Fulkerson algorithm	  
        while (isSinkReachable(residualGraph, parentOf, source, sink)) {
        	// the minimum residual capacity is the maximum flow found for this path
        	int minResidualCapacity = INF;
        	for (int vertex=sink; vertex!=source; vertex = parentOf[vertex]) {
        		int current = residualGraph[parentOf[vertex]][vertex]; // res capacity of the edge from parent to this vertex
       
        		if (current < minResidualCapacity)
        			minResidualCapacity = current;
        	}
        	
        	nPaths += minResidualCapacity;
        	// update the residual graph for the path
        	for (int vertex=sink; vertex!=source; vertex = parentOf[vertex]) {
        		int parent = parentOf[vertex];
        		residualGraph[parent][vertex] -= minResidualCapacity;	
        		residualGraph[vertex][parent] += minResidualCapacity; // create/increase the back edges
        }
        }
        
        return nPaths;
  }

 // returns whether there is a path from source to sink. If yes, then the flow can be increased
 // also stores the BFS path from source to sink in parentOf
 boolean isSinkReachable(int[][] residualGraph, int[] parentOf, int source, int sink) {
	  boolean isVisited[] = new boolean[V];
	  for (int i=0; i<V; i++)
		  isVisited[i] = false;
	  
	  parentOf[source] = -1;
	  
	  Queue<Integer> q = new LinkedList<Integer>();
	  q.offer(source);
	  
	  int nVisited = 0;
	  while (q.isEmpty() == false && nVisited<V) {
		  Integer vertex = q.poll();
		  
		  if (isVisited[vertex] == false) {
			  isVisited[vertex] = true;
			  nVisited++;
			  
			  if (vertex == sink)
				  return true; // no need to continue
			  
			  for (int neighbouringVertex=0; neighbouringVertex<V; neighbouringVertex++) {
				  if (residualGraph[vertex][neighbouringVertex] != 0 && isVisited[neighbouringVertex] == false) {
					  q.offer(neighbouringVertex);
					  parentOf[neighbouringVertex] = vertex;
		  }
			  }
		  } // end if	
		  
	  } // end while
	  
	  return false;
 }

    void run() throws Exception {
    // you can alter this method if you need to do so
    IntegerScanner sc = new IntegerScanner(System.in);
    PrintWriter pr = new PrintWriter(new BufferedWriter(new OutputStreamWriter(System.out)));

    int TC = sc.nextInt(); // there will be several test cases
    while (TC-- > 0) {
      V = sc.nextInt();

      // clear the graph and read in a new graph as Adjacency List
      adjList = new ArrayList< ArrayList< IntegerPair > >();
      for (int i = 0; i < V; i++) {
        adjList.add(new ArrayList< IntegerPair >());

        int k = sc.nextInt();
        while (k-- > 0) {
          int j = sc.nextInt(), w = sc.nextInt();
          adjList.get(i).add(new IntegerPair(w, j)); // edge (road) weight (in minutes) is stored here
        }
      }

      preprocess(); // optional

      Q = sc.nextInt();
      while (Q-- > 0)
        pr.println(Query(sc.nextInt(), sc.nextInt(), sc.nextInt()));

      if (TC > 0)
        pr.println();
    }

    pr.close();
  }

  public static void main(String[] args) throws Exception {
    // do not alter this method
    LaborD ps5 = new LaborD();
    ps5.run();
  }
}



class IntegerScanner { // coded by Ian Leow, using any other I/O method is not recommended
  BufferedInputStream bis;
  IntegerScanner(InputStream is) {
    bis = new BufferedInputStream(is, 1000000);
  }
  
  public int nextInt() {
    int result = 0;
    try {
      int cur = bis.read();
      if (cur == -1)
        return -1;

      while ((cur < 48 || cur > 57) && cur != 45) {
        cur = bis.read();
      }

      boolean negate = false;
      if (cur == 45) {
        negate = true;
        cur = bis.read();
      }

      while (cur >= 48 && cur <= 57) {
        result = result * 10 + (cur - 48);
        cur = bis.read();
      }

      if (negate) {
        return -result;
      }
      return result;
    }
    catch (IOException ioe) {
      return -1;
    }
  }
}



class IntegerPair implements Comparable < IntegerPair > {
  Integer _first, _second;

  public IntegerPair(Integer f, Integer s) {
    _first = f;
    _second = s;
  }

  public void setFirst(int f) {
	  _first = f;
  }
  
  public int compareTo(IntegerPair o) {
    if (!this.first().equals(o.first()))
      return this.first() - o.first();
    else
      return this.second() - o.second();
  }

  Integer first() { return _first; }
  Integer second() { return _second; }
}