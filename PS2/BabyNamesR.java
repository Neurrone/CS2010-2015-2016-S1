import java.util.*;
import java.io.*;

// year 2015 hash code: JESg5svjYpIsmHmIjabX (do NOT delete this line)

// from CPP3
class SuffixArray {
	StringBuilder input; // original concated names
	int n;                                             // the length of input string

	int[] RA, tempRA;             // rank array and temporary rank array
	int[] SA, tempSA;         // suffix array and temporary suffix array
	int[] c;                                         // for counting/radix sort

	// char P[];     // the pattern string (for string matching)
	// int m;             // the length of pattern string

	
	SuffixArray(StringBuilder str)  {
		input = str;
		n = str.length();
		c = new int[Math.max(300, n)]; // otherwise breaks on small inputs
		RA = new int[n+100];
		tempRA = new int[n+100];
		SA = new int[n];
		tempSA = new int[n];
		constructSA();
	}

	void constructSA() {
		
		int i, k, r;
		for (i = 0; i < n; i++) 
			RA[i] = input.charAt(i);                      // initial rankings

		for (i = 0; i < n; i++) 
			SA[i] = i;          // initial SA: {0, 1, 2, ..., n-1}

		for (k = 1; k < n; k <<= 1) {            // repeat sorting process log n times
			countingSort(k);       // actually radix sort: sort based on the second item
			countingSort(0);               // then (stable) sort based on the first item
			tempRA[SA[0]] = r = 0;                  // re-ranking; start from rank r = 0
			for (i = 1; i < n; i++)                         // compare adjacent suffices
				tempRA[SA[i]] =      // if same pair => same rank r; otherwise, increase r
				(RA[SA[i]] == RA[SA[i-1]] && RA[SA[i]+k] == RA[SA[i-1]+k]) ? r : ++r;

			for (i = 0; i < n; i++)                          // update the rank array RA
				RA[i] = tempRA[i];
		}
		}

	void countingSort(int k) {
		int i, sum, maxi = Math.max(300, n);   // up to 255 ASCII chars or length of n
		for (i = 0; i < c.length; i++) c[i] = 0;                // clear frequency table
		for (i = 0; i < n; i++)                    // count the frequency of each rank
			c[i + k < n ? RA[i + k] : 0]++;
		for (i = sum = 0; i < maxi; i++) {
			int t = c[i]; 
			c[i] = sum; 
			sum += t;
		}
		for (i = 0; i < n; i++)               // shuffle the suffix array if necessary
			tempSA[c[SA[i] + k < n ? RA[SA[i] + k] : 0]++] = SA[i];
		for (i = 0; i < n; i++)                          // update the suffix array SA
			SA[i] = tempSA[i];
	}

	// returns the number of matches of pattern in suffix array
	int find(String pat) {
		int low = findLowerBound(pat);
		if (low == -1)
			return 0; // no lower bound = not found
		
		int high = findUpperBound(pat);
				// the indices in the interval [low, high] count all occurrences of pat for a name
		// but the qn wants each name to only be counted once, so must filter
// check if the first chars of the suffixes starting at sa[low] to sa[high]    are in the same name
		int count=1; // at least 1 as low is found, check for others
  for (int i=low+1; i<=high && count < 100; i++) {
	  boolean isDuplicateRes = false;
	  
	  for (int j=low; j<i; j++)
		  if (isCharsPartOfSameName(SA[i], SA[j]) )
			  isDuplicateRes = true;
	
		  if (isDuplicateRes == false)
		  count++;
  }

			return count;
	}

	int findLowerBound(String pat) {
		int lo = 0, hi = n-1, mid;                 // valid matching = [0 .. n-1]
		
		while (lo < hi) {
			mid = (lo + hi) / 2;
			int res = strncmp(input, SA[mid], pat, 0, pat.length());       // try to find P in suffix 'mid'
			if (res >= 0) 
				hi = mid; // remove upper half
			else
				lo = mid+1;
		}

	    if (strncmp(input,SA[lo], pat,0, pat.length()) != 0) 
	    	return -1; // not found
	   
	    return lo;
	}
	
	// precond: lower bound should exist
	int findUpperBound(String pat) {
		int lo = 0, hi = n - 1, mid = lo;
		
	    while (lo < hi) {
	    	mid = (lo + hi)/2;
	        int res = strncmp(input, SA[mid], pat,0, pat.length());
	        if (res > 0) 
	        	hi = mid;                                   // prune upper half
	        else         
	        	lo = mid + 1;                 // prune lower half including mid
	    }
	    
	    if (strncmp(input, SA[hi], pat,0, pat.length()) != 0) 
	    	hi--;                      // special case
	    
	    return hi;
	}
	
	// checks if the characters at 2 positions of the original string belong to the same name
	boolean isCharsPartOfSameName(int a, int b) {
		// its easier if we ensure that we traverse from left to right
		int start = Math.min(a, b), end = Math.max(a, b);
	
		for (int i=start+1; i<=end; i++)
			if (input.charAt(i) == '$')
				return false; 
		
		return true;
	}
	
	// this was modified slightly from CPP3 to avoid the O(n) copies to the char[] arrays
	int strncmp(StringBuilder a, int i, String b, int j, int n){
		int k;
		for (k=0; i+k < a.length() && j+k < b.length(); k++){
			int res = a.charAt(i+k) - b.charAt(j+k);
			if (res != 0) 
				return res; //  not equal
		}
 
			// if there are still characters left in string b that weren't checked because we've reached the end of a, 
			// then that means that we want the binary search to proceed to the right
		// i.e trying to find er in the string e, er is greater than e 
			if (j+k < b.length())
				return -1; // actual value doesn't matter for our purposes, as long as < 0
		
		return 0;
	}

}

class BabyNamesR {
	StringBuilder concatNames; // all names concated into massive string
	SuffixArray names;

	public BabyNamesR() {
		concatNames = new StringBuilder(110*10000); // to avoid costly reallocation
	}

	void AddSuggestion(String babyName) {
concatNames.append(babyName);
concatNames.append("$"); // separates words
	}

	void preprocess() {
		names = new SuffixArray(concatNames);
	}
	
	int Query(String substr) { 
				if (substr.length() > 110)
			return 0; // no names match if the substr is too long

		return names.find(substr);
	}

	void run() throws Exception {
		// do not alter this method to avoid unnecessary errors with the automated judging
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		PrintWriter pr = new PrintWriter(new BufferedWriter(new OutputStreamWriter(System.out)));
		int N = Integer.parseInt(br.readLine());
		while (N-- > 0)
			AddSuggestion(br.readLine());
		
		preprocess(); // build the suffix array
		
		int Q = Integer.parseInt(br.readLine());
		while (Q-- > 0)
			pr.println(Query(br.readLine())); // SUBSTR
		
		pr.close();
	}

	public static void main(String[] args) throws Exception {
		// do not alter this method
		BabyNamesR ps2R = new BabyNamesR();
		ps2R.run();
	}
}