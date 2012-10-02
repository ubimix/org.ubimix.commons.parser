/**
 * 
 */
package org.ubimix.commons.parser.xml;

import org.ubimix.commons.parser.CharStream;
import org.ubimix.commons.parser.ITokenizer;
import org.ubimix.commons.parser.StreamToken;
import org.ubimix.commons.parser.CharStream.Marker;
import org.ubimix.commons.parser.CharStream.Pointer;

public class TagTokenizer implements ITokenizer {

    public final static TagTokenizer INSTANCE = new TagTokenizer();

    private AttrTokenizer fAttrTokenizer;

    private String fKey;

    public TagTokenizer() {
        this(XMLDict.TAG, AttrTokenizer.INSTANCE);
    }

    public TagTokenizer(String key, AttrTokenizer attrTokenizer) {
        fKey = key;
        fAttrTokenizer = attrTokenizer;
    }

    protected boolean isSpecialSymbol(char ch) {
        boolean result = false;
        switch (ch) {
            case '!':
            case '"':
            case '#':
            case '$':
            case '%':
            case '&':
            case '\'':
            case '(':
            case ')':
            case '*':
            case '+':
            case ',':
            case '-':
            case '.':
                // case '/':
            case ':':
            case ';':
                // case '<':
            case '=':
                // case '>':
            case '?':
            case '@':
            case '[':
            case '\\':
            case ']':
            case '^':
            case '_':
            case '`':
            case '{':
            case '|':
            case '}':
            case '~':
                result = true;
                break;
            default:
                break;
        }
        return result;
    }

    public StreamToken read(CharStream stream) {
        char ch = stream.getChar();
        if (ch != '<')
            return null;

        TagToken result = null;
        Marker marker = stream.markPosition();
        try {
            Pointer begin = stream.getPointer();
            if (!stream.incPos())
                return null;
            ch = stream.getChar();
            boolean open = true;
            boolean close = false;
            if (ch == '/') {
                open = false;
                close = true;
                if (!stream.incPos())
                    return result;
            }

            Pointer nameBegin = stream.getPointer();
            skipName(stream);
            Pointer nameEnd = stream.getPointer();
            if (nameBegin.pos == nameEnd.pos)
                return null;
            String name = marker.getSubstring(nameBegin, nameEnd);

            AttrToken attr = null;
            AttrToken prev = null;
            while (true) {
                ch = stream.getChar();
                if (ch == '/') {
                    if (!stream.incPos())
                        break;
                    ch = stream.getChar();
                    if (ch == '>') {
                        close = true;
                        stream.incPos();
                        break;
                    }
                } else if (ch == '>') {
                    stream.incPos();
                    break;
                } else if (ch == '<') {
                    return null;
                }

                if (skipSpaces(stream))
                    continue;

                if (skipSpecialSymbols(stream))
                    continue;

                AttrToken next = (AttrToken) fAttrTokenizer.read(stream);
                if (next != null) {
                    if (attr == null) {
                        attr = next;
                    }
                    next.insertBefore(prev);
                    prev = next;
                    continue;
                }

                if (!stream.incPos())
                    break;
            }
            Pointer end = stream.getPointer();
            result = new TagToken(fKey, open, close, begin, end, marker
                .getSubstring());
            result.setName(nameBegin, nameEnd, name);
            result.setFirstAttribute(attr);
            return result;
        } finally {
            marker.close(result == null);
        }
    }

    private void skipName(CharStream stream) {
        char ch = stream.getChar();
        if (!Character.isLetter(ch))
            return;
        while (stream.incPos()) {
            ch = stream.getChar();
            if (!Character.isLetterOrDigit(ch) && ch != ':')
                break;
        }
    }

    private boolean skipSpaces(CharStream stream) {
        boolean result = false;
        while (Character.isSpaceChar(stream.getChar())) {
            result = true;
            if (!stream.incPos())
                break;
        }
        return result;
    }

    private boolean skipSpecialSymbols(CharStream stream) {
        boolean result = false;
        while (isSpecialSymbol(stream.getChar())) {
            result = true;
            if (!stream.incPos())
                break;
        }
        return result;
    }

}