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

    private static IEncoder<String> huffmanEncodder;

    private static String testText10K;
    private static String testText1M;

    @BeforeAll
    static void setUp()
    {
        huffmanEncodder = new HuffmanEncoder();

        testText10K = obtainText(10000);
        testText1M = obtainText(1000000);
    }

    @TestFactory
    static String obtainText(int length)
    {
        Random random = new Random(System.currentTimeMillis());
        String alphabet = "qwertyuiopasdfghjklzxcvbnm QWERTYUIOPASDFGHJKLZXCVBNM1234567890\n,.";
        StringBuilder textBuilder = new StringBuilder();

        for (int i = 0; i < length; i++)
        {
            textBuilder.append(alphabet.charAt(random.nextInt(alphabet.length())));
        }

        return textBuilder.toString();
    }

    private long averageTestTime(Long[] times)
    {
        long time = 0;

        for (Long aLong : times)
        {
            time += aLong;
        }

        return time / times.length;
    }

    @Test
    void encode() throws Exception
    {
        int testAmount = 7;

        Long[] times = new Long[testAmount];
        for (int i = 0; i < testAmount; i++)
        {
            long beginTime = System.nanoTime();
            huffmanEncodder.encode(testText10K, new File("10k"));
            times[i] = System.nanoTime() - beginTime;
        }

        long time10k = averageTestTime(times);
        LOGGER.log(Level.INFO, "10k encode average time in nanoseconds: " + time10k);

        times = new Long[testAmount];
        for (int i = 0; i < testAmount; i++)
        {
            long beginTime = System.nanoTime();
            huffmanEncodder.encode(testText1M, new File("1m"));
            times[i] = System.nanoTime() - beginTime;
        }

        long time1m = averageTestTime(times);
        LOGGER.log(Level.INFO, "1m encode average time in nanoseconds: " + time1m);
    }

    @Test
    void decode() throws Exception
    {
        int testAmount = 7;

        Long[] times = new Long[testAmount];
        for (int i = 0; i < testAmount; i++)
        {
            long beginTime = System.nanoTime();
            huffmanEncodder.decode(new File("10k"), new File("10k.key"));
            times[i] = System.nanoTime() - beginTime;
        }

        long time10k = averageTestTime(times);
        LOGGER.log(Level.INFO, "10k decode average time in nanoseconds: " + time10k);

        times = new Long[testAmount];
        for (int i = 0; i < testAmount; i++)
        {
            long beginTime = System.nanoTime();
            huffmanEncodder.decode(new File("1m"), new File("1m.key"));
            times[i] = System.nanoTime() - beginTime;
        }

        long time1m = averageTestTime(times);
        LOGGER.log(Level.INFO, "1m decode average time in nanoseconds: " + time1m);
    }
}