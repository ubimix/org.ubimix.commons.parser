/**
 * 
 */
package org.ubimix.commons.parser.text;

/**
 * A basic dictionary defining all basic tokens like "word", "space", "special"
 * (special symbols), and "eol" (end of the line).
 * 
 * @author kotelnikov
 */
public class TextDict {

    public final static String[] _ALL;

    public final static String EOL = "eol";

    public final static String SPACES = "spaces";

    public final static String SPECIAL = "special";

    public final static String WORD = "word";

    static {
        _ALL = new String[] { EOL, SPACES, SPECIAL, WORD };
    }

}
