package src;

import java.io.*;



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

    // btree GeneBankCreateBTree constructor
    public BTree(int sequence, int deg, File fileName, boolean cache, int cSize) throws IOException {
        if (deg == 0) {
            degree = optimal();
        }else {
            degree = deg;
        }

        try {
            //file = new File(fileName);
            file = BTreeFileCreate(fileName.getName(),sequence,degree);//new RandomAccessFile(fileName.getName(), "rw");
            raf = new RandomAccessFile(file, "rw");
        }
        catch (Exception e){
            System.err.println("Error creating BTree binary file");
        }
        //if cache
        if (cache){treeCache = new BTCache(cSize);}

        //init metadata
        nodeSize = (degree * 32);
        rootOffset = 12;
        insertOffset = rootOffset + nodeSize;
        WriteTreeMetaData();

        //allocate a new node for root
        BTreeNode x = AllocateNode();
        x.setOffset(rootOffset);
        root = x;



    }


//constructor for GeneBankSearch
    public BTree(File fileName, int sequence, int degree, boolean cache, int cSize) throws IOException {
        try {
            raf = new RandomAccessFile(fileName, "r");
        }
        catch (Exception e){
            System.err.println("Error creating file");
        }
        //init tree metadata variables
        this.degree = 0;
        this.nodeSize = 0;
        this.rootOffset = 0;

        ReadTreeMetaData();
        root = ReadNodeFromFile(12);
    }


    public void BTreeInsert(long key) throws IOException {
        BTreeNode r = root;
        int i = r.numKeys();

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
            }
        }
        else{
            BTreeInsertNonFull(r,key);
        }
    }

    private void BTreeSplitChild1(BTreeNode x, int i, BTreeNode y) throws IOException {
        BTreeNode z = new BTreeNode();
        z.isLeaf = y.isLeaf;
        z.setParent(y.getParent());
        z.setNumKeys(degree-1);
        //x.children1.add(i, y.getOffset());
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
        //move middle key up to parent
        if(x.numKeys() == 0) x.keyList.add((y.keyList.remove(degree-1)));
        else{

            TreeObject obj = (y.getKey(degree-1));
            int j = x.numKeys();
            while (j > 0 && obj.compareTo(x.getKey(j-1)) < 0){
                j--;
            }
            x.keyList.add(j,(y.keyList.remove(degree-1)));

        }
        x.numKeys++;
        y.numKeys--;
        if(x == root && x.numKeys == 1){
            DiskWrite(y, insertOffset);//at insertOffset
            insertOffset+=nodeSize;
            z.setOffset(insertOffset);
            x.children1.add(i +1, z.getOffset());
            DiskWrite(z, insertOffset);//at insertOffset
            DiskWrite(x, rootOffset);//at root offset


        }else{
            DiskWrite(y, y.getOffset());
            z.setOffset(insertOffset);
            DiskWrite(z, insertOffset);//at insertOffset
            x.children1.add(z.getOffset());
            DiskWrite(x, x.getOffset());
            insertOffset+=nodeSize;

        }
    }

    //TODO Page 496
