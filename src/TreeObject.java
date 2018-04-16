package src;

public class TreeObject {
    //TODO a TreeObject with constructor to save in BTree struct
	
	//Metadata
	//long - dna substring - data - comparable by key
	//int frequency
	
	//
	
	//Linked List or ArrayList of tree objects
	private long dnaString;
	private int frequency;
	
	
	public TreeObject(long dna){
		frequency = 0;
		dnaString = dna;
	}
}
