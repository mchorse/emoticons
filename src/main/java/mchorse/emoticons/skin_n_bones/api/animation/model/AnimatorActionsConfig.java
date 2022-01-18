package mchorse.emoticons.skin_n_bones.api.animation.model;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Animated actions config. This little dude right there is 
 * responsible for storing configuration for the name of actions 
 * which should be used for particular <s>set of skills</s> actions. 
 */
@SideOnly(Side.CLIENT)
public class AnimatorActionsConfig
{
    public Map<String, ActionConfig> actions = new HashMap<String, ActionConfig>();

    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof AnimatorActionsConfig)
        {
            AnimatorActionsConfig config = (AnimatorActionsConfig) obj;

            return Objects.equals(this.actions, config.actions);
        }

        return super.equals(obj);
    }

    public void copy(AnimatorActionsConfig config)
    {
        this.actions.clear();
        this.actions.putAll(config.actions);
    }

    public void fromNBT(NBTTagCompound tag)
    {
        this.actions.clear();

        for (String key : tag.getKeySet())
        {
            NBTBase base = tag.getTag(key);
            String newKey = this.toKey(key);
            ActionConfig config = new ActionConfig(newKey);

            config.fromNBT(base);
            this.actions.put(newKey, config);
        }
    }

    public NBTTagCompound toNBT(NBTTagCompound tag)
    {
        if (this.actions.isEmpty())
        {
            return null;
        }

        if (tag == null)
        {
            tag = new NBTTagCompound();
        }

        for (Map.Entry<String, ActionConfig> entry : this.actions.entrySet())
        {
            ActionConfig action = entry.getValue();
            String key = entry.getKey();

            if (!(key.equals(action.name) && action.isDefault()))
            {
                tag.setTag(key, action.toNBT());
            }
        }

        return tag;
    }

    /**
     * Get key for the action 
     */
    public ActionConfig getConfig(String key)
    {
        ActionConfig output = this.actions.get(key);

        return output == null ? new ActionConfig(key) : output;
    }

    /**
     * Translates JSON or NBT (camelCase or PascalCase) based key into 
     * internal under_score case.  
     */
    public String toKey(String key)
    {
        return key.replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase();
    }
}