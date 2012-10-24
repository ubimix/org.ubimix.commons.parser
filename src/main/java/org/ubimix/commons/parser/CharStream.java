/**
 * 
 */
package org.ubimix.commons.parser;

/**
 * @author kotelnikov
 */
public class CharStream extends AbstractCharStream {

    private String fString;

    public CharStream(String str) {
        fString = str;
    }

    @Override
    protected String getBufferedSubstring(int pos, int len) {
        return fString.substring(pos, pos + len);
    }

    @Override
    protected int loadNextChar(int length, int nextPos) {
        return nextPos < fString.length() ? fString.charAt(nextPos) : -1;
    }

}
