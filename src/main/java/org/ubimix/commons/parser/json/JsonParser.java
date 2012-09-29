/**
 * 
 */
package org.ubimix.commons.parser.json;

import org.ubimix.commons.parser.AbstractParser;
import org.ubimix.commons.parser.AbstractParser.IParserListener;
import org.ubimix.commons.tokenizer.CharStream.Marker;
import org.ubimix.commons.tokenizer.ITokenizer;
import org.ubimix.commons.tokenizer.StreamToken;
import org.ubimix.commons.tokenizer.json.JsonDict;
import org.ubimix.commons.tokenizer.json.JsonTokenizer;

/**
 * @author kotelnikov
 */
public class JsonParser extends AbstractParser<JsonParser.IJsonParserListener> {

    public interface IJsonParserListener extends IParserListener {

        void beginArray();

        void beginArrayElement();

        void beginObject();

        void beginObjectProperty(String property);

        void endArray();

        void endArrayElement();

        void endObject();

        void endObjectProperty(String property);

        void onValue(boolean value);

        void onValue(double value);

        void onValue(int value);

        void onValue(long value);

        void onValue(String value);
    }

    public static class JsonParserListener extends ParserListener
        implements
        IJsonParserListener {

        @Override
        public void beginArray() {
        }

        @Override
        public void beginArrayElement() {
        }

        @Override
        public void beginObject() {
        }

        @Override
        public void beginObjectProperty(String property) {
        }

        @Override
        public void endArray() {
        }

        @Override
        public void endArrayElement() {
        }

        @Override
        public void endObject() {
        }

        @Override
        public void endObjectProperty(String property) {
        }

        @Override
        public void onValue(boolean value) {
        }

        @Override
        public void onValue(double value) {
        }

        @Override
        public void onValue(int value) {
        }

        @Override
        public void onValue(long value) {
        }

        @Override
        public void onValue(String value) {
        }

    }

    /**
     * 
     */
    public JsonParser() {
        this(new JsonTokenizer());
    }

    protected JsonParser(ITokenizer tokenizer) {
        super(tokenizer);
    }

    @Override
    public void doParse() {
        if (readValues(true, false) && fListener.reportErrors()) {
            Marker marker = getStream().markPosition();
            StreamToken token = skipSpaces(true);
            marker.close(true);
            if (token != null) {
                onError("The end of the stream is expected.");
            }
        }
    }

    protected JsonTokenizer newTokenizer() {
        return new JsonTokenizer();
    }

    private void notifyBoolean(String str) {
        boolean value = Boolean.getBoolean(str.toLowerCase());
        fListener.onValue(value);
    }

    private void notifyValue(String str) {
        if (str == null) {
            fListener.onValue(null);
        } else if ("".equals(str)) {
            fListener.onValue("");
        } else {
            char ch = str.charAt(0);
            if (ch == '\'' || ch == '\"') {
                String value = resolveStringValue(str);
                fListener.onValue(value);
            } else {
                boolean result = false;
                int i = 0;
                try {
                    i = Integer.parseInt(str);
                    result = true;
                } catch (NumberFormatException e) {
                }
                if (result) {
                    fListener.onValue(i);
                } else {
                    long l = 0;
                    try {
                        l = Long.parseLong(str);
                        result = true;
                    } catch (NumberFormatException e) {
                    }
                    if (result) {
                        fListener.onValue(l);
                    } else {
                        double d = 0;
                        try {
                            d = Double.parseDouble(str);
                            result = true;
                        } catch (NumberFormatException e) {
                        }
                        if (result) {
                            fListener.onValue(d);
                        } else {
                            result = true;
                            fListener.onValue(str);
                        }
                    }
                }
            }
        }
    }

