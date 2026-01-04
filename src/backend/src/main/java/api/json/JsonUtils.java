// java/api/json/JsonUtils.java
package api.json;

import java.io.Reader;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import domain.modifiers.proficiency.Proficiency;

/**
 * Utility class to provide standardised transformation of domain objects to and
 * from Json String formatting.
 */
public class JsonUtils {

    // --- Attributes ---
    private final static Gson gson = new GsonBuilder()
        .registerTypeAdapterFactory(new EntityIdAdapterFactory())
        .registerTypeAdapter(Proficiency.class, new ProficiencyDeserializer())
        .create();

    // Private Constructor (disallow instantiation)
    private JsonUtils() {}

    /**
     * Convert an object into a (reversible) json formatted String.
     *
     * @param src Object to be exported as a json String
     * @return json String generated from {@code src} Object
     */
    public static String toJson(Object src) {
        return gson.toJson(src);
    }

    /**
     * Convert a json String into the relevant class object.
     *
     * @param <T> type of the the object the json represents
     * @param json the json formatted String from which a type {@code T} object
     *         is deserialised
     * @param classOfT {@code .class} of the object to be deserialised
     * @return instantiated object of type {@code T} with json details.
     *         Returns null if json is null or if json is empty.
     */
    public static <T> T fromJson(String json, Class<T> classOfT) {
        return gson.fromJson(json, classOfT);
    }

    /**
     * Convert a json producing reader into the relevant class object.
     *
     * @param <T> type of the the object the json represents
     * @param json the json producing reader from which a type {@code T} object
     *         is deserialised
     * @param classOfT {@code .class} of the object to be deserialised
     * @return instantiated object of type {@code T} with json details.
     *         Returns null if json is at EOF.
     */
    public static <T> T fromJson(Reader json, Class<T> classOfT) {
        return gson.fromJson(json, classOfT);
    }
}
