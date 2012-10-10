package org.ubimix.commons.parser;

/**
 * @author kotelnikov
 */
public interface IParserListener {

    void onError(String message, Throwable error);

    boolean reportErrors();
}