//    private void BTreeInsertNonFull(BTreeNode rootNode, long key) throws IOException {
//        int i = rootNode.numKeys();
//        TreeObject obj = new TreeObject(key);
//        if(rootNode.isLeaf){
//            if(rootNode.numKeys() != 0) {
//                //handle the case in which x is a leaf node by inserting key k into x.
//                while (i > 0 && obj.compareTo(rootNode.getKey(i - 1)) < 0) {
////                    if(obj.compareTo(rootNode.getKey(i-1)) == 0) break;
//                    if (i == rootNode.keyList.size()) rootNode.keyList.add(new TreeObject(key));
////                    else rootNode.keyList.set(i, rootNode.keyList.get(i-1));
//                    rootNode.keyList.set(i, rootNode.keyList.get(i - 1));
//                    i--;
//                }
//
//                //if we are still at the end of the list
//                if (i == rootNode.keyList.size()) {
//                    //check for duplicate
//                    if (i > 0) { //if at the end of the list
//                        if (obj.compareTo(rootNode.getKey(i-1)) == 0) rootNode.getKey(i-1).incrementFrequency();
//                        else rootNode.keyList.add(obj);
//                    }
//                }
//                else if(i == 0 && i != rootNode.keyList.size()){
//                    if (i == 0) rootNode.keyList.set(i, obj);
//                    else rootNode.keyList.set(i, obj);
//                }
//                else rootNode.keyList.set(i, obj);
//                rootNode.setNumKeys(rootNode.numKeys() + 1);
//                //DiskWrite(rootNode, rootNode.getOffset());
//                //WriteNodeToFile(rootNode, rootNode.getOffset());
//            }else {
//                rootNode.insertKey(obj, i);
//                rootNode.setNumKeys(rootNode.numKeys() + 1);
//            }
//                //DiskWrite(rootNode, rootNode.getOffset());
//                WriteNodeToFile(rootNode, rootNode.getOffset());
//        }
//        else{
//            while(i>=1 && obj.compareTo(rootNode.getKey(i-1)) < 0){
//                i--;
//            }
//            if(i > 0 && obj.compareTo(rootNode.getKey(i-1))==0) {
//                rootNode.getKey(i-1).incrementFrequency();
//                DiskWrite(rootNode, rootNode.getOffset());
//                return;
//            }
//            BTreeNode child = null;
//
//            if(i == 0) child = ReadNodeFromFile(rootNode.children1.get(i));
//            else child = ReadNodeFromFile(rootNode.children1.get(i-1));
//
//            if(child.numKeys == 2*degree-1){
//
//                int j = child.numKeys();
//                while(j > 0 && obj.compareTo(child.getKey(j-1)) < 0){
//                    j--;
//                }
//                if(j > 0 && obj.compareTo(child.getKey(j-1)) == 0){
//                    child.getKey(i).incrementFrequency();
//                    DiskWrite(child, child.getOffset());
//                    return;
//                }else{
//                    BTreeSplitChild1(rootNode,i,child);
//                        if(obj.compareTo(rootNode.getKey(i)) > 0){
//                            i++;
//                        }
//                }
//            }
//            BTreeInsertNonFull(child,key);
//        }
//
//    }
    private void BTreeInsertNonFull(BTreeNode rootNode, long key) throws IOException {
        int i = rootNode.numKeys();
        TreeObject obj = new TreeObject(key);
        if(rootNode.isLeaf){
            if(rootNode.numKeys() != 0) {
                //handle the case in which x is a leaf node by inserting key k into x.
                while (i > 0 && obj.compareTo(rootNode.getKey(i - 1)) < 0) {
//                    if(obj.compareTo(rootNode.getKey(i-1)) == 0) break;
//                    if (i == rootNode.keyList.size()) rootNode.keyList.add(new TreeObject(key));
//                    else rootNode.keyList.set(i, rootNode.keyList.get(i-1));
//                    rootNode.keyList.set(i, rootNode.keyList.get(i - 1));
                    i--;
                }

                //if we are still at the end of the list
                if (i == rootNode.keyList.size()) {
                    //check for duplicate
                    if (i > 0) { //if at the end of the list
                        if (obj.compareTo(rootNode.getKey(i-1)) == 0) rootNode.getKey(i-1).incrementFrequency();
                        else rootNode.keyList.add(obj);
                    }
                }
                else if(i == 1 && obj.compareTo(rootNode.getKey(i-1)) == 0) {
                   rootNode.getKey(i-1).incrementFrequency();
                    //else rootNode.keyList.add(obj);
                }
                else if(i == 0 && i != rootNode.keyList.size()){
                    if (i == 0) rootNode.keyList.add(i, obj);
                    else rootNode.keyList.add(i, obj);
                }
                else if(obj.compareTo(rootNode.getKey(i-1)) == 0) rootNode.getKey(i-1).incrementFrequency();
                else if (obj.compareTo(rootNode.getKey(i)) == 0) rootNode.getKey(i).incrementFrequency();
                else rootNode.keyList.add(i, obj);
                rootNode.setNumKeys(rootNode.numKeys() + 1);

            }else {
                rootNode.insertKey(obj, i);
                rootNode.setNumKeys(rootNode.numKeys() + 1);
            }
            //WriteNodeToFile(rootNode, rootNode.getOffset());
            DiskWrite(rootNode, rootNode.getOffset());
        }
        else{
            while(i>=1 && obj.compareTo(rootNode.getKey(i-1)) < 0){
                i--;
            }
            if(i > 0 && obj.compareTo(rootNode.getKey(i-1))==0) {
                rootNode.getKey(i-1).incrementFrequency();
                DiskWrite(rootNode, rootNode.getOffset());
                return;
            }
//            BTreeNode child = null;
            int offset = rootNode.getChild(i);
            BTreeNode child = ReadNodeFromFile(rootNode.getChild(i));

            if(i == 0) child = ReadNodeFromFile(rootNode.children1.get(i));
            else child = ReadNodeFromFile(rootNode.children1.get(i-1));

            if(child.numKeys() == 2*degree-1){

                int j = child.numKeys();
                while(j > 0 && obj.compareTo(child.getKey(j-1)) < 0){
                    j--;
                }
                if(j > 0 && obj.compareTo(child.getKey(j-1)) == 0){
                    child.getKey(i).incrementFrequency();
                    DiskWrite(child, child.getOffset());
                    return;
                }else{
                    BTreeSplitChild1(rootNode,i,child);
                    if(obj.compareTo(rootNode.getKey(i)) > 0){
                        i++;
                    }
                }
            }
//            BTreeInsertNonFull(child,key);
            BTreeNode y = ReadNodeFromFile(rootNode.getChild(i));
            BTreeInsertNonFull(y, key);
        }

    }

