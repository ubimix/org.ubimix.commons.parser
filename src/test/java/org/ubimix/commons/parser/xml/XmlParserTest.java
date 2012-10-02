/**
 * 
 */
package org.ubimix.commons.parser.xml;

import java.util.Map;

import junit.framework.TestCase;

import org.ubimix.commons.parser.CharStream;
import org.ubimix.commons.parser.xml.Entity;

/**
 * @author kotelnikov
 */
public class XmlParserTest extends TestCase {

    public static class TestListener extends XmlParser.XmlParserListener {

        private StringBuilder fBuf = new StringBuilder();

        @Override
        public void beginElement(
            String tagName,
            Map<String, String> attributes,
            Map<String, String> namespaces) {
            print("<" + tagName);
            printAttributes(null, attributes);
            printAttributes("xmlns", namespaces);
            print(">");
        }

        @Override
        public void endElement(
            String tagName,
            Map<String, String> attributes,
            Map<String, String> namespaces) {
            print("</" + tagName + ">");
        }

        @Override
        public void onCDATA(String content) {
            print("<![CDATA[");
            print(content);
            print("]]>");
        }

        @Override
        public void onComment(String commentContent) {
            print("<!--");
            print(commentContent);
            print("-->");
        }

        @Override
        public void onEntity(Entity entity) {
            print(entity.toString());
        }

        @Override
        public void onText(String string) {
            print(string);
        }

        protected void print(String str) {
            fBuf.append(str);
        }

        protected void printAttributes(String prefix, Map<String, String> map) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                String name = entry.getKey();
                String value = entry.getValue();
                if (prefix != null) {
                    if (!"".equals(name)) {
                        name = prefix + ":" + name;
                    } else {
                        name = prefix;
                    }
                }
                print(" ");
                print(name);
                print("=");
                print("'");
                print(value);
                print("'");
            }
        }

        @Override
        public String toString() {
            return fBuf.toString();
        }

    }

    /**
     * @param name
     */
    public XmlParserTest(String name) {
        super(name);
    }

    public void test() {
        testParser("<div />", "<div></div>");
        testParser("<div>This is a text</div>", "<div>This is a text</div>");
        testParser("<div>This is a text", "<div>This is a text</div>");
        testParser("", "");
        testParser("<a/>", "<a></a>");
        testParser("    <div />   ", "<div></div>");
        testParser("<root>"
            + "<a xmlns='foo'><x></x><y></y></a>"
            + "<a xmlns:n='bar'><n:x></n:x><n:y></n:y></a>"
            + "</root>");
        testParser(
            "<feed xmlns='http://www.w3.org/2005/Atom' />",
            "<feed xmlns='http://www.w3.org/2005/Atom'></feed>");
        testParser("<a><b><c><d><e><f>Text</f></e></d></c></b></a>");
        testParser("<a><b>Text</b><c>Text</c><d>Text</d><e>Text</e><f>Text</f></a>");
        testParser(""
            + "<html>"
            + "<head>"
            + "<title>Hello, world</title>"
            + "</head>"
            + "<body>"
            + "<p class='first'>A new paragraph</p>"
            + "</body>"
            + "</html>");
        testParser(
            ""
                + "<feed xmlns='http://www.w3.org/2005/Atom'>\n"
                + " \n"
                + "    <title>Example Feed</title>\n"
                + "    <subtitle>A subtitle.</subtitle>\n"
                + "    <link href='http://example.org/feed/' rel='self' />\n"
                + "    <link href='http://example.org/' />\n"
                + "    <id>urn:uuid:60a76c80-d399-11d9-b91C-0003939e0af6</id>\n"
                + "    <updated>2003-12-13T18:30:02Z</updated>\n"
                + "    <author>\n"
                + "        <name>John Doe</name>\n"
                + "        <email>johndoe@example.com</email>\n"
                + "    </author>\n"
                + " \n"
                + "    <entry>\n"
                + "        <title>Atom-Powered Robots Run Amok</title>\n"
                + "        <link href='http://example.org/2003/12/13/atom03' />\n"
                + "        <link rel='alternate' type='text/html' href='http://example.org/2003/12/13/atom03.html'/>\n"
                + "        <link rel='edit' href='http://example.org/2003/12/13/atom03/edit'/>\n"
                + "        <id>urn:uuid:1225c695-cfb8-4ebb-aaaa-80da344efa6a</id>\n"
                + "        <updated>2003-12-13T18:30:02Z</updated>\n"
                + "        <summary>Some text.</summary>\n"
                + "        <category term='robots'/>\n"
                + "        <category term='test' label='Test Label' scheme='http://example.org/ns/tags/'/>\n"
                + "        <content type='html' xmlns='http://www.w3.org/1999/xhtml'>Robot-generated content.</content>\n"
                + "    </entry>\n"
                + " \n"
                + "</feed>",
            ""
                + "<feed xmlns='http://www.w3.org/2005/Atom'>\n"
                + " \n"
                + "    <title>Example Feed</title>\n"
                + "    <subtitle>A subtitle.</subtitle>\n"
                + "    <link href='http://example.org/feed/' rel='self'></link>\n"
                + "    <link href='http://example.org/'></link>\n"
                + "    <id>urn:uuid:60a76c80-d399-11d9-b91C-0003939e0af6</id>\n"
                + "    <updated>2003-12-13T18:30:02Z</updated>\n"
                + "    <author>\n"
                + "        <name>John Doe</name>\n"
                + "        <email>johndoe@example.com</email>\n"
                + "    </author>\n"
                + " \n"
                + "    <entry>\n"
                + "        <title>Atom-Powered Robots Run Amok</title>\n"
                + "        <link href='http://example.org/2003/12/13/atom03'></link>\n"
                + "        <link rel='alternate' type='text/html' href='http://example.org/2003/12/13/atom03.html'></link>\n"
                + "        <link rel='edit' href='http://example.org/2003/12/13/atom03/edit'></link>\n"
                + "        <id>urn:uuid:1225c695-cfb8-4ebb-aaaa-80da344efa6a</id>\n"
                + "        <updated>2003-12-13T18:30:02Z</updated>\n"
                + "        <summary>Some text.</summary>\n"
                + "        <category term='robots'></category>\n"
                + "        <category term='test' label='Test Label' scheme='http://example.org/ns/tags/'></category>\n"
                + "        <content type='html' xmlns='http://www.w3.org/1999/xhtml'>Robot-generated content.</content>\n"
                + "    </entry>\n"
                + " \n"
                + "</feed>");
    }

    private void testParser(String str) {
        testParser(str, str);
    }

    private void testParser(String str, String control) {
        TestListener listener = new TestListener() {
            @Override
            protected void print(String str) {
                super.print(str);
                System.out.print(str);
            }
        };
        XmlParser parser = new XmlParser();
        parser.parse(new CharStream(str), listener);
        assertEquals(control, listener.toString());
        System.out.println();
    }

}
