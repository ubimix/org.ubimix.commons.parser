/**
 * 
 */
package org.ubimix.commons.parser.xml;

import org.ubimix.commons.parser.StreamToken;
import org.ubimix.commons.parser.CharStream.Pointer;
import org.ubimix.commons.parser.base.DelimitedTextTokenizer;

public class CommentTokenizer extends DelimitedTextTokenizer {

    public static class CommentToken extends StreamToken {

        public CommentToken(String key, Pointer begin, Pointer end, String str) {
            super(key, begin, end, str);
        }

        public String getCommentContent() {
            String str = getContent();
            str = str.substring(BEGIN.length());
            str = str.substring(0, str.length() - END.length());
            return str;
        }

    }

    private static final String BEGIN = "<!--";

    private static final String END = "-->";

    public static final CommentTokenizer INSTANCE = new CommentTokenizer();

    public CommentTokenizer() {
        this(XMLDict.COMMENT);
    }

    public CommentTokenizer(String key) {
        super(key, BEGIN, END);
    }

    @Override
    protected StreamToken newToken(
        String key,
        Pointer begin,
        Pointer end,
        String str,
        int level) {
        return new CommentToken(key, begin, end, str);
    }

}