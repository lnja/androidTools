package len.tools.android;

import android.support.annotation.Nullable;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonSerializer;
import com.google.gson.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
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
    /**
     * It is used for json view
     */
    private static final int JSON_INDENT = 2;
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
     * @param rawJsonStr 需要格式化的json串
     * @return 格式化后的json串
     */
    public static String toJsonViewStr(@Nullable String rawJsonStr) {
        if (StringUtils.isEmpty(rawJsonStr)) {
            return "Empty/Null json content";
        }
        try {
            rawJsonStr = rawJsonStr.trim();
            if (rawJsonStr.startsWith("{")) {
                JSONObject jsonObject = new JSONObject(rawJsonStr);
                String message = jsonObject.toString(JSON_INDENT);
                return message;
            }
            if (rawJsonStr.startsWith("[")) {
                JSONArray jsonArray = new JSONArray(rawJsonStr);
                String message = jsonArray.toString(JSON_INDENT);
                return message;
            }
            return "Invalid Json";
        } catch (JSONException e) {
            return "Invalid Json";
        }
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

    public static Class<?> getRawType(Type type) {
        if (type instanceof Class<?>) {
            // type is a normal class.
            return (Class<?>) type;

        } else if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;

            // I'm not exactly sure why getRawType() returns Type instead of Class.
            // Neal isn't either but suspects some pathological case related
            // to nested classes exists.
            Type rawType = parameterizedType.getRawType();
            if (!(rawType instanceof Class)) {
                throw new IllegalArgumentException();
            }
            return (Class<?>) rawType;

        } else if (type instanceof GenericArrayType) {
            Type componentType = ((GenericArrayType) type).getGenericComponentType();
            return Array.newInstance(getRawType(componentType), 0).getClass();

        } else if (type instanceof TypeVariable) {
            // we could use the variable's bounds, but that won't work if there are multiple.
            // having a raw type that's more general than necessary is okay
            return Object.class;

        } else if (type instanceof WildcardType) {
            return getRawType(((WildcardType) type).getUpperBounds()[0]);

        } else {
            String className = type == null ? "null" : type.getClass().getName();
            throw new IllegalArgumentException("Expected a Class, ParameterizedType, or "
                    + "GenericArrayType, but <" + type + "> is of type " + className);
        }
    }

}