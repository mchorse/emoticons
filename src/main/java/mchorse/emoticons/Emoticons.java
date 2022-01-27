package mchorse.emoticons;

import mchorse.emoticons.utils.ValueButtons;
import mchorse.mclib.McLib;
import mchorse.mclib.client.gui.utils.keys.IKey;
import mchorse.mclib.config.ConfigBuilder;
import mchorse.mclib.config.values.ValueBoolean;
import mchorse.mclib.config.values.ValueInt;
import mchorse.mclib.events.RegisterConfigEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLEventChannel;
import net.minecraftforge.fml.common.network.NetworkRegistry;

/**
 * Emoticons mod
 * 
 * This mod provides a functionality to load and playback animated 
 * models. This is going to make Minecraft machinimas 100% awesome!
 */
@Mod(modid = Emoticons.MOD_ID, name = "Emoticons", version = Emoticons.VERSION, dependencies = "required-after:mclib@[%MCLIB%,);after:metamorph@[%METAMORPH%,);", updateJSON = "https://raw.githubusercontent.com/mchorse/emoticons/master/version.json")
public final class Emoticons
{
    public static final String MOD_ID = "emoticons";
    public static final String VERSION = "%VERSION%";

    @Mod.Instance
    public static Emoticons instance;

    @SidedProxy(serverSide = "mchorse.emoticons.CommonProxy", clientSide = "mchorse.emoticons.ClientProxy")
    public static CommonProxy proxy;

    public static String config;

    public static ValueBoolean disableSoundEvents;

    public static ValueBoolean disableAnimations;
    public static ValueInt modelType;

    public static ValueInt playerPreviewMode;
    public static ValueInt playerRenderingScale;
    public static ValueInt playerRenderingX;
    public static ValueInt playerRenderingY;

    /**
     * Custom payload channel 
     */
    public static FMLEventChannel channel;

    @SubscribeEvent
    public void onConfigRegister(RegisterConfigEvent event)
    {
        ConfigBuilder builder = event.createBuilder(MOD_ID);

        builder.category("general").register(new ValueButtons("buttons").clientSide());

        disableSoundEvents = builder.getBoolean("disable_sound_events", false);

        disableAnimations = builder.category("animations").getBoolean("disable", true);
        disableAnimations.syncable();
        modelType = builder.getInt("model_type", 0).modes(
            IKey.lang("emoticons.player_model.default"),
            IKey.lang("emoticons.player_model.simple"),
            IKey.lang("emoticons.player_model.extruded"),
            IKey.lang("emoticons.player_model.simple_plus")
        );
        modelType.clientSide();

        playerPreviewMode = builder.category("player_preview").getInt("mode", 0).modes(
            IKey.lang("emoticons.player_preview.emote"),
            IKey.lang("emoticons.player_preview.always"),
            IKey.lang("emoticons.player_preview.never")
        );
        playerRenderingScale = builder.getInt("scale", 20, 10, 220);
        playerRenderingX = builder.getInt("x", 20, 0, 100);
        playerRenderingY = builder.getInt("y", 50, 0, 100);

        builder.getCategory().markClientSide();
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        channel = NetworkRegistry.INSTANCE.newEventDrivenChannel("Emoticons");
        McLib.EVENT_BUS.register(this);

        proxy.preInit(event);
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        proxy.init(event);
    }

    @EventHandler
    public void serverStart(FMLServerStartedEvent event)
    {
        CommonProxy.registerEmotes();
    }
}
