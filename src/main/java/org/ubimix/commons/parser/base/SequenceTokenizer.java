/**
 * 
 */
package org.ubimix.commons.parser.base;

import org.ubimix.commons.parser.StreamToken;


/**
 * @author kotelnikov
 */
public class SequenceTokenizer extends SimpleTokenizer {

    public static class SequenceToken extends StreamToken {
    }

    protected String fSequence;

    public SequenceTokenizer(String sequence) {
        fSequence = sequence;
    }

    /**
     * @see org.ubimix.commons.parser.base.SimpleTokenizer#checkChar(char, int)
     */
    @Override
    protected boolean checkChar(char ch, int pos) {
        char c = fSequence.charAt(pos);
        return ch == c;
    }

    @Override
    protected int getMaxLength() {
        return fSequence.length();
    }

    @Override
    protected int getMinLength() {
        return fSequence.length();
    }

    @Override
    protected StreamToken newToken() {
        return new SequenceToken();
    }

}
