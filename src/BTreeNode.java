package src;


import java.util.ArrayList;

public class BTreeNode {
    //TODO A B-Tree node class
	
	//Metadata
		//int n number of objects
		//boolean leaf
		//int location (byte location of nodes in file (offset of node in file))
	public int numObjects;
	public boolean isLeaf;
	public int nodeLocation;
	public ArrayList<Long> children;
	public ArrayList<TreeObject> keyList;
	public ArrayList<Integer> children1;
	public int parent, leftChild, rightChild, node;
	public int numKeys;



	public int offset;


	//default (root)
	public BTreeNode(){
		int numObjects = 1;
		nodeLocation = 0;
		parent = (nodeLocation-1)/2;
		keyList = new ArrayList<TreeObject>();
//		children = new ArrayList<Long>();
		children1 = new ArrayList<Integer>();
		//leafList = new ArrayList<Long?>();


//		objectList.add(obj);
		//how to get/set this nodes location
		//new node location method in RAM class??

		
		
		
		
	}
	//if not root
	public BTreeNode(Long obj, BTreeNode parentNode){
		int numObjects = 1;
		children = new ArrayList<Long>();
		children1 = new ArrayList<Integer>();
//		children.add(obj);
		children1.add(obj.intValue());

		//how to get/set this nodes location
		//new node location method in RAM class??
		nodeLocation = 0;
		parent = parentNode.getLocation();
		
		
		
		
		
	}
	public TreeObject getKey(int key){
		TreeObject obj = keyList.get(key);
		return obj;
	}
//	public Long getKey(int key){
//		return children.get(key);
//	}
	public int getNode(){
		return node;
	}
	public int getLocation() {
		return nodeLocation;
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
}
