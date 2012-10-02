/**
 * 
 */
package org.ubimix.commons.parser.base;

import org.ubimix.commons.parser.text.TextDict;

public final class WordTokenizer extends SimpleTokenizer {

    public static final WordTokenizer INSTANCE = new WordTokenizer();

    public WordTokenizer() {
        this(TextDict.WORD);
    }

    public WordTokenizer(String key) {
        super(key);
    }

    @Override
    protected boolean checkChar(char ch, int pos) {
        return Character.isLetterOrDigit(ch);
    }
}