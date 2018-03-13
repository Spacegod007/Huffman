package compressor.huffman;

import compressor.IEncoder;

import java.io.*;
import java.nio.file.Files;
import java.util.*;

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

    private List<Character> getCharacterListFromString(final String string) {
        return new AbstractList<Character>() {
            public int size() { return string.length(); }
            public Character get(int index) { return string.charAt(index); }
        };
    }

    private Map<Character,String> getTreeCodes(Tree tree)
    {
        Map<Character, String> map = new HashMap<>();
        generateCode(tree, map, "");
        return map;
    }

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

    private void writeToFile(File file, Tree tree, BitSet bitSet) throws IOException
    {
        File keyFile = new File(file.getName() + ".key");

        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(keyFile))))
        {
            objectOutputStream.writeObject(tree);
        }

        Files.write(file.toPath(), bitSet.toByteArray());
    }

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

    private BitSet readFileToBitSet(File file) throws IOException
    {
        return BitSet.valueOf(Files.readAllBytes(file.toPath()));
    }

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