//    private void BTreeInsertNonFull(BTreeNode rootNode, long key) throws IOException {
//        int i = rootNode.numKeys();
//        TreeObject obj = new TreeObject(key);
//        if (rootNode.isLeaf) {
//            //if there is more than 0 keys
//            if (rootNode.numKeys() != 0) {
//                //handle the case in which x is a leaf node by inserting key k into x.
//                //scan to the correct position in the arraylist
//                while (i >= 1 && obj.compareTo(rootNode.getKey(i - 1)) < 0) {
////                    if(i == rootNode.keyList.size()) rootNode.insertKey(rootNode.getKey(i - 1), i);
////                    else rootNode.keyList.set(i, rootNode.keyList.get(i-1));
//                    i--;
//                }
//                //if the keys are the same, increment freq instead of inserting
//                if (i == 0)
//                    if (obj.compareTo(rootNode.getKey(i)) == 0) rootNode.getKey(i).incrementFrequency();
////                else if(i == rootNode.numKeys())
////                    if (obj.compareTo(rootNode.getKey(i-1)) == 0) rootNode.getKey(i - 1).incrementFrequency();
////
////                else if (obj.compareTo(rootNode.getKey(i)) == 0) rootNode.getKey(i).incrementFrequency();
////
////                if (i >= rootNode.keyList.size()){
////                    rootNode.keyList.add(obj);
////                }else rootNode.keyList.set(i, obj);
////                rootNode.numKeys++;
////                WriteNodeToFile(rootNode, rootNode.getOffset());
//            }
//            //else add the key at this position because it's now greater than all the values to the left of it
//            else {
//                rootNode.insertKey(obj, i);
//                rootNode.setNumKeys(rootNode.numKeys() + 1);
//            }
//            //then write the node to - the file
//            WriteNodeToFile(rootNode, rootNode.getOffset());
//        }
//        //else if it's not a leaf
//        else {
//            //scan to the correct position in the arraylist
//            while (i >= 1 && obj.compareTo(rootNode.getKey(i - 1)) < 0) {
//                i--;
//            }
//            if (i == 0) {
//                if (obj.compareTo(rootNode.getKey(i - 1)) == 0) {
//                    rootNode.getKey(i).incrementFrequency();
//                    WriteNodeToFile(rootNode, rootNode.getOffset());
//                    return;
//                }
//            }
//
////            if(i > 0 && obj.compareTo(rootNode.getKey(i-1))==0) {
////                rootNode.getKey(i-1).incrementFrequency();
////                WriteNodeToFile(rootNode, rootNode.getOffset());
////                return;
////            }
//
//            int offset = rootNode.getChild(i);
//            BTreeNode child = ReadNodeFromFile(rootNode.getChild(i));
////            if(i == 0) child = ReadNodeFromFile(rootNode.children1.get(i));
////            else child = ReadNodeFromFile(rootNode.children1.get(i-1));
//
//            if (child.numKeys() == 2 * degree - 1) {
//                int j = child.numKeys();
//                while (j > 0 && obj.compareTo(child.getKey(j - 1)) < 0) {
//                    j--;
//                }
//                if (j > 0 && obj.compareTo(child.getKey(j - 1)) == 0) {
//                    child.getKey(j - 1).incrementFrequency();
//                    WriteNodeToFile(child, child.getOffset());
//                    return;
//                } else {
//                    BTreeSplitChild1(rootNode, i, child);
//                    if (obj.compareTo(rootNode.getKey(i)) > 0) {
//                        i++;
//                    }
//                }
//            }
//
//            BTreeNode y = ReadNodeFromFile(rootNode.getChild(i));
//            BTreeInsertNonFull(y, key);
//        }
//    }
    //raf write node to a specific offset in binary file
