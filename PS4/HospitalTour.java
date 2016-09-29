import java.util.*;
import java.io.*;

// year 2015 hash code: JESg5svjYpIsmHmIjabX (do NOT delete this line)

class HospitalTour {
  private int nVertices; // number of vertices in the graph (number of rooms in the hospital)
  private int[] ratings; // the weight of each vertex (rating score of each room)
  private ArrayList<ArrayList<Integer>> adjList;
    
  public HospitalTour() {
    
  }

  // returns the number of vertices visited in the BFS of the graph with the specified vertex disconnected
    private int bfs(int disconnectedVertex) {
    	// if a vertex is disconnected and there are only 1 vertices in total, then no vertices can be visited
    	if (nVertices == 1)
    		return 0;
    	 
    	int startVertex = (disconnectedVertex != 0) ? 0 : 1;
    	int nVisited = 0; // total number of vertices visited    	
    	boolean[] visited = new boolean[nVertices];
    	for (int i=0; i<nVertices; i++)
    		visited[i] = false;
    	
    	Queue<Integer> q = new LinkedList<Integer>();
    	// visit the start vertex
    	q.offer(startVertex);
    	visited[startVertex] = true;
    	nVisited++;
    	
    	while (q.isEmpty() == false) {
    		int curVertex = q.peek();
    		q.poll();
    		for (int i=0; i<adjList.get(curVertex).size(); i++) {
    			int neighbour = adjList.get(curVertex).get(i);
    			if (neighbour != disconnectedVertex && visited[neighbour] == false) {
    				visited[neighbour] = true;
    				nVisited++;
    				q.offer(neighbour);
    			}
    				
    		}
    	}
    	
    	return nVisited;
    }
    
  int Query() {
    int ans = 0;

    // You have to report the rating score of the important room (vertex)
    // with the lowest rating score in this hospital
    //
    // or report -1 if that hospital has no important room
  
// try disconnecting each vertex, and see if the graph remains connected
    ArrayList<IntegerPair> articulationPoints = new ArrayList<IntegerPair>();
    
for (int vertexToDisconnect=0; vertexToDisconnect < nVertices; vertexToDisconnect++) {
	if (bfs(vertexToDisconnect) < nVertices-1)
		articulationPoints.add(new IntegerPair(ratings[vertexToDisconnect], vertexToDisconnect));
}

if (articulationPoints.isEmpty())
	return -1; // if there are no articulation points, there are no important rooms

    Collections.sort(articulationPoints);
    return articulationPoints.get(0).first();
  }

    void run() throws Exception {
    // for this PS3, you can alter this method as you see fit

    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    PrintWriter pr = new PrintWriter(new BufferedWriter(new OutputStreamWriter(System.out)));
    int TC = Integer.parseInt(br.readLine()); // there will be several test cases
    while (TC-- > 0) {
      br.readLine(); // ignore dummy blank line
      nVertices = Integer.parseInt(br.readLine());

      StringTokenizer st = new StringTokenizer(br.readLine());
      // read rating scores, A (index 0), B (index 1), C (index 2), ..., until the nVertices-th index
      ratings = new int[nVertices];
      for (int i = 0; i < nVertices; i++)
        ratings[i] = Integer.parseInt(st.nextToken());

      // clear the graph and read in a new graph as Adjacency list
//       AdjMatrix = new int[nVertices][nVertices];
      adjList = new ArrayList<ArrayList<Integer>>(nVertices);
      
      for (int vertex = 0; vertex < nVertices; vertex++) {
        st = new StringTokenizer(br.readLine());
        int nNeighbours = Integer.parseInt(st.nextToken());
        ArrayList<Integer> neighbours = new ArrayList<Integer>(nNeighbours); // neighbours of adjList[vertex]
        
        while (nNeighbours-- > 0) {
          int neighbour = Integer.parseInt(st.nextToken());
          // AdjMatrix[vertex][neighbour] = 1; // edge weight is always 1 (the weight is on vertices now)
          neighbours.add(neighbour);
        }
        adjList.add(neighbours);
      }

      pr.println(Query());
    }
    pr.close();
  }

  public static void main(String[] args) throws Exception {
    // do not alter this method
    HospitalTour ps3 = new HospitalTour();
    ps3.run();
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