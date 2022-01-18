package mchorse.emoticons.skin_n_bones.api.animation.model;

/**
 * Animator factory's only job is to create an animator for 
 * {@link AnimatorController} 
 */
public interface IAnimatorFactory
{
    /**
     * Create an animator for controller 
     */
    public IAnimator createAnimator(AnimatorController controller);
}