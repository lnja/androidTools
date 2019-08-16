package len.tools.android;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonSerializer;
import com.google.gson.*;

import java.io.IOException;
import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class JsonUtils {

    private static final String FORMAT_STRING = "yyyy-MM-dd HH:mm:ss";
    private static Gson gson = null;
    private static ObjectMapper objectMapper = null;

    private static Gson getGson() {
        if (gson == null) {
            gson = new GsonBuilder().setDateFormat(FORMAT_STRING).registerTypeAdapter(Timestamp.class, new TimestampTypeAdapter()).registerTypeAdapter(java.sql.Date.class, new SQLDateTypeAdapter()).create();
        }
        return gson;
    }

    public static <T> T gsonToEntity(JsonElement json, Class<T> classOfT) {
        JsonInterfaceCheck.assetType(classOfT);
        try {
            return getGson().fromJson(json, classOfT);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T> T gsonToEntity(String json, Class<T> classOfT) {
        JsonInterfaceCheck.assetType(classOfT);
        try {
            return getGson().fromJson(json, classOfT);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T> T gsonToEntity(String json, Type type) {
        JsonInterfaceCheck.assetType(type);
        try {
            return getGson().fromJson(json, type);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String gsonToJson(Object src) {
        try {
            return getGson().toJson(src);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            return null;
        }
    }

    protected static ObjectMapper getObjectMapper() {
        if (objectMapper == null) {
            objectMapper = new ObjectMapper();
            // JsonInclude.Include.ALWAYS 默认
            // JsonInclude.Include.NON_DEFAULT 属性为默认值不序列化
            // JsonInclude.Include.NON_EMPTY 属性为 空（””） 或者为 NULL 都不序列化
            // JsonInclude.Include.NON_NULL 属性为NULL 不序列化, 实体转json时，只对VO起作用，Map List不起作用
            //objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
            // 忽略在JSON字符串中存在，而在Java实体中不存在的属性
            objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        }
        return objectMapper;
    }

    public static <T> T toEntity(String json, JavaType javaType) {
        if (json == null || "".equals(json.trim()) || javaType == null) {
            return null;
        }
        try {
            return getObjectMapper().readValue(json, javaType);
        } catch (Exception e) {
            Log.e(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public static <T> T toEntity(String json, Class<T> classOfT) {
        if (json == null || "".equals(json.trim()) || classOfT == null) {
            return null;
        }
        try {
            return getObjectMapper().readValue(json, classOfT);
        } catch (Exception e) {
            Log.e(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public static String toJson(Object src) {
        try {
            return getObjectMapper().writeValueAsString(src);
        } catch (JsonProcessingException e) {
            Log.e(e.getMessage());
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 将json转化成List
     *
     * @param json
     * @param collectionClass
     * @param elementClass
     * @return
     * @throws com.fasterxml.jackson.core.JsonParseException
     * @throws JsonMappingException
     * @throws IOException
     */
    @SuppressWarnings("rawtypes")
    public static <T> List<T> toList(String json, Class<? extends List> collectionClass, Class<T> elementClass)
            throws IOException {
        JavaType javaType = getObjectMapper().getTypeFactory().constructCollectionType(collectionClass, elementClass);
        return getObjectMapper().readValue(json, javaType);
    }

    /**
     * 将json转化成Map
     *
     * @param json
     * @param mapClass
     * @param keyClass
     * @param valueClass
     * @return
     * @throws com.fasterxml.jackson.core.JsonParseException
     * @throws JsonMappingException
     * @throws IOException
     */
    @SuppressWarnings("rawtypes")
    public static <K, V> Map<K, V> toMap(String json, Class<? extends Map> mapClass, Class<K> keyClass,
                                         Class<V> valueClass) throws IOException {
        JavaType javaType = getObjectMapper().getTypeFactory().constructMapType(mapClass, keyClass, valueClass);
        return getObjectMapper().readValue(json, javaType);
    }

    public static String toPrintFormatJson(Object src) {
        try {
            return getObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(src);
        } catch (JsonGenerationException e) {
            Log.e(e.getMessage());
            e.printStackTrace();
        } catch (JsonMappingException e) {
            Log.e(e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            Log.e(e.getMessage());
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 格式化json字符串
     *
     * @param jsonStr 需要格式化的json串
     * @return 格式化后的json串
     */
    public static String toJsonViewStr(String jsonStr) {
        if (null == jsonStr || "".equals(jsonStr)) return "";
        StringBuilder sb = new StringBuilder();
        char last = '\0';
        char current = '\0';
        int indent = 0;
        for (int i = 0; i < jsonStr.length(); i++) {
            last = current;
            current = jsonStr.charAt(i);
            //遇到{ [换行，且下一行缩进
            switch (current) {
                case '{':
                case '[':
                    sb.append(current);
                    sb.append('\n');
                    indent++;
                    addIndentBlank(sb, indent);
                    break;
                //遇到} ]换行，当前行缩进
                case '}':
                case ']':
                    sb.append('\n');
                    indent--;
                    addIndentBlank(sb, indent);
                    sb.append(current);
                    break;
                //遇到,换行
                case ',':
                    sb.append(current);
                    if (last != '\\') {
                        sb.append('\n');
                        addIndentBlank(sb, indent);
                    }
                    break;
                default:
                    sb.append(current);
            }
        }
        return sb.toString();
    }

    /**
     * 添加space
     *
     * @param sb
     * @param indent
     */
    private static void addIndentBlank(StringBuilder sb, int indent) {
        for (int i = 0; i < indent; i++) {
            sb.append('\t');
        }
    }

    /**
     * http 请求数据返回 json 中中文字符为 unicode 编码转汉字转码
     *
     * @param theString
     * @return 转化后的结果.
     */
    public static String decodeUnicode(String theString) {
        char aChar;
        int len = theString.length();
        StringBuffer outBuffer = new StringBuffer(len);
        for (int x = 0; x < len; ) {
            aChar = theString.charAt(x++);
            if (aChar == '\\') {
                aChar = theString.charAt(x++);
                if (aChar == 'u') {
                    int value = 0;
                    for (int i = 0; i < 4; i++) {
                        aChar = theString.charAt(x++);
                        switch (aChar) {
                            case '0':
                            case '1':
                            case '2':
                            case '3':
                            case '4':
                            case '5':
                            case '6':
                            case '7':
                            case '8':
                            case '9':
                                value = (value << 4) + aChar - '0';
                                break;
                            case 'a':
                            case 'b':
                            case 'c':
                            case 'd':
                            case 'e':
                            case 'f':
                                value = (value << 4) + 10 + aChar - 'a';
                                break;
                            case 'A':
                            case 'B':
                            case 'C':
                            case 'D':
                            case 'E':
                            case 'F':
                                value = (value << 4) + 10 + aChar - 'A';
                                break;
                            default:
                                throw new IllegalArgumentException(
                                        "Malformed   \\uxxxx   encoding.");
                        }

                    }
                    outBuffer.append((char) value);
                } else {
                    if (aChar == 't')
                        aChar = '\t';
                    else if (aChar == 'r')
                        aChar = '\r';
                    else if (aChar == 'n')
                        aChar = '\n';
                    else if (aChar == 'f')
                        aChar = '\f';
                    outBuffer.append(aChar);
                }
            } else
                outBuffer.append(aChar);
        }
        return outBuffer.toString();
    }

    private static class TimestampTypeAdapter implements JsonSerializer<Timestamp>, JsonDeserializer<Timestamp> {
        private final DateFormat format = new SimpleDateFormat(FORMAT_STRING, Locale.getDefault());

        @Override
        public JsonElement serialize(Timestamp src, Type arg1, JsonSerializationContext arg2) {
            String dateFormatAsString = format.format(new Date(src.getTime()));
            return new JsonPrimitive(dateFormatAsString);
        }

        @Override
        public Timestamp deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            if (!(json instanceof JsonPrimitive)) {
                throw new JsonParseException("The date should be a string value");
            }
            try {
                Date date = format.parse(json.getAsString());
                return new Timestamp(date.getTime());
            } catch (ParseException e) {
                throw new JsonParseException(e);
            }
        }
    }

    private static class SQLDateTypeAdapter implements JsonSerializer<java.sql.Date> {
        private final DateFormat format = new SimpleDateFormat(FORMAT_STRING, Locale.getDefault());

        @Override
        public JsonElement serialize(java.sql.Date src, Type arg1, JsonSerializationContext arg2) {
            String dateFormatAsString = format.format(new java.sql.Date(src.getTime()));
            return new JsonPrimitive(dateFormatAsString);
        }
    }
}