package mchorse.emoticons.skin_n_bones.api.animation.model;

import mchorse.emoticons.skin_n_bones.api.animation.AnimationMeshConfig;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants.NBT;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.HashMap;
import java.util.Map;

/**
 * Animated morph config. This class represents configuration class for 
 * a model to be customized by user either via JSON in 
 * user's animations folder or via NBT and /morph command.
 */
@SideOnly(Side.CLIENT)
public class AnimatorConfig
{
    /**
     * The display name of the this morph
     */
    public String name = "";

    /**
     * The primary mesh of this player-model (used currently by 
     * Metamorph only)
     */
    public String primaryMesh = "";

    /**
     * Animated model's scale 
     */
    public float scale = 1;

    /**
     * Animated model's scale within GUI 
     */
    public float scaleGui = 1;

    /**
     * Held items' scale 
     */
    public float scaleItems = 1;

    /**
     * Should held items be rendered
     */
    public boolean renderHeldItems = true;

    /**
     * Names of bones that should be used to render off hand item
     */
    public Map<String, AnimatorHeldItemConfig> leftHands = new HashMap<String, AnimatorHeldItemConfig>();

    /**
     * Names of bones that should be used to render main hand item 
     */
    public Map<String, AnimatorHeldItemConfig> rightHands = new HashMap<String, AnimatorHeldItemConfig>();

    /**
     * Name of the bone that should be used as a head (i.e. look into 
     * direction of player's sight) 
     */
    public String head = "head";

    /**
     * Configuration for actions 
     */
    public AnimatorActionsConfig actions = new AnimatorActionsConfig();

    /**
     * Per mesh configuration
     */
    public Map<String, AnimationMeshConfig> meshes = new HashMap<String, AnimationMeshConfig>();

    public AnimatorConfig()
    {}

    /**
     * Overwrite all properties from given config 
     */
    public void copy(AnimatorConfig config)
    {
        this.name = config.name;
        this.primaryMesh = config.primaryMesh;

        this.scale = config.scale;
        this.scaleGui = config.scaleGui;
        this.scaleItems = config.scaleItems;

        this.renderHeldItems = config.renderHeldItems;
        this.head = config.head;

        this.actions.copy(config.actions);
        this.leftHands.clear();
        this.rightHands.clear();
        this.meshes.clear();

        for (Map.Entry<String, AnimatorHeldItemConfig> entry : config.leftHands.entrySet())
        {
            this.leftHands.put(entry.getKey(), entry.getValue().clone());
        }

        for (Map.Entry<String, AnimatorHeldItemConfig> entry : config.rightHands.entrySet())
        {
            this.rightHands.put(entry.getKey(), entry.getValue().clone());
        }

        for (Map.Entry<String, AnimationMeshConfig> entry : config.meshes.entrySet())
        {
            this.meshes.put(entry.getKey(), entry.getValue().clone());
        }
    }

    /**
     * Read NBT tag for overwritable config properties 
     */
    public void fromNBT(NBTTagCompound tag)
    {
        if (tag.hasKey("Name", NBT.TAG_STRING))
        {
            this.name = tag.getString("Name");
        }

        if (tag.hasKey("Scale", NBT.TAG_ANY_NUMERIC))
        {
            this.scale = tag.getFloat("Scale");
        }

        if (tag.hasKey("ScaleGUI", NBT.TAG_ANY_NUMERIC))
        {
            this.scaleGui = tag.getFloat("ScaleGUI");
        }

        if (tag.hasKey("ScaleItems", NBT.TAG_ANY_NUMERIC))
        {
            this.scaleItems = tag.getFloat("ScaleItems");
        }

        if (tag.hasKey("RenderHeldItems", NBT.TAG_ANY_NUMERIC))
        {
            this.renderHeldItems = tag.getBoolean("RenderHeldItems");
        }

        if (tag.hasKey("LeftHands"))
        {
            this.readHandsFromNBT(this.leftHands, tag.getTag("LeftHands"));
        }

        if (tag.hasKey("RightHands"))
        {
            this.readHandsFromNBT(this.rightHands, tag.getTag("RightHands"));
        }

        if (tag.hasKey("Head", NBT.TAG_STRING))
        {
            this.head = tag.getString("Head");
        }

        if (tag.hasKey("Actions", NBT.TAG_COMPOUND))
        {
            this.actions.fromNBT(tag.getCompoundTag("Actions"));
        }

        if (tag.hasKey("Meshes", NBT.TAG_COMPOUND))
        {
            NBTTagCompound meshes = tag.getCompoundTag("Meshes");

            for (String key : meshes.getKeySet())
            {
                NBTBase nbt = meshes.getTag(key);
                AnimationMeshConfig config = this.meshes.get(key);

                if (config == null)
                {
                    this.meshes.put(key, config = new AnimationMeshConfig());
                }

                if (nbt.getId() == NBT.TAG_COMPOUND)
                {
                    config.fromNBT((NBTTagCompound) nbt);
                }
            }
        }
    }

