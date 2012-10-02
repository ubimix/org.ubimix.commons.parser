/**
 * 
 */
package org.ubimix.commons.parser;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import junit.framework.TestCase;

import org.ubimix.commons.parser.CharStream;
import org.ubimix.commons.parser.stream.StreamCharLoader;

/**
 * @author kotelnikov
 */
public class ComparativeCharStreamTest extends TestCase {

    /**
     * @param name
     */
    public ComparativeCharStreamTest(String name) {
        super(name);
    }

    protected String getPackageResourceName(String localName) {
        localName = localName.replace('\\', '/');
        if (!localName.startsWith("/"))
            localName = "/" + localName;
        String fullName = "/"
            + getClass().getPackage().getName().replace('.', '/')
            + localName;
        return fullName;
    }

    /**
     * @param resourceName
     * @return
     */
    protected InputStream getResourceAsStream(String resourceName) {
        InputStream in = getClass().getResourceAsStream(resourceName);
        return in;
    }

    /**
     * @param resourceName
     * @return
     * @throws IOException
     * @throws UnsupportedEncodingException
     */
    protected String readResourceAsString(String resourceName)
        throws IOException,
        UnsupportedEncodingException {
        String str;
        InputStream in = getResourceAsStream(resourceName);
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buf = new byte[10 * 1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            str = new String(out.toByteArray(), "UTF-8");
        } finally {
            in.close();
        }
        return str;
    }

    public void testStreams() throws UnsupportedEncodingException, IOException {
        String resourceName = getPackageResourceName("test.wiki");
        InputStream input = getResourceAsStream(resourceName);
        try {
            CharStream first = new CharStream(new StreamCharLoader(input));
            String str = readResourceAsString(resourceName);
            CharStream second = new CharStream(str);
            int counter = 0;
            while (true) {
                char firstCh = first.getChar();
                char secondCh = second.getChar();
                assertEquals(firstCh, secondCh);
                assertEquals(first.getPointer(), second.getPointer());
                boolean a = first.incPos();
                boolean b = second.incPos();
                assertEquals(a, b);
                if (!a)
                    break;
                counter++;
            }
            assertEquals(str.length(), counter);
        } finally {
            input.close();
        }
    }

}
