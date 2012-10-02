/**
 * 
 */
package org.ubimix.commons.parser.xml;

import org.ubimix.commons.parser.base.DelimitedTextTokenizer;

/**
 * @author kotelnikov
 */
public class ProcessingInstructionTokenizer extends DelimitedTextTokenizer {

    public static final ProcessingInstructionTokenizer INSTANCE = new ProcessingInstructionTokenizer();

    public ProcessingInstructionTokenizer() {
        this(XMLDict.PROCESSING);
    }

    /**
     * 
     */
    public ProcessingInstructionTokenizer(String key) {
        super(key, "<?", "?>");
    }

}
