import java.util.*;
import java.io.*;

// year 2015 hash code: JESg5svjYpIsmHmIjabX (do NOT delete this line)

class Labor {
  private int V; // number of vertices in the graph (number of junctions in Singapore map)
  private int Q; // number of queries
  private ArrayList< ArrayList< IntegerPair > > adjList; // the weighted graph (the Singapore map), the length of each edge (road) is stored here too, as the weight of edge
private int[][] dist; // the distance from a source to destination for a path of length k
private static final int INF = 1000000000;
 
  public Labor() {
    

  }

  void preprocess() {
    // initSssp(); // sets the distance info array to INF
   // for (int source=0; source<V; source++) // generate distance info for all possible starting sources
	   // dijkstra(source);
  }

  void initSssp(int source, int k) {
	  dist = new int[k][V];
	  
	  for (int i=0; i<k; i++)
		  for (int j=0; j<V; j++)
			  dist[i][j] = INF;
		  
	  for (int i=0; i<k; i++)
		  dist[i][source] = 0;
		  }

 int Query(int source, int dest, int k) {
	      // You have to report the shortest path from Steven and Grace's current position s
    // to reach their chosen hospital t, output -1 if t is not reachable from s
    // with one catch: this path cannot use more than k vertices
    //
    // PS: this query means different thing for the Subtask D (R-option)
	
	 initSssp(source, k);
	 return dijkstra(source, dest, k);

  }

 int dijkstra(int source, int destination, int maxVerticesInPath) {
	  
	  PriorityQueue<IntegerTriple> q = new PriorityQueue<IntegerTriple>();
	  q.offer(new IntegerTriple(0, 1, source)); // dist, nVerticesFromSource, vertex
	  
	  
	  
	  while (q.isEmpty() == false) {
		  IntegerTriple nearest = q.poll();
		  
		  int currentDist = nearest.first(), nVerticesFromSource = nearest.second(), vertex = nearest.third();
	// System.out.println("at vertex " + vertex + ", " + nVerticesInPathTo[vertex] + " vertices from source ");
		  if (vertex == destination)
			  break;
		  
		  if (dist[nVerticesFromSource-1][vertex] == currentDist && nVerticesFromSource < maxVerticesInPath) {  
	for (IntegerPair neighbour : adjList.get(vertex)) {
		int newDistance = currentDist + neighbour.first();
		int neighbouringVertex = neighbour.second();
		  if (dist[nVerticesFromSource][neighbouringVertex] > newDistance) { // if it is possible to relax via the current vertex
		dist[nVerticesFromSource][neighbouringVertex] = newDistance;
		q.offer(new IntegerTriple(newDistance, nVerticesFromSource+1, neighbouringVertex));
		// System.out.println("neighbouring vertex " + neighbouringVertex + ": " + nVerticesInPathTo[neighbouringVertex] + " vertices from source, distance " + dist[neighbouringVertex]);
		  }
		  }
	}
		  }
		
	  
	  int minDist = INF; 
	  
 for (int i=maxVerticesInPath-1; i>=0; i--)
	 if (dist[i][destination] < minDist)
		 minDist = dist[i][destination];
	    
	  
return minDist!= INF ? minDist: -1;
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
    Labor ps5 = new Labor();
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

  public int compareTo(IntegerPair o) {
    if (!this.first().equals(o.first()))
      return this.first() - o.first();
    else
      return this.second() - o.second();
  }

  Integer first() { return _first; }
  Integer second() { return _second; }
}

class IntegerTriple implements Comparable < IntegerTriple > {
	  Integer _first, _second, _third;

	  public IntegerTriple(Integer f, Integer s, Integer t) {
	    _first = f;
	    _second = s;
	    _third = t;
	  }

	  public int compareTo(IntegerTriple o) {
	    if (!this.first().equals(o.first()))
	      return this.first() - o.first();
	    else if (!this.second().equals(o.second()))
	      return this.second() - o.second();
	    else
	      return this.third() - o.third();
	  }

	  Integer first() { return _first; }
	  Integer second() { return _second; }
	  Integer third() { return _third; }
	}