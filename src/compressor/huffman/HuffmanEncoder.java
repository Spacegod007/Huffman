package compressor.huffman;

import compressor.IEncoder;

import java.io.*;
import java.nio.file.Files;
import java.util.*;

/**
 * A class which encodes/decodes a message
 */
public class HuffmanEncoder implements IEncoder<String>
{
    @Override
    public void encode(String compressible, File location) throws IOException
    {
        Tree tree = buildTree(compressible);
        Map<Character, String> codeMapping = getTreeCodes(tree);
        BitSet bitSet = encodeMessage(codeMapping, compressible);

        writeToFile(location, tree, bitSet);
    }

    @Override
    public String decode(File file, File keyFile) throws IOException
    {
        BitSet bitSet = readFileToBitSet(file);
        Tree key = readKeyFile(keyFile);

        return decodeMessage(bitSet, key);
    }

    /**
     * Builds a tree of nodes and leaves to make codes
     * @param text on which the tree is based
     * @return a tree containing nodes and leaves
     */
    private Tree buildTree(String text)
    {
        List<Character> characters = getCharacterListFromString(text);
        Set<Character> characterSet = new HashSet<>(characters);

        Queue<Tree> treeQueue = new PriorityQueue<>();

        characterSet.forEach(character -> treeQueue.add(new Leaf(Collections.frequency(characters, character), character)));

        while (treeQueue.size() > 1)
        {
            treeQueue.add(new Node(treeQueue.remove(), treeQueue.remove()));
        }

        return treeQueue.remove();
    }

    /**
     * Converts a string to a list of characters
     * @param string to create a list out of
     * @return an abstract list object bound to the string
     */
    private List<Character> getCharacterListFromString(final String string) {
        return new AbstractList<Character>() {
            public int size() { return string.length(); }
            public Character get(int index) { return string.charAt(index); }
        };
    }

    /**
     * Creates codes out of the tree with characters
     * @param tree to create a code mapping out of
     * @return a mapping of characters bound to a code written as string
     */
    private Map<Character,String> getTreeCodes(Tree tree)
    {
        Map<Character, String> map = new HashMap<>();
        generateCode(tree, map, "");
        return map;
    }

    /**
     * Generates a code specific for the tree object
     * @param tree location from where decisions are made
     * @param map to bind the character to a bit-combination
     * @param bitString the combination of bits found
     */
    private void generateCode(Tree tree, Map<Character, String> map, String bitString)
    {
        if (tree instanceof Leaf)
        {
            map.put(((Leaf) tree).getValue(), bitString);
        }
        else
        {
            Node node = (Node) tree;
            generateCode(node.getLeftTree(), map, bitString + '1');
            generateCode(node.getRightTree(), map, bitString + '0');
        }
    }

    /**
     * Encodes the message according to the bit combination
     * @param codeMapping character to bit-combination code mapping
     * @param compressible the message which is going to be encrypted
     * @return a BitSet object containing the encoded message
     */
    private BitSet encodeMessage(Map<Character, String> codeMapping, String compressible)
    {
        int position = 0;
        BitSet bitSet = new BitSet();

        for (char character : compressible.toCharArray())
        {
            String s = codeMapping.get(character);
            for (char c : s.toCharArray())
            {
                if (c == '1')
                {
                    bitSet.set(position);
                }

                position++;
            }
        }

        return bitSet;
    }

    /**
     * Writes the encrypted message to a file and creates a key file
     * @param file to write the encrypted message to
     * @param tree to create a key file out of
     * @param bitSet to write in the encrypted file
     * @throws IOException if something goes wrong while writing the file
     */
    private void writeToFile(File file, Tree tree, BitSet bitSet) throws IOException
    {
        File keyFile = new File(file.getName() + ".key");

        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(keyFile))))
        {
            objectOutputStream.writeObject(tree);
        }

        Files.write(file.toPath(), bitSet.toByteArray());
    }

    /**
     * Reads the key file
     * @param keyFile to get the tree out of
     * @return a Tree object which is linked to the encrypted file
     * @throws IOException if something goes wrong while reading the key file
     */
    private Tree readKeyFile(File keyFile) throws IOException
    {
        try (ObjectInputStream objectInputStream = new ObjectInputStream(new BufferedInputStream(new FileInputStream(keyFile))))
        {
            Object object = objectInputStream.readObject();

            if (object instanceof Tree)
            {
                return (Tree) object;
            }
        }
        catch (ClassNotFoundException ignored)
        { /* result will be handled outside of this class due to thrown exception */ }

        throw new InvalidClassException("Incorrect object found, object is not an instance of Tree");
    }

    /**
     * Reads the BitSet file to get the encrypted data
     * @param file to read the encrypted data out of
     * @return a BitSet object containing the encrypted data
     * @throws IOException if something goes wrong while reading the file
     */
    private BitSet readFileToBitSet(File file) throws IOException
    {
        return BitSet.valueOf(Files.readAllBytes(file.toPath()));
    }

    /**
     * decodes the encoded message
     * @param bitSet containing the encoded message
     * @param key which links specific bit combinations to a character
     * @return the decoded message
     */
    private String decodeMessage(BitSet bitSet, Tree key)
    {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < bitSet.length();)
        {
            Tree temp = key;

            while (temp instanceof Node)
            {
                Node node = (Node) temp;
                temp = bitSet.get(i) ? node.getLeftTree() : node.getRightTree();

                i++;
            }

            builder.append(((Leaf) temp).getValue());
        }

        return builder.toString();
    }
}

