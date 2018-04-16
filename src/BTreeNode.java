package src;

public class BTreeNode {
    //TODO A B-Tree node class
	
	//Metadata
		//int n number of objects
		//boolean leaf
		//int location (byte location of nodes in file (offset of node in file))
	private int numObjects;
	private boolean isLeaf;
	private int nodeLocation;
	private ArrayList<TreeObject> objectList; //maybe linked list so shifting not needed when splitting?
	private int parent, leftChild, rightChild;
	
	//default (root)
	public BTreeNode(TreeObject obj){
		int numObjects = 1;	
		objectList = new ArrayList<TreeObject>();
		objectList.add(obj);
		
		//how to get/set this nodes location
		//new node location method in RAM class??
		nodeLocation = 0;
		
		parent = null;
		
		
		
		
	}
	//if not root
	public BTreeNode(TreeObject obj, BTreeNode parentNode){
		int numObjects = 1;	
		objectList = new ArrayList<TreeObject>();
		objectList.add(obj);
		
		//how to get/set this nodes location
		//new node location method in RAM class??
		nodeLocation = 0;
		parent = parentNode.getLocation();
		
		
		
		
		
	}
	
	public int getLocation() {
		return nodeLocation;
	}
}
