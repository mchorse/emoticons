package mchorse.emoticons.client;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;

import com.google.gson.JsonArray;
import org.apache.commons.io.FileUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class EmoteKeys
{
    public List<String> emotes = Arrays.asList("default", "dab", "confused", "pure_salt", "wave", "star_power");

    public static Gson gson = new GsonBuilder().registerTypeAdapter(EmoteKeys.class, new EmoteKeysAdapter()).setPrettyPrinting().create();

    public static EmoteKeys fromFile(File file)
    {
        if (!file.isFile()) return null;

        try
        {
            return gson.fromJson(FileUtils.readFileToString(file, Charset.defaultCharset()), EmoteKeys.class);
        }
        catch (Exception e)
        {
            return null;
        }
    }

    public static void toFile(EmoteKeys keys, File file)
    {
        try
        {
            FileUtils.write(file, gson.toJson(keys), Charset.defaultCharset());
        }
        catch (IOException e)
        {}
    }

    public static class EmoteKeysAdapter implements JsonDeserializer<EmoteKeys>, JsonSerializer<EmoteKeys>
    {
        @Override
        public EmoteKeys deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
        {
            EmoteKeys keys = new EmoteKeys();

            if (json.isJsonObject())
            {
                JsonObject object = json.getAsJsonObject();

                if (object.has("emotes") && object.get("emotes").isJsonArray())
                {
                    int i = 0;

                    for (JsonElement element : object.getAsJsonArray("emotes"))
                    {
                        if (i < keys.emotes.size())
                        {
                            keys.emotes.set(i, element.getAsString());
                        }
                        else
                        {
                            keys.emotes.add(element.getAsString());
                        }

                        i ++;
                    }
                }
                else
                {
                    for (int i = 1; i <= 6; i ++)
                    {
                        keys.emotes.set(i - 1, this.getString(object, "emote" + i, keys.emotes.get(i - 1)));
                    }
                }
            }

            return keys;
        }

        private String getString(JsonObject object, String key, String defaultValue)
        {
            JsonElement element = object.get(key);

            if (element != null && element.isJsonPrimitive())
            {
                return element.getAsString();
            }

            return defaultValue;
        }

        @Override
        public JsonElement serialize(EmoteKeys src, Type typeOfSrc, JsonSerializationContext context)
        {
            JsonObject object = new JsonObject();
            JsonArray emotes = new JsonArray();

            for (String emote : src.emotes)
            {
                emotes.add(emote);
            }

            object.add("emotes", emotes);

            return object;
        }
    }
}