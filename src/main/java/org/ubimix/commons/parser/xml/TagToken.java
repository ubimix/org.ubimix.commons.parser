/**
 * 
 */
package org.ubimix.commons.parser.xml;

import java.util.LinkedHashMap;
import java.util.Map;

import org.ubimix.commons.parser.StreamToken;
import org.ubimix.commons.parser.CharStream.Pointer;

public class TagToken extends StreamToken {

    private Map<String, String> fAttributes;

    private AttrToken fFirstAttribute;

    private String fName;

    private Pointer fNameBegin;

    private Pointer fNameEnd;

    public TagToken(
        String key,
        boolean open,
        boolean close,
        Pointer begin,
        Pointer end,
        String str) {
        super(key, open, close, begin, end, str);
    }

    public Map<String, String> getAttributes() {
        if (fAttributes == null) {
            fAttributes = new LinkedHashMap<String, String>();
            AttrToken attr = getFirstAttribute();
            while (attr != null) {
                String name = attr.getName();
                String value = attr.getValueOnly();
                fAttributes.put(name, value);
                attr = (AttrToken) attr.getNext();
            }
        }
        return fAttributes;
    }

    /**
     * @return the firstAttribute
     */
    public AttrToken getFirstAttribute() {
        return fFirstAttribute;
    }

    /**
     * @return the name
     */
    public String getName() {
        return fName;
    }

    /**
     * @return the nameBegin
     */
    public Pointer getNameBegin() {
        return fNameBegin;
    }

    /**
     * @return the nameEnd
     */
    public Pointer getNameEnd() {
        return fNameEnd;
    }

    /**
     * @param firstAttribute the firstAttribute to set
     */
    public void setFirstAttribute(AttrToken firstAttribute) {
        fFirstAttribute = firstAttribute;
    }

    public void setName(Pointer begin, Pointer end, String str) {
        fNameBegin = begin;
        fNameEnd = end;
        fName = str;
    }

    @Override
    public String toString() {
        return super.toString() + "(" + fName + ")";
    }

}