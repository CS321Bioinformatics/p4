package src;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;

public class BTCache implements Iterable<BTreeNode>
{
    private LinkedList<BTreeNode> list;
    private int size1;

    public BTCache(int inputSize) {
        list = new LinkedList<BTreeNode>();
        size1 = inputSize;
    }


    public BTreeNode getObject(BTreeNode object){
        return object;
    }


    public boolean findObject(BTreeNode object) {
        return list.contains(object);
    }


    public void moveObject(BTreeNode object) {
        list.remove(object);
        list.addFirst(object);
    }


    public void removeObject(BTreeNode object) {
        list.remove(object);
    }


    public void removeLast() {
        list.removeLast();
    }


    public void addObject(BTreeNode object) {
        if(list.size()>=size1) {
            list.addFirst(object);
            list.removeLast();
        }else {
            moveObject(object);
        }
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