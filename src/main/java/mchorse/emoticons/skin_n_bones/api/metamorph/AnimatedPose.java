package mchorse.emoticons.skin_n_bones.api.metamorph;

import mchorse.emoticons.skin_n_bones.api.animation.model.AnimatorPoseTransform;
import net.minecraft.nbt.NBTTagCompound;

import java.util.HashMap;
import java.util.Map;

public class AnimatedPose
{
    public final Map<String, AnimatorPoseTransform> bones = new HashMap<String, AnimatorPoseTransform>();

    public void copy(AnimatedPose pose)
    {
        for (Map.Entry<String, AnimatorPoseTransform> entry : this.bones.entrySet())
        {
            AnimatorPoseTransform transform = pose.bones.get(entry.getKey());

            if (transform != null)
            {
                entry.getValue().copy(transform);
            }
        }
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof AnimatedPose)
        {
            return this.bones.equals(((AnimatedPose) obj).bones);
        }

        return super.equals(obj);
    }

    public AnimatedPose clone()
    {
        AnimatedPose pose = new AnimatedPose();

        for (Map.Entry<String, AnimatorPoseTransform> entry : this.bones.entrySet())
        {
            pose.bones.put(entry.getKey(), entry.getValue().clone());
        }

        return pose;
    }

    public void fromNBT(NBTTagCompound tag)
    {
        for (String key : tag.getKeySet())
        {
            AnimatorPoseTransform config = new AnimatorPoseTransform(key);

            config.fromNBT(tag.getCompoundTag(key));
            this.bones.put(key, config);
        }
    }

    public NBTTagCompound toNBT()
    {
        NBTTagCompound pose = new NBTTagCompound();

        for (Map.Entry<String, AnimatorPoseTransform> entry : this.bones.entrySet())
        {
            pose.setTag(entry.getKey(), entry.getValue().toNBT(null));
        }

        return pose;
    }
}