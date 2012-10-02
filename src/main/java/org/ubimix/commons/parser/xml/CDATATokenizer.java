/**
 * 
 */
package org.ubimix.commons.parser.xml;

import org.ubimix.commons.parser.StreamToken;
import org.ubimix.commons.parser.CharStream.Pointer;
import org.ubimix.commons.parser.base.DelimitedTextTokenizer;

public class CDATATokenizer extends DelimitedTextTokenizer {

    public static final CDATATokenizer INSTANCE = new CDATATokenizer();

    public CDATATokenizer() {
        this(XMLDict.CDATA);
    }

    public CDATATokenizer(String key) {
        super(key, "<![CDATA[", "]]>");
    }

    @Override
    protected StreamToken newToken(
        String key,
        Pointer begin,
        Pointer end,
        String str,
        int level) {
        return new CDATAToken(key, begin, end, str);
    }
}