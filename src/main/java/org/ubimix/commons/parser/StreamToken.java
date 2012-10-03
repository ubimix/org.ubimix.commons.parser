package org.ubimix.commons.parser;

/**
 * @author kotelnikov
 */
public class StreamToken {

    private CharStream.Pointer fBegin;

    private CharStream.Pointer fEnd;

    private String fText;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof StreamToken)) {
            return false;
        }
        StreamToken o = (StreamToken) obj;
        return equals(getKey(), o.getKey())
            && equals(fBegin, o.fBegin)
            && equals(fEnd, o.fEnd)
            && equals(fText, o.fText);
    }

    private boolean equals(Object a, Object b) {
        return a == null || b == null ? a == b : a.equals(b);
    }

    public CharStream.Pointer getBegin() {
        return fBegin;
    }

    public CharStream.Pointer getEnd() {
        return fEnd;
    }

    protected Object getKey() {
        return getClass();
    }

    /**
     * @return the token
     */
    public String getText() {
        return fText;
    }

    @Override
    public int hashCode() {
        return getKey().hashCode();
    }

    protected void init(
        CharStream.Pointer begin,
        CharStream.Pointer end,
        String text) {
        fBegin = begin;
        fEnd = end;
        fText = text;
    }

    @Override
    public String toString() {
        String typeName = getClass().getName();
        int idx = typeName.lastIndexOf('.');
        if (idx > 0) {
            typeName = typeName.substring(idx + 1);
        }
        return typeName + "[" + fBegin + "-" + fEnd + "]:" + fText;
    }

}