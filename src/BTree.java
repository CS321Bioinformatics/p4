package src;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
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
//    long offset;



//    int parent = (btreeList.indexOf(node)-1)/2;


    //TODO Page 492
    public void BTreeCreate(BTree T, String fileName, int degree) throws IOException {
        try {
            file = new File(fileName);
            raf = new RandomAccessFile(file, "rw");
        }
        catch (Exception e){
            System.err.println("Error creating file");
        }
        BTreeNode x = AllocateNode();
        x.isLeaf = true;
        x.setNumKeys(0);

//      TODO  DiskWrite(x);
        T.root = x;

    }


    //TODO btreewritetofile



    //TODO Page 495
	public void BTreeInsert(BTree T, long key) throws IOException {
		BTreeNode r = T.root;
		//the root node r is full
		if(r.numKeys() == 2*degree-1){
		    BTreeNode newNode = AllocateNode();
		    T.root = newNode;
		    newNode.isLeaf = false;
            newNode.setNumKeys(0);
            newNode.children = r.children;//set(1,root.getKey(1)); //TODO from book: s.c_1 = r
            //assuming that this index is full of child
            BTreeSplitChild(newNode,1, r);
            BTreeInsertNonFull(newNode,key);
        }
        else{
            BTreeInsertNonFull(r,key);
        }
	}
    //TODO Page 494
    /*
     *  Tree grows in size by 1
     *  splits a "full" node
     */
    private void BTreeSplitChild(BTreeNode x, int i, BTreeNode y) throws IOException {
        //"Cut(Node) and Paste(Node)"
        BTreeNode z = AllocateNode();
        //Here, x is the node being split, and y is x’s ith child
        z.children = y.children;
//        z.isLeaf = y.isLeaf;
        z.setNumKeys(degree-1);
        for(int j = 1; j <= degree-1; j++){
            z.keyList.set(j, y.keyList.get(j+1));
        }
        //if y is not a leaf
        if(!y.isLeaf){
            for(int j=1; j <= 2*degree-1; j++){
                z.children.set(j, y.children.get(j+1));
            }
        }
        y.setNumKeys(y.numKeys+1);
        for(int j = x.numKeys+1; j >= i+1; j--){
            x.children.set(j+1, x.children.get(j));
        }
        x.children.set(i+1, z.children.get(i));//children_i+1[X] = z TODO
        for(int j = x.numKeys; j >= i; j--){
            x.keyList.set(j+i, x.keyList.get(j));
        }
        x.keyList.set(i,y.keyList.get(i));
        x.setNumKeys(x.numKeys+1);
        DiskWrite(y);
        DiskWrite(z);
        DiskWrite(x);
    }
    //TODO Page 496
    private void BTreeInsertNonFull(BTreeNode newNode, long key) throws IOException {
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
            DiskWrite(newNode);
        }
        else{
            while(i>=1 && (key < obj.compareTo(newNode.getKey(i)))){
                i--;
            }
            i++;
            DiskRead(newNode.children.get(i));
            if(newNode.children.get(i) == 2*degree-1){
                BTreeSplitChild(newNode,i,newNode.keyList.get());
                if(key > newNode.children.get(i)){
                    i++;
                }
            }
            BTreeInsertNonFull(newNode,key); //TODO replace w/ child of newNode instead(?)
        }
    }


    private void DiskWrite(BTreeNode node) {
        try {
            raf.seek(node.getOffset());
            raf.writeLong(node.getOffset());
            for(int i =0; i < root.children.size();i++){
                raf.writeLong(root.children.get(i));
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void DiskRead(Long aLong) {
    }
    //allocate the full node size, not the variable (NOT size of subsequence)
    private BTreeNode AllocateNode() throws IOException {
        long offset = 12;
	    raf.seek(offset);
	    BTreeNode n = new BTreeNode();
	    n.isLeaf = true;
	    n.setNumKeys(0);
	    n.setOffset((int)offset);
	    DiskWrite(n);
        return n;
    }




}