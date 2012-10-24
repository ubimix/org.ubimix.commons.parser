/**
 * 
 */
package org.ubimix.commons.parser;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import org.ubimix.commons.parser.stream.StreamCharLoader;

import junit.framework.TestCase;

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

    protected int compareStreams(ICharStream first, ICharStream second) {
        int counter = 0;
        while (!first.isTerminated() && !second.isTerminated()) {
            char firstCh = first.getChar();
            char secondCh = second.getChar();
            assertEquals(firstCh, secondCh);
            assertEquals(first.getPointer(), second.getPointer());
            counter++;
            boolean a = first.incPos();
            boolean b = second.incPos();
            assertEquals(a, b);
        }
        assertEquals(first.isTerminated(), second.isTerminated());
        return counter;
    }

    protected String getPackageResourceName(String localName) {
        localName = localName.replace('\\', '/');
        if (!localName.startsWith("/")) {
            localName = "/" + localName;
        }
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

    public void testBinaryStreams()
        throws UnsupportedEncodingException,
        IOException {
        String resourceName = getPackageResourceName("test.wiki");
        InputStream input = getResourceAsStream(resourceName);
        try {
            ICharStream first = new UnboundedCharStream(new StreamCharLoader(
                input));
            String str = readResourceAsString(resourceName);
            ICharStream second = new CharStream(str);
            int counter = compareStreams(first, second);
            assertEquals(str.length(), counter);
        } finally {
            input.close();
        }
    }

    public void testStringStreams()
        throws UnsupportedEncodingException,
        IOException {
        String resourceName = getPackageResourceName("test.wiki");
        InputStream input = getResourceAsStream(resourceName);
        try {
            String str = readResourceAsString(resourceName);
            ICharStream first = new CharStream(str);
            ICharStream second = new CharStream(str);
            int counter = compareStreams(first, second);
            assertEquals(str.length(), counter);
        } finally {
            input.close();
        }
    }

}
