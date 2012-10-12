package org.ubimix.commons.parser;

/**
 * @author kotelnikov
 */
public class StreamToken {

    private ICharStream.IPointer fBegin;

    private ICharStream.IPointer fEnd;

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

    public ICharStream.IPointer getBegin() {
        return fBegin;
    }

    public ICharStream.IPointer getEnd() {
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
        ICharStream.IPointer begin,
        ICharStream.IPointer end,
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