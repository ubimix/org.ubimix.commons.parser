/**
 * 
 */
package org.ubimix.commons.parser.base;


/**
 * @author kotelnikov
 */
public class SequenceTokenizer extends SimpleTokenizer {

    protected String fSequence;

    public SequenceTokenizer(String key, String sequence) {
        super(key);
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

}
