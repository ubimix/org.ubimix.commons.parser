/**
 * 
 */
package org.ubimix.commons.parser;

import org.ubimix.commons.parser.ICharStream.IMarker;
import org.ubimix.commons.parser.ICharStream.IPointer;


/**
 * @author kotelnikov
 */
public abstract class AbstractTokenizer implements ITokenizer {

    public static String getString(ICharStream.IMarker marker, int len) {
        return marker.getSubstring(marker.getPointer().getPos(), len);
    }

    public static String getString(ICharStream.IMarker marker, ICharStream.IPointer end) {
        return getString(marker, marker.getPointer(), end);
    }

    public static String getString(ICharStream.IMarker marker, ICharStream.IPointer begin, ICharStream.IPointer end) {
        String match = marker.getSubstring(
            begin.getPos(),
            end.getPos() - begin.getPos());
        return match;
    }

    public AbstractTokenizer() {
    }

    protected abstract StreamToken newToken();

    protected final <T extends StreamToken> T newToken(
        ICharStream stream,
        ICharStream.IMarker marker) {
        ICharStream.IPointer begin = marker.getPointer();
        ICharStream.IPointer end = stream.getPointer();
        int len = end.getPos() - begin.getPos();
        String str = getString(marker, len);
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
        ICharStream.IPointer begin,
        ICharStream.IPointer end,
        String str) {
        @SuppressWarnings("unchecked")
        T token = (T) newToken();
        token.init(begin, end, str);
        return token;
    }

}
