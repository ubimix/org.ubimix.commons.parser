/**
 * 
 */
package org.ubimix.commons.parser.stream;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import org.ubimix.commons.parser.UnboundedCharStream;
import org.ubimix.commons.parser.UnboundedCharStream.ICharLoader;

/**
 * @author kotelnikov
 */
public class StreamCharLoader implements UnboundedCharStream.ICharLoader {

    private Reader fReader;

    public StreamCharLoader(InputStream input) throws IOException {
        this(new InputStreamReader(input, "UTF-8"));
    }

    public StreamCharLoader(Reader reader) {
        fReader = reader;
    }

    /**
     * @see org.ubimix.commons.parser.CharStream.ICharLoader#readNext()
     */
    public int readNext() {
        try {
            return fReader.read();
        } catch (IOException e) {
            return -1;
        }
    }

}
