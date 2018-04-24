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
	public int parent, leftChild, rightChild, node;
	public int numKeys;



	public int offset;


	//default (root)
	public BTreeNode(){
		int numObjects = 1;
		nodeLocation = 0;
		parent = (nodeLocation-1)/2;
		keyList = new ArrayList<TreeObject>();
		children = new ArrayList<Long>();


//		objectList.add(obj);
		//how to get/set this nodes location
		//new node location method in RAM class??

		
		
		
		
	}
	//if not root
	public BTreeNode(Long obj, BTreeNode parentNode){
		int numObjects = 1;
		children = new ArrayList<Long>();
		children.add(obj);
		
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
	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}
}
