package mchorse.emoticons.skin_n_bones.api.animation.json;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import mchorse.emoticons.skin_n_bones.api.animation.model.ActionConfig;
import mchorse.emoticons.skin_n_bones.api.animation.model.AnimatorActionsConfig;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.lang.reflect.Type;
import java.util.Map;

@SideOnly(Side.CLIENT)
public class AnimatorActionsConfigAdapter implements JsonDeserializer<AnimatorActionsConfig>
{
    @Override
    public AnimatorActionsConfig deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
    {
        if (!json.isJsonObject())
        {
            return null;
        }

        JsonObject object = json.getAsJsonObject();
        AnimatorActionsConfig config = new AnimatorActionsConfig();

        for (Map.Entry<String, JsonElement> entry : object.entrySet())
        {
            JsonElement element = entry.getValue();
            String key = config.toKey(entry.getKey());

            if (element.isJsonObject())
            {
                ((JsonObject) element).addProperty("name", key);
            }

            config.actions.put(key, context.deserialize(element, ActionConfig.class));
        }

        return config;
    }
}