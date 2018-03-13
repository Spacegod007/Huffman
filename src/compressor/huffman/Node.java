package compressor.huffman;

import java.io.Serializable;

/**
 * A tree object which contains two other tree objects
 */
class Node extends Tree implements Serializable
{
    /**
     * left branch of this node
     */
    private final Tree leftTree;

    /**
     * right branch of this node
     */
    private final Tree rightTree;

    /**
     * constructs a node object
     * @param leftTree of the left branch
     * @param rightTree of the right branch
     */
    public Node(Tree leftTree, Tree rightTree)
    {
        super(leftTree.getFrequency() + rightTree.getFrequency());
        this.leftTree = leftTree;
        this.rightTree = rightTree;
    }

    /**
     * Gets the left tree branch
     * @return the left tree branch
     */
    public Tree getLeftTree()
    {
        return leftTree;
    }

    /**
     * Gets the right tree branch
     * @return the right tree branch
     */
    public Tree getRightTree()
    {
        return rightTree;
    }
}
