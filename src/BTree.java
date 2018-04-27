package src;
import java.io.*;
import java.util.ArrayList;


public class BTree {
    //TODO data structure to store TreeObjects
	
	//Metadata
	//degree
	//key
	//location of root
	public RandomAccessFile raf;
	private int degree, nodeSize, insertOffset, rootOffset;
	public BTreeNode root;
	private File file;

//    long offset;



//    int parent = (btreeList.indexOf(node)-1)/2;


	//constructor for new btree created in createBTree
	public BTree(int deg ,String fileName) {
		if (deg == 0) {
			degree = optimal();
		}else {
			degree = deg;
		}


		root = new BTreeNode();
        root.setNumKeys(0);
        root.offset = 0;//root.getOffset();
        root.isLeaf=true;

        try {
            file = new File("filename");
            raf = new RandomAccessFile(file, "rw");
        }
        catch (Exception e){
            System.err.println("Error creating file");
        }

    }
//    //constructor for btree from btree file in gbsearch
//    public BTree(int degree, File fileName, boolean cache, int cSize) throws IOException {
//        if(cache){
//            //create cache with cSize
//        }
//
//	    this.degree = degree;
//        nodeSize = (degree * 32) - 3;
//        rootOffset = 12;
//        insertOffset = rootOffset + nodeSize;
//        BTreeNode x = AllocateNode();
//        x.isLeaf = true;
//        x.setNumKeys(0);
//
////      TODO  DiskWrite(x);
//
//        T.root = x;
//
//    }
    public BTree(int degree, File fileName, boolean cache, int cSize) throws IOException {
        try {
            //file = new File(fileName);
            raf = new RandomAccessFile(file, "rw");
        }
        catch (Exception e){
            System.err.println("Error creating file");
        }
        DiskRead();
        root = MakeNodeFromFile(12);
    }

	
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
		int i;
//        for (i = 0;i<10;i++){
//           writeToFile(r,i);
//
//        }
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
            for(i = 0; i<r.children.size();i++){
                writeToFile(r,i);
            }

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
            if(newNode.numKeys != 0) {
                System.out.println(key);
                //handle the case in which x is a leaf node by inserting key k into x.
                while (i >= 1 && obj.compareTo(newNode.getKey(i - 1)) < 0) {
                    newNode.insertKey(newNode.getKey(i-1), i);
                    i--;
                }

            //if frequency
            if(obj.compareTo(newNode.getKey(i - 1)) == 0){
                    newNode.getKey(i -1).incrementFrequency();
            }else {
                // newNode.getKey(i-1
//            else{
//                newNode.keyList.add(i,obj);
//            }
                newNode.insertKey(obj, i); //x.key_i+1 = k
                newNode.setNumKeys(newNode.numKeys() + 1);
                DiskWrite(newNode);
            }
            }
        }
        else{
            while(i>=1 && obj.compareTo(newNode.getKey(i)) > 0){
                i--;
            }
            if(obj.compareTo(newNode.getKey(i - 1)) == 0){
                newNode.getKey(i -1).incrementFrequency();
            }
            i++;
            DiskRead(newNode.children.get(i));
            if(newNode.children.get(i) == 2*degree-1){
//                BTreeSplitChild(newNode,i,newNode.keyList.get(i));
                if(key > newNode.getKey(i).getDnaString()){
                    i++;
                }
            }
            BTreeInsertNonFull(newNode,key); //TODO replace w/ child of newNode instead(?)
        }

    }

    public void writeToFile(BTreeNode nodeToWrite, int i) throws FileNotFoundException {//, String filename
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream("dump"), "utf-8"))) {
            writer.write(nodeToWrite.keyList.get(i).toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
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


    private void DiskRead() {
        try{
            raf.seek(0);
            degree = raf.readInt(); //assuming degree is being stored in front
            nodeSize = raf.readInt();
            rootOffset = raf.readInt();

        }catch(IOException e){
            e.printStackTrace();
        }
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

    public static int optimal() {
    	int optimum = 4096;
    	int pointerSize = 4, objectSize  = 12, metadataSize = 5; 
    	optimum = optimum + objectSize - metadataSize - pointerSize;
    	optimum /= 2;
    	optimum /= (objectSize + pointerSize);
    	return optimum;
    }

    public BTreeNode MakeNodeFromFile(int offset){
        BTreeNode x = null;
        //cache stuff

        x = new BTreeNode();
        TreeObject y = null;

        x.setOffset(offset);
        int childIndex = 0;
        try {
            raf.seek(offset);
            boolean isLeaf = raf.readBoolean(); //if this is where we are storing this info
            x.isLeaf = isLeaf;
            x.setNumKeys(raf.readInt());
            x.parent = raf.readInt();
            for (childIndex = 0; childIndex < 2 * degree - 1; childIndex++) {
                if(childIndex < x.numKeys() + 1 && !x.isLeaf){
                int child = raf.readInt();
                x.insertChild(child);
                }else if(childIndex >= x.numKeys() + 1 || x.isLeaf){
                raf.seek(raf.getFilePointer() + 4);
                }
                if(childIndex < x.numKeys()) {
                    long val = raf.readLong();
                    int freq = raf.readInt();
                    y = new TreeObject(val, freq);
                    x.insertKey(y,0);/////need to check this
                }
            }
            if(childIndex == x.numKeys() && !x.isLeaf){
            int child = raf.readInt();
            x.insertChild(child);
            }
        }catch(IOException e){
            e.printStackTrace();
        }

        return x;
    }


}