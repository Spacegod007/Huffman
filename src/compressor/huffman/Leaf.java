package compressor.huffman;

import java.io.Serializable;

public class Leaf extends Tree implements Serializable
{
    private final char value;

    public Leaf(int frequency, char value)
    {
        super(frequency);
        this.value = value;
    }

    public char getValue()
    {
        return value;
    }
}
