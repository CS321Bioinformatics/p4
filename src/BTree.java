package src;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class BTree {
    //TODO data structure to store TreeObjects
	
	//Metadata
	//degree
	//key
	//location of root
	
	private int degree;
	private RAM ram;
	private BTreeNode root;
	private ArrayList<TreeObject> btreeList = new ArrayList<TreeObject>();

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

	public void bstInsert(long key){
//		btreeList.set((btreeList.indexOf(node)-1)/2,null);
		BTreeNode insertRoot = root;
        int n = insertRoot.getNode();
        TreeObject obj = new TreeObject(key);
        while (n>0 && obj.compareTo(insertRoot.getKey(n-1))<0){
            n--;
        }
        if(obj.compareTo(insertRoot.getKey(n-1)) == 1){
            insertRoot.getKey(n-1).incrementFrequency();
        }
//        else{
//            BTreeNode node = new BTreeNode();
//            insert node into list data structure in TreeNodeClass
//        }
	}

	//public Boolean bstSearch(){ To determine
    //
	//}



}