    public NBTTagCompound toNBT(NBTTagCompound tag)
    {
        if (tag == null)
        {
            tag = new NBTTagCompound();
        }

        if (!this.name.isEmpty()) tag.setString("Name", this.name);
        if (this.scale != 1) tag.setFloat("Scale", this.scale);
        if (this.scaleGui != 1) tag.setFloat("ScaleGUI", this.scaleGui);
        if (this.scaleItems != 1) tag.setFloat("ScaleItems", this.scaleItems);
        if (this.renderHeldItems != true) tag.setBoolean("RenderHeldItems", this.renderHeldItems);
        if (!this.head.equals("head")) tag.setString("Head", this.head);

        /* Hands */
        if (!this.leftHands.isEmpty()) tag.setTag("LeftHands", this.writeHandsToNBT(this.leftHands));
        if (!this.rightHands.isEmpty()) tag.setTag("RightHands", this.writeHandsToNBT(this.rightHands));

        /* Actions */
        NBTTagCompound actions = this.actions.toNBT(null);

        if (actions != null && !actions.hasNoTags())
        {
            tag.setTag("Actions", actions);
        }

        /* Meshes */
        if (!this.meshes.isEmpty())
        {
            NBTTagCompound meshes = new NBTTagCompound();

            for (Map.Entry<String, AnimationMeshConfig> entry : this.meshes.entrySet())
            {
                meshes.setTag(entry.getKey(), entry.getValue().toNBT(null));
            }

            tag.setTag("Meshes", meshes);
        }

        return tag;
    }

    /**
     * This method is responsible for reading held item configuration 
     * from given NBT tag.  
     */
    private void readHandsFromNBT(Map<String, AnimatorHeldItemConfig> hands, NBTBase tag)
    {
        hands.clear();

        if (tag.getId() == NBT.TAG_LIST)
        {
            NBTTagList list = (NBTTagList) tag;

            for (int i = 0, c = list.tagCount(); i < c; i++)
            {
                String key = list.getStringTagAt(i);

                hands.put(key, new AnimatorHeldItemConfig(key));
            }
        }
        else if (tag.getId() == NBT.TAG_COMPOUND)
        {
            NBTTagCompound compound = (NBTTagCompound) tag;

            for (String key : compound.getKeySet())
            {
                AnimatorHeldItemConfig item = hands.get(key);

                if (item == null)
                {
                    hands.put(key, item = new AnimatorHeldItemConfig(key));
                }

                item.fromNBT(compound.getCompoundTag(key));
            }
        }
    }

    /**
     * Write held item transformations to NBT
     */
    private NBTTagCompound writeHandsToNBT(Map<String, AnimatorHeldItemConfig> hands)
    {
        NBTTagCompound tag = new NBTTagCompound();

        for (Map.Entry<String, AnimatorHeldItemConfig> entry : hands.entrySet())
        {
            tag.setTag(entry.getKey(), entry.getValue().toNBT(null));
        }

        return tag;
    }

    @SideOnly(Side.CLIENT)
    public static class AnimatorConfigEntry
    {
        public AnimatorConfig config;
        public long lastModified;

        public AnimatorConfigEntry(AnimatorConfig config, long lastModified)
        {
            this.config = config;
            this.lastModified = lastModified;
        }
    }
}