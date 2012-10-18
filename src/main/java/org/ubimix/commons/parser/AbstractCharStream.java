/**
 * 
 */
package org.ubimix.commons.parser;

/**
 * @author kotelnikov
 */
public abstract class AbstractCharStream implements ICharStream {

    /**
     * @author kotelnikov
     */
    public static class Pointer implements ICharStream.IPointer {

        public final int column;

        public final int line;

        public final int pos;

        public Pointer(int pos, int column, int line) {
            this.pos = pos;
            this.line = line;
            this.column = column;
        }

        /**
         * @see org.ubimix.commons.parser.IPointer#compareTo(org.ubimix.commons.parser.CharStream.IPointer)
         */
        @Override
        public int compareTo(ICharStream.IPointer o) {
            int result = pos - o.getPos();
            if (result != 0) {
                return result;
            }
            result = line - o.getLine();
            if (result != 0) {
                return result;
            }
            result = column - o.getColumn();
            if (result != 0) {
                return result;
            }
            return 0;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof ICharStream.IPointer)) {
                return false;
            }
            ICharStream.IPointer p = (ICharStream.IPointer) obj;
            return pos == p.getPos();
            // return pos == p.pos && line == p.line && column == p.column;
        }

        @Override
        public int getColumn() {
            return column;
        }

        @Override
        public int getLine() {
            return line;
        }

        @Override
        public int getPos() {
            return pos;
        }

        @Override
        public int hashCode() {
            return pos;
        }

        /**
         * @param pointer
         * @return
         */
        public int len(ICharStream.IPointer pointer) {
            return pos - pointer.getPos();
        }

        /**
         * @see java.lang.Object#toString()
         */
        @Override
        public String toString() {
            return pos + "[" + line + ":" + column + "]";
        }

    }

    /**
     * @author kotelnikov
     */
    protected static class StreamMarker implements IMarker {

        private char[] fMarkerChars = { 0, 0, 0 };

        private int fMarkerColumn;

        private int fMarkerLine;

        private IPointer fMarkerPointer;

        private int fMarkerPos;

        private AbstractCharStream fStream;

        public StreamMarker(AbstractCharStream stream) {
            fStream = stream;
            System.arraycopy(
                fStream.fChars,
                0,
                fMarkerChars,
                0,
                fMarkerChars.length);
            fMarkerColumn = fStream.fColumn;
            fMarkerLine = fStream.fLine;
            fMarkerPos = fStream.fPos;
            fMarkerPointer = fStream.getPointer();
            fStream.onMarkerOpen(this);
        }

        @Override
        public void close(boolean reset) {
            fStream.onMarkerClose(this, reset);
            if (reset) {
                System.arraycopy(
                    fMarkerChars,
                    0,
                    fStream.fChars,
                    0,
                    fStream.fChars.length);
                fStream.fColumn = fMarkerColumn;
                fStream.fLine = fMarkerLine;
                fStream.fPos = fMarkerPos;
                fStream.fPointer = fMarkerPointer;
            }
        }

        public int getColumn() {
            return fMarkerColumn;
        }

        public int getLine() {
            return fMarkerLine;
        }

        @Override
        public IPointer getPointer() {
            if (fMarkerPointer == null) {
                fMarkerPointer = fStream.newPointer(
                    fMarkerPos,
                    fMarkerColumn,
                    fMarkerLine);
            }
            return fMarkerPointer;
        }

        public int getPos() {
            return fMarkerPos;
        }

        @Override
        public String getSubstring(int pos, int len) {
            return fStream.getBufferedSubstring(pos, len);
        }

        @Override
        public String toString() {
            int len = fStream.fPos - fMarkerPos;
            return "Marker("
                + fMarkerPos
                + "["
                + fMarkerLine
                + ":"
                + fMarkerColumn
                + "]"
                + ":'"
                + getSubstring(fMarkerPos, len)
                + "')";
        }
    }

    protected char[] fChars = { 0, 0, 0 };

    protected int fColumn;

    protected int fLine;

    protected IPointer fPointer;

    protected int fPos = -1;

    protected boolean fStreamFinished;

    protected int fStreamLength;

    /**
     * 
     */
    public AbstractCharStream() {
    }

    protected void checkPos() {
        if (fPos < 0) {
            initPos();
        }
    }

    protected abstract String getBufferedSubstring(int pos, int len);

    /**
     * @see org.ubimix.commons.parser.ICharStream#getChar()
     */
    @Override
    public char getChar() {
        checkPos();
        return fChars[fPos % fChars.length];
    }

    /**
     * @see org.ubimix.commons.parser.ICharStream#getPointer()
     */
    @Override
    public IPointer getPointer() {
        if (fPointer == null) {
            checkPos();
            fPointer = newPointer(fPos, fColumn, fLine);
        }
        return fPointer;
    }

    /**
     * @see org.ubimix.commons.parser.ICharStream#incPos()
     */
    @Override
    public boolean incPos() {
        if (fStreamFinished && fPos >= fStreamLength) {
            return false;
        }
        int nextCharCode = -1;
        fPos++;
        nextCharCode = loadNextChar(fStreamLength, fPos);
        if (fPos == fStreamLength) {
            if (nextCharCode >= 0) {
                fStreamLength++;
            } else {
                fStreamFinished = true;
            }
        }
        fChars[fPos % fChars.length] = nextCharCode >= 0
            ? (char) (nextCharCode & 0xFFFF)
            : 0;
        char ch = fChars[Math.abs((fPos - 1) % fChars.length)];
        boolean newLine = fPos == 0;
        boolean incLine = false;
        if (ch == '\n' || ch == '\r') {
            newLine = true;
            incLine = true;
            char prevChar = fChars[Math.abs((fPos - 2) % fChars.length)];
            if (ch == '\n' && prevChar == '\r') {
                incLine = false;
            }
        }
        if (newLine) {
            fColumn = 0;
        } else {
            fColumn++;
        }
        if (incLine) {
            fLine++;
        }
        fPointer = null;
        return nextCharCode >= 0;
    }

    protected void initPos() {
        incPos();
    }

    /**
     * @see org.ubimix.commons.parser.ICharStream#isTerminated()
     */
    @Override
    public boolean isTerminated() {
        checkPos();
        return fStreamFinished && fPos >= fStreamLength;
    }

    protected abstract int loadNextChar(int length, int nextPos);

    /**
     * @see org.ubimix.commons.parser.ICharStream#markPosition()
     */
    @Override
    public IMarker markPosition() {
        checkPos();
        StreamMarker marker = new StreamMarker(this);
        return marker;
    }

    protected IPointer newPointer(
        int markerPos,
        int markerColumn,
        int markerLine) {
        return new Pointer(markerPos, markerColumn, markerLine);
    }

    protected void onMarkerClose(StreamMarker marker, boolean reset) {
    }

    protected void onMarkerOpen(StreamMarker marker) {
    }
}
