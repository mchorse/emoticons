package mchorse.emoticons;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import mchorse.emoticons.api.metamorph.MetamorphHandler;
import mchorse.emoticons.blockbuster.BBIntegration;
import mchorse.emoticons.capabilities.CapabilitiesHandler;
import mchorse.emoticons.capabilities.cosmetic.Cosmetic;
import mchorse.emoticons.capabilities.cosmetic.CosmeticStorage;
import mchorse.emoticons.capabilities.cosmetic.ICosmetic;
import mchorse.emoticons.common.emotes.Emote;
import mchorse.emoticons.common.emotes.Emotes;
import mchorse.emoticons.network.Dispatcher;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.io.File;
import java.io.FileReader;
import java.util.Map;
import java.util.function.Function;

/**
 * Common proxy 
 */
public class CommonProxy
{
    /**
     * Client folder where saved selectors and animations are getting
     * stored.
     */
    public static File configFolder;

    /**
     * Register server-side emotes
     */
    public static void registerEmotes()
    {
        File[] folder = new File(CommonProxy.configFolder, "emotes").listFiles();

        if (folder != null)
        {
            for (File file : folder)
            {
                if (file.isFile() && file.getName().endsWith(".json"))
                {
                    CommonProxy.registerEmotes(file, (key) -> 100);
                }
            }
        }
    }

    /**
     * Register emotes for given JSON file
     */
    public static void registerEmotes(File file, Function<String, Integer> callback)
    {
        try
        {
            JsonObject element = new JsonParser().parse(new FileReader(file)).getAsJsonObject();

            for (Map.Entry<String, JsonElement> entry : element.entrySet())
            {
                String key = entry.getKey();
                JsonElement value = entry.getValue();
                int length = callback.apply(key);

                if (length <= 0 || !value.isJsonObject())
                {
                    continue;
                }

                Emotes.register(createEmote(key, length, value.getAsJsonObject()));
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private static Emote createEmote(String key, int length, JsonObject object)
    {
        boolean looping = object.has("looping") && object.get("looping").getAsBoolean();
        Emote emote = MetamorphHandler.createEmote(key, length, looping, object);

        if (object.has("title"))
        {
            emote.customTitle = object.get("title").getAsString();
        }

        if (object.has("description"))
        {
            emote.customDescription = object.get("description").getAsString();
        }

        return emote;
    }

    public void preInit(FMLPreInitializationEvent event)
    {
        /* Setup config folder path */
        String path = event.getModConfigurationDirectory().getAbsolutePath();

        configFolder = new File(path, Emoticons.MOD_ID);
        configFolder.mkdir();

        Dispatcher.register();

        if (BBIntegration.isLoaded()) BBIntegration.register();
        if (MetamorphHandler.isLoaded()) MetamorphHandler.register();
    }

    public void init(FMLInitializationEvent event)
    {
        Emotes.register();

        /* Register the capability */
        CapabilityManager.INSTANCE.register(ICosmetic.class, new CosmeticStorage(), Cosmetic::new);

        /* Register event handlers */
        MinecraftForge.EVENT_BUS.register(new CapabilitiesHandler());
    }
}