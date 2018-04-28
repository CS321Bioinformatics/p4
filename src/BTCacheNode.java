package src;

public class BTCacheNode {
    private int offset;
    private BTreeNode nodeData;

    public BTCacheNode(BTreeNode data, int x){
        offset = x;
        nodeData = data;
    }

    public int getOffset() {
        return offset;
    }

    public BTreeNode getNodeData(){
        return nodeData;
    }
}
