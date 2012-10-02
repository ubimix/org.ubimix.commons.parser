/**
 * 
 */
package org.ubimix.commons.parser;

import org.ubimix.commons.parser.CharStream.Marker;
import org.ubimix.commons.parser.CharStream.Pointer;

/**
 * @author kotelnikov
 */
public class StreamToken {

    private Pointer fBegin;

    private boolean fClose;

    private Pointer fEnd;

    private String fKey;

    private StreamToken fNext;

    private boolean fOpen;

    private StreamToken fPrev;

    private String fToken;

    /**
     * @param key
     * @param open
     */
    public StreamToken(
        String key,
        boolean open,
        boolean close,
        Pointer begin,
        Pointer end,
        String str) {
        fKey = key;
        fOpen = open;
        fClose = close;
        init(begin, end, str);
    }

    public StreamToken(String key, CharStream stream, Marker marker) {
        this(key, marker.getPointer(), stream.getPointer(), marker
            .getSubstring());
    }

    /**
     * @param key
     */
    public StreamToken(String key, Pointer begin, Pointer end, String str) {
        this(key, true, false, begin, end, str);
    }

    private void bind(StreamToken prev, StreamToken next) {
        if (prev != null) {
            prev.fNext = next;
        }
        if (next != null) {
            next.fPrev = prev;
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof StreamToken)) {
            return false;
        }
        StreamToken o = (StreamToken) obj;
        return fKey.equals(o.fKey);
    }

    /**
     * @return the begin
     */
    public Pointer getBegin() {
        return fBegin;
    }

    /**
     * @see org.statewalker.dict.ITextToken#getContent()
     */
    public String getContent() {
        return getToken();
    }

    /**
     * @return the end
     */
    public Pointer getEnd() {
        return fEnd;
    }

    /**
     * @see org.statewalker.dict.IToken#getKey()
     */
    public String getKey() {
        return fKey;
    }

    /**
     * @return the next
     */
    public StreamToken getNext() {
        return fNext;
    }

    /**
     * @return the prev
     */
    public StreamToken getPrev() {
        return fPrev;
    }

    /**
     * @return the token
     */
    public String getToken() {
        return fToken;
    }

    @Override
    public int hashCode() {
        return fKey.hashCode();
    }

    public void init(CharStream stream, Marker marker) {
        fBegin = marker.getPointer();
        fEnd = stream.getPointer();
        fToken = marker.getSubstring();
    }

    protected void init(Pointer begin, Pointer end, String str) {
        fBegin = begin;
        fEnd = end;
        fToken = str;
    }

    /**
     * @param next the next to set
     */
    public void insertAfter(StreamToken next) {
        bind(this, next);
    }

    /**
     * @param prev the prev to set
     */
    public void insertBefore(StreamToken prev) {
        bind(prev, this);
    }

    /**
     * @see org.statewalker.dict.IToken#isClose()
     */
    public boolean isClose() {
        return fClose;
    }

    /**
     * @see org.statewalker.dict.IToken#isOpen()
     */
    public boolean isOpen() {
        return fOpen;
    }

    public void remove(StreamToken token) {
        if (token == null) {
            return;
        }
        StreamToken next = token.fNext;
        StreamToken prev = token.fPrev;
        if (next != null) {
            next.fPrev = prev;
        }
        if (prev != null) {
            prev.fNext = next;
        }
        token.fNext = token.fPrev = null;
    }

    public void setBegin(Pointer begin) {
        fBegin = begin;
    }

    public void setClose(boolean close) {
        fClose = close;
    }

    public void setEnd(Pointer end) {
        fEnd = end;
    }

    public void setKey(String key) {
        fKey = key;
    }

    public void setOpen(boolean open) {
        fOpen = open;
    }

    public void setToken(String token) {
        fToken = token;
    }

    @Override
    public String toString() {
        return getKey() + "[" + fBegin + "-" + fEnd + "]:" + fToken;
    }

}
