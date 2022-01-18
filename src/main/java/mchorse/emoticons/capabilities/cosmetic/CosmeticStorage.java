package mchorse.emoticons.capabilities.cosmetic;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;

public class CosmeticStorage implements IStorage<ICosmetic>
{
    @Override
    public NBTBase writeNBT(Capability<ICosmetic> capability, ICosmetic instance, EnumFacing side)
    {
        return new NBTTagCompound();
    }

    @Override
    public void readNBT(Capability<ICosmetic> capability, ICosmetic instance, EnumFacing side, NBTBase nbt)
    {}
}