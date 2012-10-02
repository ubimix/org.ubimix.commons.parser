/**
 * 
 */
package org.ubimix.commons.parser.xml;

import org.ubimix.commons.parser.CharStream;
import org.ubimix.commons.parser.CharStream.Marker;
import org.ubimix.commons.parser.CharStream.Pointer;

/**
 * @author kotelnikov
 */
public class EntityReader {

    public static void main(String[] args) {
        String str = "039";
        System.out.println(Integer.parseInt(str));
    }

    private EntityFactory fFactory;

    private boolean fIgnoreUnknownEntities;

    /**
     * 
     */
    public EntityReader(EntityFactory factory) {
        this(factory, true);
    }

    /**
     * 
     */
    public EntityReader(EntityFactory factory, boolean ignoreUnknownEntities) {
        fFactory = factory;
        fIgnoreUnknownEntities = ignoreUnknownEntities;
    }

    protected Entity getEntityKey(boolean digit, String str) {
        Entity entity;
        int code = -1;
        if (digit) {
            code = Integer.parseInt(str);
            entity = fFactory.getEntityKeyByCode(code);
            str = null;
        } else {
            entity = fFactory.getEntityKeyByName(str);
        }
        if (entity == null && (digit || !fIgnoreUnknownEntities))
            entity = new Entity(str, code);
        return entity;
    }

    public Entity readEntity(CharStream stream) {
        char ch = stream.getChar();
        if (ch != '&')
            return null;
        Entity entity = null;
        Marker marker = stream.markPosition();
        try {
            if (!stream.incPos())
                return null;
            Pointer begin = stream.getPointer();
            ch = stream.getChar();
            boolean digit = false;
            if (ch == '#') {
                digit = true;
                if (!stream.incPos())
                    return null;
                ch = stream.getChar();
                begin = stream.getPointer();
                while (Character.isDigit(ch)) {
                    if (!stream.incPos())
                        return null;
                    ch = stream.getChar();
                }
            } else {
                while (Character.isLetterOrDigit(ch)) {
                    if (!stream.incPos())
                        return null;
                    ch = stream.getChar();
                }
            }
            Pointer end = stream.getPointer();
            if (begin.pos == end.pos)
                return null;
            ch = stream.getChar();
            if (ch != ';')
                return null;
            stream.incPos();

            String str = marker.getSubstring(begin, end);
            entity = getEntityKey(digit, str);
            if (entity == null)
                return null;
            return entity;
        } finally {
            marker.close(entity == null);
        }
    }
}
