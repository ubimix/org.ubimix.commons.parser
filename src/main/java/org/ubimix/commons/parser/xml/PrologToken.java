/**
 * 
 */
package org.ubimix.commons.parser.xml;

import org.ubimix.commons.parser.StreamToken;
import org.ubimix.commons.parser.CharStream.Pointer;

/**
 * @author kotelnikov
 */
public class PrologToken extends StreamToken {

    private PrologToken fFirstChild;

    public PrologToken(String key, Pointer begin, Pointer end, String str) {
        super(key, begin, end, str);
    }

    public PrologToken getFirstChild() {
        return fFirstChild;
    }

    public void setFirstChild(PrologToken firstChild) {
        fFirstChild = firstChild;
    }

    @Override
    public String toString() {
        return super.toString()
            + (fFirstChild != null ? "[" + fFirstChild + "]" : "");
    }
}