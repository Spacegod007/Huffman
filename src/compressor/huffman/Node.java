package compressor.huffman;

import java.io.Serializable;

class Node extends Tree implements Serializable
{
    private final Tree leftTree;
    private final Tree rightTree;

    public Node(Tree leftTree, Tree rightTree)
    {
        super(leftTree.getFrequency() + rightTree.getFrequency());
        this.leftTree = leftTree;
        this.rightTree = rightTree;
    }

    public Tree getLeftTree()
    {
        return leftTree;
    }

    public Tree getRightTree()
    {
        return rightTree;
    }
}
