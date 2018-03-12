package compressor.huffman;

import java.io.Serializable;

public class Tree implements Comparable<Tree>, Serializable
{
    private final int frequency;

    public Tree(int frequency)
    {
        this.frequency = frequency;
    }

    @Override
    public int compareTo(Tree other)
    {
        return this.frequency - other.frequency;
    }

    public int getFrequency()
    {
        return frequency;
    }
}
