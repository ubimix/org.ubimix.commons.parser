/**
 * 
 */
package org.ubimix.commons.parser.text;

import org.ubimix.commons.parser.StreamToken;

/**
 * A basic dictionary defining all basic tokens like "word", "space", "special"
 * (special symbols), and "eol" (end of the line).
 * 
 * @author kotelnikov
 */
public class TextDict {

    public static class NewLineToken extends StreamToken {
    }

    public static class SpacesToken extends StreamToken {
    }

    public static class SpecialSymbolsToken extends StreamToken {
    }

    public static class WordToken extends StreamToken {
    }

}
