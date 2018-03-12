package compressor;

import java.io.File;
import java.io.IOException;

public interface IEncoder<T>
{
    void encode(T compressable, File location) throws IOException;
    T decode(File file, File keyFile) throws IOException, ClassNotFoundException;
}