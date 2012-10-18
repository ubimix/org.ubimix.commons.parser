package org.ubimix.commons.parser;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

    public static Test suite() {
        TestSuite suite = new TestSuite("Test for org.ubimix.commons.parser");
        // $JUnit-BEGIN$
        suite.addTestSuite(BasicTokenizerTest.class);
        suite.addTestSuite(CharStreamTest.class);
        suite.addTestSuite(ComparativeCharStreamTest.class);
        suite.addTestSuite(QuotedValueTokenizerTest.class);
        suite.addTestSuite(StringBufferCharStreamTest.class);

        // $JUnit-END$
        return suite;
    }
}
