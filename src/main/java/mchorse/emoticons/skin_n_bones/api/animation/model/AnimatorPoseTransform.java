package mchorse.emoticons.skin_n_bones.api.animation.model;

import net.minecraft.nbt.NBTTagCompound;

public class AnimatorPoseTransform extends AnimatorHeldItemConfig
{
    public static final int FIXED = 0;
    public static final int ANIMATED = 1;

    public float fixed = ANIMATED;

    public AnimatorPoseTransform(String name)
    {
        super(name);
    }

    public AnimatorPoseTransform clone()
    {
        AnimatorPoseTransform item = new AnimatorPoseTransform(this.boneName);

        item.copy(this);

        return item;
    }

    public void copy(AnimatorPoseTransform transform)
    {
        this.x = transform.x;
        this.y = transform.y;
        this.z = transform.z;
        this.scaleX = transform.scaleX;
        this.scaleY = transform.scaleY;
        this.scaleZ = transform.scaleZ;
        this.rotateX = transform.rotateX;
        this.rotateY = transform.rotateY;
        this.rotateZ = transform.rotateZ;
        this.fixed = transform.fixed;
    }

    @Override
    public boolean equals(Object obj)
    {
        boolean result = super.equals(obj);

        if (obj instanceof AnimatorPoseTransform)
        {
            result = result && this.fixed == ((AnimatorPoseTransform) obj).fixed;
        }

        return result;
    }

    @Override
    public void fromNBT(NBTTagCompound tag)
    {
        super.fromNBT(tag);

        if (tag.hasKey("F")) this.fixed = tag.getBoolean("F") ? ANIMATED : FIXED;
    }

    @Override
    public NBTTagCompound toNBT(NBTTagCompound tag)
    {
        tag = super.toNBT(tag);

        if (this.fixed != ANIMATED) tag.setBoolean("F", false);

        return tag;
    }
}