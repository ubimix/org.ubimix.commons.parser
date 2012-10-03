/**
 * 
 */
package org.ubimix.commons.parser.base;

import org.ubimix.commons.parser.AbstractTokenizer;
import org.ubimix.commons.parser.CharStream;
import org.ubimix.commons.parser.CharStream.Marker;

/**
 * @author kotelnikov
 */
public class QuotedValueTokenizer extends AbstractTokenizer {

    public static class QuotedValueToken extends StreamToken {
    }

    private boolean fStrictPatterns;

    public QuotedValueTokenizer() {
        this(true);
    }

    public QuotedValueTokenizer(boolean strictPattern) {
        fStrictPatterns = strictPattern;
    }

    protected char getEscapeSymbol() {
        return '\\';
    }

    @Override
    protected StreamToken newToken() {
        return new QuotedValueToken();
    }

    @Override
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
        return fStrictPatterns;
    }

}