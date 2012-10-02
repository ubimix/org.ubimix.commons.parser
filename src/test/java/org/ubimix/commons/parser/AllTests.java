package org.ubimix.commons.parser;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.ubimix.commons.parser.json.JsonParserTest;
import org.ubimix.commons.parser.xml.XmlParserTest;

public class AllTests {

    public static Test suite() {
        TestSuite suite = new TestSuite(AllTests.class.getName());
        // $JUnit-BEGIN$
        suite.addTestSuite(JsonParserTest.class);
        suite.addTestSuite(XmlParserTest.class);
        // $JUnit-END$
        return suite;
    }

}
