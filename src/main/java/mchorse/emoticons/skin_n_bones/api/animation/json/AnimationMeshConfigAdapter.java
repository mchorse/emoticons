package mchorse.emoticons.skin_n_bones.api.animation.json;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import mchorse.emoticons.skin_n_bones.api.animation.AnimationMeshConfig;
import mchorse.mclib.utils.resources.RLUtils;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import java.lang.reflect.Type;

@SideOnly(Side.CLIENT)
public class AnimationMeshConfigAdapter implements JsonDeserializer<AnimationMeshConfig>, JsonSerializer<AnimationMeshConfig>
{
    @Override
    public AnimationMeshConfig deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
    {
        if (!json.isJsonObject())
        {
            return null;
        }

        JsonObject object = json.getAsJsonObject();
        AnimationMeshConfig config = new AnimationMeshConfig();

        if (object.has("texture"))
        {
            config.texture = RLUtils.create(object.get("texture"));
        }

        if (object.has("filtering"))
        {
            config.filtering = object.get("filtering").getAsString().equalsIgnoreCase("linear") ? GL11.GL_LINEAR : GL11.GL_NEAREST;
        }

        if (object.has("normals"))
        {
            config.normals = object.get("normals").getAsBoolean();
        }

        if (object.has("smooth"))
        {
            config.smooth = object.get("smooth").getAsBoolean();
        }

        if (object.has("visible"))
        {
            config.visible = object.get("visible").getAsBoolean();
        }

        if (object.has("lighting"))
        {
            config.lighting = object.get("lighting").getAsBoolean();
        }

        if (object.has("color"))
        {
            config.color = object.get("color").getAsInt();
        }

        return config;
    }

    @Override
    public JsonElement serialize(AnimationMeshConfig src, Type typeOfSrc, JsonSerializationContext context)
    {
        JsonObject object = new JsonObject();

        if (src.texture != null) object.add("texture", RLUtils.writeJson(src.texture));

        object.addProperty("filtering", src.filtering == GL11.GL_LINEAR ? "linear" : "nearest");
        object.addProperty("normals", src.normals);
        object.addProperty("smooth", src.smooth);
        object.addProperty("visible", src.visible);
        object.addProperty("lighting", src.lighting);
        object.addProperty("color", src.color);

        return object;
    }
}