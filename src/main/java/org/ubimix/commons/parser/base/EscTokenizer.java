/**
 * 
 */
package org.ubimix.commons.parser.base;

import org.ubimix.commons.parser.AbstractTokenizer;
import org.ubimix.commons.parser.ICharStream;
import org.ubimix.commons.parser.ICharStream.IPointer;
import org.ubimix.commons.parser.StreamToken;

/**
 * @author kotelnikov
 */
public abstract class EscTokenizer extends AbstractTokenizer {

    /**
     * 
     */
    public EscTokenizer() {
    }

    protected abstract boolean checkChar(char ch, int pos);

    protected char getEscapeSymbol() {
        return '\\';
    }

    protected int getMaxLength() {
        return Integer.MAX_VALUE;
    }

    protected int getMinLength() {
        return 1;
    }

    @Override
    public StreamToken read(ICharStream stream) {
        int localPos = 0;
        int minLen = getMinLength();
        int maxLen = getMaxLength();
        char esc = getEscapeSymbol();
        StreamToken result = null;
        ICharStream.IMarker marker = stream.markPosition();
        try {
            IPointer start = stream.getPointer();
            boolean escaped = false;
            while (localPos < maxLen && !stream.isTerminated()) {
                if (escaped) {
                    escaped = false;
                } else {
                    char ch = stream.getChar();
                    escaped = ch == esc;
                    if (!escaped && !checkChar(ch, localPos)) {
                        break;
                    }
                }
                stream.incPos();
                localPos++;
            }
            IPointer stop = stream.getPointer();
            if (localPos >= minLen && localPos < maxLen && !start.equals(stop)) {
                result = newToken(stream, marker);
            }
            return result;
        } finally {
            marker.close(result == null);
        }
    }
}
