package mchorse.emoticons.network;

import mchorse.emoticons.Emoticons;
import mchorse.emoticons.network.client.ClientHandlerEmote;
import mchorse.emoticons.network.client.ClientHandlerGameMode;
import mchorse.emoticons.network.common.PacketEmote;
import mchorse.emoticons.network.common.PacketGameMode;
import mchorse.emoticons.network.server.ServerHandlerEmote;
import mchorse.mclib.network.AbstractDispatcher;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityTracker;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.relauncher.Side;

/**
 * Network dispatcher
 */
public class Dispatcher
{
    public static final AbstractDispatcher DISPATCHER = new AbstractDispatcher(Emoticons.MOD_ID)
    {
        @Override
        public void register()
        {
            register(PacketGameMode.class, ClientHandlerGameMode.class, Side.CLIENT);
            register(PacketEmote.class, ClientHandlerEmote.class, Side.CLIENT);
            register(PacketEmote.class, ServerHandlerEmote.class, Side.SERVER);
        }
    };

    /**
     * Send message to players who are tracking given entity
     */
    public static void sendToTracked(Entity entity, IMessage message)
    {
        EntityTracker tracker = ((WorldServer) entity.world).getEntityTracker();

        for (EntityPlayer player : tracker.getTrackingPlayers(entity))
        {
            sendTo(message, (EntityPlayerMP) player);
        }
    }

    /**
     * Send message to given player
     */
    public static void sendTo(IMessage message, EntityPlayerMP player)
    {
        DISPATCHER.sendTo(message, player);
    }

    /**
     * Send message to the server
     */
    public static void sendToServer(IMessage message)
    {
        DISPATCHER.sendToServer(message);
    }

    /**
     * Register all the networking messages and message handlers
     */
    public static void register()
    {
        DISPATCHER.register();
    }
}