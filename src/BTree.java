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
	private File file, binFile;
	public PrintWriter writer;

//    long offset;



//    int parent = (btreeList.indexOf(node)-1)/2;



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
//constructor for GeneBankSearch
    public BTree(int sequence, int degree, String fileName, boolean cache, int cSize) throws IOException {
        try {
            file = new File(fileName);
            raf = new RandomAccessFile(file, "rw");
        }
        catch (Exception e){
            System.err.println("Error creating file");
        }
        //if cache stuff

        //this.degree = degree;

        //BTreeNode x = AllocateNode();
        //x.isLeaf = true;
        //x.setNumKeys(0);
        this.degree = 0;
        this.nodeSize = 0;
        this.rootOffset = 0;
        DiskRead();
        root = MakeNodeFromFile(12);

    }
// btree createbtree constructor
    public BTree(int sequence, int deg, File fileName, boolean cache, int cSize) throws IOException {
        try {
            //file = new File(fileName);
            raf = new RandomAccessFile(fileName.getName(), "rw");
        }
        catch (Exception e){
            System.err.println("Error creating file");
        }
        //if cache



        if (deg == 0) {
            degree = optimal();
        }else {
            degree = deg;
        }
        nodeSize = (degree * 32);
        rootOffset = 12;
        insertOffset = rootOffset + nodeSize;

        BTreeFileCreate(fileName, sequence);
        DiskWrite();
        BTreeNode x = AllocateNode();
        x.isLeaf = true;
        x.setNumKeys(0);



        root = x;

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

        DiskWrite(x);

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
        BTreeNode newNode = AllocateNode();
        if(r.numKeys() == 2*degree-1){
		    //newNode = AllocateNode();
		    T.root = newNode;
		    newNode.isLeaf = false;
            newNode.setNumKeys(0);
            newNode.children = r.children;//set(1,root.getKey(1)); //TODO from book: s.c_1 = r\
            r.parent = newNode.nodeLocation;
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
            if(newNode.numKeys != 0) {
                System.out.println(key);
                //handle the case in which x is a leaf node by inserting key k into x.
                while (i > 1 && obj.compareTo(newNode.getKey(i-1)) < 0) {
                    newNode.insertKey(newNode.getKey(i - 1), i);
                    i--;
                }

                //if frequency
                if (obj.compareTo(newNode.getKey(i-1)) == 0)
                    newNode.getKey(i - 1).incrementFrequency();
            }
            else {
                // newNode.getKey(i-1
//            else{
//                newNode.keyList.add(i,obj);
//            }
                System.out.println(key);
                newNode.insertKey(obj, i); //x.key_i+1 = k
                newNode.numKeys++;
                DiskWrite(newNode);
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
            //DiskRead(newNode.children.get(i));
            BTreeNode child = MakeNodeFromFile(newNode.children1.get(i));
            if(child.numKeys == 2*degree-1){
                BTreeSplitChild(newNode,i,child);
                if(key > newNode.getKey(i).getDnaString()){
                    i++;
                }
            }
            BTreeInsertNonFull(child,key); //TODO replace w/ child of newNode instead(?)
        }

    }

    //raf write node to a specific offset in binary file
    private void DiskWrite(BTreeNode node) {
        int childIndex=0;
        try {
            raf.seek(insertOffset);
            raf.writeBoolean(node.isLeaf); //if this is where we are storing this info
            raf.writeInt(node.numKeys());
            raf.writeInt(node.parent);
            for (childIndex = 0; childIndex < 2 * degree - 1; childIndex++) {
                if(childIndex < node.numKeys() + 1 && !node.isLeaf){
                    raf.writeInt(node.children1.get(childIndex));
                }else if(childIndex >= node.numKeys() + 1 || node.isLeaf){
                    raf.seek(raf.getFilePointer() + 4);
                }
                if(childIndex < node.numKeys()) {
                    raf.writeLong(node.keyList.get(childIndex).dnaString);
                    raf.writeInt(node.keyList.get(childIndex).frequency);
                }
            }
            if(childIndex == node.numKeys() && !node.isLeaf){
                raf.writeInt(node.children1.get(childIndex));
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    //
//    private void DiskRead(Long aLong) {
//
//    }
    //default raf write, writes tree metadata
    private void DiskWrite(){
        try{
            raf.seek(0);
        raf.write(degree); //assuming degree is being stored in front
        raf.write(nodeSize);
        raf.write(rootOffset);
        }catch(IOException e){
            e.printStackTrace();
        }

    }
    //default raf grab metadata from front of bin file
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

    //returns optimal degree
    public static int optimal() {
    	int optimum = 4096;
    	int pointerSize = 4, objectSize  = 12, metadataSize = 5; 
    	optimum = optimum + objectSize - metadataSize - pointerSize;
    	optimum /= 2;
    	optimum /= (objectSize + pointerSize);
    	return optimum;
    }

    //read btree binary file and gets node at offset
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

    //gbsearch driver searches btree for sequence
    public TreeObject BTreeSearch(BTreeNode x, long key){
        int i = 1;
        while ((i <= x.numKeys) && key > x.keyList.get(i).dnaString){
            i++;
        }
        if ((i <= x.numKeys) && (key == x.keyList.get(i).dnaString))
                return x.keyList.get(i);
        else if (x.isLeaf)
                return null;
        else return BTreeSearch(MakeNodeFromFile(x.children1.get(i)), key);

    }

    //write node data to binary file
//    public void writeToFile(BTreeNode x){
//        try {
//            raf.seek(x.nodeLocation);
//            writer.write(raf.);
//        }
//        catch (Exception e){
//            System.err.println("Error writing to .gbk.btree");
//        }
//    }

    //initialize bin file for btreecreate constructor
    public void BTreeFileCreate(File loadedGBK,int seqLength){
        try {
            binFile = new File(loadedGBK+".btree."+seqLength+"."+degree);
            writer = new PrintWriter(new FileWriter(loadedGBK+".btree."+seqLength+"."+degree, true),true);

            //writer.write("something");

        }
        catch (Exception e){
            System.err.println("Error creating .gbk.btree");
        }
    }
//    public String inOrderPrint(){
//        if(root != null){
//            inOrderPrint(root.keyList.get());
//
//        }
//    }

    //public String dumpString
}