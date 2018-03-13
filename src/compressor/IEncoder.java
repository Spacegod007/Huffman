package compressor;

import java.io.File;
import java.io.IOException;

public interface IEncoder<T>
{
    /**
     * encodes a file and creates a keyFile in the process
     * @param compressible the to be compressed object
     * @param location where the compressed object will be saved
     * @throws IOException if something goes wrong while writing the data
     */
    void encode(T compressible, File location) throws IOException;

    /**
     * decodes a file by use of a keyfile
     * @param file to be decoded
     * @param keyFile used to decode the file
     * @return the encoded object
     * @throws IOException if something goes wrong while reading the files
     */
    T decode(File file, File keyFile) throws IOException;
}
