package org.ubimix.commons.parser;

/**
 * @author kotelnikov
 * @param <L>
 */
public interface IParser<L extends IParserListener> {

    void parse(ICharStream stream, L listener);

    void parse(String str, L listener);

}