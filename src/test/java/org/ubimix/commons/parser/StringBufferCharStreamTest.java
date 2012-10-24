package org.ubimix.commons.parser;

public class StringBufferCharStreamTest extends CharStreamTest {
    public StringBufferCharStreamTest(String name) {
        super(name);
    }

    /**
     * @return
     */
    @Override
    protected ICharStream newCharStream(String str) {
        return new CharStream(str);
    }
}
