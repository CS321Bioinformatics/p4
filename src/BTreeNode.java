package src;


import java.util.ArrayList;

public class BTreeNode {
    //TODO A B-Tree node class
	
	//Metadata
		//int n number of objects
		//boolean leaf
		//int location (byte location of nodes in file (offset of node in file))
	private int numObjects;
	private boolean isLeaf;
	private int nodeLocation;
	private ArrayList<Long> objectList;
	private ArrayList<TreeObject> keyList;
	private int parent, leftChild, rightChild, node;
	
	//default (root)
	public BTreeNode(){
		int numObjects = 1;
		nodeLocation = 0;
		parent = (nodeLocation-1)/2;
		keyList = new ArrayList<TreeObject>();
		objectList = new ArrayList<Long>();


//		objectList.add(obj);
		//how to get/set this nodes location
		//new node location method in RAM class??

		
		
		
		
	}
	//if not root
	public BTreeNode(Long obj, BTreeNode parentNode){
		int numObjects = 1;
		objectList = new ArrayList<Long>();
		objectList.add(obj);
		
		//how to get/set this nodes location
		//new node location method in RAM class??
		nodeLocation = 0;
		parent = parentNode.getLocation();
		
		
		
		
		
	}
	public TreeObject getKey(int key){
		TreeObject obj = keyList.get(key);
		return obj;
	}
	public int getNode(){
		return node;
	}
	public int getLocation() {
		return nodeLocation;
	}
}
