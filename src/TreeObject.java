package src;

public class TreeObject {
    //TODO a TreeObject with constructor to save in BTree struct
	
	//Metadata
	//long - dna substring - data - comparable by key
	//int frequency


	//Linked List or ArrayList of tree objects
	private long dnaString;
	private int frequency;


	public long getDnaString() {
		return dnaString;
	}

	public int getFrequency() {
		return frequency;
	}

	public void incrementFrequency() {
		this.frequency++;
	}

	public int compareTo(TreeObject obj){
		if(this.dnaString > obj.dnaString){return 0;}
		else if(this.dnaString < obj.dnaString){return -1;}
		else return 1;
	}

	//Constructors
	public TreeObject(long dnaString){
		this.frequency = 0;
		this.dnaString = dnaString;
	}
	public TreeObject(long dnaString, int frequency){
		this.frequency = frequency;
		this.dnaString = dnaString;
	}
}
