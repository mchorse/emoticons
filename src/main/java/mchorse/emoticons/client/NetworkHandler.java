package mchorse.emoticons.client;

import java.nio.charset.Charset;

import mchorse.emoticons.ClientProxy;
import mchorse.emoticons.capabilities.cosmetic.Cosmetic;
import mchorse.emoticons.capabilities.cosmetic.ICosmetic;
import mchorse.emoticons.capabilities.cosmetic.CosmeticMode;
import mchorse.emoticons.common.emotes.Emotes;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientCustomPacketEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Custom payload network hander
 * 
 * This network handler allows plugins to emote players
 */
@SideOnly(Side.CLIENT)
public class NetworkHandler
{
    /**
     * This handler handles custom payload messages 
     */
    @SubscribeEvent
    public void onCustomMessage(ClientCustomPacketEvent event)
    {
        try
        {
            PacketBuffer buffer = (PacketBuffer) event.getPacket().payload();
            byte[] array = new byte[buffer.capacity()];

            for (int i = 0, c = array.length; i < c; i++)
            {
                array[i] = buffer.readByte();
            }

            String data = new String(array, Charset.forName("UTF-8")).trim();
            String[] args = data.split(" ");

            if (args.length >= 1)
            {
                Minecraft.getMinecraft().addScheduledTask(new EmoteRunnable(args));
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Emote runnable
     * 
     * This class is responsible for emoting, the format for messages are:
     * 
     * - <player_name> = enable plugin mode
     * - <player_name> <emote> = emote
     */
    public static class EmoteRunnable implements Runnable
    {
        public String[] args;

        public EmoteRunnable(String[] args)
        {
            this.args = args;
        }

        @Override
        public void run()
        {
            EntityPlayer player = Minecraft.getMinecraft().world.getPlayerEntityByName(this.args[0]);

            if (player == null)
            {
                return;
            }

            ICosmetic cap = Cosmetic.get(player);

            if (this.args.length == 1)
            {
                ClientProxy.mode = CosmeticMode.PLUGIN;
            }
            else if (this.args.length > 1 && ClientProxy.mode == CosmeticMode.PLUGIN)
            {
                cap.setEmote(Emotes.get(args[1]), player);
            }
        }
    }
}