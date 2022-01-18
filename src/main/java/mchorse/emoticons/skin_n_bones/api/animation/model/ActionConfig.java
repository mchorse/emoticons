package mchorse.emoticons.skin_n_bones.api.animation.model;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagString;
import net.minecraftforge.common.util.Constants.NBT;

import java.util.Objects;

public class ActionConfig
{
    public String name = "";
    public boolean clamp = true;
    public boolean reset = true;
    public float speed = 1;
    public float fade = 5;
    public int tick = 0;

    public ActionConfig()
    {}

    public ActionConfig(String name)
    {
        this.name = name;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof ActionConfig)
        {
            ActionConfig config = (ActionConfig) obj;

            return Objects.equals(this.name, config.name)
                && this.clamp == config.clamp
                && this.reset == config.reset
                && this.speed == config.speed
                && this.fade == config.fade
                && this.tick == config.tick;
        }

        return super.equals(obj);
    }

    @Override
    public ActionConfig clone()
    {
        ActionConfig config = new ActionConfig(this.name);

        config.clamp = this.clamp;
        config.reset = this.reset;
        config.speed = this.speed;
        config.fade = this.fade;
        config.tick = this.tick;

        return config;
    }

    public void fromNBT(NBTBase base)
    {
        if (base instanceof NBTTagCompound)
        {
            NBTTagCompound tag = (NBTTagCompound) base;

            if (tag.hasKey("Name", NBT.TAG_STRING)) this.name = tag.getString("Name");
            if (tag.hasKey("Clamp", NBT.TAG_ANY_NUMERIC)) this.clamp = tag.getBoolean("Clamp");
            if (tag.hasKey("Reset", NBT.TAG_ANY_NUMERIC)) this.reset = tag.getBoolean("Reset");
            if (tag.hasKey("Speed", NBT.TAG_ANY_NUMERIC)) this.speed = tag.getFloat("Speed");
            if (tag.hasKey("Fade", NBT.TAG_ANY_NUMERIC)) this.fade = tag.getInteger("Fade");
            if (tag.hasKey("Tick", NBT.TAG_ANY_NUMERIC)) this.tick = tag.getInteger("Tick");
        }
        else if (base instanceof NBTTagString)
        {
            this.name = ((NBTTagString) base).getString();
        }
    }

    public NBTBase toNBT()
    {
        if (!this.name.isEmpty() && this.isDefault())
        {
            return new NBTTagString(this.name);
        }

        NBTTagCompound tag = new NBTTagCompound();

        if (!this.name.isEmpty()) tag.setString("Name", this.name);
        if (this.clamp != true) tag.setBoolean("Clamp", this.clamp);
        if (this.reset != true) tag.setBoolean("Reset", this.reset);
        if (this.speed != 1) tag.setFloat("Speed", this.speed);
        if (this.fade != 5) tag.setInteger("Fade", (int) this.fade);
        if (this.tick != 0) tag.setInteger("Tick", this.tick);

        return tag;
    }

    public boolean isDefault()
    {
        return this.clamp && this.reset && this.speed == 1 && this.fade == 5 && this.tick == 0;
    }
}