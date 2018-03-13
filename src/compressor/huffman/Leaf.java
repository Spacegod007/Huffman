package compressor.huffman;

import java.io.Serializable;

/**
 * A tree object which contains a character value
 */
class Leaf extends Tree implements Serializable
{
    /**
     * Value of the leaf
     */
    private final char value;

    /**
     * Constructs a leaf object
     * @param frequency of the character
     * @param value is the character linked
     */
    public Leaf(int frequency, char value)
    {
        super(frequency);
        this.value = value;
    }

    /**
     * Gets the character linked to this leaf
     * @return the character linked to this leaf
     */
    public char getValue()
    {
        return value;
    }
}
