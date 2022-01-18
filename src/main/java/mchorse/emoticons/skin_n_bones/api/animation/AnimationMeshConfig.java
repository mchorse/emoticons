package mchorse.emoticons.skin_n_bones.api.animation;

import mchorse.mclib.utils.resources.RLUtils;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.Constants.NBT;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

/**
 * Animation mesh config. This class is responsible for storing data for 
 * mesh configuration. 
 */
@SideOnly(Side.CLIENT)
public class AnimationMeshConfig
{
    /**
     * Texture which should be used for given mesh 
     */
    public ResourceLocation texture;

    /**
     * GL texture filtering for both min and mag
     */
    public int filtering = GL11.GL_NEAREST;

    /**
     * Do normals affect the lighting
     */
    public boolean normals = false;

    /**
     * Is this mesh using smooth shading 
     */
    public boolean smooth = false;

    /**
     * Whether this mesh is visible 
     */
    public boolean visible = true;

    /**
     * Does light map getting applied on this mesh 
     */
    public boolean lighting = true;

    /**
     * Color filter
     */
    public int color = 0xffffff;

    /**
     * Clone this object 
     */
    @Override
    public AnimationMeshConfig clone()
    {
        AnimationMeshConfig config = new AnimationMeshConfig();

        config.texture = this.texture;
        config.filtering = this.filtering;
        config.normals = this.normals;
        config.smooth = this.smooth;
        config.visible = this.visible;
        config.lighting = this.lighting;
        config.color = this.color;

        return config;
    }

    /**
     * Populate animation mesh config properties from NBT tag 
     */
    public void fromNBT(NBTTagCompound tag)
    {
        if (tag.hasKey("Texture"))
        {
            this.texture = RLUtils.create(tag.getTag("Texture"));
        }

        if (tag.hasKey("Filtering", NBT.TAG_STRING))
        {
            this.filtering = tag.getString("Filtering").equalsIgnoreCase("linear") ? GL11.GL_LINEAR : GL11.GL_NEAREST;
        }

        if (tag.hasKey("Normals", NBT.TAG_ANY_NUMERIC))
        {
            this.normals = tag.getBoolean("Normals");
        }

        if (tag.hasKey("Smooth", NBT.TAG_ANY_NUMERIC))
        {
            this.smooth = tag.getBoolean("Smooth");
        }

        if (tag.hasKey("Visible", NBT.TAG_ANY_NUMERIC))
        {
            this.visible = tag.getBoolean("Visible");
        }

        if (tag.hasKey("Lighting", NBT.TAG_ANY_NUMERIC))
        {
            this.lighting = tag.getBoolean("Lighting");
        }

        if (tag.hasKey("Color", NBT.TAG_ANY_NUMERIC))
        {
            this.color = tag.getInteger("Color");
        }
    }

    public NBTTagCompound toNBT(NBTTagCompound tag)
    {
        if (tag == null)
        {
            tag = new NBTTagCompound();
        }

        if (this.texture != null) tag.setTag("Texture", RLUtils.writeNbt(this.texture));
        tag.setString("Filtering", this.filtering == GL11.GL_NEAREST ? "nearest" : "linear");
        tag.setBoolean("Normals", this.normals);
        tag.setBoolean("Smooth", this.smooth);
        tag.setBoolean("Visible", this.visible);
        tag.setBoolean("Lighting", this.lighting);
        tag.setInteger("Color", this.color);

        return tag;
    }
}