/**
 * 
 */
package org.ubimix.commons.parser.base;

import org.ubimix.commons.parser.StreamToken;
import org.ubimix.commons.parser.text.TextDict;

public final class SpaceTokenizer extends SimpleTokenizer {

    public final static SpaceTokenizer INSTANCE = new SpaceTokenizer();

    @Override
    protected boolean checkChar(char ch, int pos) {
        // 0xA and 0xD are end-of-line symbols.
        if (Character.isSpaceChar(ch) && (ch != 0xA && ch != 0xD)) {
            return true;
        }
        if (ch == '\t') {
            return true;
        }
        return false;
    }

    @Override
    protected StreamToken newToken() {
        return new TextDict.SpacesToken();
    }
}