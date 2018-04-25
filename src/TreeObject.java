package src;

public class TreeObject {
    //TODO a TreeObject with constructor to save in BTree struct
	
	//Metadata
	//long - dna substring - data - comparable by key
	//int frequency


	//Linked List or ArrayList of tree objects
	public long dnaString;
	public int frequency;
	public boolean isLeaf;

	public long getDnaString() {
		return dnaString;
	}

	public int getFrequency() {
		return frequency;
	}

	public void incrementFrequency() {
		this.frequency++;
	}

	public boolean compareTo(TreeObject obj){
		if(this.dnaString > obj.dnaString){return false;}
		else if(this.dnaString < obj.dnaString){return true;}
		else return true;
	}

	//Constructors
	public TreeObject(long dnaString){
		this.frequency = 0;
		this.dnaString = dnaString;
		isLeaf = false;

	}
	public TreeObject(long dnaString, int frequency){
		this.frequency = frequency;
		this.dnaString = dnaString;
		isLeaf = false;
	}
}
