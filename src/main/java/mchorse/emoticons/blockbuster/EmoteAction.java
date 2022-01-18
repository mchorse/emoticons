package mchorse.emoticons.blockbuster;

import io.netty.buffer.ByteBuf;
import mchorse.blockbuster.recording.actions.Action;
import mchorse.emoticons.api.metamorph.MetamorphHandler;
import mchorse.emoticons.common.EmoteAPI;
import mchorse.emoticons.common.emotes.Emote;
import mchorse.emoticons.common.emotes.Emotes;
import mchorse.emoticons.network.Dispatcher;
import mchorse.emoticons.network.common.PacketEmote;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;

public class EmoteAction extends Action
{
    public String emote = "";

    public EmoteAction()
    {}

    public EmoteAction(String action)
    {
        this.emote = action;
    }

    @Override
    public void apply(EntityLivingBase actor)
    {
        if (actor.world.isRemote)
        {
            Emote emote = Emotes.get(this.emote);

            MetamorphHandler.setEmote(emote, actor);
        }
        else if (actor instanceof EntityPlayerMP)
        {
            EmoteAPI.setEmote(this.emote, (EntityPlayerMP) actor);
        }
        else
        {
            Emote emote = Emotes.get(this.emote);

            MetamorphHandler.setEmote(emote, actor);
            Dispatcher.sendToTracked(actor, new PacketEmote(actor.getEntityId(), emote));
        }
    }

    @Override
    public boolean isSafe()
    {
        return true;
    }

    @Override
    public void fromBuf(ByteBuf buf)
    {
        super.fromBuf(buf);

        this.emote = ByteBufUtils.readUTF8String(buf);
    }

    @Override
    public void toBuf(ByteBuf buf)
    {
        super.toBuf(buf);

        ByteBufUtils.writeUTF8String(buf, this.emote);
    }

    @Override
    public void fromNBT(NBTTagCompound tag)
    {
        super.fromNBT(tag);

        this.emote = tag.getString("Emote");
    }

    @Override
    public void toNBT(NBTTagCompound tag)
    {
        super.toNBT(tag);

        tag.setString("Emote", this.emote);
    }
}