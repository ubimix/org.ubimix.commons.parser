/**
 * 
 */
package org.ubimix.commons.parser.json;

import org.ubimix.commons.parser.CompositeTokenizer;
import org.ubimix.commons.parser.base.QuotedValueTokenizer;
import org.ubimix.commons.parser.base.SequenceTokenizer;
import org.ubimix.commons.parser.base.SimpleTokenizer;
import org.ubimix.commons.parser.text.TextDict;

/**
 * @author kotelnikov
 */
public class JsonTokenizer extends CompositeTokenizer {

    public JsonTokenizer() {
        addTokenizer(new SimpleTokenizer(TextDict.SPACES) {
            @Override
            protected boolean checkChar(char ch, int pos) {
                return Character.isSpaceChar(ch)
                    || ch == '\r'
                    || ch == '\n'
                    || ch == '\t';
            }
        });
        addTokenizer(new QuotedValueTokenizer(JsonDict.STRING));
        addTokenizer(new SequenceTokenizer(JsonDict.COL, ":"));
        addTokenizer(new SequenceTokenizer(JsonDict.ARRAY_BEGIN, "["));
        addTokenizer(new SequenceTokenizer(JsonDict.ARRAY_END, "]"));
        addTokenizer(new SequenceTokenizer(JsonDict.OBJ_BEGIN, "{"));
        addTokenizer(new SequenceTokenizer(JsonDict.OBJ_END, "}"));
        addTokenizer(new SequenceTokenizer(JsonDict.NULL, "null"));
        addTokenizer(new SequenceTokenizer(JsonDict.COMMA, ","));
        addTokenizer(new SimpleTokenizer(JsonDict.STRING) {
            @Override
            protected boolean checkChar(char ch, int pos) {
                if (Character.isLetterOrDigit(ch) || ch == '+' || ch == '-') {
                    return true;
                }
                return pos > 0 && (ch == '.' || ch == ';');
            }
        });
    }

}
