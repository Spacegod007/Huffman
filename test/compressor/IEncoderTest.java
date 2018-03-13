package compressor;

import compressor.huffman.HuffmanEncoder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class IEncoderTest
{
    private static IEncoder<String> encoder;

    private static String text1;
    private static File testFile1;
    private static File testKeyFile1;

    private static String text2;
    private static File testFile2;
    private static File testKeyFile2;

    @BeforeAll
    static void beforeAll()
    {
        text1 = "thIs iS tesT 1";
        testFile1 = new File("file1");
        testKeyFile1 = new File("file1.key");

        text2 = "anoTher Test tExt nuM2";
        testFile2 = new File("file2");
        testKeyFile2 = new File("file2.key");

        encoder = new HuffmanEncoder();
    }

    @Test
    void encodingTest() throws Exception
    {
        encode();
        decode();
    }

    private void encode() throws Exception
    {
        encoder.encode(text1, testFile1);
        encoder.encode(text2, testFile2);
    }
    
    private void decode() throws Exception
    {
        String decode1 = encoder.decode(testFile1, testKeyFile1);
        String decode2 = encoder.decode(testFile2, testKeyFile2);

        assertEquals(text1, decode1, "The read text is not equal to the written text");
        assertEquals(text2, decode2, "The read text is not equal to the written text");
    }
}
