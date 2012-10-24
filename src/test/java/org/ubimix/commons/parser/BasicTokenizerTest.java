/**
 * 
 */
package org.ubimix.commons.parser;

import junit.framework.TestCase;

import org.ubimix.commons.parser.base.DelimitedTextTokenizer;
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

    protected ICharStream newCharStream(String str) {
        // return new CharStream(str);
        return new CharStream(str);
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

    public void test() {
        CompositeTokenizer tokenizer = new CompositeTokenizer();
        tokenizer.addTokenizer(new DelimitedTextTokenizer("<?", ">", false));
        tokenizer.addTokenizer(new DelimitedTextTokenizer("<!", ">", false));
        tokenizer.addTokenizer(new TextTokenizer());
        test(tokenizer, "a", "a");
        test(tokenizer, "a ", "a", " ");
        test(tokenizer, "<!b>", "<!b>");
        test(tokenizer, "<?b>", "<?b>");
        test(tokenizer, "<!b><?c>", "<!b>", "<?c>");
        test(tokenizer, "<?a><!b><?c>", "<?a>", "<!b>", "<?c>");
        test(tokenizer, "<!a><?b><!c>", "<!a>", "<?b>", "<!c>");
        test(tokenizer, "<!b>c", "<!b>", "c");
        test(tokenizer, "a<!b>c", "a", "<!b>", "c");
    }

    private void test(ITokenizer tokenizer, String string, String... control) {
        ICharStream stream = newCharStream(string);
        for (String t : control) {
            StreamToken token = tokenizer.read(stream);
            assertNotNull(token);
            assertEquals(t, token.getText());
        }
        StreamToken token = tokenizer.read(stream);
        assertNull(token);
    }

    private void testBasicTokenizer(ITokenizer tokenizer, String str) {
        StringBuilder builder = new StringBuilder();
        ICharStream stream = newCharStream(str);
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

    public void testXHTMLTokenizer() throws Exception {
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
}
