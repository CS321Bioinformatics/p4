package src;


import java.util.ArrayList;

public class BTreeNode {
    //TODO A B-Tree node class
	
	//Metadata
		//boolean leaf
		//int n number of objects
		//int location (byte location of nodes in file (offset of node in file))
		//Size ----> 9 bytes

	//without location --> 5 bytes

	public boolean isLeaf;
	public int numKeys;
	public int offset;

	//other data
		//parent pointer
		//objects -- keyList
		//child pointers -- children1 int list
		//Size -----> 4(2t+1) + 12(2t-1) = 8t + 4 + 24t - 12 = 32t - 8

	public int parent;
	public ArrayList<TreeObject> keyList;
	public ArrayList<Integer> children1;


	/*unused items*/
	//public int node;
	//public int numKeys;
	//public ArrayList<Long> children;
	//public int offset;


	//default (root)
	public BTreeNode(){
		//int numObjects = 1;


		//parent = 0;

		offset = 0;
		parent = -1;
		isLeaf = false;
		keyList = new ArrayList<TreeObject>();
		children1 = new ArrayList<Integer>();


//		children = new ArrayList<Long>();
//		leafList = new ArrayList<Long?>();
//		objectList.add(obj);
		//how to get/set this nodes location
		//new node location method in RAM class??

		
		
		
		
	}
	//if not root
	public BTreeNode(Long obj, BTreeNode parentNode){
		int numObjects = 1;
//		children = new ArrayList<Long>();

//		children.add(obj);

		children1 = new ArrayList<Integer>();
		children1.add(obj.intValue());

		//how to get/set this nodes location
		//new node location method in RAM class??
		offset = 0;
		parent = parentNode.getLocation();
		
		
		
		
		
	}
	public TreeObject getKey(int key){
		TreeObject obj = keyList.get(key);
		return obj;
	}
//	public Long getKey(int key){
//		return children.get(key);
//	}
//	public BTreeNode getNode(){
//		return node;
//	}
	public int getLocation() {
		return offset;
	}

	public int numKeys() {
		return keyList.size();
	}

	public void setNumKeys(int numKeys) {
		this.numKeys = numKeys;
	}

	public void insertKey(TreeObject obj, int i) {
		keyList.add(i,obj);

	}
	public int getChild(int i){
		return children1.get(i); //convert to regular int from Primitive?
	}
	public void insertChild(int i){
		children1.add(i);
	}
	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

//	public ArrayList<Long> getKey() {
//		return children;
//	}


	public int getParent() {
		return parent;
	}

	public void setParent(int parent) {
		this.parent = parent;
	}

	public ArrayList<Integer> getKey(){return children1;}

	public String toString(){
		String nodeString = new String();
		nodeString += "file offset: " + this.offset;
		nodeString += "\nkeys: ";
		for (int i = 0; i < keyList.size(); i++){
			nodeString += (keyList.get(i).dnaString + " ");
		}
		nodeString += "\nchildren: ";
		for (int i = 0; i < children1.size(); i++){
			nodeString += (children1.get(i) + " ");
		}

		nodeString += "\nParent pointer: " + this.parent;
		return nodeString;
	}
}