    private boolean readArray() {
        boolean result = false;
        StreamToken token = getToken(false);
        if (token != null && JsonDict.ARRAY_BEGIN.equals(token.getKey())) {
            fListener.beginArray();
            while (true) {
                result = true;
                token = skipSpaces(true);
                if (token != null && !JsonDict.ARRAY_END.equals(token.getKey())) {
                    fListener.beginArrayElement();
                    readValues(false, true);
                    fListener.endArrayElement();
                    token = skipSpaces(false);
                    if (token != null && !JsonDict.COMMA.equals(token.getKey())) {
                        break;
                    }
                } else {
                    break;
                }
            }
            if (token == null || !JsonDict.ARRAY_END.equals(token.getKey())) {
                onError("The ']' symbol expected.");
            }
            fListener.endArray();
        }
        return result;
    }

    private boolean readObject() {
        boolean result = false;
        StreamToken token = getToken(false);
        if (token != null && JsonDict.OBJ_BEGIN.equals(token.getKey())) {
            fListener.beginObject();
            while (true) {
                result = true;
                token = skipSpaces(true);
                if (token != null && JsonDict.STRING.equals(token.getKey())) {
                    String property = resolveStringValue(token.getContent());
                    fListener.beginObjectProperty(property);
                    token = skipSpaces(true);
                    if (token == null || !JsonDict.COL.equals(token.getKey())) {
                        onError("The ':' symbol expected");
                    }
                    if (!readValues(true, true)) {
                        onError("Property value is expected.");
                        notifyValue(null);
                    }
                    fListener.endObjectProperty(property);
                    token = skipSpaces(false);
                    if (token == null || !JsonDict.COMMA.equals(token.getKey())) {
                        break;
                    }
                } else {
                    break;
                }
            }
            if (token == null || !JsonDict.OBJ_END.equals(token.getKey())) {
                onError("The '}' symbol expected.");
            }
            fListener.endObject();
        }
        return result;
    }

    private boolean readValues(boolean loadFirst, boolean loadNext) {
        boolean handled = false;
        StreamToken token = skipSpaces(loadFirst);
        if (token != null) {
            handled = true;
            if (JsonDict.ARRAY_BEGIN.equals(token.getKey())) {
                handled = readArray();
            } else if (JsonDict.OBJ_BEGIN.equals(token.getKey())) {
                handled = readObject();
            } else if (JsonDict.BOOLEAN.equals(token.getKey())) {
                notifyBoolean(token.getContent());
            } else if (JsonDict.STRING.equals(token.getKey())) {
                notifyValue(token.getContent());
            } else if (JsonDict.NULL.equals(token.getKey())) {
                notifyValue(null);
            } else {
                handled = false;
            }
            if (handled && loadNext) {
                skipSpaces(true);
            }
        }
        return handled;
    }

    private String resolveStringValue(String str) {
        if (str == null) {
            return str;
        }
        StringBuffer buf = new StringBuffer();
        boolean escaped = false;
        char skipChar = 0;
        int len = str.length();
        for (int pos = 0; pos < len; pos++) {
            char ch = str.charAt(pos);
            if (pos == 0 && (ch == '\'' || ch == '\"')) {
                skipChar = ch;
                continue;
            }
            if (escaped) {
                switch (ch) {
                    case 'n':
                        buf.append('\n');
                        break;
                    case 'r':
                        buf.append('\r');
                        break;
                    case 't':
                        buf.append('\t');
                        break;
                    case 'f':
                        buf.append('\f');
                        break;
                    case 'u':
                    case 'U':
                        int code = 0;
                        for (int i = 0; i < 4; i++) {
                            pos++;
                            if (pos >= len) {
                                break;
                            }
                            ch = str.charAt(pos);
                            int v;
                            if (ch >= '0' && ch <= '9') {
                                v = ch - '0';
                            } else if (ch >= 'a' && ch <= 'f') {
                                v = ch - 'a' + 10;
                            } else if (ch >= 'A' && ch <= 'F') {
                                v = ch - 'A' + 10;
                            } else {
                                break;
                            }
                            code |= v << ((3 - i) * 4);
                        }
                        ch = (char) code;
                        buf.append(ch);
                        break;
                    default:
                        buf.append(ch);
                        break;
                }
                escaped = false;
                continue;
            }
            escaped = ch == '\\';
            if (escaped) {
                continue;
            }
            if (pos != len - 1 || ch != skipChar) {
                buf.append(ch);
            }
        }
        return buf.toString();
    }

}
