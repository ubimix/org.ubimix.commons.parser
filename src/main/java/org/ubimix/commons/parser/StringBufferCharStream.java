/**
 * 
 */
package org.ubimix.commons.parser;

import org.ubimix.commons.parser.CharStream.Pointer;

/**
 * @author kotelnikov
 */
public class StringBufferCharStream implements ICharStream {

    private char fChar;

    private int fColumn;

    private int fLine;

    private IPointer fPointer;

    private int fPos = -1;

    private String fString;

    /**
     * 
     */
    public StringBufferCharStream(String str) {
        fString = str;
    }

    protected void checkPos() {
        if (fPos < 0) {
            incPos();
        }
    }

    /**
     * @see org.ubimix.commons.parser.ICharStream#getChar()
     */
    @Override
    public char getChar() {
        checkPos();
        return fChar;
    }

    /**
     * @see org.ubimix.commons.parser.ICharStream#getPointer()
     */
    @Override
    public IPointer getPointer() {
        if (fPointer == null) {
            checkPos();
            fPointer = new Pointer(fPos, fColumn, fLine);
        }
        return fPointer;
    }

    /**
     * @see org.ubimix.commons.parser.ICharStream#incPos()
     */
    @Override
    public boolean incPos() {
        fChar = 0;
        if (fPos > fString.length()) {
            return false;
        }
        int prevPos = fPos;
        fPos++;
        if (fPos < fString.length()) {
            boolean newLine = fPos == 0;
            boolean incLine = false;
            if (prevPos >= 0) {
                char ch = fString.charAt(prevPos);
                if (ch == '\n' || ch == '\r') {
                    newLine = true;
                    incLine = true;
                    char prevChar = prevPos > 0
                        ? fString.charAt(prevPos - 1)
                        : 0;
                    if (ch == '\n' && prevChar == '\r') {
                        incLine = false;
                    }
                }
            }
            fChar = fString.charAt(fPos);
            if (newLine) {
                fColumn = 0;
            } else {
                fColumn++;
            }
            if (incLine) {
                fLine++;
            }
            fPointer = null;
        } else {
            fChar = 0;
            return false;
        }
        fChar = fString.charAt(fPos);
        return true;
    }

    /**
     * @see org.ubimix.commons.parser.ICharStream#isTerminated()
     */
    @Override
    public boolean isTerminated() {
        checkPos();
        return fPos >= fString.length();
    }

    /**
     * @see org.ubimix.commons.parser.ICharStream#markPosition()
     */
    @Override
    public IMarker markPosition() {
        checkPos();
        return new IMarker() {

            char ch = fChar;

            int col = fColumn;

            int line = fLine;

            IPointer pointer;

            int pos = fPos;

            @Override
            public void close(boolean reset) {
                if (reset) {
                    fPos = pos;
                    fColumn = col;
                    fLine = line;
                    fPointer = null;
                    fChar = ch;
                }
            }

            @Override
            public IPointer getPointer() {
                if (pointer == null) {
                    pointer = new Pointer(pos, col, line);
                }
                return pointer;
            }

            @Override
            public String getSubstring(int pos, int len) {
                return fString.substring(pos, pos + len);
            }

            @Override
            public String toString() {
                int len = fPos - pos;
                return "Marker("
                    + pos
                    + "["
                    + line
                    + ":"
                    + col
                    + "]"
                    + ":'"
                    + getSubstring(pos, len)
                    + "')";
            }

        };
    }

}
