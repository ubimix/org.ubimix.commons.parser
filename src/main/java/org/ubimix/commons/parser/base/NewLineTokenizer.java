/**
 * 
 */
package org.ubimix.commons.parser.base;

import org.ubimix.commons.parser.AbstractTokenizer;
import org.ubimix.commons.parser.ICharStream;
import org.ubimix.commons.parser.StreamToken;
import org.ubimix.commons.parser.text.TextDict;

public final class NewLineTokenizer extends AbstractTokenizer {

    public static final NewLineTokenizer INSTANCE = new NewLineTokenizer();

    /**
     * @param stream
     * @return
     */
    public static String readNewLine(ICharStream stream) {
        char ch = stream.getChar();
        if (ch == '\r') {
            stream.incPos();
            ch = stream.getChar();
            if (ch == '\n') {
                stream.incPos();
                return "\r\n";
            }
            return "\r";
        } else if (ch == '\n') {
            stream.incPos();
            return "\n";
        }
        return null;
    }

    @Override
    protected StreamToken newToken() {
        return new TextDict.NewLineToken();
    }

    /**
     * @see org.ubimix.commons.parser.org.ubimix.commons.parser.tokenizer.Tokenizer.ITokenizer#read(org.ubimix.commons.parser.CharStream)
     */
    @Override
    public StreamToken read(ICharStream stream) {
        ICharStream.IPointer startPos = stream.getPointer();
        String result = readNewLine(stream);
        if (result == null) {
            return null;
        }
        ICharStream.IPointer endPos = stream.getPointer();
        return newToken(startPos, endPos, result);
    }

}