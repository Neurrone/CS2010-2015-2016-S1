import java.util.*;
import java.io.*;
// year 2015 hash code: JESg5svjYpIsmHmIjabX (do NOT delete this line)

class Vertex {
	public Vertex parent, left, right;
	public String key;
	public int height, size;

Vertex(String v, int size, int height) {
	key = v; 
parent = left = right = null; 
this.height = height;
this.size = size;
}
}

class AVL {
private Vertex root;

public AVL() { 
	root = null; 
	}

// searches the AVL tree for the given key, returning it if found, null otherwise
public String find(String key) {
 Vertex res = find(root, key);
 return res == null ? null : res.key;
}

// searches for a key starting at the  given root
private Vertex find(Vertex root, String key) {
    if (root == null)  
    	return root;                                  // not found
else if (root.key.equals(key)) 
	return root;                                      // found
else if (key.compareTo(root.key) > 0)  
	return find(root.right, key);       // search to the right
else                 
	return find(root.left, key);         // search to the left
}

public void insert(String key) { 
	root = insert(root, key); 
	}

private Vertex insert(Vertex root, String key) {
	 if (root == null) 
		 return new Vertex(key, 1, 0);

	 if (key.compareTo(root.key) > 0) {
	   root.right = insert(root.right, key);
	   root.right.parent = root;
	 }
	 else {                                                 // search to the left
	   root.left = insert(root.left, key);
	   root.left.parent = root;
	 }

	 root.size = size(root.left) + size(root.right) + 1;	
	 root.height = Math.max(getHeight(root.left), getHeight(root.right)) + 1;
	 root = rebalance(root);
	 return root;                                         
	}

private int size(Vertex root) {
	if (root == null)
		return 0;
	
	return root.size;
}

public void innerTraversal() { 
 innerTraversal(root);
 System.out.println();
}

private void innerTraversal(Vertex root) {
	 if (root == null) 
		 return;
	 
	 innerTraversal(root.left);                               // recursively go to the left
	 System.out.printf(" %s %d %d", root.key, size(root), rank(root.key));                      // visit this BST node
	 innerTraversal(root.right);                             // recursively go to the right
	}

public String findMin() { 
	return findMin(root); 
	}

private String findMin(Vertex root) throws NoSuchElementException {
    if (root == null)      
    	throw new NoSuchElementException("AVL is empty, no minimum");
else if (root.left == null) 
	return root.key;                   
else                     
	return findMin(root.left);
}

public String findMax() { 
	return findMax(root); 
	}

private String findMax(Vertex root) {
    if (root == null)       
    	throw new NoSuchElementException("BST is empty, no maximum");
else if (root.right == null) 
	return root.key;
else                      
	return findMax(root.right);        // go to the right
}

public String predecessor(String key) { 
Vertex vPos = find(root, key);
return vPos == null ? null : predecessor(vPos);
}

