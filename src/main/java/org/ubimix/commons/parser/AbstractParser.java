/**
 * 
 */
package org.ubimix.commons.parser;

import org.ubimix.commons.tokenizer.CharStream;
import org.ubimix.commons.tokenizer.ITokenizer;
import org.ubimix.commons.tokenizer.StreamToken;
import org.ubimix.commons.tokenizer.text.TextDict;

/**
 * @author kotelnikov
 */
public abstract class AbstractParser<L extends AbstractParser.IParserListener> {

    public interface IParserListener {

        void onError(String message, CharStream.Pointer streamPosition);

        boolean reportErrors();
    }

    public static class ParserListener implements IParserListener {

        private boolean fReportErrors;

        @Override
        public void onError(String message, CharStream.Pointer streamPosition) {
            throw new IllegalStateException(message + " Pos: " + streamPosition);
        }

        @Override
        public boolean reportErrors() {
            return fReportErrors;
        }

        public void setReportErrors(boolean reportErrors) {
            fReportErrors = reportErrors;
        }

    }

    protected L fListener;

    private CharStream fStream;

    private StreamToken fToken;

    private final ITokenizer fTokenizer;

    /**
     * 
     */
    public AbstractParser(ITokenizer tokenizer) {
        fTokenizer = tokenizer;
    }

    protected abstract void doParse();

    public CharStream getStream() {
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
            fListener.onError(message, fStream.getPointer());
        }
    }

    public final void parse(CharStream stream, L listener) {
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
        if (token != null && TextDict.SPACES.equals(token.getKey())) {
            token = getToken(true);
        }
        return token;
    }
}
