package mchorse.emoticons.api.metamorph;

import mchorse.emoticons.Emoticons;
import mchorse.emoticons.common.emotes.Emote;
import mchorse.emoticons.skin_n_bones.api.animation.model.ActionConfig;
import mchorse.emoticons.skin_n_bones.api.animation.model.ActionPlayback;
import mchorse.emoticons.skin_n_bones.api.bobj.BOBJArmature;
import mchorse.emoticons.skin_n_bones.api.metamorph.AnimatedMorph;
import mchorse.metamorph.api.Morph;
import mchorse.metamorph.api.morphs.AbstractMorph;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Objects;

/**
 * Animated morph. This morph class allows players and any morph API 
 * consumers to update and render animated models as entities.
 */
public class EmoticonsMorph extends AnimatedMorph
{
    @SideOnly(Side.CLIENT)
    public ActionPlayback emoteAction;

    public Emote emote;

    public Morph placeholder = new Morph();

    /* Trackers */
    private int emoteTimer;
    private double lastX;
    private double lastY;
    private double lastZ;

    /* Render methods */

    @Override
    @SideOnly(Side.CLIENT)
    public void render(EntityLivingBase entity, double x, double y, double z, float entityYaw, float partialTicks)
    {
        if (this.emote == null && Emoticons.disableAnimations.get() && !this.placeholder.isEmpty())
        {
            this.placeholder.get().render(entity, x, y, z, entityYaw, partialTicks);

            return;
        }

        this.parts.initBodyParts();
        this.initiateAnimator();

        if (this.animator != null && this.animator.animation != null)
        {
            this.animator.render(this.emote, entity, x, y, z, 0, partialTicks);

            BOBJArmature armature = this.animator.animation.meshes.get(0).getArmature();

            if (this.emote != null && this.emoteAction != null && !Minecraft.getMinecraft().isGamePaused())
            {
                int tick = (int) this.emoteAction.getTick(0);

                this.emote.progressAnimation(entity, armature, this.animator, tick, partialTicks);
            }
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean renderHand(EntityPlayer player, EnumHand hand)
    {
        if (!this.placeholder.isEmpty())
        {
            return this.placeholder.get().renderHand(player, hand);
        }

        return super.renderHand(player, hand);
    }

    /* Stupid emote integration */

    public void setEmote(Emote emote, EntityLivingBase target)
    {
        if (target.world.isRemote)
        {
            this.stopAction(target);
        }

        this.emote = emote;
        this.emoteTimer = 0;

        if (target.world.isRemote)
        {
            this.setActionEmote(emote, target);
        }
    }

    @SideOnly(Side.CLIENT)
    private void stopAction(EntityLivingBase target)
    {
        if (this.emote != null)
        {
            this.emote.stopAnimation(this.animator);
        }
    }

    @SideOnly(Side.CLIENT)
    private void setActionEmote(Emote emote, EntityLivingBase target)
    {
        if (this.animator == null)
        {
            return;
        }

        if (emote != null)
        {
            ActionConfig config = this.animator.config.config.actions.getConfig("emote_" + emote.name);

            this.emoteAction = this.animator.animation.createAction(null, config, emote.looping);
            this.animator.setEmote(this.emoteAction);

            emote.startAnimation(this.animator);
        }
        else
        {
            this.emoteAction = null;
            this.animator.setEmote(null);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    protected void updateAnimator(EntityLivingBase target)
    {
        super.updateAnimator(target);

        /* On servers without Emoticons mod, reset the emote when
         * finished */
        if (this.emote != null)
        {
            /* Turn off emote when player moves */
            double diff = Math.abs((target.posX - this.lastX) + (target.posY - this.lastY) + (target.posZ - this.lastZ));

            if (diff > 0.015 || (!this.emote.looping && this.emoteTimer >= this.emote.duration))
            {
                this.setEmote(null, target);
            }
        }
        else if (!this.placeholder.isEmpty() && Emoticons.disableAnimations.get())
        {
            this.placeholder.get().update(target);
        }

        this.lastX = target.posX;
        this.lastY = target.posY;
        this.lastZ = target.posZ;

        if (this.emote != null && this.emoteAction != null)
        {
            if (this.emote.sound != null && this.emoteAction.getTick(0) == 0)
            {
                target.world.playSound(target.posX, target.posY, target.posZ, this.emote.sound, SoundCategory.MASTER, 0.33F, 1, false);
            }

            this.emote.updateEmote(target, this.animator, (int) this.emoteAction.getTick(0));
            this.emoteTimer++;
        }
    }

    @Override
    public boolean equals(Object obj)
    {
        boolean result = super.equals(obj);

        if (obj instanceof EmoticonsMorph)
        {
            EmoticonsMorph morph = (EmoticonsMorph) obj;

            result = result && Objects.equals(this.placeholder, morph.placeholder);
        }

        return result;
    }

    @Override
    public boolean canMerge(AbstractMorph morph)
    {
        boolean result = super.canMerge(morph);

        if (result && morph instanceof EmoticonsMorph)
        {
            EmoticonsMorph m = (EmoticonsMorph) morph;

            this.placeholder.set(m.placeholder.copy());
        }

        return result;
    }

    @Override
    public AbstractMorph create()
    {
        return new EmoticonsMorph();
    }

    @Override
    public void copy(AbstractMorph from)
    {
        super.copy(from);

        if (from instanceof EmoticonsMorph)
        {
            EmoticonsMorph morph = (EmoticonsMorph) from;

            this.placeholder.copy(morph.placeholder);
        }
    }

    @Override
    public void fromNBT(NBTTagCompound tag)
    {
        super.fromNBT(tag);

        if (tag.hasKey("Placeholder"))
        {
            this.placeholder.fromNBT(tag.getCompoundTag("Placeholder"));
        }
    }

    @Override
    public void toNBT(NBTTagCompound tag)
    {
        super.toNBT(tag);

        if (!this.placeholder.isEmpty())
        {
            tag.setTag("Placeholder", this.placeholder.toNBT());
        }
    }
}