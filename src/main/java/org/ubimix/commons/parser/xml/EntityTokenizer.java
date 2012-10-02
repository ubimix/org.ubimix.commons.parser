/**
 * 
 */
package org.ubimix.commons.parser.xml;

import org.ubimix.commons.parser.AbstractTokenizer;
import org.ubimix.commons.parser.CharStream;
import org.ubimix.commons.parser.StreamToken;
import org.ubimix.commons.parser.CharStream.Marker;
import org.ubimix.commons.parser.CharStream.Pointer;

public class EntityTokenizer extends AbstractTokenizer {

    private EntityReader fReader;

    public EntityTokenizer(EntityFactory factory, String entityKey) {
        this(new EntityReader(factory), entityKey);
    }

    public EntityTokenizer(EntityReader reader) {
        this(reader, XMLDict.ENTITY);
    }

    public EntityTokenizer(EntityReader reader, String entityKey) {
        super(entityKey);
        fReader = reader;
    }

    @Override
    protected StreamToken newToken(Pointer begin, Pointer end, String str) {
        return new EntityToken(fKey, begin, end, str);
    }

    public StreamToken read(CharStream stream) {
        char ch = stream.getChar();
        if (ch != '&')
            return null;
        EntityToken result = null;
        Marker marker = stream.markPosition();
        try {
            Entity entity = fReader.readEntity(stream);
            if (entity != null) {
                result = (EntityToken) newToken(stream, marker);
                result.setEntityKey(entity);
            }
            return result;
        } finally {
            marker.close(result == null);
        }
    }

}