/**
 * 
 */
package org.ubimix.commons.parser;

import java.io.IOException;

import junit.framework.TestCase;

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
    protected ICharStream newCharStream(String str) {
        return new CharStream(str);
    }

    public void test() throws IOException {
        ICharStream stream;
        int pos = 0;
        int line = 0;

        stream = newCharStream("a\r\nbc");
        testStream(stream, 'a', pos++, line, 0);
        testStream(stream, '\r', pos++, line++, 1);
        testStream(stream, '\n', pos++, line, 0);
        testStream(stream, 'b', pos++, line, 0);
        testStream(stream, 'c', pos++, line, 1);

        pos = 0;
        line = 0;
        stream = newCharStream("a\r\n\nbc");
        testStream(stream, 'a', pos++, line, 0);
        testStream(stream, '\r', pos++, line++, 1);
        testStream(stream, '\n', pos++, line, 0);
        testStream(stream, '\n', pos++, line++, 0);
        testStream(stream, 'b', pos++, line, 0);
        testStream(stream, 'c', pos++, line, 1);
    }

    private void test(ICharStream stream, String... chars) throws IOException {
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
        ICharStream stream = newCharStream("abcdef");
        test(stream, "a", "b");
        {
            ICharStream.IMarker cMarker = stream.markPosition();
            test(stream, "c", "d");
            {
                ICharStream.IMarker eMarker = stream.markPosition();
                test(stream, "e", "f", null);
                eMarker.close(true);
            }
            test(stream, "e", "f", null);
            test(stream, (String) null);
            cMarker.close(true);
        }
        test(stream, "c", "d");
        {
            ICharStream.IMarker eMarker = stream.markPosition();
            test(stream, "e", "f", null);
            test(stream, (String) null);
            eMarker.close(true);
        }
        test(stream, "e", "f", null);
    }

    public void testMarker0() throws IOException {
        ICharStream stream = newCharStream("ab");
        test(stream, "a");
        ICharStream.IMarker marker = stream.markPosition();
        test(stream, "b", null);
        marker.close(true);
        test(stream, "b", null);
    }

    public void testMarker1() throws IOException {
        ICharStream stream = newCharStream("abcd");
        test(stream, "a");
        {
            ICharStream.IMarker aMarker = stream.markPosition();
            test(stream, "b");
            {
                ICharStream.IMarker bMarker = stream.markPosition();
                test(stream, "c");
                {
                    ICharStream.IMarker dMarker = stream.markPosition();
                    test(stream, "d");
                    {
                        ICharStream.IMarker nullMarker = stream.markPosition();
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
        ICharStream stream = newCharStream("abcdef");
        test(stream, "a", "b", "c");
        ICharStream.IMarker marker = stream.markPosition();
        test(stream, "d", "e", "f", null);
        marker.close(true);
        test(stream, "d", "e", "f", null);
    }

    public void testMarker3() throws IOException {
        ICharStream stream = newCharStream("abcdef");
        test(stream, "a", "b");
        {
            ICharStream.IMarker cMarker = stream.markPosition();
            {
                ICharStream.IMarker cMarker1 = stream.markPosition();
                test(stream, "c", "d");
                {
                    ICharStream.IMarker eMarker = stream.markPosition();
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
        ICharStream stream = newCharStream("abcdef");
        test(stream, "a", "b");
        {
            for (int i = 0; i < 4; i++) {
                ICharStream.IMarker cMarker = stream.markPosition();
                {
                    for (int j = 0; j < 4; j++) {
                        ICharStream.IMarker cMarker1 = stream.markPosition();
                        test(stream, "c", "d");
                        {
                            for (int k = 0; k < 4; k++) {
                                ICharStream.IMarker eMarker = stream
                                    .markPosition();
                                test(stream, "e", "f", null);
                                eMarker.close(true);
                            }
                        }
                        test(stream, "e", "f", null);
                        test(stream, (String) null);
                        cMarker1.close(true);
                    }
                }
                test(stream, "c", "d", "e", "f", null);
                cMarker.close(true);
            }
        }
        test(stream, "c", "d", "e", "f", null);
    }

    public void testMarker5() throws IOException {
        ICharStream stream = newCharStream("abcdefgh");
        test(stream, "a", "b");

        ICharStream.IMarker marker = stream.markPosition();
        test(stream, "c", "d");
        marker.close(true);

        marker = stream.markPosition();
        test(stream, "c", "d");
        marker.close(true);

        marker = stream.markPosition();
        test(stream, "c", "d");
        marker.close(false);

        marker = stream.markPosition();
        test(stream, "e", "f", "g", "h", null);
        marker.close(true);

        marker = stream.markPosition();
        test(stream, "e", "f");
        marker.close(false);

        test(stream, "g", "h", null);
    }

    public void testMarkers() throws IOException {
        ICharStream stream = newCharStream("abcd");
        test(stream, "a");
        {
            ICharStream.IMarker bMarker = stream.markPosition();
            assertEquals("Marker(1[0:1]:'')", bMarker.toString());
            // test(stream, "b", "c", "d", null);
            // test(stream, (String) null);
            bMarker.close(true);
        }
        test(stream, "b");
        {
            ICharStream.IMarker cMarker = stream.markPosition();
            assertEquals("Marker(2[0:2]:'')", cMarker.toString());
            test(stream, "c", "d", null);
            test(stream, (String) null);
            assertEquals("Marker(2[0:2]:'cd')", cMarker.toString());
            cMarker.close(true);
        }
        test(stream, "c", "d", null);
    }

    public void testSequence() throws IOException {
        ICharStream stream = newCharStream("abc");
        test(stream, "a", "b", "c", null);
    }

    public void testStream() throws IOException {
        ICharStream stream = newCharStream("abc");
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
        testStream(stream, '\r', pos++, line++, 1);
        testStream(stream, '\n', pos++, line, 0);
        testStream(stream, 'b', pos++, line, 0);
        testStream(stream, 'c', pos++, line, 1);
    }

    private void testStream(
        ICharStream stream,
        char c,
        int pos,
        int line,
        int linePos) {
        assertEquals(c, stream.getChar());
        ICharStream.IPointer pointer = stream.getPointer();
        assertNotNull(pointer);
        assertEquals(pos, pointer.getPos());
        assertEquals(line, pointer.getLine());
        assertEquals(linePos, pointer.getColumn());
        stream.incPos();
    }

}
