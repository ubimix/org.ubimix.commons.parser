/**
 * 
 */
package org.ubimix.commons.parser;

import org.ubimix.commons.parser.text.TextDict;

/**
 * @author kotelnikov
 */
public abstract class AbstractParser<L extends IParserListener> {

    /**
     * @author kotelnikov
     */
    public static class ParseError extends RuntimeException {

        private static final long serialVersionUID = -3496147491438922289L;

        private ICharStream.IPointer fPointer;

        public ParseError() {
            super();
        }

        public ParseError(String message) {
            super(message);
        }

        public ParseError(String message, Throwable cause) {
            super(message, cause);
        }

        public ParseError(Throwable cause) {
            super(cause);
        }

        public ICharStream.IPointer getPointer() {
            return fPointer;
        }

        public void setPointer(ICharStream.IPointer pointer) {
            fPointer = pointer;
        }

    }

    protected L fListener;

    private ICharStream fStream;

    private StreamToken fToken;

    private final ITokenizer fTokenizer;

    /**
     * 
     */
    public AbstractParser(ITokenizer tokenizer) {
        fTokenizer = tokenizer;
    }

    protected abstract void doParse();

    public ICharStream getStream() {
        return fStream;
    }

    protected StreamToken getToken(boolean load) {
        if (fToken == null || load) {
            fToken = fTokenizer.read(fStream);
        }
        return fToken;
    }

    protected void onError(String message) {
        if (fListener.reportErrors()) {
            ParseError error = new ParseError(message);
            error.setPointer(fStream.getPointer());
            fListener.onError(message, error);
        }
    }

    protected void onError(String message, Throwable t) {
        if (fListener.reportErrors()) {
            ParseError error = new ParseError(message, t);
            error.setPointer(fStream.getPointer());
            fListener.onError(message, error);
        }
    }

    public final void parse(ICharStream stream, L listener) {
        try {
            fStream = stream;
            fListener = listener;
            doParse();
        } finally {
            fStream = null;
            fListener = null;
        }
    }

    public final void parse(String str, L listener) {
        parse(new CharStream(str), listener);
    }

    protected StreamToken skipSpaces(boolean loadNext) {
        StreamToken token = getToken(loadNext);
        if (token != null && token instanceof TextDict.SpacesToken) {
            token = getToken(true);
        }
        return token;
    }
}
