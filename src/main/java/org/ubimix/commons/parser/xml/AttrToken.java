/**
 * 
 */
package org.ubimix.commons.parser.xml;

import org.ubimix.commons.parser.StreamToken;
import org.ubimix.commons.parser.CharStream.Pointer;

/**
 * @author kotelnikov
 */
public class AttrToken extends StreamToken {

    private String fName;

    private Pointer fNameBegin;

    private Pointer fNameEnd;

    private String fValue;

    private Pointer fValueBegin;

    private Pointer fValueEnd;

    public AttrToken(
        String key,
        boolean open,
        boolean close,
        Pointer begin,
        Pointer end,
        String str) {
        super(key, open, close, begin, end, str);
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
     * @return the value
     */
    public String getValue() {
        return fValue;
    }

    /**
     * @return the valueBegin
     */
    public Pointer getValueBegin() {
        return fValueBegin;
    }

    /**
     * @return the valueEnd
     */
    public Pointer getValueEnd() {
        return fValueEnd;
    }

    public String getValueOnly() {
        String value = getValue();
        if (value.startsWith("'") || value.startsWith("\""))
            value = value.substring(1);
        if (value.endsWith("'") || value.endsWith("\""))
            value = value.substring(0, value.length() - 1);
        return value;
    }

    void setName(Pointer begin, Pointer end, String str) {
        fNameBegin = begin;
        fNameEnd = end;
        fName = str;
    }

    void setValue(Pointer begin, Pointer end, String str) {
        fValueBegin = begin;
        fValueEnd = end;
        fValue = str;
    }

    @Override
    public String toString() {
        return super.toString() + "(" + fName + "=" + fValue + ")";
    }

}