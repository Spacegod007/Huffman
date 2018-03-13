package compressor;

import compressor.huffman.HuffmanEncoder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;

class IEncoderFun
{
    private static IEncoder<String> encoder;

    private static String text;

    @BeforeAll
    static void setup()
    {
        encoder = new HuffmanEncoder();
        text = "ab";
    }

    @Test
    void testCode() throws Exception
    {
        encode();
        decode();
    }

    void encode() throws Exception
    {
        encoder.encode(text, new File("JustForFun"));
    }

    void decode() throws Exception
    {
        String message = encoder.decode(new File("JustForFun"), new File("JustForFun.key"));

        System.out.println(message);
    }

}