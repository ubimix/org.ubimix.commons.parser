/**
 * 
 */
package org.ubimix.commons.parser.xml;

import org.ubimix.commons.parser.StreamToken;
import org.ubimix.commons.parser.CharStream.Pointer;

/**
 * @author kotelnikov
 */
public class EntityToken extends StreamToken {

    private Entity fEntity;

    /**
     * @param key
     * @param begin
     * @param end
     * @param str
     */
    public EntityToken(String key, Pointer begin, Pointer end, String str) {
        super(key, begin, end, str);
    }

    @Override
    public String getContent() {
        return fEntity.toString();
    }

    /**
     * @return the entityKey
     */
    public Entity getEntityKey() {
        return fEntity;
    }

    /**
     * @param entity the entityKey to set
     */
    public void setEntityKey(Entity entity) {
        fEntity = entity;
    }

}