	private String predecessor(Vertex root) {
		// if there's a left subtree, then predecessor must be the left subtree's max
	 if (root.left != null)                         
	   return findMax(root.left);  
	 else { // traverse up the ancestor chain till the current node is the right child of its parent
	   Vertex parent = root.parent;
	   Vertex cur = root;
	   
	   while (parent != null && cur == parent.left) { 
	     cur = parent;                                         // continue moving up
	     parent = cur.parent;
	   }
	   return parent == null ? null : parent.key;           // this is the successor of T
	 }
	}
	
public String successor(String key) { 
	 Vertex vPos = find(root, key);
	 return vPos == null ? null : successor(vPos);
	}

private String successor(Vertex root) {
	// if the root has a right subtree, then the successor is the minimum
	 if (root.right != null)                       
	   return findMin(root.right);
	 else { // traverse up the ancestor chain until we are on a node that is the left child of its parent
	   Vertex parent = root.parent;
	   Vertex cur = root;
	   while (parent != null && cur == parent.right) {
	     cur = parent;                                         // continue moving up
	     parent = cur.parent;
	   }
	   
	   return parent == null ? null : parent.key;           
	 }
	}

public String floor(String key) {
	Vertex res = floor(root, key);
	return res == null ? null : res.key;
}
 
private Vertex floor(Vertex root, String key) {
	if (root == null)
		return null;
	
	int compare = key.compareTo(root.key);
	
	if (compare == 0)
		return root;
	// if the key is < root.key, then floor must be in the left subtree
	else if (compare < 0)
		return floor(root.left, key);
		// if key > root.key, then floor could be on the right if there exists a key <= key
	// otherwise, the root must be the floor
	Vertex result = floor(root.right, key);
	if (result != null)
		return result;
	else
		return root;
}

// returns the smallest node with a key >= key
public String ceiling(String key) {
	Vertex res = ceiling(root, key);
	return res == null ? null : res.key;
}

private Vertex ceiling(Vertex root, String key) {
	if (root == null)
		return null;
	
	int compare = key.compareTo(root.key);
	
	if (compare == 0)
		return root;
	// if the key is > root.key, then ceiling must be in the right subtree
	else if (compare > 0)
		return ceiling(root.right, key);
		// if key < root.key, then ceiling could be on the left if there exists a key >= key
	// otherwise, the root must be the ceiling
	Vertex result = ceiling(root.left, key);
	if (result != null)
		return result;
	else
		return root;
}

public int getHeight() { 
	return getHeight(root); 
	}

private int getHeight(Vertex root) {
 if (root == null) 
	 return -1;
 else 
	 return root.height;
}

// returns count of keys < key (i.e 0-based rank)
public int rank(String key) {
	return rank(root, key);
}

private int rank(Vertex root, String key) {
	if (root == null)
		return 0;
	
	int compare = key.compareTo(root.key);
	
	if (compare == 0)
		return size(root.left);
	else if (compare > 0)
		return size(root.left) + 1 + rank(root.right, key);
	else
		return rank(root.left, key); // we haven't found something < key yet
}

// returns the number of elements in the interval [start, end)
public int getIntervalCount(String start, String end) {
	if (root == null) return 0;
	// the endpoints we want are from ceiling(start) and the greatest element < end
	String startElem = ceiling(start);
	String endElem = floor(end);
	
	int startRank, endRank=0;
	 
	if (startElem != null) {
		startRank = rank(startElem);
	}
	else
		return 0;
	
if (endElem == null)
	return 0;

	if (endElem.equals(end)) 
		endElem = predecessor(endElem); 
		
	if (endElem != null) {
		endRank = rank(endElem);
	}
	else { // this happens if start == end
		return 0;
}
	
	return endRank - startRank + 1;
}

private int getBalance(Vertex root) {
	return getHeight(root.left) - getHeight(root.right);
}

// rebalances the tree at the root, does not traverse up ancestor chain 
private Vertex rebalance(Vertex root) {
	int balance = getBalance(root);	
	
	if (balance == 2) { // left heavy 
		if (getBalance(root.left) == -1) // right heavy
			root = rotateLeftRight(root);
		else
			root = rotateRight(root);
	}
	else if (balance == -2) { // right heavy
		if (getBalance(root.right) == 1) // left heavy
			root = rotateRightLeft(root);
		else
			root = rotateLeft(root);
	}
	return root;
}

private Vertex rotateLeft(Vertex root) {
	Vertex left = root.left, right = root.right, newRoot = root.right;
	
	// connect the new node to the rest of tree
	newRoot.parent = root.parent;
	if (newRoot.parent != null) {
		if (newRoot.parent.left == root) {
			newRoot.parent.left = newRoot;
		} 
		else if (newRoot.parent.right == root){
			newRoot.parent.right = newRoot;
		}
	}
	
	root.right = right.left;
	if (root.right != null) 
		root.right.parent = root;
	
	newRoot.left = root; // rotate down
	root.parent = newRoot;
	root.size = size(root.left) + size(root.right) + 1;
	root.height = Math.max(getHeight(root.left), getHeight(root.right)) + 1;
	newRoot.size = size(newRoot.left) + size(newRoot.right) + 1;
	newRoot.height = Math.max(getHeight(newRoot.left), getHeight(newRoot.right)) + 1;
	return newRoot;
}

private Vertex rotateRight(Vertex root) {
	Vertex left = root.left, right = root.right, newRoot = root.left;
	// connect the new node to the rest of tree
	newRoot.parent = root.parent;
	if (newRoot.parent != null) {
		if (newRoot.parent.left == root) {
			newRoot.parent.left = newRoot;
		} 
		else if (newRoot.parent.right == root){
			newRoot.parent.right = newRoot;
		}
	}
	
	root.left= left.right;
	if (root.left != null) 
		root.left.parent = root;
	
	newRoot.right = root; // rotate down
	root.parent = newRoot;
	root.size = size(root.left) + size(root.right) + 1;
	newRoot.size = size(newRoot.left) + size(newRoot.right) + 1;
	return newRoot;
}

private Vertex rotateLeftRight(Vertex root) {
	root.left = rotateLeft(root.left);
	return rotateRight(root);
}

private Vertex rotateRightLeft(Vertex root) {
	root.right = rotateRight(root.right);
	return rotateLeft(root);
}

// returns whether the tree was modified as result of delete
	public boolean delete(String key) { 
		int oldCount = root.size;
 root = delete(root, key);
 int newCount = (root != null) ? root.size : 0;
 return oldCount != newCount;
 }

private Vertex delete(Vertex root, String key) {
 if (root == null)  
	 return null;              // cannot find the item to be deleted

 int compare = key.compareTo(root.key);
 
 if (compare > 0) 
	   root.right = delete(root.right, key);
	 else if (compare < 0)
	   root.left = delete(root.left, key);

	 else
// if we get here, then this is the node to be deleted
   if (root.left == null && root.right == null) {                  // leaf
     root = null;                                      // simply erase this node
     return null;
   }
   else if (root.left == null && root.right != null) {   // only one child at right
     root.right.parent = root.parent;
     root = root.right;
   }
   else if (root.left != null && root.right == null) {    // only one child at left
     root.left.parent = root.parent;
     root = root.left;
   }
   else {                                 // has two children, find successor
     String successor = successor(key);
     root.key = successor;         // replace this key with the successor's key
     root.right = delete(root.right, successor);      // delete the old successorV
   }

 root.size = size(root.left) + size(root.right) + 1;
	root.height = Math.max(getHeight(root.left), getHeight(root.right)) + 1;
	
 root = rebalance(root);
 return root;                                          // return the updated BST
}

}

