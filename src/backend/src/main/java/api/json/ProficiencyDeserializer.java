// java/api/json/ProficiencyDeserializer.java
package api.json;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import domain.modifiers.proficiency.Proficiency;
import domain.types.ProficiencyType;

/**
 * Polymorphic Google Gson deserialiser for parsing concrete {@link Proficiency}
 * subclasses.
 */
public class ProficiencyDeserializer implements JsonDeserializer<Proficiency> {

    @Override
    public Proficiency deserialize(
        JsonElement json, Type typeOfT, JsonDeserializationContext context
    ) throws JsonParseException {
        // Ignore `typeOfT` and determine concrete class via ProficiencyType
        JsonObject obj = json.getAsJsonObject();
        String typeStr = obj.get("proficiencyType").getAsString();
        ProficiencyType type = ProficiencyType.fromString(typeStr);
        // Delegate deserialisation to the relevant subclass
        return context.deserialize(json, type.getProficiencyClass());
    }
}
