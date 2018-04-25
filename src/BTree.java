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
    public void BTreeCreate(BTree T, String fileName) throws IOException {
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
//        z.leafList = y.leafList;
        z.children = y.children;
        z.setNumKeys(degree-1);
        for(){

        }
//      2  BTreeNode y = Math.toIntExact(newNode.children.get(i));
//        3 ́:leaf D y:leaf
//        4  ́:nDt 1
//        5 forjD1tot 1
//        6  ́:keyj D y:keyjCt
//        7 if not y:leaf
//        8 for j D 1 to t
//        9  ́:cj Dy:cjCt
//        10 y:nDt 1
//        11 forjDx:nC1downtoiC1
//        12 x:cjC1 Dx:cj
//        13 x:ciC1D ́
//        14 forjDx:ndowntoi
//        15 x:keyjC1 D x:keyj
//        16 x.key_i D y.key_t
//        17 x.n = (x.n)+1
//        18 DISK-WRITE(y)
//        19 DISK-WRITE(z)
//        20 DISK-WRITE(x)


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
            DiskWrite(newNode, newNode.getOffset());
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
//            BTreeInsertNonFull(newNode.children.get(i).getKey(),key); TODO
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
	    DiskWrite(n,(int)offset);
        return n;
    }

    //public Boolean bstSearch(){ To determine
    //
	//}



}