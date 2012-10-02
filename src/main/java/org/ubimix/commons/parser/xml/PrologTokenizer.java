/**
 * 
 */
package org.ubimix.commons.parser.xml;

import org.ubimix.commons.parser.StreamToken;
import org.ubimix.commons.parser.CharStream.Pointer;
import org.ubimix.commons.parser.base.DelimitedTextTokenizer;

/**
 * @author kotelnikov
 */
public class PrologTokenizer extends DelimitedTextTokenizer {

    public static final PrologTokenizer INSTANCE = new PrologTokenizer();

    public PrologTokenizer() {
        this(XMLDict.PROLOG);
    }

    /**
     * 
     */
    public PrologTokenizer(String key) {
        super(key, "<!", ">", true);
    }

    @Override
    protected void addSubtokens(StreamToken parent, StreamToken firstChild) {
        PrologToken p = (PrologToken) parent;
        p.setFirstChild((PrologToken) firstChild);
    }

    @Override
    protected StreamToken newToken(
        String key,
        Pointer begin,
        Pointer end,
        String str,
        int level) {
        return new PrologToken(key, begin, end, str);
    }
}
