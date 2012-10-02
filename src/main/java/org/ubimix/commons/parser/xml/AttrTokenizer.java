/**
 * 
 */
package org.ubimix.commons.parser.xml;

import org.ubimix.commons.parser.CharStream;
import org.ubimix.commons.parser.ITokenizer;
import org.ubimix.commons.parser.StreamToken;
import org.ubimix.commons.parser.CharStream.Marker;
import org.ubimix.commons.parser.CharStream.Pointer;

public class AttrTokenizer implements ITokenizer {

    public static final AttrTokenizer INSTANCE = new AttrTokenizer();

    public final static boolean skipQuotedText(CharStream stream, char esc) {
        boolean result = false;
        char openChar = stream.getChar();
        if (openChar == '\'' || openChar == '"') {
            result = true;
            boolean escaped = false;
            while (stream.incPos()) {
                if (escaped) {
                    escaped = false;
                    continue;
                }
                char ch = stream.getChar();
                if (ch == openChar) {
                    stream.incPos();
                    result = true;
                    break;
                } else if (ch == esc) {
                    escaped = true;
                }
            }
        }
        return result;
    }

    private String fKey;

    public AttrTokenizer() {
        this(XMLDict.ATTR);
    }

    public AttrTokenizer(String key) {
        fKey = key;
    }

    protected char getEscapeSymbol() {
        return '\\';
    }

    protected boolean isValueChar(char ch) {
        return !Character.isSpaceChar(ch) && ch != '>' && ch != '<';
    }

    public StreamToken read(CharStream stream) {
        AttrToken result = null;
        Marker marker = stream.markPosition();
        try {
            Pointer nameBegin = stream.getPointer();
            skipName(stream);
            Pointer nameEnd = stream.getPointer();
            if (nameBegin.pos == nameEnd.pos)
                return null;
            String name = marker.getSubstring(nameEnd.pos - nameBegin.pos);
            skipSpaces(stream);
            Pointer valueBegin = stream.getPointer();
            Pointer valueEnd = stream.getPointer();
            if (stream.getChar() == '=') {
                stream.incPos();
                skipSpaces(stream);
                valueBegin = valueEnd = stream.getPointer();
                skipValue(stream);
                valueEnd = stream.getPointer();
            }
            String value = marker.getSubstring(valueBegin, valueEnd.pos
                - valueBegin.pos);
            result = new AttrToken(
                fKey,
                true,
                false,
                nameBegin,
                valueEnd,
                marker.getSubstring(valueEnd.pos));
            result.setName(nameBegin, nameEnd, name);
            result.setValue(valueBegin, valueEnd, value);
            return result;
        } finally {
            marker.close(result == null);
        }
    }

    private void skipName(CharStream stream) {
        char ch = stream.getChar();
        while (Character.isLetterOrDigit(ch) || ch == ':' || ch == '-') {
            if (!stream.incPos())
                break;
            ch = stream.getChar();
        }
    }

    private void skipSpaces(CharStream stream) {
        for (; Character.isSpaceChar(stream.getChar()); stream.incPos()) {
        }
    }

    private boolean skipValue(CharStream stream) {
        char esc = getEscapeSymbol();
        boolean result = skipQuotedText(stream, esc);
        if (!result) {
            char ch = stream.getChar();
            while (isValueChar(ch)) {
                result = true;
                if (!stream.incPos())
                    break;
                ch = stream.getChar();
            }
        }
        return result;
    }

}