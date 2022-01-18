package mchorse.emoticons.network.client;

import mchorse.emoticons.ClientProxy;
import mchorse.emoticons.capabilities.cosmetic.CosmeticMode;
import mchorse.emoticons.network.common.PacketGameMode;
import mchorse.mclib.network.ClientMessageHandler;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ClientHandlerGameMode extends ClientMessageHandler<PacketGameMode>
{
    @Override
    @SideOnly(Side.CLIENT)
    public void run(EntityPlayerSP player, PacketGameMode message)
    {
        ClientProxy.mode = CosmeticMode.SERVER;
    }
}