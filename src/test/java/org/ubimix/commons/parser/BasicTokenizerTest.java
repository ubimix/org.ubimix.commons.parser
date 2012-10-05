/**
 * 
 */
package org.ubimix.commons.parser;

import junit.framework.TestCase;

import org.ubimix.commons.parser.ICharStream.IPointer;
import org.ubimix.commons.parser.text.TextTokenizer;

/**
 * @author kotelnikov
 */
public class BasicTokenizerTest extends TestCase {

    public BasicTokenizerTest(String name) {
        super(name);
    }

    private ITokenizer getXHTMLTokenizer() {
        return new TextTokenizer();
    }

    protected void printToken(StreamToken token) {
        String s = token.getText();
        s = s
            .replaceAll("\\\\", "\\\\")
            .replaceAll("\\t", "\\\\t")
            .replaceAll("\\r\\n", "\\\\n")
            .replaceAll("\\n", "\\\\n");

        ICharStream.IPointer p = token.getBegin();
        String pos = "(";
        pos += p.getLine() + ":" + p.getColumn();
        pos += " - ";
        p = token.getEnd();
        pos += p.getLine() + ":" + p.getColumn();
        pos += ")";

        System.out.println(token + "\t" + pos + "\t'" + s + "'");
    }

    public void test() throws Exception {
        ITokenizer tokenizer = getXHTMLTokenizer();
        testBasicTokenizer(tokenizer, "–*");
        testBasicTokenizer(tokenizer, "<p>–");
        testBasicTokenizer(tokenizer, "<p>–&#160;</p>");
        testBasicTokenizer(tokenizer, ""
            + "<!-- This is a comment -->\n"
            + "<html>\n"
            + "<head>\n"
            + "   <title>Hello, world!</title>\n"
            + "</head>\n"
            + "<body>\n"
            + "   <h1>I am here!</h1>"
            + "   <p>This is a new paragraph &nbsp; &nbsp;</p>\n"
            + "<p>An another paragraph <br /> with \r\n a line break.</p>\n"
            + "<hr \n"
            + "     style='border: 1px solid red; \r\n margin: 1em 0;'/>\n"
            + "</body>\n"
            + "</html>");
    }

    private void testBasicTokenizer(ITokenizer tokenizer, String str) {
        StringBuilder builder = new StringBuilder();
        ICharStream stream = new CharStream(str);
        while (true) {
            StreamToken token = tokenizer.read(stream);
            if (token == null) {
                break;
            }
            printToken(token);
            builder.append(token.getText());
        }
        assertEquals(str, builder.toString());
    }
}
