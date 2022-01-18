package mchorse.emoticons.skin_n_bones.api.animation.model;

import mchorse.emoticons.skin_n_bones.api.bobj.BOBJArmature;
import net.minecraft.entity.EntityLivingBase;

/**
 * Animator interface is responsible for calculating animated 
 * transformations based on some calculations.
 */
public interface IAnimator
{
    /**
     * Refreshes the configuration 
     */
    public void refresh();

    /**
     * Set current emote action playback 
     */
    public void setEmote(ActionPlayback emote);

    /**
     * Update animator. This method is responsible for updating action 
     * pipeline and also change current actions based on entity's state.
     */
    public void update(EntityLivingBase target);

    public BOBJArmature useArmature(BOBJArmature armature);

    /**
     * Apply currently running action pipeline onto given armature
     */
    public void applyActions(BOBJArmature armature, float partialTicks);
}