//    private void WriteNodeToFile(BTreeNode node, int offset) {
//        int childIndex=0;
//        try {
//            raf.seek(offset);
//            //write node meta
//            raf.writeBoolean(node.isLeaf);
//            raf.writeInt(node.numKeys());
//            raf.writeInt(node.offset);
//            //write other data
//            raf.writeInt(node.parent);
//            for (childIndex = 0; childIndex < 2 * degree - 1; childIndex++) {
//                if(childIndex < node.numKeys()  && !node.isLeaf){
//                    raf.writeInt(node.getChild(childIndex));
//                }else if(childIndex >= node.numKeys() + 1 || node.isLeaf){
//                    //raf.seek(raf.getFilePointer() + 4);
//                    raf.writeInt(0);
//                }
//                if(childIndex < node.numKeys()) {
//                    raf.writeLong(node.keyList.get(childIndex).dnaString);
//                    raf.writeInt(node.keyList.get(childIndex).frequency);
//                }
//                else if(childIndex >= node.numKeys() || node.isLeaf){
//                    raf.writeLong(0);
//                }
//            }
//            if(childIndex == node.children1.size() && !node.isLeaf){
//                raf.writeInt(node.children1.get(childIndex-1));
//            }
//        }catch(IOException e){
//            e.printStackTrace();
//        }
//    }
//
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
    private void WriteNodeToFile(BTreeNode node, int offset) {
        int childIndex=0;
        try {
            raf.seek(offset);
            //write node meta
            raf.writeBoolean(node.isLeaf);
            raf.writeInt(node.numKeys());
            raf.writeInt(node.getOffset());
            //write other data
            raf.writeInt(node.getParent());
            for (childIndex = 0; childIndex < 2 * degree - 1; childIndex++) {
                if(childIndex < node.numKeys()  && !node.isLeaf){
                    raf.writeInt(node.getChild(childIndex));
                }else if(childIndex >= node.numKeys() + 1 || node.isLeaf){
//                    raf.seek(raf.getFilePointer() + 4);
                    raf.writeInt(0);
                }
                if(childIndex < node.numKeys()) {
                    raf.writeLong(node.getKey(childIndex).getDnaString());
                    raf.writeInt(node.getKey(childIndex).getFrequency());
                }
                else if(childIndex >= node.numKeys() || node.isLeaf){
                    raf.writeLong(0);
                }
            }
            if(childIndex == node.numKeys()-1 && !node.isLeaf){
                raf.writeInt(node.getChild(childIndex));
            }
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

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //allocate the full node size, not the variable (NOT size of subsequence)
    private BTreeNode AllocateNode() throws IOException {
        //long offset = 13;
        //long offset = 12;
	    raf.seek(insertOffset);
	    BTreeNode n = new BTreeNode();
	    n.isLeaf = true;
	    n.setNumKeys(0);
	    n.setOffset(insertOffset);
        DiskWrite(n, n.getOffset());
        return n;
    }

    //returns optimal degree
    public static int optimal() {
    	int optimum = 4096;
    	int pointerSize = 4, objectSize  = 12, metadataSize = 9;
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
            //node metadata
            x.isLeaf = raf.readBoolean();   //1 byte
            x.setNumKeys(raf.readInt());    //4 bytes
            x.offset = raf.readInt();       //4 bytes
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
                    x.keyList.add(y);
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
        int i = 0;
        TreeObject y = new TreeObject(key);
        while ((i < x.numKeys()) && (y.compareTo(x.getKey(i))) > 0){
            i++;
        }
        if ((i < x.numKeys()) && (y.compareTo(x.getKey(i))) == 0) return x.getKey(i);
        else if (!x.isLeaf) return null;
        else return BTreeSearch(ReadNodeFromFile(x.getChild(i)), key);

    }

    //initialize bin file for BTreeCreate constructor
    public File BTreeFileCreate(String loadedGBK,int seqLength,int degree){
        try {
            binFile = new File(loadedGBK+".btree.data."+seqLength+"."+degree);
        }
        catch (Exception e){
            System.err.println("Error creating .gbk.btree");
        }
        return binFile;
    }
    //Needed to print when debug level is 0
    public void inOrderWriteFile(BTreeNode node,PrintWriter writer,int sequence){
        RAM ram = new RAM();
        for(int i = 0; i < node.numKeys(); i++){
            writer.print(ram.convertLongtoString(node.getKey(i).dnaString,sequence));
            writer.println(": " + node.getKey(i).frequency);
        }
        if(!node.isLeaf){
            for(int j = 0; j < node.numKeys(); j++){
                BTreeNode x = ReadNodeFromFile(node.children1.get(j));
                inOrderWriteFile(x, writer, sequence);
                if(j < node.numKeys()){
                    writer.println(ram.convertLongtoString(node.getKey(j).dnaString,sequence));
                    writer.println(": " + node.getKey(j).frequency);
                }
            }
        }
    }



    public void flushCache(){
        if(treeCache != null){
            for (BTreeNode node : treeCache) WriteNodeToFile(node, node.getOffset());
        }
    }

    public void DiskWrite(BTreeNode x, int offset){
        if (treeCache != null){
            BTreeNode node = treeCache.addObject(x);
            if (node != null){
                WriteNodeToFile(node, node.getOffset());
            }
        }else{
            WriteNodeToFile(x, offset);
        }
    }


}