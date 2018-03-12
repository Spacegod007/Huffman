package compressor.huffman;

public class Leaf extends Tree
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
