package src;
//
//
//import java.util.ArrayList;
//
//public class BTreeNode {
//    //TODO A B-Tree node class
//
//	//Metadata
//		//boolean leaf
//		//int n number of objects
//		//int location (byte location of nodes in file (offset of node in file))
//		//Size ----> 9 bytes
//
//	public boolean isLeaf;
//	public int numKeys;
//	public int offset;
//
//	//other data
//		//parent pointer
//		//objects -- keyList
//		//child pointers -- children1 int list
//		//Size -----> 4(2t+1) + 12(2t-1) = 8t + 4 + 24t - 12 = 32t - 8
//
//	public int parent;
//	public ArrayList<TreeObject> keyList;
//	public ArrayList<Integer> children1;
//
//	//default (root)
//	public BTreeNode(){
//
//		offset = 0;
//		parent = -1;
//		isLeaf = false;
//		keyList = new ArrayList<TreeObject>();
//		children1 = new ArrayList<Integer>();
//
//	}
//	//if not root
//	public BTreeNode(Long obj, BTreeNode parentNode){
//		children1 = new ArrayList<Integer>();
//		children1.add(obj.intValue());
//		offset = 0;
//		parent = parentNode.getLocation();
//
//
//
//
//
//	}
//	public TreeObject getKey(int key){
//		TreeObject obj = keyList.get(key);
//		return obj;
//	}
//	public int getLocation() {
//		return offset;
//	}
//
//	public int numKeys() {
//		return keyList.size();
//	}
//
//	public void setNumKeys(int numKeys) {
//		this.numKeys = numKeys;
//	}
//
//	public void insertKey(TreeObject obj, int i) {
//		keyList.add(i,obj);
//
//	}
//
//	public void insertChild(int i){
//		children1.add(i);
//	}
//	public int getOffset() {
//		return offset;
//	}
//
//	public void setOffset(int offset) {
//		this.offset = offset;
//	}
//
//
//	public int getParent() {
//		return parent;
//	}
//
//	public void setParent(int parent) {
//		this.parent = parent;
//	}
//
//
//	public String toString(){
//		String nodeString = new String();
//		nodeString += "file offset: " + this.offset;
//		nodeString += "\nkeys: ";
//		for (int i = 0; i < keyList.size(); i++){
//			nodeString += (keyList.get(i).dnaString + " ");
//		}
//		nodeString += "\nchildren: ";
//		for (int i = 0; i < children1.size(); i++){
//			nodeString += (children1.get(i) + " ");
//		}
//
//		nodeString += "\nParent pointer: " + this.parent;
//		return nodeString;
//	}
//}

import java.util.ArrayList;

public class BTreeNode {
	//TODO A B-Tree node class

	//Metadata
	//boolean leaf
	//int n number of objects
	//int location (byte location of nodes in file (offset of node in file))
	//Size ----> 9 bytes

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

	//default (root)
	public BTreeNode(){

		offset = 0;
		parent = -1;
		isLeaf = false;
		keyList = new ArrayList<TreeObject>();
		children1 = new ArrayList<Integer>();

	}
	//if not root
	public BTreeNode(Long obj, BTreeNode parentNode){
		children1 = new ArrayList<Integer>();
		children1.add(obj.intValue());
		offset = 0;
		parent = parentNode.getLocation();





	}
	public TreeObject getKey(int key){
		TreeObject obj = keyList.get(key);
		return obj;
	}
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

	public void insertChild(int i){
		children1.add(i);
	}
	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}


	public int getParent() {
		return parent;
	}

	public void setParent(int parent) {
		this.parent = parent;
	}


	public int getChild(int i){
		return children1.get(i).intValue();
	}

	public String toString(){
		String nodeString = new String();
		nodeString += "file offset: " + this.offset;
		nodeString += "  keys: ";
		for (int i = 0; i < keyList.size(); i++){
			nodeString += (keyList.get(i).dnaString + " ");
		}
		nodeString += "  children: ";
		for (int i = 0; i < children1.size(); i++){
			nodeString += (children1.get(i) + " ");
		}

		nodeString += "  Parent pointer: " + this.parent;
		return nodeString;
	}
}
