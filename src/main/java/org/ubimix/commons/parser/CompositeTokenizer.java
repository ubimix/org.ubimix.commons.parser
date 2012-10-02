/**
 * 
 */
package org.ubimix.commons.parser;

import java.util.ArrayList;
import java.util.List;


/**
 * @author kotelnikov
 */
public class CompositeTokenizer implements ITokenizer {

    private List<ITokenizer> fList = new ArrayList<ITokenizer>();

    public void addTokenizer(ITokenizer tokenizer) {
        fList.add(tokenizer);
    }

    public StreamToken read(CharStream stream) {
        for (ITokenizer tokenizer : fList) {
            StreamToken token = tokenizer.read(stream);
            if (token != null) {
                return token;
            }
        }
        return null;
    }

    public void removeTokenizer(ITokenizer tokenizer) {
        fList.remove(tokenizer);
    }

}
