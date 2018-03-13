package compressor;

import compressor.huffman.HuffmanEncoder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

import java.io.File;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

class IEncoderPerformanceTest
{
    private static final Logger LOGGER = Logger.getLogger(IEncoderPerformanceTest.class.getName());

    private static HuffmanEncoder huffmanCompressor;

    private static String testText10K;
    private static String testText1M;

    @BeforeAll
    static void setUp()
    {
        huffmanCompressor = new HuffmanEncoder();

        testText10K = textObtainer(10000);
        testText1M = textObtainer(1000000);
    }

    @TestFactory
    static String textObtainer(int length)
    {
        Random random = new Random(System.currentTimeMillis());
        String alphabet = "qwertyuiopasdfghjklzxcvbnm";
        StringBuilder textBuilder = new StringBuilder();

        for (int i = 0; i < length; i++)
        {
            textBuilder.append(alphabet.charAt(random.nextInt(alphabet.length())));
        }

        return textBuilder.toString();
    }

    @Test
    void encode() throws Exception
    {
        huffmanCompressor.encode("this is a random test text which got manually written", new File("testText"));
        huffmanCompressor.encode(testText10K, new File("10k"));
        huffmanCompressor.encode(testText1M, new File("1m"));
    }

    @Test
    void decode() throws Exception
    {
        String resultTestText = huffmanCompressor.decode(new File("testText"), new File("testText.key"));
        String result10k = huffmanCompressor.decode(new File("10k"), new File("10k.key"));
        String result1m = huffmanCompressor.decode(new File("1m"), new File("1m.key"));

        LOGGER.log(Level.INFO, "result test text: " + resultTestText);
        LOGGER.log(Level.INFO, "result 10k text: " + result10k);
        LOGGER.log(Level.INFO, "result 1m text: " + result1m);
    }
}