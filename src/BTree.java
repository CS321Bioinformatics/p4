package src;
import com.sun.xml.internal.fastinfoset.alphabet.BuiltInRestrictedAlphabets;

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
	private BTCache treeCache;

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


    // btree GeneBankCreateBTree constructor
    public BTree(int sequence, int deg, File fileName, boolean cache, int cSize) throws IOException {
        if (deg == 0) {
            degree = optimal();
        }else {
            degree = deg;
        }

        try {
            //file = new File(fileName);
            file = BTreeFileCreate("data/"+fileName.getName(),sequence,degree);//new RandomAccessFile(fileName.getName(), "rw");
            raf = new RandomAccessFile(file, "rw");
        }
        catch (Exception e){
            System.err.println("Error creating BTree binary file");
        }
        //if cache
        if (cache){treeCache = new BTCache(cSize);}

        //init metadata
        nodeSize = (degree * 32);
//        rootOffset = 13;
        rootOffset = 12;
        insertOffset = rootOffset + nodeSize;
        WriteTreeMetaData();

        //allocate a new node for root
        BTreeNode x = AllocateNode();
        //x.isLeaf = true;
        //x.setNumKeys(0);
        x.setOffset(rootOffset);
        root = x;



    }


//constructor for GeneBankSearch
    public BTree(File fileName, int sequence, int degree, boolean cache, int cSize) throws IOException {
        try {
//            file = new File(fileName);
            raf = new RandomAccessFile(fileName, "r");
        }
        catch (Exception e){
            System.err.println("Error creating file");
        }

        //this.degree = degree;

        //BTreeNode x = AllocateNode();
        //x.isLeaf = true;
        //x.setNumKeys(0);

        //init tree metadata variables
        this.degree = 0;
        this.nodeSize = 0;
        this.rootOffset = 0;

        //DiskWrite();
        ReadTreeMetaData();
        //root = MakeNodeFromFile(13);
        root = ReadNodeFromFile(12);

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

        WriteNodeToFile(x, x.getOffset());

        T.root = x;

    }


    //TODO btreewritetofile



    //TODO Page 495
