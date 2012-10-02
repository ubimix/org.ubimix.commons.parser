/**
 * 
 */
package org.ubimix.commons.parser.text;

import org.ubimix.commons.parser.CompositeTokenizer;
import org.ubimix.commons.parser.base.NewLineTokenizer;
import org.ubimix.commons.parser.base.SpaceTokenizer;
import org.ubimix.commons.parser.base.SpecialSymbolTokenizer;
import org.ubimix.commons.parser.base.WordTokenizer;

/**
 * @author kotelnikov
 */
public class TextTokenizer extends CompositeTokenizer {

    public final static TextTokenizer INSTANCE = new TextTokenizer();

    /**
     * 
     */
    public TextTokenizer() {
        addTokenizer(SpaceTokenizer.INSTANCE);
        addTokenizer(WordTokenizer.INSTANCE);
        addTokenizer(NewLineTokenizer.INSTANCE);
        addTokenizer(SpecialSymbolTokenizer.INSTANCE);
    }
}
