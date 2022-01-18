package mchorse.emoticons.network.client;

import mchorse.emoticons.api.metamorph.MetamorphHandler;
import mchorse.emoticons.capabilities.cosmetic.Cosmetic;
import mchorse.emoticons.capabilities.cosmetic.ICosmetic;
import mchorse.emoticons.network.common.PacketEmote;
import mchorse.mclib.network.ClientMessageHandler;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ClientHandlerEmote extends ClientMessageHandler<PacketEmote>
{
    @Override
    @SideOnly(Side.CLIENT)
    public void run(EntityPlayerSP player, PacketEmote message)
    {
        Entity entity = player.world.getEntityByID(message.id);

        if (entity != null)
        {
            ICosmetic cap = Cosmetic.get(entity);

            if (cap != null)
            {
                cap.setEmote(message.emote, (EntityLivingBase) entity);
            }

            if (MetamorphHandler.isLoaded() && entity instanceof EntityLivingBase)
            {
                MetamorphHandler.setEmote(message.emote, (EntityLivingBase) entity);
            }
        }
    }
}