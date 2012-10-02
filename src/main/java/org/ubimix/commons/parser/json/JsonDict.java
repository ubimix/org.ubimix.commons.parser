/**
 * 
 */
package org.ubimix.commons.parser.json;

/**
 * A basic dictionary defining all basic tokens like "word", "space", "special"
 * (special symbols), and "eol" (end of the line).
 * 
 * @author kotelnikov
 */
public class JsonDict {

    public final static String[] _ALL;

    public final static String ARRAY_BEGIN = "beginArray";

    public final static String ARRAY_END = "endArray";

    public final static String BOOLEAN = "boolean";

    public final static String COL = "col";

    public final static String COMMA = "comma";

    public final static String NULL = "null";

    public final static String OBJ_BEGIN = "beginObject";

    public final static String OBJ_END = "endObject";

    public final static String PROPERTY = "property";

    public final static String STRING = "string";

    static {
        _ALL = new String[] {
            ARRAY_BEGIN,
            ARRAY_END,
            BOOLEAN,
            COL,
            COMMA,
            NULL,
            OBJ_BEGIN,
            OBJ_END,
            PROPERTY,
            STRING };
    }

}
