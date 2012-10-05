/**
 * 
 */
package org.ubimix.commons.parser.base;

import org.ubimix.commons.parser.AbstractTokenizer;
import org.ubimix.commons.parser.ICharStream;
import org.ubimix.commons.parser.ICharStream.IMarker;
import org.ubimix.commons.parser.StreamToken;

public abstract class SimpleTokenizer extends AbstractTokenizer {

    protected abstract boolean checkChar(char ch, int pos);

    protected int getMaxLength() {
        return Integer.MAX_VALUE;
    }

    protected int getMinLength() {
        return 1;
    }

    @Override
    public StreamToken read(ICharStream stream) {
        char ch = stream.getChar();
        int localPos = 0;
        if (!checkChar(ch, localPos)) {
            return null;
        }
        int minLen = getMinLength();
        int maxLen = getMaxLength();
        StreamToken result = null;
        ICharStream.IMarker marker = stream.markPosition();
        try {
            localPos++;
            if (stream.incPos()) {
                while (localPos < maxLen
                    && checkChar(stream.getChar(), localPos)) {
                    localPos++;
                    if (!stream.incPos()) {
                        break;
                    }
                }
            }
            result = localPos >= minLen ? newToken(stream, marker) : null;
            return result;
        } finally {
            marker.close(result == null);
        }
    }
}