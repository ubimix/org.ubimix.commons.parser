/**
 * 
 */
package org.ubimix.commons.parser;

/**
 * @author kotelnikov
 */
public interface ITokenizer {

    StreamToken read(ICharStream stream);

}
