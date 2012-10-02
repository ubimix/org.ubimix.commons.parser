/**
 * 
 */
package org.ubimix.commons.parser;

import org.ubimix.commons.parser.CharStream;
import org.ubimix.commons.parser.StreamToken;
import org.ubimix.commons.parser.base.QuotedValueTokenizer;

import junit.framework.TestCase;

/**
 * @author kotelnikov
 */
public class QuotedValueTokenizerTest extends TestCase {

    /**
     * @param name
     */
    public QuotedValueTokenizerTest(String name) {
        super(name);
    }

    public void test() throws Exception {
        test("a", null);
        test("'a", null);
        test("'a", "'a", false);
        test("'a'", "'a'");
        test("'a' xxx ", "'a'");
        test("\"a", null);
        test("\"a", "\"a", false);
        test("\"a'", null);
        test("\"a'", "\"a'", false);
        test("\"a\"", "\"a\"");
        test("\"a\" xxx ", "\"a\"");
    }

    private void test(String str, String control) {
        test(str, control, true);
    }

    private void test(String str, String control, final boolean strict) {
        CharStream stream = new CharStream(str);
        QuotedValueTokenizer tokenizer = new QuotedValueTokenizer("x") {
            @Override
            protected boolean strictPatterns() {
                return strict;
            }
        };
        StreamToken token = tokenizer.read(stream);
        if (control == null) {
            assertNull(token);
        } else {
            assertNotNull(token);
            assertEquals(control, token.getContent());
        }
    }
}
