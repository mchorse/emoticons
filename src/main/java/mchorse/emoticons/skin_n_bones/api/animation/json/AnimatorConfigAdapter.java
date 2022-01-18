package mchorse.emoticons.skin_n_bones.api.animation.json;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import mchorse.emoticons.skin_n_bones.api.animation.AnimationMeshConfig;
import mchorse.emoticons.skin_n_bones.api.animation.model.AnimatorActionsConfig;
import mchorse.emoticons.skin_n_bones.api.animation.model.AnimatorConfig;
import mchorse.emoticons.skin_n_bones.api.animation.model.AnimatorHeldItemConfig;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SideOnly(Side.CLIENT)
public class AnimatorConfigAdapter implements JsonDeserializer<AnimatorConfig>
{
    @Override
    public AnimatorConfig deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
    {
        if (!json.isJsonObject())
        {
            return null;
        }

        JsonObject object = json.getAsJsonObject();
        AnimatorConfig config = new AnimatorConfig();

        if (object.has("name"))
        {
            config.name = object.get("name").getAsString();
        }

        if (object.has("primaryMesh"))
        {
            config.primaryMesh = object.get("primaryMesh").getAsString();
        }

        if (object.has("scale"))
        {
            config.scale = object.get("scale").getAsFloat();
        }

        if (object.has("scaleGui"))
        {
            config.scaleGui = object.get("scaleGui").getAsFloat();
        }

        if (object.has("scaleItems"))
        {
            config.scaleItems = object.get("scaleItems").getAsFloat();
        }

        if (object.has("renderHeldItems"))
        {
            config.renderHeldItems = object.get("renderHeldItems").getAsBoolean();
        }

        if (object.has("leftHands"))
        {
            this.addHeldConfig(config.leftHands, object.get("leftHands"), context);
        }

        if (object.has("rightHands"))
        {
            this.addHeldConfig(config.rightHands, object.get("rightHands"), context);
        }

        if (object.has("head"))
        {
            config.head = object.get("head").getAsString();
        }

        if (object.has("actions"))
        {
            config.actions = context.deserialize(object.get("actions"), AnimatorActionsConfig.class);
        }

        if (object.has("meshes"))
        {
            config.meshes = context.deserialize(object.get("meshes"), new TypeToken<Map<String, AnimationMeshConfig>>()
            {}.getType());
        }

        return config;
    }

    private void addHeldConfig(Map<String, AnimatorHeldItemConfig> list, JsonElement element, JsonDeserializationContext context)
    {
        list.clear();

        if (element.isJsonArray())
        {
            for (String bone : toStringArray(element.getAsJsonArray()))
            {
                list.put(bone, new AnimatorHeldItemConfig(bone));
            }
        }
        else if (element.isJsonObject())
        {
            for (Map.Entry<String, JsonElement> entry : ((JsonObject) element).entrySet())
            {
                AnimatorHeldItemConfig item = context.deserialize(entry.getValue(), AnimatorHeldItemConfig.class);

                item.boneName = entry.getKey();
                list.put(item.boneName, item);
            }
        }
    }

    public static String[] toStringArray(JsonArray array)
    {
        List<String> strings = new ArrayList<String>();

        for (int i = 0, c = array.size(); i < c; i++)
        {
            JsonElement element = array.get(i);

            if (element.isJsonPrimitive())
            {
                strings.add(element.getAsString());
            }
        }

        return strings.toArray(new String[strings.size()]);
    }
}