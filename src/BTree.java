package src;

public class BTree {
    //TODO data structure to store TreeObjects
	
	//Metadata
	//degree
	//key
	//location of root
	
	private int degree;
	private RAM ram;
	private BTreeNode root;
	
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
}