class BabyNames {
	private static final int NO_PREFERENCE = 0;
	private static final int MALE = 1;
	private static final int FEMALE = 2;
 AVL maleNames, femaleNames;
 
  public BabyNames() {
  maleNames = new AVL();     
  femaleNames = new AVL();
}

  void AddSuggestion(String babyName, int genderSuitability) {
	  switch(genderSuitability) {
	  case MALE:
		  maleNames.insert(babyName);
		  break;
	  case FEMALE:
		  femaleNames.insert(babyName);
	  }
  }
  
  void RemoveSuggestion(String babyName) {
   if (maleNames.delete(babyName) == false) {
    	femaleNames.delete(babyName);
    }
  }

  int Query(String start, String end, int genderPreference) {
    // You have to answer how many baby name starts
    // with prefix that is inside query interval [START..END)
   
	  int count = 0;

    if (genderPreference == NO_PREFERENCE|| genderPreference == MALE) { 
    count += maleNames.getIntervalCount(start, end);
    }
    
    if (genderPreference == NO_PREFERENCE || genderPreference == FEMALE) {
    	count += femaleNames.getIntervalCount(start, end);
   }

    return count;
  }
  
  void run() throws Exception {
    // do not alter this method to avoid unnecessary errors with the automated judging
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    PrintWriter pr = new PrintWriter(new BufferedWriter(new OutputStreamWriter(System.out)));
    while (true) {
      StringTokenizer st = new StringTokenizer(br.readLine());
      int command = Integer.parseInt(st.nextToken());
      if (command == 0) // end of input
        break;
      else if (command == 1) // AddSuggestion
        AddSuggestion(st.nextToken(), Integer.parseInt(st.nextToken()));
      else if (command == 2) // RemoveSuggestion
        RemoveSuggestion(st.nextToken());
      else // if (command == 3) // Query
        pr.println(Query(st.nextToken(), // START
                         st.nextToken(), // END
                         Integer.parseInt(st.nextToken()))); // GENDER
    }
    pr.close();
  }

  public static void main(String[] args) throws Exception {
    // do not alter this method to avoid unnecessary errors with the automated judging
    BabyNames ps2 = new BabyNames();
    ps2.run();
  }
}