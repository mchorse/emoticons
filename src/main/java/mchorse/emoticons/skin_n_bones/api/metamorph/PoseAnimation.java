package mchorse.emoticons.skin_n_bones.api.metamorph;

import mchorse.emoticons.skin_n_bones.api.animation.model.AnimatorPoseTransform;
import mchorse.emoticons.skin_n_bones.api.bobj.BOBJArmature;
import mchorse.metamorph.api.morphs.utils.Animation;
import org.lwjgl.Sys;

/**
 * Animation details
 */
public class PoseAnimation extends Animation
{
    public static final AnimatorPoseTransform ZERO = new AnimatorPoseTransform("");

    public AnimatedPose last;
    public AnimatedPose pose = new AnimatedPose();

    @Override
    public void merge(Animation animation)
    {
        super.merge(animation);
        this.progress = 0;
        this.pose.bones.clear();
    }

    public AnimatedPose calculatePose(AnimatedPose pose, BOBJArmature armature, float partialTicks)
    {
        float factor = this.getFactor(partialTicks);

        for (String key : armature.bones.keySet())
        {
            AnimatorPoseTransform trans = this.pose.bones.get(key);
            AnimatorPoseTransform last = this.last == null ? null : this.last.bones.get(key);
            AnimatorPoseTransform current = pose == null ? null : pose.bones.get(key);

            if (trans == null)
            {
                trans = new AnimatorPoseTransform(key);
                this.pose.bones.put(key, trans);
            }

            if (last == null) last = ZERO;
            if (current == null) current = ZERO;

            trans.fixed = this.interp.interpolate(last.fixed, current.fixed, factor);
            trans.interpolate(last, current, factor, this.interp);
        }

        return this.pose;
    }
}