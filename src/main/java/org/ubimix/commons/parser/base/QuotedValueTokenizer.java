/**
 * 
 */
package org.ubimix.commons.parser.base;

import org.ubimix.commons.parser.CharStream;
import org.ubimix.commons.parser.ITokenizer;
import org.ubimix.commons.parser.StreamToken;
import org.ubimix.commons.parser.CharStream.Marker;

/**
 * @author kotelnikov
 */
public class QuotedValueTokenizer implements ITokenizer {

    private String fKey;

    public QuotedValueTokenizer(String key) {
        fKey = key;
    }

    protected char getEscapeSymbol() {
        return '\\';
    }

    protected StreamToken newToken(CharStream stream, Marker marker) {
        StreamToken result = new StreamToken(fKey, stream, marker);
        return result;
    }

    public StreamToken read(CharStream stream) {
        StreamToken result = null;
        char esc = getEscapeSymbol();
        char firstChar = stream.getChar();
        if (firstChar == '\'' || firstChar == '"') {
            Marker marker = stream.markPosition();
            try {
                boolean endFound = false;
                boolean escaped = false;
                while (stream.incPos()) {
                    if (escaped) {
                        escaped = false;
                        continue;
                    }
                    char ch = stream.getChar();
                    if (ch == firstChar) {
                        stream.incPos();
                        endFound = true;
                        break;
                    } else if (ch == esc) {
                        escaped = true;
                    }
                }
                if (endFound || !strictPatterns()) {
                    result = newToken(stream, marker);
                }
            } finally {
                marker.close(result == null);
            }
        }
        return result;
    }

    protected boolean strictPatterns() {
        return true;
    }

}