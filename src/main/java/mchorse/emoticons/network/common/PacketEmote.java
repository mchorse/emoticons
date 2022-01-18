package mchorse.emoticons.network.common;

import io.netty.buffer.ByteBuf;
import mchorse.emoticons.common.emotes.Emote;
import mchorse.emoticons.common.emotes.Emotes;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class PacketEmote implements IMessage
{
    public int id;
    public Emote emote;

    public PacketEmote()
    {}

    public PacketEmote(int id, Emote emote)
    {
        this.id = id;
        this.emote = emote;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.id = buf.readInt();
        this.emote = Emotes.get(ByteBufUtils.readUTF8String(buf));
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(this.id);
        ByteBufUtils.writeUTF8String(buf, this.emote == null ? "" : this.emote.getKey());
    }
}