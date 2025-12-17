// java/api/json/EntityIdAdapterFactory.java
package api.json;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import domain.core.EntityId;

/**
 * Google Gson {@link TypeAdapterFactory} for generating {@link TypeAdapter}s
 * for all generics of {@link EntityId}s within objects.
 * Simplifies the Json generated for ID owning entities to improve readability.
 *
 * <p> Created by modifying code found in {@link TypeAdapterFactory} javadoc.
 */
public class EntityIdAdapterFactory implements TypeAdapterFactory {

    /* ======================================================================
     * ------------------------- TypeAdapterFactory -------------------------
     * ====================================================================== */

    @SuppressWarnings("unchecked")
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
        Type type = typeToken.getType();

        // Check for EntityId<> parameterized class - return null if so
        if (typeToken.getRawType() != EntityId.class
            || !(type instanceof ParameterizedType)) {
            return null;
        }

        // Otherwise, return correct TypeAdapter if matching
        return (TypeAdapter<T>) new EntityIdAdapter<>();
    }

    /* ======================================================================
     * ---------------------------- TypeAdapter  ----------------------------
     * ====================================================================== */

    /**
     * Google Gson {@link TypeAdapter} for {@link EntityId} possessing objects.
     * Private for internal use only - requires TypeAdapterFactory generation to
     * match generics.
     */
    private class EntityIdAdapter<T> extends TypeAdapter<EntityId<T>> {

        @Override
        public void write(JsonWriter out, EntityId<T> id) throws IOException {
            // Print a null value or ID value depending on if ID has been set
            if (id == null) {
                out.nullValue();
            } else {
                out.value(id.value());
            }
        }

        @Override
        public EntityId<T> read(JsonReader in) throws IOException {
            // Validate and decipher EntityId shipped type
            JsonToken token = in.peek();
            switch (token) {

                // If no ID included, do not set EntityId
                case NULL:
                    in.nextNull();
                    return null;

                // If a valid number, read as ID
                case NUMBER:
                    return new EntityId<T>(in.nextLong());

                // If a String, try to interpret as numeric ID
                case STRING:
                    String s = in.nextString();
                    try { return new EntityId<T>(Long.parseLong(s)); }
                    catch (NumberFormatException invalid) { return null; }

                // Skip unrecognised value format
                default:
                    in.skipValue();
                    return null;
            }
        }
    }
}
