/**
 * 
 */
package org.ubimix.commons.parser.base;

import org.ubimix.commons.parser.text.TextDict;

public final class WordTokenizer extends SimpleTokenizer {

    public static final WordTokenizer INSTANCE = new WordTokenizer();

    @Override
    protected boolean checkChar(char ch, int pos) {
        return Character.isLetterOrDigit(ch);
    }

    @Override
    protected StreamToken newToken() {
        return new TextDict.WordToken();
    }
}