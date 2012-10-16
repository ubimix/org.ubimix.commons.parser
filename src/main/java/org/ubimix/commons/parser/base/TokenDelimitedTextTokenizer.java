/**
 * 
 */
package org.ubimix.commons.parser.base;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.ubimix.commons.parser.AbstractTokenizer;
import org.ubimix.commons.parser.ICharStream;
import org.ubimix.commons.parser.ITokenizer;
import org.ubimix.commons.parser.StreamToken;

/**
 * @author kotelnikov
 */
public class TokenDelimitedTextTokenizer extends AbstractTokenizer {

    public static class DelimitedTextToken extends StreamToken {

        private StreamToken fBeginToken;

        private List<StreamToken> fChildren;

        private StreamToken fEndToken;

        public StreamToken getBeginToken() {
            return fBeginToken;
        }

        public List<StreamToken> getChildren() {
            return fChildren;
        }

        public StreamToken getEndToken() {
            return fEndToken;
        }

        public void init(
            StreamToken beginToken,
            StreamToken endToken,
            List<StreamToken> children) {
            fBeginToken = beginToken;
            fEndToken = endToken;
            fChildren = children != null ? children : Collections
                .<StreamToken> emptyList();
        }
    }

    private boolean fAllowNonLimitedContent;

    private ITokenizer fBeginTokenizer;

    private ITokenizer fEndToknizer;

    private boolean fRecursive;

    public TokenDelimitedTextTokenizer(
        ITokenizer begin,
        ITokenizer end,
        boolean recursive,
        boolean allowNonLimitedContent) {
        fBeginTokenizer = begin;
        fEndToknizer = end;
        fRecursive = recursive;
        fAllowNonLimitedContent = allowNonLimitedContent;
    }

    public TokenDelimitedTextTokenizer(
        String begin,
        String end,
        boolean recursive,
        boolean allowNonLimitedContent) {
        this(
            new SequenceTokenizer(begin),
            new SequenceTokenizer(end),
            recursive,
            allowNonLimitedContent);
    }

    /**
     * Appends a list of child tokens to the parent token. This method should be
     * overloaded in subclasses.
     * 
     * @param parent to this token the first child should be attached
     * @param beginToken start delimiting token
     * @param endToken end delimiting token
     * @param children list of internal tokens; could be <code>null</code>.
     */
    protected void addSubtokens(
        DelimitedTextToken parent,
        StreamToken beginToken,
        StreamToken endToken,
        List<StreamToken> children) {
        parent.init(beginToken, endToken, children);
    }

    public boolean allowNonlimitedTokens() {
        return fAllowNonLimitedContent;
    }

    public ITokenizer getBeginTokenizer() {
        return fBeginTokenizer;
    }

    public ITokenizer getEndToknizer() {
        return fEndToknizer;
    }

    protected boolean isEmptyBeginToken(StreamToken beginToken) {
        return false;
    }

    @Override
    protected StreamToken newToken() {
        return new DelimitedTextToken();
    }

    /**
     * Returns a newly created token corresponding to the given start and end
     * positions. This method could be overloaded in subclasses.
     * 
     * @param begin the start position of the token to create
     * @param end the end position of the token
     * @param str the string representation of the token
     * @param level the level of the token to create (0 for the topmost one)
     * @return a new token with the given parameters
     */
    protected DelimitedTextToken newToken(
        ICharStream.IPointer begin,
        ICharStream.IPointer end,
        String str,
        int level) {
        DelimitedTextToken token = newToken(begin, end, str);
        return token;
    }

    @Override
    public DelimitedTextToken read(ICharStream stream) {
        ICharStream.IMarker marker = stream.markPosition();
        DelimitedTextToken result = null;
        try {
            result = skip(marker, stream, 0);
            return result;
        } finally {
            marker.close(result == null);
        }
    }

    public DelimitedTextToken skip(
        ICharStream.IMarker marker,
        ICharStream stream,
        int level) {
        ICharStream.IPointer begin = stream.getPointer();
        StreamToken beginToken = fBeginTokenizer.read(stream);
        if (beginToken == null) {
            return null;
        }
        DelimitedTextToken result = null;
        if (isEmptyBeginToken(beginToken)) {
            result = newToken(
                beginToken.getBegin(),
                beginToken.getEnd(),
                beginToken.getText(),
                level);
            addSubtokens(result, beginToken, null, null);
        } else {
            StreamToken endToken = null;
            List<StreamToken> children = null;
            while ((endToken = fEndToknizer.read(stream)) == null) {
                if (!skipContentSymbols(stream)) {
                    break;
                }
                if (!fRecursive) {
                    continue;
                }
                StreamToken token = skip(marker, stream, level + 1);
                if (token != null) {
                    if (children == null) {
                        children = new ArrayList<StreamToken>();
                    }
                    children.add(token);
                }
            }
            if (endToken != null || allowNonlimitedTokens()) {
                ICharStream.IPointer end = stream.getPointer();
                String str = getString(marker, begin, end);
                result = newToken(begin, end, str, level);
                addSubtokens(result, beginToken, endToken, children);
            }
        }
        return result;
    }

    protected boolean skipContentSymbols(ICharStream stream) {
        return stream.incPos();
    }
}
