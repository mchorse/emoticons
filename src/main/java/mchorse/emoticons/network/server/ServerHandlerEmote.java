package mchorse.emoticons.network.server;

import mchorse.emoticons.common.EmoteAPI;
import mchorse.emoticons.network.common.PacketEmote;
import mchorse.mclib.network.ServerMessageHandler;
import net.minecraft.entity.player.EntityPlayerMP;

public class ServerHandlerEmote extends ServerMessageHandler<PacketEmote>
{
    @Override
    public void run(EntityPlayerMP player, PacketEmote message)
    {
        EmoteAPI.setEmote(message.emote, player);
    }
}