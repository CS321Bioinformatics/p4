package src;
import jdk.nashorn.api.tree.Tree;

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


	/*
	TODO list
	-update nodeLocation to set parent for btree insert

	-




	 */


    public int getInsertOffset() {
        return insertOffset;
    }

    public void setInsertOffset(int insertOffset) {
        this.insertOffset = insertOffset;
    }

    /*
     *Constructor for BTree Create driver
     *
     */
    public BTree(int sequence, int deg, File fileName, boolean cache, int cSize) throws IOException {
        try {
//            file = new File(fileName.getName());
            file = new File(fileName+".btree."+sequence+"."+degree);//BTreeFileCreate(fileName,sequence); //create file name
            raf = new RandomAccessFile(file, "rw");
        }
        catch (Exception e){
            System.err.println("Error creating file");
        }
        //if cache
        if (deg == 0) {
            this.degree = optimal();
        }else {
            this.degree = deg;
        }
        nodeSize = (degree * 32); //        -int?
        rootOffset = 12; //Either 12, or 4096(degree) + 12 = 4104
        setInsertOffset(rootOffset + nodeSize);

//        BTreeFileCreate(fileName, sequence);
        //write tree metadata

        BTreeNode x = AllocateNode();

        root = x;
        x.isLeaf = true;
        x.setNumKeys(0);
        root.setOffset(12);
        DiskWriteMetadata();
    }
    /*
    *Constructor for BTree Search driver
    *
    */
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
//        DiskWrite();
        DiskRead();
        root = MakeNodeFromFile(12);

    }


	
    // Page 492
//    public void BTreeCreate(BTree T, String fileName, int degree) throws IOException {
//        try {
//            file = new File(fileName);
//            raf = new RandomAccessFile(file, "rw");
//        }
//        catch (Exception e){
//            System.err.println("Error creating file");
//        }
//        BTreeNode x = AllocateNode();
//        x.isLeaf = true;
//        x.setNumKeys(0);
//
//        DiskWrite(x);
//
//        T.root = x;
//
//    }


    //TODO btreewritetofile

