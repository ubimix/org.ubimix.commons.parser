/**
 * 
 */
package org.ubimix.commons.parser.xml;

import org.ubimix.commons.parser.CharStream;
import org.ubimix.commons.parser.ITokenizer;
import org.ubimix.commons.parser.StreamToken;
import org.ubimix.commons.parser.CharStream.Marker;
import org.ubimix.commons.parser.CharStream.Pointer;

/**
 * @author kotelnikov
 */
public abstract class AbstractEntityTokenizer implements ITokenizer {

    public AbstractEntityTokenizer() {
    }

    protected abstract String getParseKey(boolean digit, String str);

    public StreamToken read(CharStream stream) {
        char ch = stream.getChar();
        if (ch != '&')
            return null;
        EntityToken result = null;
        Marker marker = stream.markPosition();
        try {
            if (!stream.incPos())
                return null;

            Pointer begin = stream.getPointer();
            ch = stream.getChar();
            boolean digit = false;
            if (ch == '#') {
                digit = true;
                if (!stream.incPos())
                    return null;
                ch = stream.getChar();
                begin = stream.getPointer();
                while (Character.isDigit(ch)) {
                    if (!stream.incPos())
                        return null;
                    ch = stream.getChar();
                }
            } else {
                while (Character.isLetterOrDigit(ch)) {
                    if (!stream.incPos())
                        return null;
                    ch = stream.getChar();
                }
            }
            Pointer end = stream.getPointer();
            if (begin.pos == end.pos)
                return null;
            ch = stream.getChar();
            if (ch != ';')
                return null;
            stream.incPos();

            String str = marker.getSubstring(begin, end);
            String key = getParseKey(digit, str);
            if (key == null)
                return null;
            result = new EntityToken(key, marker.getPointer(), stream
                .getPointer(), marker.getSubstring());
            return result;
        } finally {
            marker.close(result == null);
        }
    }
}
