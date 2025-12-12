// java/domain/core/EntityIdAdapter.java
package domain.core;

import java.io.IOException;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

/**
 * Google Gson {@link TypeAdapter} for {@link EntityId} possessing objects.
 * Simplifies the Json generated for ID owning entities to improve readability.
 */
public class EntityIdAdapter<T> extends TypeAdapter<EntityId<T>> {

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
        // If no ID included, do not set EntityId
        if (in.peek() == JsonToken.NULL) {
            in.nextNull();
            return null;
        }

        // Otherwise, interpret it as a long
        long value = in.nextLong();
        return new EntityId<T>(value);
    }
}
