package mchorse.emoticons.skin_n_bones.api.metamorph;

import com.google.common.base.Objects;

import mchorse.emoticons.skin_n_bones.api.animation.AnimationMesh;
import mchorse.emoticons.skin_n_bones.api.bobj.BOBJArmature;
import mchorse.metamorph.api.models.IMorphProvider;
import mchorse.metamorph.api.morphs.AbstractMorph;
import mchorse.metamorph.api.morphs.utils.Animation;
import mchorse.metamorph.api.morphs.utils.IAnimationProvider;
import mchorse.metamorph.api.morphs.utils.IMorphGenerator;
import mchorse.metamorph.api.morphs.utils.ISyncableMorph;
import mchorse.metamorph.bodypart.BodyPartManager;
import mchorse.metamorph.bodypart.IBodyPartProvider;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants.NBT;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Animated morph. This morph class allows players and any morph API 
 * consumers to update and render animated models as entities.
 */
public class AnimatedMorph extends AbstractMorph implements IBodyPartProvider, ISyncableMorph, IAnimationProvider, IMorphGenerator
{
    /**
     * Animation name
     */
    public String animationName = "";

    /**
     * User supplied NBT data 
     */
    public NBTTagCompound userConfigData = new NBTTagCompound();

    /**
     * Whether user config has changed 
     */
    public boolean userConfigChanged = false;

    /**
     * Animator controller
     */
    @SideOnly(Side.CLIENT)
    public AnimatorMorphController animator;

    /**
     * Pose data
     */
    public AnimatedPose pose;

    public PoseAnimation animation = new PoseAnimation();

    /**
     * List of body parts (on client side only)
     */
    public BodyPartManager parts = new BodyPartManager();

    /**
     * Whether pose is animated 
     */
    public boolean animated = true;

    @Override
    public void pause(AbstractMorph previous, int offset)
    {
        this.animation.pause(offset);

        while (previous instanceof IMorphProvider)
        {
            previous = ((IMorphProvider) previous).getMorph();
        }

        AnimatedPose pose = null;

        if (previous instanceof AnimatedMorph)
        {
            pose = ((AnimatedMorph) previous).getCurrentPose(0F);

            if (pose != null)
            {
                pose = pose.clone();
            }
        }

        this.animation.last = pose == null ? (previous == null ? this.pose : new AnimatedPose()) : pose;
        this.parts.pause(previous, offset);
    }

    @Override
    public boolean isPaused()
    {
        return this.animation.paused;
    }

    @Override
    public Animation getAnimation()
    {
        return this.animation;
    }

    @Override
    public boolean canGenerate()
    {
        return this.animation.isInProgress();
    }

