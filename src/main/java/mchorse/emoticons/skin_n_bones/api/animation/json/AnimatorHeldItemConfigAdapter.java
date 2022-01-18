package mchorse.emoticons.skin_n_bones.api.animation.json;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import mchorse.emoticons.skin_n_bones.api.animation.model.AnimatorHeldItemConfig;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.lang.reflect.Type;

@SideOnly(Side.CLIENT)
public class AnimatorHeldItemConfigAdapter implements JsonDeserializer<AnimatorHeldItemConfig>
{
    @Override
    public AnimatorHeldItemConfig deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
    {
        if (!json.isJsonObject())
        {
            return null;
        }

        JsonObject object = (JsonObject) json;
        AnimatorHeldItemConfig config = new AnimatorHeldItemConfig("");

        if (object.has("x"))
        {
            config.x = object.get("x").getAsFloat();
        }

        if (object.has("y"))
        {
            config.y = object.get("y").getAsFloat();
        }

        if (object.has("z"))
        {
            config.z = object.get("z").getAsFloat();
        }

        if (object.has("sx"))
        {
            config.scaleX = object.get("sx").getAsFloat();
        }

        if (object.has("sy"))
        {
            config.scaleY = object.get("sy").getAsFloat();
        }

        if (object.has("sz"))
        {
            config.scaleZ = object.get("sz").getAsFloat();
        }

        if (object.has("rx"))
        {
            config.rotateX = object.get("rx").getAsFloat();
        }

        if (object.has("ry"))
        {
            config.rotateY = object.get("ry").getAsFloat();
        }

        if (object.has("rz"))
        {
            config.rotateZ = object.get("rz").getAsFloat();
        }

        return config;
    }
}