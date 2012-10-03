/**
 * 
 */
package org.ubimix.commons.parser;

import junit.framework.TestCase;

import org.ubimix.commons.parser.base.QuotedValueTokenizer;

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
        QuotedValueTokenizer tokenizer = new QuotedValueTokenizer(strict);
        StreamToken token = tokenizer.read(stream);
        if (control == null) {
            assertNull(token);
        } else {
            assertNotNull(token);
            assertEquals(control, token.getText());
        }
    }
}
