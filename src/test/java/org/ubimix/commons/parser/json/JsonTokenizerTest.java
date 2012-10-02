/**
 * 
 */
package org.ubimix.commons.parser.json;

import junit.framework.TestCase;

import org.ubimix.commons.parser.CharStream;
import org.ubimix.commons.parser.StreamToken;
import org.ubimix.commons.parser.json.JsonTokenizer;

/**
 * @author kotelnikov
 */
public class JsonTokenizerTest extends TestCase {

    /**
     * @param name
     */
    public JsonTokenizerTest(String name) {
        super(name);
    }

    public void test() throws Exception {
        test("\n", "spaces:\n");
        test("{", "beginObject:{");
        test("{}", "beginObject:{", "endObject:}");
        test(" ", "spaces: ");
        test(":", "col::");
        test(
            "{ a: b }",
            "beginObject:{",
            "spaces: ",
            "string:a",
            "col::",
            "spaces: ",
            "string:b",
            "spaces: ",
            "endObject:}");
    }

    private void test(String str, String... tokens) {
        CharStream stream = new CharStream(str);
        JsonTokenizer tokenizer = new JsonTokenizer();
        for (String control : tokens) {
            StreamToken token = tokenizer.read(stream);
            assertNotNull(token);
            assertEquals(control, token.getKey() + ":" + token.getContent());
        }
    }

}
