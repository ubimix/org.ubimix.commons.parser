/**
 * 
 */
package org.ubimix.commons.parser.xml;

import org.ubimix.commons.parser.CharStream;
import org.ubimix.commons.parser.StreamToken;
import org.ubimix.commons.parser.CharStream.Marker;
import org.ubimix.commons.parser.CharStream.Pointer;

/**
 * @author kotelnikov
 */
public class CDATAToken extends StreamToken {

    public CDATAToken(String key, CharStream stream, Marker marker) {
        super(key, stream, marker);
        setOpen(true);
        setClose(true);
    }

    /**
     * @param key
     * @param begin
     * @param end
     * @param str
     */
    public CDATAToken(String key, Pointer begin, Pointer end, String str) {
        super(key, true, true, begin, end, str);
    }

    public String getCDATAValue() {
        String token = getToken();
        if (token.startsWith("<![CDATA[")) {
            token = token.substring("<![CDATA[".length());
            if (token.endsWith("]]>")) {
                token = token.substring(0, token.length() - "]]>".length());
            }
        }
        return token;
    }

}
