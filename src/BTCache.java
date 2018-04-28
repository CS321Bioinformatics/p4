package src;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;

public class BTCache implements Iterable<BTreeNode>
{
    private LinkedList<BTreeNode> list;
    private int size1;
    private int hits, misses;

    public BTCache(int inputSize) {
        list = new LinkedList<BTreeNode>();
        size1 = inputSize;
    }





    public boolean findObject(BTreeNode object) {
        return list.contains(object);
    }


    public void moveObject(BTreeNode object) {
        list.remove(object);
        list.addFirst(object);
    }

    public void incHits(){hits++;}
    public int getHits(){return hits;}
    public void incMisses(){misses++;}
    public int getMisses(){return misses;}
    public int totalRefs(){return hits + misses;}
    public double hitRatio(){return (((double)getHits()) / totalRefs());}
    public void removeObject(BTreeNode object) {
        list.remove(object);
    }
    public BTreeNode getObject(int fileOffset){
        for(BTreeNode x : list){
            if(x.getOffset() == fileOffset){
                moveObject(x);
                incHits();
                return x;
            }
        }
        incMisses();
        return null;
    }


    public BTreeNode removeLast() {return list.removeLast();}


    public BTreeNode addObject(BTreeNode object) {
        BTreeNode removedNode = null;
        if(list.size()>=size1) {
            removedNode = removeLast();
        }
            list.addFirst(object);

        return removedNode;

    }
    /**
     * Emptys the cache
     */
    public void clearCache() {
        list = null;
    }

    public Iterator<BTreeNode> iterator(){
        return list.iterator();
    }
}