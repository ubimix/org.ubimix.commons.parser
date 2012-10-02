/**
 * 
 */
package org.ubimix.commons.parser;

import org.ubimix.commons.parser.CharStream.Marker;
import org.ubimix.commons.parser.CharStream.Pointer;

/**
 * @author kotelnikov
 */
public abstract class AbstractTokenizer implements ITokenizer {

    protected String fKey;

    public AbstractTokenizer(String key) {
        fKey = key;
    }

    protected boolean isClosing() {
        return false;
    }

    protected boolean isOpening() {
        return true;
    }

    protected StreamToken newToken(CharStream stream, Marker marker) {
        Pointer begin = marker.getPointer();
        Pointer end = stream.getPointer();
        int len = end.len(begin);
        String str = marker.getSubstring(len);
        return newToken(begin, end, str);
    }

    /**
     * Returns a newly created token.
     * 
     * @param begin the start position of the token
     * @param end the end position of the token
     * @param str the string representation of the token
     * @return a newly created token
     */
    protected StreamToken newToken(Pointer begin, Pointer end, String str) {
        StreamToken token = new StreamToken(
            fKey,
            isOpening(),
            isClosing(),
            begin,
            end,
            str);
        return token;
    }

}
