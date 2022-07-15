package mchorse.emoticons.skin_n_bones.api.animation.model;

import mchorse.mclib.client.gui.framework.elements.input.GuiTransformations;
import mchorse.mclib.utils.ITransformationObject;
import mchorse.mclib.utils.Interpolation;
import mchorse.mclib.utils.MatrixUtils;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.Constants.NBT;

import javax.vecmath.Matrix4f;
import javax.vecmath.Vector4f;

/**
 * Configuration class for hand held item transformations 
 */
public class AnimatorHeldItemConfig implements ITransformationObject
{
    public String boneName = "";

    /* Translate */
    public float x;
    public float y;
    public float z;

    /* Scale */
    public float scaleX = 1;
    public float scaleY = 1;
    public float scaleZ = 1;

    /* Rotate */
    public float rotateX;
    public float rotateY;
    public float rotateZ;

    public AnimatorHeldItemConfig(String name)
    {
        this.boneName = name;
    }

    @Override
    public void addTranslation(double x, double y, double z, GuiTransformations.TransformOrientation orientation)
    {
        Vector4f trans = new Vector4f((float) x,(float) y,(float) z, 1);

        if (orientation == GuiTransformations.TransformOrientation.LOCAL)
        {
            MatrixUtils.getRotationMatrix(this.rotateX, this.rotateY, this.rotateZ, MatrixUtils.RotationOrder.XYZ).transform(trans);
        }

        this.x += trans.x;
        this.y += trans.y;
        this.z += trans.z;
    }

    public void interpolate(AnimatorHeldItemConfig a, AnimatorHeldItemConfig b, float x, Interpolation interp)
    {
        this.x = interp.interpolate(a.x, b.x, x);
        this.y = interp.interpolate(a.y, b.y, x);
        this.z = interp.interpolate(a.z, b.z, x);
        this.scaleX = interp.interpolate(a.scaleX, b.scaleX, x);
        this.scaleY = interp.interpolate(a.scaleY, b.scaleY, x);
        this.scaleZ = interp.interpolate(a.scaleZ, b.scaleZ, x);
        this.rotateX = interp.interpolate(a.rotateX, b.rotateX, x);
        this.rotateY = interp.interpolate(a.rotateY, b.rotateY, x);
        this.rotateZ = interp.interpolate(a.rotateZ, b.rotateZ, x);
    }

    /**
     * Clone this object 
     */
    @Override
    public AnimatorHeldItemConfig clone()
    {
        AnimatorHeldItemConfig item = new AnimatorHeldItemConfig(this.boneName);

        item.x = this.x;
        item.y = this.y;
        item.z = this.z;
        item.scaleX = this.scaleX;
        item.scaleY = this.scaleY;
        item.scaleZ = this.scaleZ;
        item.rotateX = this.rotateX;
        item.rotateY = this.rotateY;
        item.rotateZ = this.rotateZ;

        return item;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof AnimatorHeldItemConfig)
        {
            AnimatorHeldItemConfig config = (AnimatorHeldItemConfig) obj;

            boolean result = config.x == this.x && config.y == this.y && config.z == this.z;

            result = result && config.scaleX == this.scaleX && config.scaleY == this.scaleY && config.scaleZ == this.scaleZ;
            result = result && config.rotateX == this.rotateX && config.rotateY == this.rotateY && config.rotateZ == this.rotateZ;

            return result;
        }

        return super.equals(obj);
    }

    public void fromNBT(NBTTagCompound tag)
    {
        if (tag.hasKey("X", NBT.TAG_ANY_NUMERIC))
        {
            this.x = tag.getFloat("X");
        }

        if (tag.hasKey("Y", NBT.TAG_ANY_NUMERIC))
        {
            this.y = tag.getFloat("Y");
        }

        if (tag.hasKey("Z", NBT.TAG_ANY_NUMERIC))
        {
            this.z = tag.getFloat("Z");
        }

        if (tag.hasKey("SX", NBT.TAG_ANY_NUMERIC))
        {
            this.scaleX = tag.getFloat("SX");
        }

        if (tag.hasKey("SY", NBT.TAG_ANY_NUMERIC))
        {
            this.scaleY = tag.getFloat("SY");
        }

        if (tag.hasKey("SZ", NBT.TAG_ANY_NUMERIC))
        {
            this.scaleZ = tag.getFloat("SZ");
        }

        if (tag.hasKey("RX", NBT.TAG_ANY_NUMERIC))
        {
            this.rotateX = tag.getFloat("RX");
        }

        if (tag.hasKey("RY", NBT.TAG_ANY_NUMERIC))
        {
            this.rotateY = tag.getFloat("RY");
        }

        if (tag.hasKey("RZ", NBT.TAG_ANY_NUMERIC))
        {
            this.rotateZ = tag.getFloat("RZ");
        }
    }

    public NBTTagCompound toNBT(NBTTagCompound tag)
    {
        if (tag == null)
        {
            tag = new NBTTagCompound();
        }

        if (this.x != 0) tag.setFloat("X", this.x);
        if (this.y != 0) tag.setFloat("Y", this.y);
        if (this.z != 0) tag.setFloat("Z", this.z);
        if (this.scaleX != 1) tag.setFloat("SX", this.scaleX);
        if (this.scaleY != 1) tag.setFloat("SY", this.scaleY);
        if (this.scaleZ != 1) tag.setFloat("SZ", this.scaleZ);
        if (this.rotateX != 0) tag.setFloat("RX", this.rotateX);
        if (this.rotateY != 0) tag.setFloat("RY", this.rotateY);
        if (this.rotateZ != 0) tag.setFloat("RZ", this.rotateZ);

        return tag;
    }
}