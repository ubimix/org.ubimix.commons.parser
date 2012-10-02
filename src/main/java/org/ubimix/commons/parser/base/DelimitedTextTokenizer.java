/**
 * 
 */
package org.ubimix.commons.parser.base;

import org.ubimix.commons.parser.CharStream;
import org.ubimix.commons.parser.ITokenizer;
import org.ubimix.commons.parser.StreamToken;
import org.ubimix.commons.parser.CharStream.Marker;
import org.ubimix.commons.parser.CharStream.Pointer;

/**
 * @author kotelnikov
 */
public class DelimitedTextTokenizer implements ITokenizer {

    private SequenceTokenizer fBeginToknizer;

    private SequenceTokenizer fEndToknizer;

    private char fFirstChar;

    private String fKey;

    private boolean fRecursive;

    public DelimitedTextTokenizer(String key, String begin, String end) {
        this(key, begin, end, false);
    }

    public DelimitedTextTokenizer(
        String key,
        String begin,
        String end,
        boolean recursive) {
        fKey = key;
        fFirstChar = begin.charAt(0);
        fBeginToknizer = new SequenceTokenizer(fKey, begin);
        fEndToknizer = new SequenceTokenizer(fKey, end);
        fRecursive = recursive;
    }

    /**
     * Appends a list of child tokens to the parent token. This method should be
     * overloaded in subclasses.
     * 
     * @param parent to this token the first child should be attached
     * @param firstChild the first child in the list
     */
    protected void addSubtokens(StreamToken parent, StreamToken firstChild) {
        // 
    }

    public String getKey() {
        return fKey;
    }

    /**
     * Returns a newly created token corresponding to the given start and end
     * positions. This method could be overloaded in subclasses.
     * 
     * @param key the key of the token
     * @param begin the start position of the token to create
     * @param end the end position of the token
     * @param str the string representation of the token
     * @param level the level of the token to create (0 for the topmost one)
     * @return a new token with the given parameters
     */
    protected StreamToken newToken(
        String key,
        Pointer begin,
        Pointer end,
        String str,
        int level) {
        return new StreamToken(key, begin, end, str);
    }

    public StreamToken read(CharStream stream) {
        char ch = stream.getChar();
        if (ch != fFirstChar)
            return null;
        Marker marker = stream.markPosition();
        StreamToken result = null;
        try {
            result = skip(marker, stream, 0);
            return result;
        } finally {
            marker.close(result == null);
        }
    }

    public StreamToken skip(Marker marker, CharStream stream, int level) {
        char ch = stream.getChar();
        if (ch != fFirstChar)
            return null;
        Pointer begin = stream.getPointer();
        if (fBeginToknizer.read(stream) == null)
            return null;
        StreamToken first = null;
        StreamToken last = null;
        StreamToken endToken = null;
        while ((endToken = fEndToknizer.read(stream)) == null) {
            if (!skipContentSymbols(stream))
                break;
            if (!fRecursive)
                continue;
            StreamToken token = skip(marker, stream, level + 1);
            if (token != null) {
                if (first == null) {
                    first = token;
                } else if (last != null) {
                    last.insertAfter(token);
                }
                last = token;
            }
        }
        if (endToken == null)
            return null;
        Pointer end = stream.getPointer();
        String str = marker.getSubstring(begin, end);
        StreamToken result = newToken(fKey, begin, end, str, level);
        if (first != null) {
            addSubtokens(result, first);
        }
        return result;
    }

    protected boolean skipContentSymbols(CharStream stream) {
        return stream.incPos();
    }
}