    @Override
    public AbstractMorph genCurrentMorph(float partialTicks)
    {
        AnimatedMorph morph = (AnimatedMorph) this.copy();

        morph.pose = this.getCurrentPose(partialTicks);

        return morph;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public String getDisplayName()
    {
        this.initiateAnimator();

        if (this.animator != null)
        {
            this.animator.fetchAnimation();

            if (this.animator.userConfig != null)
            {
                String name = this.animator.userConfig.name;

                if (!name.isEmpty())
                {
                    return name;
                }
            }
        }

        return this.animationName;
    }

    @Override
    public BodyPartManager getBodyPart()
    {
        return this.parts;
    }

    /* Render methods */

    @Override
    @SideOnly(Side.CLIENT)
    public void renderOnScreen(EntityPlayer player, int x, int y, float scale, float alpha)
    {
        this.parts.initBodyParts();
        this.initiateAnimator();

        if (this.animator != null)
        {
            this.animator.renderOnScreen(player, x, y, scale, alpha);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void render(EntityLivingBase entity, double x, double y, double z, float entityYaw, float partialTicks)
    {
        this.parts.initBodyParts();
        this.initiateAnimator();

        if (this.animator != null)
        {
            this.animator.render(entity, x, y, z, entityYaw, partialTicks);
        }
    }

    @SideOnly(Side.CLIENT)
    public void initiateAnimator()
    {
        if (this.animator == null)
        {
            this.animator = new AnimatorMorphController(this.animationName, this.userConfigData, this);
            this.animator.fetchAnimation();
        }
    }

    @Override
    public void update(EntityLivingBase target)
    {
        this.animation.update();

        super.update(target);

        if (target.world.isRemote)
        {
            this.parts.updateBodyLimbs(this, target);
            this.updateAnimator(target);
        }
    }

    @SideOnly(Side.CLIENT)
    protected void updateAnimator(EntityLivingBase target)
    {
        if (this.userConfigChanged)
        {
            this.userConfigChanged = false;

            if (this.animator != null)
            {
                this.updateAnimator();
            }
        }

        this.initiateAnimator();

        if (this.animator != null && !this.isPaused())
        {
            this.animator.update(target);
        }
    }

    public void updateAnimator()
    {
        if (this.animator.animation == null)
        {
            return;
        }

        this.animator.userConfig.copy(this.animator.config.config);
        this.animator.userConfig.fromNBT(this.userConfigData);
        this.animator.animator.refresh();
    }

    @Override
    public boolean equals(Object obj)
    {
        boolean result = super.equals(obj);

        if (obj instanceof AnimatedMorph)
        {
            AnimatedMorph morph = (AnimatedMorph) obj;

            result = result && Objects.equal(morph.userConfigData, this.userConfigData);
            result = result && Objects.equal(morph.pose, this.pose);
            result = result && morph.animated == this.animated;
            result = result && this.parts.equals(morph.parts);
            result = result && this.animation.equals(morph.animation);

            return result && Objects.equal(morph.animationName, this.animationName);
        }

        return result;
    }

    @Override
    public boolean canMerge(AbstractMorph morph)
    {
        if (morph instanceof IMorphProvider)
        {
            return this.canMerge(((IMorphProvider) morph).getMorph());
        }

        if (morph instanceof AnimatedMorph)
        {
            AnimatedMorph animated = (AnimatedMorph) morph;

            if (Objects.equal(this.animationName, animated.animationName))
            {
                this.mergeBasic(morph);

                this.animation.paused = false;

                this.animation.last = this.getCurrentPose(0F);
                this.userConfigData = animated.userConfigData.copy();
                this.userConfigChanged = true;
                this.pose = animated.pose == null ? null : animated.pose.clone();
                this.animated = animated.animated;
                this.parts.merge(animated.parts);
                this.animation.merge(animated.animation);

                return true;
            }
        }

        return false;
    }

    @Override
    public void afterMerge(AbstractMorph morph)
    {
        super.afterMerge(morph);

        while (morph instanceof IMorphProvider)
        {
            morph = ((IMorphProvider) morph).getMorph();
        }

        if (morph instanceof IBodyPartProvider)
        {
            this.recursiveAfterMerge(this, (IBodyPartProvider) morph);
        }

        if (morph instanceof AnimatedMorph)
        {
            AnimatedMorph animated = (AnimatedMorph) morph;

            if (Objects.equal(this.animationName, animated.animationName))
            {
                this.animation.last = animated.getCurrentPose(0F);

                if (animated.animator != null)
                {
                    this.animator = animated.animator;
                    this.animator.morph = this;
                    this.animator.fetchAnimation();
                    this.userConfigChanged = true;
                }
            }
        }
    }

    private void recursiveAfterMerge(IBodyPartProvider target, IBodyPartProvider destination)
    {
        for (int i = 0, c = target.getBodyPart().parts.size(); i < c; i++)
        {
            if (i >= destination.getBodyPart().parts.size())
            {
                break;
            }

            AbstractMorph a = target.getBodyPart().parts.get(i).morph.get();
            AbstractMorph b = destination.getBodyPart().parts.get(i).morph.get();

            if (a != null)
            {
                a.afterMerge(b);
            }
        }
    }

    private AnimatedPose getCurrentPose(float partialTicks)
    {
        if (FMLCommonHandler.instance().getEffectiveSide().isServer())
        {
            return this.pose == null ? new AnimatedPose() : this.pose.clone();
        }

        this.initiateAnimator();

        if (this.animator.animation == null)
        {
            return this.pose == null ? new AnimatedPose() : this.pose.clone();
        }

        AnimationMesh mesh = this.animator.animation.meshes.get(0);
        BOBJArmature original = mesh.getArmature();
        BOBJArmature armature = this.animator.animator.useArmature(original);

        return this.animation.calculatePose(this.pose, armature, partialTicks).clone();
    }

    @Override
    public AbstractMorph create()
    {
        return new AnimatedMorph();
    }

    @Override
    public void copy(AbstractMorph from)
    {
        super.copy(from);

        if (from instanceof AnimatedMorph)
        {
            AnimatedMorph morph = (AnimatedMorph) from;

            this.animationName = morph.animationName;
            this.userConfigData = morph.userConfigData.copy();
            this.pose = morph.pose == null ? null : morph.pose.clone();

            this.animated = morph.animated;
            this.parts.copy(morph.parts);
            this.animation.copy(morph.animation);
        }
    }

    @Override
    public float getWidth(EntityLivingBase target)
    {
        return 0.6F;
    }

    @Override
    public float getHeight(EntityLivingBase target)
    {
        return 1.8F;
    }

    @Override
    public void reset()
    {
        super.reset();

        this.userConfigChanged = true;
        this.pose = null;
        this.animated = false;
        this.parts.reset();
        this.animated = true;
    }

    @Override
    public void fromNBT(NBTTagCompound tag)
    {
        super.fromNBT(tag);

        this.animationName = tag.getString("Animation");
        this.userConfigData = tag.getCompoundTag("UserData");

        if (tag.hasKey("Pose", NBT.TAG_COMPOUND))
        {
            this.pose = new AnimatedPose();
            this.pose.fromNBT(tag.getCompoundTag("Pose"));
        }

        if (tag.hasKey("Animated"))
        {
            this.animated = tag.getBoolean("Animated");
        }

        if (tag.hasKey("BodyParts", 9))
        {
            this.parts.fromNBT(tag.getTagList("BodyParts", 10));
        }

        if (tag.hasKey("Transition"))
        {
            this.animation.fromNBT(tag.getCompoundTag("Transition"));
        }
    }

    @Override
    public void toNBT(NBTTagCompound tag)
    {
        super.toNBT(tag);

        tag.setString("Animation", this.animationName);

        if (this.userConfigData != null)
        {
            tag.setTag("UserData", this.userConfigData);
        }

        if (this.pose != null)
        {
            tag.setTag("Pose", this.pose.toNBT());
        }

        if (!this.animated) tag.setBoolean("Animated", this.animated);

        NBTTagList bodyParts = this.parts.toNBT();

        if (bodyParts != null)
        {
            tag.setTag("BodyParts", bodyParts);
        }

        NBTTagCompound animation = this.animation.toNBT();

        if (!animation.hasNoTags())
        {
            tag.setTag("Transition", animation);
        }
    }
}