package mchorse.emoticons.common;

import io.netty.buffer.Unpooled;
import mchorse.emoticons.ClientProxy;
import mchorse.emoticons.Emoticons;
import mchorse.emoticons.capabilities.cosmetic.Cosmetic;
import mchorse.emoticons.capabilities.cosmetic.ICosmetic;
import mchorse.emoticons.capabilities.cosmetic.CosmeticMode;
import mchorse.emoticons.common.emotes.Emote;
import mchorse.emoticons.common.emotes.Emotes;
import mchorse.emoticons.network.Dispatcher;
import mchorse.emoticons.network.common.PacketEmote;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.network.internal.FMLProxyPacket;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EmoteAPI
{
    public static void setEmote(String emote, EntityPlayerMP player)
    {
        setEmote(Emotes.get(emote), player);
    }

    public static void setEmote(Emote emote, EntityPlayerMP player)
    {
        ICosmetic cap = Cosmetic.get(player);

        if (cap != null)
        {
            cap.setEmote(emote, player);
            PacketEmote message = new PacketEmote(player.getEntityId(), emote);

            Dispatcher.sendTo(message, player);
            Dispatcher.sendToTracked(player, message);
        }
    }

    @SideOnly(Side.CLIENT)
    public static void setEmoteClient(String emote, EntityPlayer player)
    {
        ICosmetic cap = Cosmetic.get(player);

        if (cap == null)
        {
            return;
        }

        Emote memote = Emotes.get(emote);
        CosmeticMode mode = ClientProxy.mode;

        if (mode == CosmeticMode.CLIENT)
        {
            cap.setEmote(memote, player);
        }
        else if (mode == CosmeticMode.SERVER)
        {
            Dispatcher.sendToServer(new PacketEmote(0, memote));
        }
        else
        {
            PacketBuffer buffer = new PacketBuffer(Unpooled.buffer(emote.getBytes().length));
            buffer.writeString(emote);

            Emoticons.channel.sendToServer(new FMLProxyPacket(buffer, "Emoticons"));
        }
    }
}