//TODO before changing insert
//    public void BTreeInsert(BTree T, long key) throws IOException {
//        BTreeNode r = T.root;
//        int i;
//
//        //the root node r is full
//        BTreeNode newNode = AllocateNode();
//        if(r.numKeys() == 2*degree-1){
//            //newNode = AllocateNode();
////            TreeObject insertOb = new TreeObject(key);
//            T.root = newNode;
//            newNode.isLeaf = false;
//            newNode.setNumKeys(0);
//            newNode.children = r.children;//set(1,root.getKey(1)); //TODO from book: s.c_1 = r\
//            r.parent = newNode.nodeLocation;
//            //assuming that this index is full of child
//            BTreeSplitChild(newNode,1, r);
//            BTreeInsertNonFull(newNode,key);
//        }
//        else{
//            BTreeInsertNonFull(r,key);
//        }
//    }


    //TODO Page 495
	public void BTreeInsert(BTree T, long key) throws IOException {
		BTreeNode r = root;
		int index = r.numKeys();
		//the root node r is full
//        BTreeNode node = AllocateNode();
        if(index == 2*degree-1){
		    //newNode = AllocateNode();
            BTreeNode newNode = AllocateNode();
		    root = newNode;
//		    newNode.setOffset(r.getOffset()); Done in AllocateNode
            r.setParent(newNode.getOffset());
		    r.setOffset(insertOffset);

//            newNode.insertChild(r.getOffset());
            newNode.children = r.children;//set(1,root.getKey(1)); //TODO from book: s.c_1 = r\

            //assuming that this index is full of child
            BTreeSplitChild(newNode,0, r);
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
        z.setParent(y.getParent());
        //Here, x is the node being split, and y is x’s ith child



//        z.isLeaf = y.isLeaf;

//        z.setNumKeys(degree-1);



//        for (int j = 0; j < degree - 1; j++){
//            z.insertKey(y.removeKey(j), j);
//            z.setNumKeys(z.numKeys()+1);
//            y.setNumKeys(y.numKeys()-1);
//        }
        for(int j = 1; j <= degree-1; j++){
            z.keyList.set(j, y.keyList.get(j+1));
        }
//        z.children = y.children; replaced by above loop

        //if y is not a leaf
        if(!y.isLeaf){
            for(int j = 1; j <= degree-1; j++){
                z.children.set(j, y.children.get(j+1));
                y.children.remove(j+1);
            }
        }
//        y.setNumKeys(y.numKeys+1);

        for(int j = x.numKeys+1; j >= i+1; j--){
            x.children.set(j+1, x.children.get(j));
        }
        x.insertKey(y.removeKey(degree),i); //"i" could be degree instead if we add to center of arraylist
        x.setNumKeys(x.numKeys()+1);
        y.setNumKeys(y.numKeys()-1);
//        x.children.set(i+1, z.children.get(i));//children_i+1[X] = z TODO
//        for(int j = x.numKeys; j >= i; j--){
//            x.keyList.set(j+i, x.keyList.get(j));
//        }
        //if x is root and only has the key from y-node
        if(x == root && x.numKeys == 1){
            DiskWrite(y);
            insertOffset+=nodeSize;
            z.setOffset(insertOffset);
            x.insertChild(z.getOffset());
            DiskWrite(z);
            insertOffset+=nodeSize;
            DiskWrite(x);
        }
        DiskWrite(y);
        z.setOffset(insertOffset);
        DiskWrite(z);
//        x.keyList.set(i,y.keyList.get(i));
//        x.setNumKeys(x.numKeys+1);
        x.insertChild(i);
//        DiskWrite(y);
//        DiskWrite(z);
        DiskWrite(x);
        insertOffset+=nodeSize;
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



    /*
     *  Default raf write, writes tree metadata
     *
     */
    private void DiskWriteMetadata(){//TODO int position | Byte array and -
        try{
            raf.seek(getInsertOffset());
        raf.writeInt(degree); //We can choose whichever order we want, we have to explain our structure in README
        raf.writeInt(nodeSize);
        raf.writeInt(rootOffset);


            System.out.println("degree "+ degree);
            System.out.println("nodesize " + nodeSize);
            System.out.println("rootOffset " + rootOffset);


        }catch(IOException e){
            e.printStackTrace();
        }

    }
    public void WriteNodeMetadata(BTreeNode n){
        try {
            raf.seek(getInsertOffset());
            raf.writeBoolean(n.isLeaf);
            raf.writeInt(n.numKeys());
            raf.writeInt(n.getParent());
        }
        catch (IOException ioe){
            System.err.println("IO Exception occurred!");
            System.exit(-1);
        }
    }

    /*
     *  Default raf grab metadata from front of bin file
     * TODO needs work
     */
    private void DiskRead() {
        try{
            raf.seek(getInsertOffset());
            degree = raf.readInt(); //assuming degree is being stored in front
            nodeSize = raf.readInt();
            rootOffset = raf.readInt();
            System.out.println("Disk"+degree);
            System.out.println(nodeSize);
            System.out.println(rootOffset);

        }catch(IOException e){
            e.printStackTrace();
        }
    }

    //raf write node to a specific offset in binary file
    private void DiskWrite(BTreeNode node) {
        int childIndex=0;
        try {
            WriteNodeMetadata(node);
//            raf.seek(insertOffset);
//            raf.writeBoolean(node.isLeaf); //if this is where we are storing this info
//            raf.writeInt(node.numKeys());
//            raf.writeInt(node.parent);
            for (childIndex = 0; childIndex < 2 * degree - 1; childIndex++) {
                //write one child
                if(childIndex < node.numKeys() + 1 && !node.isLeaf){
//                    raf.writeInt(node.children1.get(childIndex));
                    raf.writeInt(node.getChild(childIndex));
                }
                //write 0 because it's a leaf
                else if(childIndex >= node.numKeys() + 1 || node.getLeaf()){
//                    raf.seek(raf.getFilePointer() + 4);
                    raf.writeInt( 0);
                }
                // write node DNA
                // write node frequency
                if(childIndex < node.numKeys()) {
//                    raf.writeLong(node.keyList.get(childIndex).dnaString);
//                    raf.writeInt(node.keyList.get(childIndex).frequency);
                    raf.writeLong(node.getKey(childIndex).dnaString);
                    raf.writeInt(node.getKey(childIndex).frequency);
                }
                // if it's a leaf, write none
                else if(childIndex >= node.numKeys() || node.isLeaf){
                    raf.writeLong(0);
//                    raf.writeInt(0); may need to write frequency of 0
                }
            }
            if(childIndex == node.numKeys() && !node.isLeaf){
                raf.writeInt(node.getChild(childIndex));
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }


    //allocate the full node size, not the variable (NOT size of subsequence)
    private BTreeNode AllocateNode() throws IOException {
        int offset = insertOffset;
	    raf.seek(offset);
	    BTreeNode n = new BTreeNode();
	    n.setLeaf(true);
	    n.setNumKeys(0);
	    n.setOffset(root.getOffset());
        setInsertOffset(getInsertOffset()+nodeSize);
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
            x.setLeaf(isLeaf);
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
    public File BTreeFileCreate(File loadedGBK,int seqLength){
        try {
            binFile = new File(loadedGBK+".btree."+seqLength+"."+degree);
//            writer = new PrintWriter(new FileWriter(loadedGBK+".btree."+seqLength+"."+degree, true),true);

            //writer.write("something");

        }
        catch (Exception e){
            System.err.println("Error creating .gbk.btree");
        }
        return binFile;
    }
    //Needed to print when debug level is 0
    public void inOrderWriteFile(BTreeNode node,PrintWriter writer,int sequence){
        RAM ram = new RAM();
        int i = 0;
        while(i<node.numKeys()){
            writer.println(ram.convertLongtoString(node.getKey(i).dnaString,sequence));
            writer.append(": " + node.getKey(i).frequency);
            i++;
        }
        if(!node.isLeaf){
            while(i<node.numKeys()+1){
//            inOrderWriteFile(root.keyList.get());

            }
        }

    //public String dumpString
    }
}