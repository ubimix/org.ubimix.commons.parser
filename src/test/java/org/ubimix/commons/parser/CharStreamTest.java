/**
 * 
 */
package org.ubimix.commons.parser;

import java.io.IOException;

import junit.framework.TestCase;

import org.ubimix.commons.parser.CharStream.Marker;

/**
 * @author kotelnikov
 */
public class CharStreamTest extends TestCase {

    /**
     * @param name
     */
    public CharStreamTest(String name) {
        super(name);
    }

    /**
     * @return
     */
    protected CharStream newCharStream(String str) {
        return new CharStream(str);
    }

    public void test() throws IOException {
        CharStream stream = newCharStream("abcd");
        test(stream, "a");
        {
            Marker bMarker = stream.markPosition();
            assertEquals("Marker(1[0:1]:'')", bMarker.toString());
            // test(stream, "b", "c", "d", null);
            // test(stream, (String) null);
            bMarker.close(true);
        }
        test(stream, "b");
        {
            Marker cMarker = stream.markPosition();
            assertEquals("Marker(2[0:2]:'')", cMarker.toString());
            test(stream, "c", "d", null);
            test(stream, (String) null);
            assertEquals("Marker(2[0:2]:'cd')", cMarker.toString());
            cMarker.close(true);
        }
        test(stream, "c", "d", null);
    }

    private void test(CharStream stream, String... chars) throws IOException {
        for (String ch : chars) {
            if (ch == null) {
                char test = stream.getChar();
                assertEquals('\0', test);
                test = stream.getChar();
                assertEquals('\0', test);
            } else {
                char test = stream.getChar();
                assertEquals(ch, "" + test);
                test = stream.getChar();
                assertEquals(ch, "" + test);
                stream.incPos();
            }
        }
    }

    public void test1() throws IOException {
        CharStream stream = newCharStream("abcdef");
        test(stream, "a", "b");
        {
            Marker cMarker = stream.markPosition();
            test(stream, "c", "d");
            {
                Marker eMarker = stream.markPosition();
                test(stream, "e", "f", null);
                eMarker.close(true);
            }
            test(stream, "e", "f", null);
            test(stream, (String) null);
            cMarker.close(true);
        }
        test(stream, "c", "d");
        {
            Marker eMarker = stream.markPosition();
            test(stream, "e", "f", null);
            test(stream, (String) null);
            eMarker.close(true);
        }
        test(stream, "e", "f", null);
    }

    public void testMarker0() throws IOException {
        CharStream stream = newCharStream("ab");
        test(stream, "a");
        Marker marker = stream.markPosition();
        test(stream, "b", null);
        marker.close(true);
        test(stream, "b", null);
    }

    public void testMarker1() throws IOException {
        CharStream stream = newCharStream("abcd");
        test(stream, "a");
        {
            Marker aMarker = stream.markPosition();
            test(stream, "b");
            {
                Marker bMarker = stream.markPosition();
                test(stream, "c");
                {
                    Marker dMarker = stream.markPosition();
                    test(stream, "d");
                    {
                        Marker nullMarker = stream.markPosition();
                        test(stream, (String) null);
                        test(stream, (String) null);
                        nullMarker.close(true);
                    }
                    test(stream, (String) null);
                    dMarker.close(true);
                }
                test(stream, "d", null);
                bMarker.close(true);
            }
            test(stream, "c", "d", null);
            aMarker.close(true);
        }
        test(stream, "b", "c", "d", null);
    }

    public void testMarker2() throws IOException {
        CharStream stream = newCharStream("abcdef");
        test(stream, "a", "b", "c");
        Marker marker = stream.markPosition();
        test(stream, "d", "e", "f", null);
        marker.close(true);
        test(stream, "d", "e", "f", null);
    }

    public void testMarker3() throws IOException {
        CharStream stream = newCharStream("abcdef");
        test(stream, "a", "b");
        {
            Marker cMarker = stream.markPosition();
            {
                Marker cMarker1 = stream.markPosition();
                test(stream, "c", "d");
                {
                    Marker eMarker = stream.markPosition();
                    test(stream, "e", "f", null);
                    eMarker.close(true);
                }
                test(stream, "e", "f", null);
                test(stream, (String) null);
                cMarker1.close(true);
            }
            test(stream, "c", "d", "e", "f", null);
            cMarker.close(true);
        }
        test(stream, "c", "d", "e", "f", null);
    }

    public void testMarker4() throws IOException {
        CharStream stream = newCharStream("abcdef");
        test(stream, "a", "b");
        {
            Marker cMarker = stream.markPosition();
            {
                Marker cMarker1 = stream.markPosition();
                test(stream, "c", "d");
                {
                    Marker eMarker = stream.markPosition();
                    test(stream, "e", "f", null);
                    eMarker.close(true);
                }
                test(stream, "e", "f", null);
                test(stream, (String) null);
                cMarker1.close(true);
            }
            test(stream, "c", "d", "e", "f", null);
            cMarker.close(true);
        }
        test(stream, "c", "d", "e", "f", null);
    }

    public void testSequence() throws IOException {
        CharStream stream = newCharStream("abc");
        test(stream, "a", "b", "c", null);
    }

    public void testStream() throws IOException {
        CharStream stream = newCharStream("abc");
        int pos = 0;
        testStream(stream, 'a', pos++, 0, 0);
        testStream(stream, 'b', pos++, 0, 1);
        testStream(stream, 'c', pos++, 0, 2);

        pos = 0;
        int line = 0;
        stream = newCharStream("a\nbc");
        testStream(stream, 'a', pos++, line, 0);
        testStream(stream, '\n', pos++, line++, 1);
        testStream(stream, 'b', pos++, line, 0);
        testStream(stream, 'c', pos++, line, 1);

        pos = 0;
        line = 0;
        stream = newCharStream("a\r\nbc");
        testStream(stream, 'a', pos++, line, 0);
        testStream(stream, '\r', pos++, line, 1);
        testStream(stream, '\n', pos++, line++, 2);
        testStream(stream, 'b', pos++, line, 0);
        testStream(stream, 'c', pos++, line, 1);
    }

    private void testStream(
        CharStream stream,
        char c,
        int pos,
        int line,
        int linePos) {
        assertEquals(c, stream.getChar());
        assertEquals(new CharStream.Pointer(pos, line, linePos), stream
            .getPointer());
        stream.incPos();
    }

}
