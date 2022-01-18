package mchorse.emoticons.capabilities;

import java.util.HashMap;
import java.util.Map;

import mchorse.emoticons.Emoticons;
import mchorse.emoticons.capabilities.cosmetic.Cosmetic;
import mchorse.emoticons.capabilities.cosmetic.CosmeticProvider;
import mchorse.emoticons.capabilities.cosmetic.ICosmetic;
import mchorse.emoticons.network.Dispatcher;
import mchorse.emoticons.network.common.PacketGameMode;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

/**
 * Capability handler class
 */
public class CapabilitiesHandler
{
    /**
     * Resource location for cosmetic capability 
     */
    public static final ResourceLocation COSMETIC = new ResourceLocation(Emoticons.MOD_ID, "cosmetic");

    public Map<EntityPlayer, Boolean> players = new HashMap<EntityPlayer, Boolean>();

    /**
     * Attach player capabilities
     */
    @SubscribeEvent
    public void attachPlayerCapability(AttachCapabilitiesEvent<Entity> event)
    {
        if (event.getObject() instanceof EntityPlayer)
        {
            event.addCapability(COSMETIC, new CosmeticProvider());
        }
    }

    @SubscribeEvent
    public void onUpdateEntity(PlayerTickEvent event)
    {
        if (event.phase == Phase.START)
        {
            return;
        }

        EntityPlayer entity = event.player;
        ICosmetic cap = Cosmetic.get(entity);

        if (cap != null)
        {
            cap.update(entity);
        }
    }

    /**
     * When player logs on the server, request 
     */
    @SubscribeEvent
    public void playerLogsIn(PlayerLoggedInEvent event)
    {
        Dispatcher.sendTo(new PacketGameMode(), (EntityPlayerMP) event.player);
    }
}