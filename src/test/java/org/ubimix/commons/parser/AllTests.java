package org.ubimix.commons.parser;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.ubimix.commons.parser.css.CssSelectorParserTest;
import org.ubimix.commons.parser.css.CssSelectorTokenizerTest;
import org.ubimix.commons.parser.json.JsonParserTest;
import org.ubimix.commons.parser.json.JsonTokenizerTest;
import org.ubimix.commons.parser.xml.StreamSequenceTokenizerTest;
import org.ubimix.commons.parser.xml.XHTMLTokenReaderTest;
import org.ubimix.commons.parser.xml.XMLFormatter;
import org.ubimix.commons.parser.xml.XmlParserTest;

public class AllTests {

    public static Test suite() {
        TestSuite suite = new TestSuite("Test for org.ubimix.commons.parser");
        // $JUnit-BEGIN$
        suite.addTestSuite(BasicTokenizerTest.class);
        suite.addTestSuite(CharStreamTest.class);
        suite.addTestSuite(ComparativeCharStreamTest.class);
        suite.addTestSuite(QuotedValueTokenizerTest.class);

        // CSS selectors
        suite.addTestSuite(CssSelectorParserTest.class);
        suite.addTestSuite(CssSelectorTokenizerTest.class);

        // JSON
        suite.addTestSuite(JsonParserTest.class);
        suite.addTestSuite(JsonTokenizerTest.class);

        // XML/XHTML
        suite.addTestSuite(StreamSequenceTokenizerTest.class);
        suite.addTestSuite(XHTMLTokenReaderTest.class);
        suite.addTestSuite(XMLFormatter.class);
        suite.addTestSuite(XmlParserTest.class);

        // $JUnit-END$
        return suite;
    }
}
