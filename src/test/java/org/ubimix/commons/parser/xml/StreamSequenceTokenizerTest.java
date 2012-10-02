/**
 * 
 */
package org.ubimix.commons.parser.xml;

import junit.framework.TestCase;

import org.ubimix.commons.parser.CharStream;
import org.ubimix.commons.parser.CompositeTokenizer;
import org.ubimix.commons.parser.ITokenizer;
import org.ubimix.commons.parser.StreamToken;
import org.ubimix.commons.parser.base.SequenceTokenizer;
import org.ubimix.commons.parser.text.TextTokenizer;

/**
 * @author kotelnikov
 */
public class StreamSequenceTokenizerTest extends TestCase {

    /**
     * @param name
     */
    public StreamSequenceTokenizerTest(String name) {
        super(name);
    }

    private ITokenizer newSequenceTokenizer(String beginSequence) {
        return new SequenceTokenizer("begin", beginSequence);
    }

    private CompositeTokenizer newTokenizer(
        String beginSequence,
        String endSequence) {
        CompositeTokenizer tokenizer = new CompositeTokenizer();
        tokenizer.addTokenizer(new SequenceTokenizer("begin", beginSequence));
        tokenizer.addTokenizer(new SequenceTokenizer("end", endSequence));
        tokenizer.addTokenizer(new TextTokenizer());
        return tokenizer;
    }

    public void test() throws Exception {
        ITokenizer tokenizer;
        tokenizer = newTokenizer("<!--", "-->");
        testTokenizer(tokenizer, "<!", "[<][!]");

        tokenizer = newSequenceTokenizer("<!--");
        testTokenizer(tokenizer, "<!", null);
        testTokenizer(tokenizer, "<!-", null);
        testTokenizer(tokenizer, "<!--", "[<!--]");

        tokenizer = newSequenceTokenizer("<-");
        testTokenizer(tokenizer, "<", null);
        testTokenizer(tokenizer, "<-", "[<-]");

        tokenizer = newTokenizer("<!--", "-->");
        testTokenizer(tokenizer, "<!", "[<][!]");
        testTokenizer(tokenizer, "-->", "[-->]");
        testTokenizer(tokenizer, "toto -->", "[toto][ ][-->]");
        testTokenizer(tokenizer, " -->", "[ ][-->]");
        testTokenizer(tokenizer, "<!-", "[<][!][-]");
        testTokenizer(tokenizer, "<!- ->", "[<][!][-][ ][-][>]");
        testTokenizer(tokenizer, "<!- -->", "[<][!][-][ ][-->]");
        testTokenizer(tokenizer, "<!- toto -->", "[<][!][-][ ][toto][ ][-->]");
        testTokenizer(tokenizer, "<!- toto ->", "[<][!][-][ ][toto][ ][-][>]");

        tokenizer = newTokenizer("<", ">");
        testTokenizer(tokenizer, "a", "[a]");
        testTokenizer(tokenizer, "<", "[<]");
        testTokenizer(tokenizer, ">", "[>]");
        testTokenizer(tokenizer, "<a>", "[<][a][>]");

        tokenizer = newTokenizer("<(", ")>");
        testTokenizer(tokenizer, "a", "[a]");
        testTokenizer(tokenizer, "<(", "[<(]");
        testTokenizer(tokenizer, ")>", "[)>]");
        testTokenizer(tokenizer, "<(a)>", "[<(][a][)>]");

        tokenizer = newTokenizer("<!--", "-->");
        testTokenizer(tokenizer, "1245 678", "[1245][ ][678]");
        testTokenizer(tokenizer, "<!- toto ->", "[<][!][-][ ][toto][ ][-][>]");
        testTokenizer(tokenizer, "<!-- toto -->", "[<!--][ ][toto][ ][-->]");
    }

    private void testTokenizer(ITokenizer tokenizer, String str, String control) {
        StringBuilder first = new StringBuilder();
        StringBuilder second = new StringBuilder();
        CharStream stream = new CharStream(str);
        while (true) {
            StreamToken token = tokenizer.read(stream);
            if (token == null)
                break;
            first.append(token.getToken());
            if (control != null) {
                second.append("[").append(token.getToken()).append("]");
            }
        }
        if (control != null) {
            assertEquals(str, first.toString());
            assertEquals(control, second.toString());
        } else {
            assertEquals("", first.toString());
        }
    }
}
