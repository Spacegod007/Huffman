package compressor.huffman;

import java.io.Serializable;

/**
 * A generic tree object
 */
public abstract class Tree implements Comparable<Tree>, Serializable
{
    /**
     * Frequency this node will be used
     */
    private final int frequency;

    /**
     * Constructs a tree object
     * @param frequency of the tree object
     */
    Tree(int frequency)
    {
        this.frequency = frequency;
    }

    @Override
    public int compareTo(Tree other)
    {
        return this.frequency - other.frequency;
    }

    /**
     * Gets the frequency of this tree object
     * @return the frequency of this tree object
     */
    int getFrequency()
    {
        return frequency;
    }
}
