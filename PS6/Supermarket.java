import java.util.*;
import java.io.*;

// year 2015 hash code: JESg5svjYpIsmHmIjabX (do NOT delete this line)

class Supermarket {
	private static final int INF = 1000000000;
  private int N; // number of items in the supermarket. V = N+1 
  private int K; // the number of items that Steven has to buy
  private int[] shoppingList; // indices of items that Steven has to buy
  private int[][] T; // the complete weighted graph that measures the direct walking time to go from one point to another point in seconds
private int[][] memo;
private int[][] adjMat; // an adjacency matrix where index 0 = vertex 0, and 1...n is the corresponding entry in shopping list
    public Supermarket() {
      }

  int Query() {
        // You have to report the quickest shopping time that is measured
    // since Steven enters the supermarket (vertex 0),
    // completes the task of buying K items in that supermarket as ordered by Grace,
    // then, reaches the cashier of the supermarket (back to vertex 0).
    //
    // write your answer here
if (K <= 16) {
	memo = new int[K+1][1 << (K+1)];

	for (int i=0; i<memo.length; i++)
		for (int j=0; j<memo[i].length; j++)
			memo[i][j] = -1;

	return tspSlow(0, 1);
}
    
    else
    	return tsp2Opt();
  }

  // does dijkstra from source
  private int[] dijkstra(int source) {
	  // copy from PS5
	  int[] distFromSource = new int[N+1];
	  for (int i=0; i<distFromSource.length; i++)
		  distFromSource[i] = INF;
	  
	  distFromSource[source] = 0;
	  
	  PriorityQueue<IntegerPair> q = new PriorityQueue<IntegerPair>();
	  q.offer(new IntegerPair(0, source));
	  
	  while (q.isEmpty() == false) {
		  IntegerPair nearest = q.poll();
		  int dist = nearest.first(), vertex = nearest.second();
		  
		  if (dist == distFromSource[vertex]) {
			  
			  for (int neighbour=0; neighbour<T[vertex].length; neighbour++) {
				  if (neighbour != vertex) {
				  int newDistance = distFromSource[vertex] + T[vertex][neighbour];
				  
				  if (distFromSource[neighbour] > newDistance) { // if it is possible to relax via the current vertex
					  distFromSource[neighbour] = newDistance;
						q.offer(new IntegerPair(newDistance, neighbour));
				  }
				  }
		  }
		  }
	}

	  return distFromSource;
  }
  
  // reduce the problem size
//   construct the k+1 * k+1 adjacency matrix that only has the shortest path distance from vertex 0 and the shopping list items
  private void reduceProblem() {
	  adjMat = new int[K+1][K+1];
	  int[] res = dijkstra(0);
	  adjMat[0][0] = 0;
	  for (int i=0; i<shoppingList.length; i++)
		  adjMat[0][i+1] = res[shoppingList[i]];
				  
	  for (int i=0; i<shoppingList.length; i++) {
		  int currentVertex = shoppingList[i];
		  res = dijkstra(currentVertex);
		  adjMat[i+1][0] = res[0];
		
		  for (int j=0; j<shoppingList.length; j++)
			  adjMat[i+1][j+1] = res[shoppingList[j]];
	  }
  }
  
  private int tspSlow(int currentVertex, int bitmask) {
	  // if every vertex is visited, return the cost of moving from currentVertex to 0
	  if (bitmask == (1 << K+1) - 1)
		  return adjMat[currentVertex][0];
	  
if (memo[currentVertex][bitmask] != -1)
	return memo[currentVertex][bitmask];

memo[currentVertex][bitmask] = INF;
for (int neighbour=0; neighbour<adjMat.length; neighbour++) {
	// if this vertex is not visited yet, then go there
	if (neighbour != currentVertex && ((1 << neighbour) & bitmask) == 0) {
		int newCost = adjMat[currentVertex][neighbour] + tspSlow(neighbour, bitmask | (1 << neighbour));
		memo[currentVertex][bitmask] = Math.min(memo[currentVertex][bitmask], newCost);
	}
}

return memo[currentVertex][bitmask];
  }
  
  // returns the total weight of this tour using 2opt
  private int tsp2Opt() {
	  // each route is just an array of K+2 integers, the ith integer  is the vertex chosen
	  // start with choosing vertex 0,1,2,...K, 0
	  int[] route = new int[K+2];
	  route[0] = route[K+1] = 0; // must always start and end at 0
	  
	  for (int i=1; i<=K; i++)
		route[i] = i;
	
	  int bestCost = computeCost(route);
	  boolean isImprovementMade = true;
	  while (isImprovementMade) {
		  isImprovementMade = false;
		  	  
	  for (int i=1; i<=K && isImprovementMade == false; i++) {// the start and end of the route are fixed
		  for (int j=i+1; j<=K && isImprovementMade == false; j++) {
			  int[] newRoute = swapVerticesInRoute(route, i, j);
			  int newCost = computeCost(newRoute);
			  
			  if (newCost < bestCost) {
				  isImprovementMade = true;
				  bestCost = newCost;
				  route = newRoute;
			  }
				  
		  }
	    
	  }
	  }
  
	  return bestCost;
  }
  
  private int computeCost(int[] route) {
	  int cost = 0;
	  for (int i=0; i<=route.length-2; i++) {
		  int current = route[i], next = route[i+1];
		  cost += adjMat[current][next];
  }
	  
	  return cost;
  }
  
  // swaps 2 vertices i and j, returning the new resulting route
  // precond: i<j
  private int[] swapVerticesInRoute(int[] route, int i, int j) {
	  int[] newRoute = new int[route.length];
	  int currentIndex; // used when doing the reversal
	  
	  for (int v=0; v<i; v++)
		  newRoute[v] = route[v];
	  
	  currentIndex = i-1;
	  
	  for (int v=j; v>=i; v--)
		newRoute[++currentIndex] = route[v];  
	  
	  for (int v=j+1; v<route.length; v++)
		  newRoute[++currentIndex] = route[v];
	  
	  return newRoute;
  }
  
  void run() throws Exception {
    // do not alter this method to standardize the I/O speed (this is already very fast)
    IntegerScanner sc = new IntegerScanner(System.in);
    PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(System.out)));

    int TC = sc.nextInt(); // there will be several test cases
    while (TC-- > 0) {
      // read the information of the complete graph with N+1 vertices
      N = sc.nextInt(); K = sc.nextInt(); // K is the number of items to be bought

      shoppingList = new int[K];
      for (int i = 0; i < K; i++)
        shoppingList[i] = sc.nextInt();

      T = new int[N+1][N+1];
      for (int i = 0; i <= N; i++)
        for (int j = 0; j <= N; j++)
          T[i][j] = sc.nextInt();
      reduceProblem(); // initializes the adjacency matrix with only the relevant shortest path info
      pw.println(Query());
    }

    pw.close();
  }

  public static void main(String[] args) throws Exception {
    // do not alter this method
    Supermarket ps6 = new Supermarket();
    ps6.run();
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