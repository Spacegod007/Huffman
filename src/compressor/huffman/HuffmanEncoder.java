package compressor.huffman;

import compressor.IEncoder;

import java.io.*;
import java.nio.file.Files;
import java.util.*;

public class HuffmanEncoder implements IEncoder<String>
{
    @Override
    public void encode(String compressable, File location) throws IOException
    {
        Tree tree = buildTree(compressable);
        Map<Character, String> codeMapping = getTreeCodes(tree);
        BitSet bitSet = encodeMessage(codeMapping, compressable);

        writeToFile(location, tree, bitSet);
    }

    @Override
    public String decode(File file, File keyfile) throws IOException
    {
        BitSet bitSet = readFileToBitSet(file);
        Tree key = readKeyFile(keyfile);

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
            Tree node1 = treeQueue.remove();
            Tree node2 = treeQueue.remove();

            treeQueue.add(new Node(node1, node2));
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

    private BitSet encodeMessage(Map<Character, String> codeMapping, String compressable)
    {
        int position = 0;
        BitSet bitSet = new BitSet();

        for (char character : compressable.toCharArray())
        {
            String s = codeMapping.get(character);
            for (char c : s.toCharArray())
            {
                if (c == '1')
                {
                    bitSet.set(position, true);
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

    private Tree readKeyFile(File keyfile) throws IOException
    {
        try (ObjectInputStream objectInputStream = new ObjectInputStream(new BufferedInputStream(new FileInputStream(keyfile))))
        {
            Object object = objectInputStream.readObject();

            if (object instanceof Tree)
            {
                return (Tree) object;
            }
        }
        catch (ClassNotFoundException ignored)
        { }

        throw new InvalidClassException("Incorrect object found, object is not an instance of Tree");
    }

    private BitSet readFileToBitSet(File file) throws IOException
    {
        return BitSet.valueOf(Files.readAllBytes(file.toPath()));
    }

    private String decodeMessage(BitSet bitSet, Tree key)
    {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < bitSet.length() - 1;)
        {
            Tree temp = key;

            while (temp instanceof Node)
            {
                Node node = (Node) temp;
                if (!bitSet.get(i))
                {
                    temp = node.getLeftTree();
                }
                else
                {
                    temp = node.getRightTree();
                }

                i++;
            }


            builder.append(((Leaf) key).getValue());
        }

        return builder.toString();
    }
}