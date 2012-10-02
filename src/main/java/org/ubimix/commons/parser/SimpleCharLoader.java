package org.ubimix.commons.parser;

import org.ubimix.commons.parser.CharStream.ICharLoader;

public class SimpleCharLoader implements ICharLoader {

    private char[] fArray;

    private int fArrayPos;

    public SimpleCharLoader(String str) {
        fArray = str.toCharArray();
        fArrayPos = 0;
    }

    /**
     * @see org.ubimix.commons.parser.CharStream.ICharLoader#readNext()
     */
    public int readNext() {
        return fArrayPos < fArray.length ? fArray[fArrayPos++] : -1;
    }

}