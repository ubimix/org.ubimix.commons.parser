/**
 * 
 */
package org.ubimix.commons.parser;

/**
 * @author kotelnikov
 */
public class CharStream implements ICharStream {

    /**
     * @author kotelnikov
     */
    public interface ICharLoader {
        int readNext();
    }

    /**
     * @author kotelnikov
     */
    public class Marker implements ICharStream.IMarker {

        private ICharStream.IPointer fMarker = fPointer;

        /**
         * @see org.ubimix.commons.parser.IMarker#close(boolean)
         */
        @Override
        public void close(boolean reset) {
            if (reset) {
                fPointer = fMarker;
            }
            fMarkCounter--;
            if (fMarkCounter == 0) {
                fFirstMark = fPointer;
            }
        }

        /**
         * @see org.ubimix.commons.parser.IMarker#getPointer()
         */
        @Override
        public ICharStream.IPointer getPointer() {
            return fMarker;
        }

        /**
         * @see org.ubimix.commons.parser.IMarker#getSubstring(int, int)
         */
        @Override
        public String getSubstring(int pos, int len) {
            if (len < 0) {
                throw new IllegalArgumentException("Length is negative");
            }
            if (pos < fMarker.getPos()) {
                throw new IllegalArgumentException(
                    "Pointer is before the marker");
            }
            if (pos > fPointer.getPos()) {
                throw new IllegalArgumentException(
                    "Pointer is after the end of the stream");
            }
            len = Math.min(fPointer.getPos() - pos, len);
            StringBuilder buf = new StringBuilder();
            for (int i = 0; i < len; i++) {
                char ch = fBuf[(pos + i) % fBuf.length];
                buf.append(ch);
            }
            return buf.toString();
        }

        @Override
        public String toString() {
            int len = fPointer.getPos() - fMarker.getPos();
            return "Marker("
                + fMarker
                + ":'"
                + getSubstring(fMarker.getPos(), len)
                + "')";
        }
    }

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

    private static final int DELTA = 2;

    public static final ICharStream.IPointer START = new Pointer(0, 0, 0);

    private char[] fBuf = new char[10];

    private ICharStream.IPointer fEnd;

    private ICharStream.IPointer fFirstMark;

    private ICharLoader fLoader;

    private int fMarkCounter;

    private ICharStream.IPointer fPointer = null;

    private ICharStream.IPointer fTop = START;

    public CharStream(ICharLoader loader) {
        fLoader = loader;
    }

    public CharStream(String str) {
        this(new SimpleCharLoader(str));
    }

    /**
     * 
     */
    private void checkPointer() {
        if (fPointer == null) {
            incPos();
        }
    }

    /**
     * @see org.ubimix.commons.parser.ICharStream#getChar()
     */
    @Override
    public char getChar() {
        checkPointer();
        return (char) (fBuf[fPointer.getPos() % fBuf.length] & 0xFFFF);
    }

    /**
     * @see org.ubimix.commons.parser.ICharStream#getPointer()
     */
    @Override
    public ICharStream.IPointer getPointer() {
        checkPointer();
        return fPointer;
    }

    /**
     * @param p
     * @param newLine
     * @return
     */
    private ICharStream.IPointer incPointer(ICharStream.IPointer p) {
        if (p == null) {
            return START;
        }
        char ch = fBuf[p.getPos() % fBuf.length];
        boolean newLine = false;
        boolean incLine = false;
        if (ch == '\r') {
            newLine = true;
            incLine = true;
        } else if (ch == '\n') {
            newLine = true;
            char prev = p.getPos() > 0
                ? fBuf[(p.getPos() - 1) % fBuf.length]
                : '\0';
            incLine = prev != '\r';
        }
        int c = newLine ? 0 : p.getColumn() + 1;
        int l = incLine ? p.getLine() + 1 : p.getLine();
        return new Pointer(p.getPos() + 1, c, l);
    }

    /**
     * @see org.ubimix.commons.parser.ICharStream#incPos()
     */
    @Override
    public boolean incPos() {
        fPointer = incPointer(fPointer);
        boolean result = false;
        if (fPointer.getPos() >= fTop.getPos() - 1) {
            if (fEnd != null) {
                fPointer = fEnd;
            } else {
                result = true;
                int val = fLoader.readNext();
                if (fMarkCounter > 0
                    && fTop.getPos() - fFirstMark.getPos() + DELTA >= fBuf.length) {
                    int len = fBuf.length * 3 / 2;
                    char[] buf = new char[len];
                    for (int pos = fFirstMark.getPos(); pos <= fTop.getPos()
                        + DELTA; pos++) {
                        buf[pos % buf.length] = fBuf[pos % fBuf.length];
                    }
                    fBuf = buf;
                }
                char ch;
                if (val >= 0) {
                    ch = (char) (val & 0xFFFF);
                } else {
                    fEnd = fTop;
                    ch = '\0';
                    result = false;
                }
                fBuf[fTop.getPos() % fBuf.length] = ch;
                fTop = incPointer(fTop);
            }
        }
        if (fMarkCounter == 0) {
            fFirstMark = fPointer;
        }
        return result;
    }

    /**
     * @see org.ubimix.commons.parser.ICharStream#isTerminated()
     */
    @Override
    public boolean isTerminated() {
        return fEnd != null && fEnd.equals(fPointer);
    }

    /**
     * @see org.ubimix.commons.parser.ICharStream#markPosition()
     */
    @Override
    public ICharStream.IMarker markPosition() {
        checkPointer();
        if (fMarkCounter == 0) {
            fFirstMark = fPointer;
        }
        fMarkCounter++;
        return new Marker();
    }

}