/**
 * 
 */
package org.ubimix.commons.parser.base;

import java.util.ArrayList;
import java.util.List;

import org.ubimix.commons.parser.AbstractTokenizer;
import org.ubimix.commons.parser.CharStream;
import org.ubimix.commons.parser.CharStream.Marker;
import org.ubimix.commons.parser.CharStream.Pointer;
import org.ubimix.commons.parser.StreamToken;

/**
 * @author kotelnikov
 */
public class DelimitedTextTokenizer extends AbstractTokenizer {

    public static class DelimitedTextToken extends StreamToken {
    }

    private SequenceTokenizer fBeginToknizer;

    private SequenceTokenizer fEndToknizer;

    private char fFirstChar;

    private boolean fRecursive;

    public DelimitedTextTokenizer(String begin, String end, boolean recursive) {
        fFirstChar = begin.charAt(0);
        fBeginToknizer = new SequenceTokenizer(begin);
        fEndToknizer = new SequenceTokenizer(end);
        fRecursive = recursive;
    }

    /**
     * Appends a list of child tokens to the parent token. This method should be
     * overloaded in subclasses.
     * 
     * @param parent to this token the first child should be attached
     * @param children TODO
     */
    protected void addSubtokens(StreamToken parent, List<StreamToken> children) {
        //
    }

    @Override
    protected StreamToken newToken() {
        return new DelimitedTextToken();
    }

    /**
     * Returns a newly created token corresponding to the given start and end
     * positions. This method could be overloaded in subclasses.
     * 
     * @param begin the start position of the token to create
     * @param end the end position of the token
     * @param str the string representation of the token
     * @param level the level of the token to create (0 for the topmost one)
     * @return a new token with the given parameters
     */
    protected StreamToken newToken(
        Pointer begin,
        Pointer end,
        String str,
        int level) {
        StreamToken token = newToken(begin, end, str);
        return token;
    }

    @Override
    public StreamToken read(CharStream stream) {
        char ch = stream.getChar();
        if (ch != fFirstChar) {
            return null;
        }
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
        if (ch != fFirstChar) {
            return null;
        }
        Pointer begin = stream.getPointer();
        if (fBeginToknizer.read(stream) == null) {
            return null;
        }
        StreamToken endToken = null;
        List<StreamToken> children = null;
        while ((endToken = fEndToknizer.read(stream)) == null) {
            if (!skipContentSymbols(stream)) {
                break;
            }
            if (!fRecursive) {
                continue;
            }
            StreamToken token = skip(marker, stream, level + 1);
            if (token != null) {
                if (children == null) {
                    children = new ArrayList<StreamToken>();
                }
                children.add(token);
            }
        }
        if (endToken == null) {
            return null;
        }
        Pointer end = stream.getPointer();
        String str = marker.getSubstring(begin, end);
        StreamToken result = newToken(begin, end, str, level);
        if (children != null) {
            addSubtokens(result, children);
        }
        return result;
    }

    protected boolean skipContentSymbols(CharStream stream) {
        return stream.incPos();
    }
}
