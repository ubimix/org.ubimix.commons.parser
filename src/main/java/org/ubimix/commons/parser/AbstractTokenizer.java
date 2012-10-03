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

    public AbstractTokenizer() {
    }

    protected abstract StreamToken newToken();

    protected final <T extends StreamToken> T newToken(
        CharStream stream,
        Marker marker) {
        Pointer begin = marker.getPointer();
        Pointer end = stream.getPointer();
        int len = end.len(begin);
        String str = marker.getSubstring(len);
        T token = newToken(begin, end, str);
        return token;
    }

    /**
     * Returns a newly created token.
     * 
     * @param begin the start position of the token
     * @param end the end position of the token
     * @param str the string representation of the token
     * @return a newly created token
     */
    protected final <T extends StreamToken> T newToken(
        Pointer begin,
        Pointer end,
        String str) {
        @SuppressWarnings("unchecked")
        T token = (T) newToken();
        token.init(begin, end, str);
        return token;
    }

}
