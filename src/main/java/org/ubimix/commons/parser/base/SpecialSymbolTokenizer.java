/**
 * 
 */
package org.ubimix.commons.parser.base;

import org.ubimix.commons.parser.text.TextDict;

public final class SpecialSymbolTokenizer extends SimpleTokenizer {

    public static SpecialSymbolTokenizer INSTANCE = new SpecialSymbolTokenizer();

    public SpecialSymbolTokenizer() {
        this(TextDict.SPECIAL);
    }

    public SpecialSymbolTokenizer(String key) {
        super(key);
    }

    @Override
    protected boolean checkChar(char ch, int pos) {
        return !Character.isLetterOrDigit(ch)
            && !Character.isSpaceChar(ch)
            && ch != 0;
    }

    protected boolean checkChar1(char ch, int pos) {
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
            case '–':
            case '-':
            case '.':
            case '/':
            case ':':
            case ';':
            case '<':
            case '=':
            case '>':
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

    @Override
    protected int getMaxLength() {
        return 1;
    }

}