import java.util.*;
import java.io.*;

// year 2015 hash code: JESg5svjYpIsmHmIjabX (do NOT delete this line)

class OutForAWalk {
  private int nVertices; // number of vertices in the graph (number of rooms in the building)
  private ArrayList< ArrayList< IntegerPair > > adjList; // the weighted graph (the building), effort rating of each corridor is stored here too
private boolean[] isVisited;

private static final int MAX_START_VERTEX = 9;
private static final int MAX_EDGES = 100000;
private int[][] ans;

    public OutForAWalk() {
    

  }

    // because the range of start vertices are limited from 0-9, it is possible to get all possible answers
    // by building the MST 10 times with different starting points 
  void PreProcess() {
	  int nStartVertexPossibilities = (nVertices< MAX_START_VERTEX+1) ? nVertices : MAX_START_VERTEX+1;
	  
	  ans = new int[nStartVertexPossibilities][nVertices]; // because MAX_START_VERTEX is 0-based indexing
    for (int i=0; i<nStartVertexPossibilities; i++)
preProcess(i);
    
  }

  private void preProcess(int start) {
	    int maxEffort = 0, nVisited = 0;

	    	// build the MST for the entire graph from the starting vertex
	    // the maxEffort encountered so far as a new node is added is the answer for the query with source=Start and destination=the newly added vertex
	for (int i=0; i<isVisited.length; i++)
		isVisited[i] = false;


	isVisited[start] = true;
	nVisited++;

	PriorityQueue<IntegerPair> q = new PriorityQueue<IntegerPair>(MAX_EDGES);
	// add all neighbours of the start vertex to the PQ
	for (int neighbour=0; neighbour< adjList.get(start).size(); neighbour++)
	q.offer(adjList.get(start).get(neighbour));

	while (nVisited < nVertices && q.isEmpty() == false) {
		IntegerPair nextMstNode = q.poll();
		int vertex = nextMstNode.second();
		int effort = nextMstNode.first();
		
		if (isVisited[vertex] == false) {
			isVisited[vertex] = true;
			nVisited++;
			
			if (effort > maxEffort)
				maxEffort = effort;
		
			ans[start][vertex] = maxEffort;
			
			// add all neighbours of this vertex into the PQ
			ArrayList<IntegerPair> neighbours = adjList.get(vertex);
			for (int neighbour=0; neighbour< neighbours.size(); neighbour++)
				if (isVisited[neighbours.get(neighbour).second()] == false)
				q.offer(neighbours.get(neighbour));
			
		}
	}
		  }
  
  int Query(int source, int destination) {
        // You have to report the weight of a corridor (an edge)
    // which has the highest effort rating in the easiest path from source to destination for Grace

		  return ans[source][destination];
}
    
    void run() throws Exception {
    // do not alter this method
    IntegerScanner sc = new IntegerScanner(System.in);
    PrintWriter pr = new PrintWriter(new BufferedWriter(new OutputStreamWriter(System.out)));

    int TC = sc.nextInt(); // there will be several test cases
    while (TC-- > 0) {
      nVertices = sc.nextInt();

      // clear the graph and read in a new graph as Adjacency List
      adjList = new ArrayList< ArrayList< IntegerPair > >(nVertices);
      isVisited = new boolean[nVertices];
      for (int i = 0; i < nVertices; i++) {
        adjList.add(new ArrayList< IntegerPair >());

        int k = sc.nextInt();
        while (k-- > 0) {
          int neighbour = sc.nextInt(), effort = sc.nextInt();
          adjList.get(i).add(new IntegerPair(effort, neighbour)); // edge (corridor) weight (effort rating) is stored here
        }
      }

      PreProcess(); // you may want to use this function or leave it empty if you do not need it

      int Q = sc.nextInt();
      while (Q-- > 0)
        pr.println(Query(sc.nextInt(), sc.nextInt()));
      pr.println(); // separate the answer between two different graphs
    }

    pr.close();
  }

  public static void main(String[] args) throws Exception {
    // do not alter this method
    OutForAWalk ps4 = new OutForAWalk();
    ps4.run();
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