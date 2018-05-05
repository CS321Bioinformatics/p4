package src;
//
//public class TreeObject {
//    //TODO a TreeObject with constructor to save in BTree struct
//
//	//Metadata
//	//long - dna substring - data - comparable by key
//	//int frequency
//
//	public long dnaString;
//	public int frequency;
//
//
//
//	public void incrementFrequency() {
//		this.frequency++;
//	}
//
//	public int compareTo(TreeObject obj){
//		if(this.dnaString > obj.dnaString){return 1;}
//		else if(this.dnaString < obj.dnaString){return -1;}
//		else return 0;
//	}
//
//	//Constructors
//	public TreeObject(long dnaString){
//		this.frequency = 0;
//		this.dnaString = dnaString;
//		//isLeaf = false;
//
//	}
//	public TreeObject(long dnaString, int frequency){
//		this.frequency = frequency;
//		this.dnaString = dnaString;
//		//isLeaf = false;
//	}
//}
public class TreeObject {
	//TODO a TreeObject with constructor to save in BTree struct

	//Metadata
	//long - dna substring - data - comparable by key
	//int frequency

	public long dnaString;
	public int frequency;



	public void incrementFrequency() {
		this.frequency++;
	}
	public int getFrequency() {
		return frequency;
	}

	public int compareTo(TreeObject obj){
		if(this.dnaString > obj.dnaString){return 1;}
		else if(this.dnaString < obj.dnaString){return -1;}
		else return 0;
	}

	public long getDnaString(){
		return dnaString;
	}
	//Constructors
	public TreeObject(long dnaString){
		this.frequency = 1;
		this.dnaString = dnaString;
		//isLeaf = false;

	}
	public TreeObject(long dnaString, int frequency){
		this.frequency = frequency;
		this.dnaString = dnaString;
		//isLeaf = false;
	}
}
