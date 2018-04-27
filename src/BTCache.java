package src;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;

public class BTCache implements Iterable<BTreeNode>
{
    private LinkedList<BTreeNode> list;
    private int size1;

    public BTCache(int inputSize) {
        list = new LinkedList<T>();
        size1 = inputSize;
    }

    /**
     * This method returns the object being searched for
     * @param object being searched for
     * @return the object
     */
    public T getObject(T object){
        return object;
    }
    /**
     * Searches for object in cache and returns true if found
     * @param object
     * @return boolean true if found
     */
    public boolean findObject(T object) {
        return list.contains(object);
    }
    /**
     * Moves a given object to the top of the cache
     * @param object
     */
    public void moveObject(T object) {
        list.remove(object);
        list.addFirst(object);
    }
    /**
     * Removes the given object from the cache
     * @param object
     */
    public void removeObject(T object) {
        list.remove(object);
    }
    /**
     * Removes the last object from the cache
     */
    public void removeLast() {
        list.removeLast();
    }
    /**
     * Adds a given object to the cache, checks capacity first
     * @param object
     */
    public void addObject(T object) {
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