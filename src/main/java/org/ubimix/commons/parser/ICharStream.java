package org.ubimix.commons.parser;

/**
 * @author kotelnikov
 */
public interface ICharStream {

    /**
     * Stream marker used to temporarily fix position on the stream. When this
     * marker is closed (using the {@link IMarker#close(boolean)} method) with
     * the <code>reset==true</code> parameter then the stream is reset to the
     * begin of the marker - ie the top stream pointer is returned to the marker
     * initial position.
     * 
     * @author kotelnikov
     */
    public interface IMarker {

        /**
         * Closes this marker and eventually resets the underlying stream (only
         * if the <code>reset</code> parameter of this method is
         * <code>true</code>). If the marker is reset (the <code>reset</code>
         * parameter is <code>true</code>) then the stream starts characters
         * from the initial marker position. Otherwise the stream continue to
         * read new characters.
         * 
         * @param reset if this flag is <code>true</code> then the stream is
         *        re-starts reading from the begin of this marker.
         */
        void close(boolean reset);

        /**
         * Returns the start stream position of this marker.
         * 
         * @return the marker start position
         */
        IPointer getPointer();

        /**
         * Returns a string generated from characters covered by this marker
         * from the specified range.
         * 
         * @param pos start position of the string; this value should be bigger
         *        than the start position of this marker
         * @param len the length of the string to return; this value should be
         *        less or equal of the pointer returned by the stream
         * @return a substring created from marker characters covered by the
         *         specified range
         */
        String getSubstring(int pos, int len);

    }

    /**
     * Character stream position. This object contains column, line and absolute
     * position of the current character in the stream.
     * 
     * @author kotelnikov
     */
    public interface IPointer extends Comparable<IPointer> {

        /**
         * @return the column (line position) of the character from the
         *         specified absolute stream position
         */
        int getColumn();

        /**
         * @return the line number of the current character
         */
        int getLine();

        /**
         * @return an absolute position of a stream character
         */
        int getPos();

    }

    /**
     * Returns the character from the current stream position. If this stream is
     * finished then this method returns 0.
     * 
     * @return the character from the current stream position
     */
    char getChar();

    /**
     * Returns a pointer defining absolute position of the current character.
     * 
     * @return a pointer defining absolute position of the current character
     */
    ICharStream.IPointer getPointer();

    /**
     * Reads the next character from the stream and returns <code>true</code> if
     * this operation was successful. If this method returns <code>false</code>
     * then it means that there is no characters in the stream. All next calls
     * of this method should not change the state of the stream and they always
     * return <code>false</code>. If this method returns <code>false</code> then
     * the {@link #isTerminated()} method returns <code>true</code>.
     * 
     * @return <code>true</code> if a next character was successfully loaded
     */
    boolean incPos();

    /**
     * Returns <code>true</code> if the stream is over and there is no more
     * available characters.
     * 
     * @return <code>true</code> if the stream is over and there is no more
     *         available characters.
     */
    boolean isTerminated();

    /**
     * @return a newly created marker
     */
    ICharStream.IMarker markPosition();

}