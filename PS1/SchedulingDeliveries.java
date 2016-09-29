import java.util.*;
import java.io.*;
// year 2015 hash code: JESg5svjYpIsmHmIjabX (do NOT delete this line)

class Woman implements Comparable<Woman> {
	private String name;
	private int dilation;
	private int time; // arrival time used to break ties

	Woman(String name, int time, int dilation) {
		this.name = name;
		this.dilation = dilation;
		this.time = time;
	}

	public int getArrivalTime() {
		return time;
	}

	public String getName() {
		return name;
	}

	public int getDilation() {
		return dilation;
	}

	public void increaseDilation(int increase) {
		dilation += increase;
	}

	// a woman is greater than another if its priority is higher.
	public int compareTo(Woman other) {
		if (name.equals(other.getName()))
			return 0; // women name are unique

		if (dilation > other.getDilation() || (dilation == other.getDilation() && time < other.getArrivalTime())) {
			return 1;
		} else if (dilation == other.getDilation() && time == other.getArrivalTime()) {
			return 0;
		}

		return -1;
	}
}

// A max binary heap based off lecture implementation with some additional methods for solving this PS
// Women added should only be modified by using this updateDilation method so that
// the heap property is preserved
class Patients {
	private ArrayList<Woman> arr;
	private HashMap<String, Integer> indices; // for fast lookup of woman's
												// index
	private int heapSize;

	Patients() {
		this(10);
	}

	Patients(int capacity) {
		arr = new ArrayList<Woman>(capacity + 1); // need slot for unused
													// position 0
		arr.add(null); // dummy value, we use 1-based heap
		heapSize = 0;
		indices = new HashMap<String, Integer>(capacity);
	}

	// some helper funcs for index calculation
	private int parentIndex(int child) {
		return child >> 1; // shortcut for i/2, round down
	} 

	private int leftChildIndex(int parent) {
		return parent << 1; // shortcut for 2*i
	} 

	private int rightChildIndex(int parent) {
		return (parent << 1) + 1; // shortcut for 2*i + 1
	} 

	// returns the left child element of the root, null if it doesn't exist
	private Woman getLeftChild(int parent) {
		int left = leftChildIndex(parent);
		if (left > heapSize)
			return null; // does not exist

		return arr.get(left);
	}

	// returns the right child element of the root, null if it doesn't exist
	private Woman getRightChild(int parent) {
		int right = rightChildIndex(parent);
		if (right > heapSize)
			return null; // does not exist

		return arr.get(right);
	}

	// returns the parent element of the child, null if it doesn't exist
	private Woman getParent(int child) {
		if (child <= 1)
			return null; // roots have no parents

		int parent = parentIndex(child);

		return arr.get(parent);
	}

	// swaps the elements at index i and j, also updates the indices mapping
	private void swap(int i, int j) {
		indices.put(arr.get(i).getName(), j);
		indices.put(arr.get(j).getName(), i);

		Woman temp = arr.get(i);
		arr.set(i, arr.get(j));
		arr.set(j, temp);
	}

	private void shiftUp(int i) {
		// continue shifting if we're not at root, and if parent < current
		Woman parent = getParent(i);
		while (parent != null && parent.compareTo(arr.get(i)) == -1) {
			swap(i, parentIndex(i));
			i = parentIndex(i);
			parent = getParent(i);
		}
	}

	public void add(Woman w) {
		heapSize++;
		if (heapSize >= arr.size())
			arr.add(w);
		else
			arr.set(heapSize, w);

		indices.put(w.getName(), heapSize);
		shiftUp(heapSize);
	}

	private void shiftDown(int i) {
		while (i <= heapSize) {
			// find max(i, leftChild, rightChild) then swap
			Woman maxElem = arr.get(i);
			int maxIndex = i;
			Woman left = getLeftChild(i);

			if (left != null && maxElem.compareTo(left) == -1) {
				maxElem = left;
				maxIndex = leftChildIndex(i);
			}

			Woman right = getRightChild(i);
			if (right != null && maxElem.compareTo(right) == -1) {
				maxElem = right;
				maxIndex = rightChildIndex(i);
			}

			if (maxIndex != i) {
				swap(i, maxIndex);
				i = maxIndex;
			} else
				break;
		}
	}

	void increaseDilation(String name, int increase) {
		  int index = indices.get(name); 
		  arr.get(index).increaseDilation(increase); 
shiftUp(index);
	  }

	public Woman peek() {
		if (isEmpty())
			return null;

		return arr.get(1);
	}

	public Woman poll() {
		Woman max = peek();
		if (max != null) {
			indices.remove(arr.get(1).getName());
			arr.set(1, arr.get(heapSize));
			indices.put(arr.get(1).getName(), 1);
			heapSize--;
			shiftDown(1);
		}

		return max;
	}

	// births happen instantly. Once done, the woman will be removed
	public void giveBirth(String womanName) {
		int index = indices.get(womanName);
		arr.get(index).increaseDilation(100); // ensure this woman has the
												// highest priority
		shiftUp(index);
		poll();
	}

	int size() {
		return heapSize;
	}

	boolean isEmpty() {
		return heapSize == 0;
	}

}

class SchedulingDeliveries {
	private Patients patients;
	private int time; // this is used to assign arrival order to women

	public SchedulingDeliveries() {
		time = 0;
		patients = new Patients(200000);
	}

	void ArriveAtHospital(String womanName, int dilation) {
		patients.add(new Woman(womanName, ++time, dilation));
	}

	void UpdateDilation(String womanName, int increase) {
		// You have to update the dilation of womanName to
		// dilation += increaseDilation
		// and modify your chosen data structure (if needed)

		patients.increaseDilation(womanName, increase);
	}

	void GiveBirth(String womanName) {
		// This womanName gives birth 'instantly'
		// remove her from your chosen data structure
		patients.giveBirth(womanName);
	}

	String Query() {
		String ans = "The delivery suite is empty";

		// You have to report the name of the woman that the doctor
		// has to give the most attention to. If there is no more woman to
		// be taken care of, return a String "The delivery suite is empty"

		if (patients.peek() == null)
			return ans;
		else
			return patients.peek().getName();
	}

	void run() throws Exception {
		// do not alter this method

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		PrintWriter pr = new PrintWriter(new BufferedWriter(new OutputStreamWriter(System.out)));
		int numCMD = Integer.parseInt(br.readLine()); // note that numCMD is >=
														// N
		while (numCMD-- > 0) {
			StringTokenizer st = new StringTokenizer(br.readLine());
			int command = Integer.parseInt(st.nextToken());
			switch (command) {
			case 0:
				ArriveAtHospital(st.nextToken(), Integer.parseInt(st.nextToken()));
				break;
			case 1:
				UpdateDilation(st.nextToken(), Integer.parseInt(st.nextToken()));
				break;
			case 2:
				GiveBirth(st.nextToken());
				break;
			case 3:
				pr.println(Query());
				break;
			}
		}
		pr.close();
	}

	public static void main(String[] args) throws Exception {
		// do not alter this method
		SchedulingDeliveries ps1 = new SchedulingDeliveries();
		ps1.run();
	}
}