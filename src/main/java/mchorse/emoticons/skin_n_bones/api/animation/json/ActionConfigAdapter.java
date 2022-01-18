package mchorse.emoticons.skin_n_bones.api.animation.json;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import mchorse.emoticons.skin_n_bones.api.animation.model.ActionConfig;

import java.lang.reflect.Type;

public class ActionConfigAdapter implements JsonDeserializer<ActionConfig>
{
    @Override
    public ActionConfig deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
    {
        ActionConfig config = new ActionConfig();

        if (json.isJsonObject())
        {
            JsonObject object = (JsonObject) json;

            if (object.has("name")) config.name = object.get("name").getAsString();
            if (object.has("clamp")) config.clamp = object.get("clamp").getAsBoolean();
            if (object.has("reset")) config.reset = object.get("reset").getAsBoolean();
            if (object.has("speed")) config.speed = object.get("speed").getAsFloat();
            if (object.has("fade")) config.fade = object.get("fade").getAsInt();
            if (object.has("tick")) config.tick = object.get("tick").getAsInt();
        }
        else if (json.isJsonPrimitive())
        {
            config.name = json.getAsString();
        }

        return config;
    }
}