//	public void BTreeInsert(BTree T, long key) throws IOException {
//    public void BTreeInsert(long key) throws IOException {
//		BTreeNode r = root;
//		int i;
////        for (i = 0;i<10;i++){
////           writeToFile(r,i);
////
////        }
//		//the root node r is full
//        BTreeNode newNode = AllocateNode();
//        if(r.numKeys() == 2*degree-1){
//		    //newNode = AllocateNode();
//		    //T.root = newNode;
//		    newNode.isLeaf = false;
//            newNode.setNumKeys(0);
//            //newNode.children = r.children;//set(1,root.getKey(1)); //TODO from book: s.c_1 = r\
//            newNode.children1.add(root.getLocation());
//            //assuming that this index is full of child
//            BTreeSplitChild1(newNode,1, r);
//            BTreeInsertNonFull(newNode,key);
//        }
//        else{
//            BTreeInsertNonFull(r,key);
//        }
//	}

	//tryer
    public void BTreeInsert(long key) throws IOException {
        System.out.println(key);
        BTreeNode r = root;
        int i = r.numKeys;

        if (i == (2*degree - 1)){
            TreeObject obj = new TreeObject(key);
            while (i > 0 && obj.compareTo(r.getKey(i-1)) < 0){
                i--;
            }
            if (i > 0 && obj.compareTo(r.getKey(i-1)) == 0){
                r.getKey(i-1).incrementFrequency();
            }else{
                BTreeNode s = new BTreeNode();
                s.setOffset(r.getOffset());
                root = s;
                r.setOffset(insertOffset);
                r.setParent(s.getOffset());
                s.isLeaf=false;
                s.children1.add(r.getOffset());
                BTreeSplitChild1(s, 0,r);
                BTreeInsertNonFull(s, key);
//                System.out.println("new object " + key + " inserted into:\n" + s.toString());
//                System.out.println("root node" + "\n" + root.toString());
            }
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
//    private void BTreeSplitChild(BTreeNode x, int i, BTreeNode y) throws IOException {
//        //"Cut(Node) and Paste(Node)"
//        BTreeNode z = AllocateNode();
//        //Here, x is the node being split, and y is x’s ith child
//        //z.children = y.children;
//        z.isLeaf = y.isLeaf;
//        z.setNumKeys(degree-1);
//        for(int j = 1; j <= degree-1; j++){
//            z.keyList.set(j, y.keyList.get(j+degree));
//        }
//        //if y is not a leaf
//        if(!y.isLeaf){
//            for(int j = 1; j <= 2*degree-1; j++){
//                z.children1.set(j, y.children1.get(j+degree));
//            }
//        }
//        y.setNumKeys(y.numKeys+1);
//        for(int j = x.numKeys+1; j >= i+1; j--){
//            x.children1.set(j+1, x.children1.get(j));
//        }
//        x.children1.set(i+1, z.children1.get(i));//children_i+1[X] = z TODO
//        for(int j = x.numKeys; j >= i; j--){
//            x.keyList.set(j+i, x.keyList.get(j));
//        }
//        x.keyList.set(i,y.keyList.get(degree));
//        x.setNumKeys(x.numKeys+1);
//
//        z.setParent(y.getParent());
//
////        WriteNodeToFile(y);
////        WriteNodeToFile(z);
////        WriteNodeToFile(x);
//        WriteNodeToFile(y, insertOffset);//at insertOffset
//            insertOffset+=nodeSize;
//            z.setOffset(insertOffset);
//            x.children1.add(z.getOffset());
//            WriteNodeToFile(z, insertOffset);//at insertOffset
//            WriteNodeToFile(x, rootOffset);//at root offset
//    }
    private void BTreeSplitChild1(BTreeNode x, int i, BTreeNode y) throws IOException {
        //trial
        //System.out.println("SPLITTING Node: " + y.getOffset());
        BTreeNode z = new BTreeNode();
        z.isLeaf = y.isLeaf;
        z.setParent(y.getParent());
        for (int j = 0; j < degree - 1; j++){
            z.keyList.add(y.keyList.remove(degree));
            z.numKeys++;
            y.numKeys--;
        }
        if(!y.isLeaf){
            for(int j = 0; j < degree; j++){
                z.children1.add(y.children1.remove(degree));
            }
        }
        if(x.numKeys() == 0) x.keyList.add((y.keyList.remove(degree-1)));
        else x.keyList.add(i,(y.keyList.remove(degree-1)));
        x.numKeys++;
        y.numKeys--;
        if(x == root && x.numKeys == 1){
            WriteNodeToFile(y, insertOffset);//at insertOffset
            insertOffset+=nodeSize;
            z.setOffset(insertOffset);
            x.children1.add(z.getOffset());
            WriteNodeToFile(z, insertOffset);//at insertOffset
            WriteNodeToFile(x, rootOffset);//at root offset



//            System.out.println(x.toString());
//            System.out.println(y.toString());
//            System.out.println(z.toString());
//            System.out.println(insertOffset);
//            System.out.println("new insert offset: " + insertOffset);
//            System.out.println("root node" + "\n" + root.toString());

        }else{
            WriteNodeToFile(y, y.getOffset());
            z.setOffset(insertOffset);
            WriteNodeToFile(z, insertOffset);//at insertOffset
            x.children1.add(z.getOffset());
            WriteNodeToFile(x, x.getOffset());
            insertOffset+=nodeSize;


//            System.out.println(x.toString());
//            System.out.println(y.toString());
//            System.out.println(z.toString());
//            System.out.println("new insert offset: " + insertOffset);
//            System.out.println("root node" + "\n" + root.toString());

        }
    }

    //TODO Page 496
    private void BTreeInsertNonFull(BTreeNode rootNode, long key) throws IOException {
        int i = rootNode.numKeys();
        TreeObject obj = new TreeObject(key);
        if(rootNode.isLeaf){
            if(rootNode.numKeys() != 0) {
//                System.out.print(key + "       ");
//                System.out.println(rootNode.getOffset());
                //handle the case in which x is a leaf node by inserting key k into x.
                while (i >= 1 && obj.compareTo(rootNode.getKey(i-1)) < 0) {
                    if(i == rootNode.keyList.size()) rootNode.insertKey(rootNode.getKey(i - 1), i);
                    else rootNode.keyList.set(i, rootNode.keyList.get(i-1));
                    i--;
                }

                //if frequency
                if(i == 0)
                    if (obj.compareTo(rootNode.getKey(i)) == 0) rootNode.getKey(i).incrementFrequency();
                else if(i == rootNode.numKeys())
                    if (obj.compareTo(rootNode.getKey(i-1)) == 0) rootNode.getKey(i - 1).incrementFrequency();

                else if (obj.compareTo(rootNode.getKey(i)) == 0) rootNode.getKey(i).incrementFrequency();

                if (i >= rootNode.keyList.size()){
                    rootNode.keyList.add(obj);
                }else rootNode.keyList.set(i, obj);
                rootNode.numKeys++;
                WriteNodeToFile(rootNode, rootNode.getOffset());
            }
            else {
//                System.out.print(key +"__________");
//                System.out.println(rootNode.getOffset());
                rootNode.insertKey(obj, i); //x.key_i+1 = k
                rootNode.numKeys++;
                WriteNodeToFile(rootNode, rootNode.getOffset());
            }
        }
        else{
            while(i>=1 && obj.compareTo(rootNode.getKey(i-1)) < 0){
                i--;
            }
//            if(i == 0)
//                if (obj.compareTo(rootNode.getKey(i)) == 0) rootNode.getKey(i).incrementFrequency();
//            else if(i == rootNode.numKeys)
//                if (obj.compareTo(rootNode.getKey(i-1)) == 0) rootNode.getKey(i - 1).incrementFrequency();
//
//            else if (obj.compareTo(rootNode.getKey(i)) == 0) rootNode.getKey(i).incrementFrequency();
//            i++;
            if(i > 0 && obj.compareTo(rootNode.getKey(i-1))==0) {
                rootNode.getKey(i-1).incrementFrequency();
                WriteNodeToFile(rootNode, rootNode.getOffset());
                return;
            }
            //DiskRead(newNode.children.get(i));
            BTreeNode child = null;

            if(i == 0) child = ReadNodeFromFile(rootNode.children1.get(i));
            else child = ReadNodeFromFile(rootNode.children1.get(i-1));

            if(child.numKeys == 2*degree-1){

                int j = child.numKeys();
//                if(key > rootNode.getKey(i).getDnaString()){
//                    i++;
//                }
                while(j > 0 && obj.compareTo(child.getKey(j-1)) < 0){
                    j--;
                }
                if(j > 0 && obj.compareTo(child.getKey(j-1)) == 0){
                    child.getKey(i).incrementFrequency();
                    WriteNodeToFile(child, child.getOffset());
                    return;
                }else{
                    BTreeSplitChild1(rootNode,i,child);
                        if(obj.compareTo(rootNode.getKey(i)) > 0){
                            i++;
                        }
                }
            }
            //BTreeNode child2 = ReadNodeFromFile(rootNode.children1.get(i-1));
            BTreeNode child2 = null;
//            if (i == 0) {
//                child2 = ReadNodeFromFile(rootNode.children1.get(i));
//            }else
//                child2 = ReadNodeFromFile(rootNode.children1.get(i));
            if (key == 1490) {
                System.out.println("woops");
            }
            BTreeInsertNonFull(child,key); //TODO replace w/ child of newNode instead(?)
        }

    }

    //raf write node to a specific offset in binary file
    private void WriteNodeToFile(BTreeNode node, int offset) {
        int childIndex=0;
        try {
            raf.seek(offset);
            //write node meta
            raf.writeBoolean(node.isLeaf); //if this is where we are storing this info
            raf.writeInt(node.numKeys());
            //raf.writeInt(node.getOffset());
            //write other data
            raf.writeInt(node.parent);
            for (childIndex = 0; childIndex < 2 * degree - 1; childIndex++) {
                if(childIndex < node.numKeys()  && !node.isLeaf){
                    raf.writeInt(node.children1.get(childIndex));
                }else if(childIndex >= node.numKeys() + 1 || node.isLeaf){
                    raf.seek(raf.getFilePointer() + 4);
//                    raf.writeInt( 0);
                }
                if(childIndex < node.numKeys()) {
                    raf.writeLong(node.keyList.get(childIndex).dnaString);
                    raf.writeInt(node.keyList.get(childIndex).frequency);
                }
                else if(childIndex >= node.numKeys() || node.isLeaf){
                    raf.writeLong(0);
                }
            }
            if(childIndex == node.children1.size() && !node.isLeaf){
                raf.writeInt(node.children1.get(childIndex-1));
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
    private void WriteTreeMetaData(){//TODO position | Byte array and
        try{
            raf.seek(0);
            raf.writeInt(degree); //assuming degree is being stored in front
            raf.writeInt(nodeSize);
            raf.writeInt(rootOffset);

        }catch(IOException e){
            e.printStackTrace();
        }

    }
    //default raf grab tree metadata from front of bin file
    private void ReadTreeMetaData() {
        try {
            raf.seek(0);
            degree = raf.readInt(); //assuming degree is being stored in front
            nodeSize = raf.readInt();
            rootOffset = raf.readInt();
            System.out.println("Disk" + degree);
            System.out.println(nodeSize);
            System.out.println(rootOffset);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
//    private BTreeNode DiskRead(int offset) {
//        try {
//            raf.seek(offset);
//
//            degree = raf.readInt(); //assuming degree is being stored in front
//            nodeSize = raf.readInt();
//            rootOffset = raf.readInt();
//            System.out.println("Disk" + degree);
//            System.out.println(nodeSize);
//            System.out.println(rootOffset);
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    //allocate the full node size, not the variable (NOT size of subsequence)
    private BTreeNode AllocateNode() throws IOException {
        //long offset = 13;
        //long offset = 12;
	    raf.seek(insertOffset);
	    BTreeNode n = new BTreeNode();
	    n.isLeaf = true;
	    n.setNumKeys(0);
	    n.setOffset(insertOffset);
        // insertOffset += nodeSize;
	    WriteNodeToFile(n, n.getOffset());
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
    public BTreeNode ReadNodeFromFile(int offset){
        BTreeNode x = null;
        //cache stuff
        if (treeCache != null){x = treeCache.getObject(offset);}
        if(x!=null) return x;

        x = new BTreeNode();
        TreeObject y = null;

        x.setOffset(offset);
        int childIndex = 0;
        try {
            raf.seek(offset);
//            boolean isLeaf = raf.readBoolean(); //if this is where we are storing this info
//            x.isLeaf = isLeaf;

            //node metadata
            x.isLeaf = raf.readBoolean();   //1 byte
            x.setNumKeys(raf.readInt());    //4 bytes
            //rest of data
            x.parent = raf.readInt();       //4 bytes
            for (childIndex = 0; childIndex < 2 * degree - 1; childIndex++) {
                if(childIndex < x.numKeys() + 1 && !x.isLeaf){
                    int child = raf.readInt();
                    if (child != 0) x.children1.add(child);
                    else raf.seek(raf.getFilePointer() + 4);
                }else if(childIndex >= x.numKeys() + 1 || x.isLeaf){
                raf.seek(raf.getFilePointer() + 4);
                }
                if(childIndex < x.numKeys) {
                    long val = raf.readLong();
                    int freq = raf.readInt();
                    y = new TreeObject(val, freq);
                    x.keyList.add(y);/////need to check this
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
        TreeObject y = new TreeObject(key);
        //while ((i <= x.numKeys) && key > x.keyList.get(i).dnaString){
        while ((i < x.numKeys) && (y.compareTo(x.keyList.get(i))) > 0){
            i++;
        }
        //if ((i <= x.numKeys) && (key == x.keyList.get(i).dnaString))
        if ((i < x.numKeys) && (y.compareTo(x.keyList.get(i))) ==0)
                return x.keyList.get(i);
        //else if (!x.isLeaf) return null;
        if (!x.isLeaf) return null;
        else return BTreeSearch(ReadNodeFromFile(x.children1.get(i)), key);

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
    public File BTreeFileCreate(String loadedGBK,int seqLength,int degree){
        try {
            binFile = new File(loadedGBK+".btree.data."+seqLength+"."+degree);
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
        //int i = 0;
        //int numNodes = (insertOffset -rootOffset/nodeSize);
        //while(i<node.numKeys()){
        for(int i = 0; i < node.numKeys; i++){
            writer.print(ram.convertLongtoString(node.getKey(i).dnaString,sequence));
            writer.println(": " + node.getKey(i).frequency);
            //i++;
        }
        if(!node.isLeaf){
            //while(i<node.numKeys()+1){
            for(int j = 0; j < node.numKeys; j++){
                BTreeNode x = ReadNodeFromFile(node.children1.get(j));
                inOrderWriteFile(x, writer, sequence);
                if(j < node.numKeys()){
                    writer.println(ram.convertLongtoString(node.getKey(j).dnaString,sequence));
                    writer.println(": " + node.getKey(j).frequency);
                }
            //inOrderWriteFile(root.keyList.get(i));
                //i++;
            }
        }
    }







    //cache and other added stuffs
    //trying this method to write the node to the raf
    public void putNodeInRAF(BTreeNode n, int offset){
        int i = 0;
        try{
            writeNodeMeta(n,n.getOffset());

            for(;i < 2; i++){
                if (i < n.numKeys() + 1 && !n.isLeaf){
                    raf.writeInt(n.getChild(i));
                }
                else if(i >= n.numKeys + 1 || n.isLeaf){
                    raf.writeInt(0);
                }
                if(i < n.numKeys){
                    raf.writeLong(n.getKey(i).getDnaString());
                    raf.writeInt(n.getKey(i).getFrequency());
                }
                else if( i >= n.numKeys || n.isLeaf){
                    raf.writeLong(0);
                }
            }
            if(i == n.numKeys && !n.isLeaf){
                raf.writeInt(n.getChild(i));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //method to write not meta data
    public void writeNodeMeta(BTreeNode x, int offset){
        try {
            raf.seek(offset);
            raf.writeBoolean(x.isLeaf); //if this is where we are storing this info
            raf.writeInt(x.numKeys());
            //raf.writeInt(x.getOffset());
            //raf.writeInt((x.parent));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void flushCache(){
        if(treeCache != null){
            for (BTreeNode node : treeCache) putNodeInRAF(node, node.getOffset());
        }
    }

    public void DiskWrite(BTreeNode x, int offset){
        if (treeCache != null){
            BTreeNode node = treeCache.addObject(x);
            if (node != null){
                putNodeInRAF(node, node.getOffset());
            }
        }else{
            putNodeInRAF(x, offset);
        }
    }

    public void inOrderFilePrint(BTreeNode node,PrintWriter writer,int sequence) throws IOException{
        RAM ram = new RAM();
        int i = 0;
        int numNodes = (insertOffset -rootOffset/nodeSize);
        while(i<node.numKeys()){
            writer.print(ram.convertLongtoString(node.getKey(i).dnaString,sequence));
            writer.println(": " + node.getKey(i).frequency);


            System.out.print(ram.convertLongtoString(node.getKey(i).dnaString,sequence));
            System.out.println(": " + node.getKey(i).frequency);
            i++;
        }
        if(!node.isLeaf) {
            while (i < node.numKeys() + 1) {
                BTreeNode x = ReadNodeFromFile(node.getChild(i));
                inOrderFilePrint(x, writer, sequence);
                if (i < node.numKeys) {
                    writer.print(ram.convertLongtoString(node.getKey(i).dnaString, sequence));
                    writer.println(": " + node.getKey(i).frequency);
                    System.out.print(ram.convertLongtoString(node.getKey(i).dnaString,sequence));
                    System.out.println(": " + node.getKey(i).frequency);
                }
                //inOrderWriteFile(root.keyList.get(i));
                i++;
            }
        }
    }
}