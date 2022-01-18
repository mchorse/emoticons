package mchorse.emoticons.capabilities.cosmetic;

import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

public class CosmeticProvider implements ICapabilityProvider
{
    @CapabilityInject(ICosmetic.class)
    public static final Capability<ICosmetic> COSMETIC = null;

    private ICosmetic instance = COSMETIC.getDefaultInstance();

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing)
    {
        return capability == COSMETIC;
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing)
    {
        return capability == COSMETIC ? COSMETIC.<T>cast(this.instance) : null;
    }
}