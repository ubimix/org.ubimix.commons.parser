/**
 * 
 */
package org.ubimix.commons.parser;

/**
 * @author kotelnikov
 */
public class CharStream extends AbstractCharStream {

    /**
     * @author kotelnikov
     */
    public interface ICharLoader {
        int readNext();
    }

    private StringBuilder fBuffer = new StringBuilder();

    private int fBufferStartPos = -1;

    private ICharLoader fLoader;

    private int fMarkerCounter;

    /**
     * 
     */
    public CharStream(ICharLoader loader) {
        fLoader = loader;
    }

    public CharStream(String str) {
        this(new SimpleCharLoader(str));
    }

    /**
     * @see org.ubimix.commons.parser.AbstractCharStream#getBufferedSubstring(int,
     *      int)
     */
    @Override
    protected String getBufferedSubstring(int pos, int len) {
        if (fMarkerCounter == 0) {
            throw new IllegalStateException(
                "This method should be used only when a marker is set.");
        }
        int bufferPos = getBufferPos(pos);
        String result = fBuffer.substring(bufferPos, bufferPos + len);
        return result;
    }

    protected int getBufferPos(int pos) {
        return pos - fBufferStartPos;
    }

    /**
     * @see org.ubimix.commons.parser.AbstractCharStream#loadNextChar(int, int)
     */
    @Override
    protected int loadNextChar(int length, int nextPos) {
        int result = -1;
        if (nextPos < length) {
            int bufferPos = getBufferPos(nextPos);
            result = fBuffer.charAt(bufferPos);
        } else {
            result = fLoader.readNext();
            if (fMarkerCounter > 0) {
                if (result >= 0) {
                    char ch = result >= 0 ? (char) (result & 0xFFFF) : 0;
                    fBuffer.append(ch);
                }
            } else if (fBufferStartPos >= 0) {
                fBuffer.delete(0, fBuffer.length());
                fBufferStartPos = -1;
            }
        }
        return result;
    }

    @Override
    protected void onMarkerClose(StreamMarker marker, boolean reset) {
        fMarkerCounter--;
        if (fMarkerCounter == 0 && !reset) {
            int delta = getBufferPos(fPos);
            fBuffer.delete(0, delta);
            if (fBuffer.length() == 0) {
                fBufferStartPos = -1;
            } else {
                fBufferStartPos += delta;
            }
        }
    }

    @Override
    protected void onMarkerOpen(StreamMarker marker) {
        if (fMarkerCounter == 0) {
            if (fBufferStartPos < 0) {
                fBuffer.append(getChar());
                fBufferStartPos = fPos;
            }
        }
        fMarkerCounter++;
    }
}
