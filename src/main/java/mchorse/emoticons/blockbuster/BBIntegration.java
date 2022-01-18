package mchorse.emoticons.blockbuster;

import mchorse.blockbuster.Blockbuster;
import mchorse.blockbuster.CommonProxy;
import mchorse.blockbuster.recording.actions.Action;
import mchorse.blockbuster.recording.actions.ActionRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Optional.Method;

import java.util.List;

public class BBIntegration
{
    public static boolean isLoaded()
    {
        return Loader.isModLoaded(Blockbuster.MOD_ID);
    }

    @Method(modid = Blockbuster.MOD_ID)
    public static void register()
    {
        ActionRegistry.register("fc_emote", EmoteAction.class);
    }

    @Method(modid = Blockbuster.MOD_ID)
    public static void recordEmote(String emote, EntityPlayer player)
    {
        List<Action> events = CommonProxy.manager.getActions(player);

        if (events != null)
        {
            events.add(new EmoteAction(emote));
        }
    }
}