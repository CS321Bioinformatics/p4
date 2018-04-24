package src;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class BTree {
    //TODO data structure to store TreeObjects
	
	//Metadata
	//degree
	//key
	//location of root
	public RandomAccessFile raf;
	private int degree;
	private RAM ram;
	public BTreeNode root;
	private ArrayList<TreeObject> btreeList = new ArrayList<TreeObject>();
	private File file;
    long offset;

/*
    BST-Insert(T,z) is to insert a node z to a binary search tree T,
    where key[z] = v, left[z] = right[z] = p[z] = nil initially.

    BST-Insert always inserts a new node z as a leaf node.
    1.  y<--nil
    2.  x <-- root(T)       //yistheparentofx
    3.  while x != nil      // x keep track of a path
    4.      do y <-- x
    5.          if key[z] <= key[x]
    6.              then x <-- left[x]
    7.              else x <-- right[x]
    8.  p[z] <-- y
    9.  if y = null
    10.     then root[T] <-- z
    11.     else if key[z] <= key[y]
    12.         then left[y] <-- z
    13.         else right[y] <-- z
*/


//    int parent = (btreeList.indexOf(node)-1)/2;


    //TODO Page 492
    public void BTreeCreate(BTree T, String fileName){
        BTreeNode x = AllocateNode();
        x.isLeaf = true;
        x.setNumKeys(0);
        try {
            file = new File(fileName);
            raf = new RandomAccessFile(file, "rw");
        }
        catch (Exception e){
            System.err.println("Error creating file");
        }
//      TODO  DiskWrite(x);
        T.root = x;
    }

    private void DiskWrite(BTreeNode node, int offset) {
        try {

            raf.seek(offset);
            raf.writeLong(offset);
            for(int i =0; i < root.children.size();i++){
                raf.writeLong(root.children.get(i));
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    //TODO Page 495
	public void BTreeInsert(BTree T, long key){
//		btreeList.set((btreeList.indexOf(node)-1)/2,null);
//		BTreeNode insertRoot = root;
		root = T.root;
		if(root.numKeys() == 2*degree-1){
		    BTreeNode newNode = AllocateNode();
		    T.root = newNode;
		    newNode.isLeaf = false;
            newNode.setNumKeys(0);
            newNode.keyList.set(1,root.getKey(1)); //TODO from book: s.c_1 = r
            BTreeSplitChild(newNode,1);
            BTreeInsertNonFull(newNode,key);
//          TreeObject obj = new TreeObject(key);
//
//		    int n = insertRoot.getNode();
//
//            while (n>0 && obj.compareTo(insertRoot.getKey(n-1))<0){
//                n--;
//            }
//            if(obj.compareTo(insertRoot.getKey(n-1)) == 1){
//                insertRoot.getKey(n-1).incrementFrequency();
//            }
//        else{
//            BTreeNode node = new BTreeNode();
//            insert node into list data structure in TreeNodeClass
//        }
        }
        BTreeInsertNonFull(root,key);

	}
    //TODO Page 494
    private void BTreeSplitChild(BTreeNode newNode, int i) {

    }
    //TODO Page 496
    private void BTreeInsertNonFull(BTreeNode newNode, long key) {
        int i = newNode.numKeys();
        TreeObject obj = new TreeObject(key);
        if(newNode.isLeaf){
            //handle the case in which x is a leaf node by inserting key k into x.
            while(i>=1 && (key < obj.compareTo(newNode.getKey(i)))){
                newNode.insertKey(newNode.getKey(i),i+1);
                i--;
            }
            newNode.insertKey(newNode.getKey(i),i+1); //x.key_i+1 = k
            newNode.setNumKeys(newNode.numKeys()+1);
            DiskWrite(newNode, newNode.getOffset());
        }
        else{
            while(i>=1 && (key < obj.compareTo(newNode.getKey(i)))){
                i--;
            }
            i++;
            DiskRead(newNode.children.get(i));
            if(newNode.children.get(i) == 2*degree-1){
                BTreeSplitChild(newNode,i);
                if(key > newNode.children.get(i)){
                    i++;
                }
            }
//            BTreeInsertNonFull(newNode.children.get(i).getKey(),key); TODO
        }
    }

    private void DiskRead(Long aLong) {
    }

    private BTreeNode AllocateNode() {
	    return null;
    }

    //public Boolean bstSearch(){ To determine
    //
	//}



}