package mchorse.emoticons.skin_n_bones.api.animation.model;

import mchorse.emoticons.skin_n_bones.api.animation.Animation;
import mchorse.emoticons.skin_n_bones.api.bobj.BOBJArmature;
import mchorse.emoticons.utils.PlayerReviveUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Animator class
 * 
 * This class is responsible for applying currently running actions onto 
 * morph (more specifically onto an armature).
 */
@SideOnly(Side.CLIENT)
public class Animator implements IAnimator
{
    /* Actions */
    public ActionPlayback idle;
    public ActionPlayback walking;
    public ActionPlayback running;
    public ActionPlayback sprinting;
    public ActionPlayback crouching;
    public ActionPlayback crouchingIdle;
    public ActionPlayback swimming;
    public ActionPlayback swimmingIdle;
    public ActionPlayback flying;
    public ActionPlayback flyingIdle;
    public ActionPlayback riding;
    public ActionPlayback ridingIdle;
    public ActionPlayback dying;
    public ActionPlayback falling;
    public ActionPlayback sleeping;

    public ActionPlayback jump;
    public ActionPlayback swipe;
    public ActionPlayback hurt;
    public ActionPlayback land;
    public ActionPlayback shoot;
    public ActionPlayback consume;

    /* Action pipeline properties */
    public ActionPlayback emote;
    public ActionPlayback active;
    public ActionPlayback lastActive;
    public List<ActionPlayback> actions = new ArrayList<ActionPlayback>();

    public double prevX = Float.MAX_VALUE;
    public double prevZ = Float.MAX_VALUE;
    public double prevMY;

    /* States */
    public boolean wasOnGround = true;
    public boolean wasShooting = false;
    public boolean wasConsuming = false;

    public AnimatorController controller;

    public Animator(AnimatorController controller)
    {
        this.controller = controller;
        this.refresh();
    }

    @Override
    public void refresh()
    {
        AnimatorActionsConfig actions = controller.userConfig.actions;
        Animation animation = controller.animation;

        this.idle = animation.createAction(this.idle, actions.getConfig("idle"), true);
        this.walking = animation.createAction(this.walking, actions.getConfig("walking"), true);
        this.running = animation.createAction(this.running, actions.getConfig("running"), true);
        this.sprinting = animation.createAction(this.sprinting, actions.getConfig("sprinting"), true);
        this.crouching = animation.createAction(this.crouching, actions.getConfig("crouching"), true);
        this.crouchingIdle = animation.createAction(this.crouchingIdle, actions.getConfig("crouching_idle"), true);
        this.swimming = animation.createAction(this.swimming, actions.getConfig("swimming"), true);
        this.swimmingIdle = animation.createAction(this.swimmingIdle, actions.getConfig("swimming_idle"), true);
        this.flying = animation.createAction(this.flying, actions.getConfig("flying"), true);
        this.flyingIdle = animation.createAction(this.flyingIdle, actions.getConfig("flying_idle"), true);
        this.riding = animation.createAction(this.riding, actions.getConfig("riding"), true);
        this.ridingIdle = animation.createAction(this.ridingIdle, actions.getConfig("riding_idle"), true);
        this.dying = animation.createAction(this.dying, actions.getConfig("dying"), false);
        this.falling = animation.createAction(this.falling, actions.getConfig("falling"), true);
        this.sleeping = animation.createAction(this.sleeping, actions.getConfig("sleeping"), true);

        this.swipe = animation.createAction(this.swipe, actions.getConfig("swipe"), false);
        this.jump = animation.createAction(this.jump, actions.getConfig("jump"), false, 2);
        this.hurt = animation.createAction(this.hurt, actions.getConfig("hurt"), false, 3);
        this.land = animation.createAction(this.land, actions.getConfig("land"), false);
        this.shoot = animation.createAction(this.shoot, actions.getConfig("shoot"), true);
        this.consume = animation.createAction(this.consume, actions.getConfig("consume"), true);
    }

    @Override
    public void setEmote(ActionPlayback emote)
    {
        if (emote != null)
        {
            this.emote = emote;
        }
        else if (this.emote != null)
        {
            this.emote = null;
        }
    }

    /**
     * Update animator. This method is responsible for updating action 
     * pipeline and also change current actions based on entity's state.
     */
    @Override
    public void update(EntityLivingBase target)
    {
        /* Fix issue with morphs sudden running action */
        if (this.prevX == Float.MAX_VALUE)
        {
            this.prevX = target.posX;
            this.prevZ = target.posZ;
        }

        this.controlActions(target);

        /* Update primary actions */
        if (this.active != null)
        {
            this.active.update();
        }

        if (this.lastActive != null)
        {
            this.lastActive.update();
        }

        /* Update secondary actions */
        Iterator<ActionPlayback> it = this.actions.iterator();

        while (it.hasNext())
        {
            ActionPlayback action = it.next();

            action.update();

            if (action.finishedFading())
            {
                action.unfade();
                it.remove();
            }
        }
    }

    /**
     * This method is designed specifically to isolate any controlling 
     * code (i.e. the ones that is responsible for switching between 
     * actions).
     */
    protected void controlActions(EntityLivingBase target)
    {
        double dx = target.posX - this.prevX;
        double dz = target.posZ - this.prevZ;
        boolean creativeFlying = target instanceof EntityPlayer && ((EntityPlayer) target).capabilities.isFlying;
        boolean wet = target.isInWater();
        final float threshold = creativeFlying ? 0.1F : (wet ? 0.025F : 0.01F);
        boolean moves = Math.abs(dx) > threshold || Math.abs(dz) > threshold;

        if (this.emote != null)
        {
            this.setActiveAction(this.emote);
        }
        else if (target.getHealth() <= 0)
        {
            this.setActiveAction(this.dying);
        }
        else if (target.isPlayerSleeping() || PlayerReviveUtils.isPlayerBleeding(target))
        {
            this.setActiveAction(this.sleeping);
        }
        else if (wet)
        {
            this.setActiveAction(!moves ? this.swimmingIdle : this.swimming);
        }
        else if (target.isRiding())
        {
            Entity riding = target.getRidingEntity();
            moves = Math.abs(riding.posX - this.prevX) > threshold || Math.abs(riding.posZ - this.prevZ) > threshold;

            this.prevX = riding.posX;
            this.prevZ = riding.posZ;
            this.setActiveAction(!moves ? this.ridingIdle : this.riding);
        }
        else if (creativeFlying || target.isElytraFlying())
        {
            this.setActiveAction(!moves ? this.flyingIdle : this.flying);
        }
        else
        {
            float speed = (float) (Math.round(Math.sqrt(dx * dx + dz * dz) * 1000) / 1000.0);

            if (target.isSneaking())
            {
                speed /= 0.065F;

                this.setActiveAction(!moves ? this.crouchingIdle : this.crouching);
                if (this.crouching != null) this.crouching.setSpeed(target.moveForward > 0 ? speed : -speed);
            }
            else if (!target.onGround && target.motionY < 0 && target.fallDistance > 1.25)
            {
                this.setActiveAction(this.falling);
            }
            else if (target.isSprinting() && this.sprinting != null)
            {
                this.setActiveAction(this.sprinting);

                this.sprinting.setSpeed(speed / 0.281F);
            }
            else
            {
                this.setActiveAction(!moves ? this.idle : this.running);

                speed /= 0.216F;

                if (this.running != null) this.running.setSpeed(target.moveForward >= 0 ? speed : -speed);
                if (this.walking != null) this.walking.setSpeed(target.moveForward > 0 ? speed : -speed);
            }

            if (target.onGround && !this.wasOnGround && !target.isSprinting() && this.prevMY < -0.5)
            {
                this.addAction(this.land);
            }
        }

        if (!target.onGround && this.wasOnGround && Math.abs(target.motionY) > 0.2F)
        {
            this.addAction(this.jump);
            this.wasOnGround = false;
        }

        /* Bow and consumables */
        boolean shooting = this.wasShooting;
        boolean consuming = this.wasConsuming;
        ItemStack stack = target.getHeldItemMainhand();

        if (!stack.isEmpty())
        {
            if (target.getItemInUseCount() > 0)
            {
                EnumAction action = stack.getItemUseAction();

                if (action == EnumAction.BOW)
                {
                    if (!this.actions.contains(this.shoot))
                    {
                        this.addAction(this.shoot);
                    }

                    this.wasShooting = true;
                }
                else if (action == EnumAction.DRINK || action == EnumAction.EAT)
                {
                    if (!this.actions.contains(this.consume))
                    {
                        this.addAction(this.consume);
                    }

                    this.wasConsuming = true;
                }
            }
            else
            {
                this.wasShooting = false;
                this.wasConsuming = false;
            }
        }
        else
        {
            this.wasShooting = false;
            this.wasConsuming = false;
        }

        if (shooting && !this.wasShooting && this.shoot != null)
        {
            this.shoot.fade();
        }

        if (consuming && !this.wasConsuming && this.consume != null)
        {
            this.consume.fade();
        }

        if (target.hurtTime == target.maxHurtTime - 1)
        {
            this.addAction(this.hurt);
        }

        if (target.isSwingInProgress && target.swingProgress == 0 && !target.isPlayerSleeping())
        {
            this.addAction(this.swipe);
        }

        this.prevX = target.posX;
        this.prevZ = target.posZ;
        this.prevMY = target.motionY;

        this.wasOnGround = target.onGround;
    }

    /**
     * Set current active (primary) action 
     */
    public void setActiveAction(ActionPlayback action)
    {
        if (this.active == action || action == null)
        {
            return;
        }

        if (this.active != null && action.priority < this.active.priority)
        {
            return;
        }

        if (this.active != null)
        {
            this.lastActive = this.active;
            this.lastActive.fade();
        }

        this.active = action;
        this.active.reset();
    }

    /**
     * Add an additional secondary action to the playback 
     */
    public void addAction(ActionPlayback action)
    {
        if (action == null)
        {
            return;
        }

        if (this.actions.contains(action))
        {
            action.reset();

            return;
        }

        action.reset();
        this.actions.add(action);
    }

    @Override
    public BOBJArmature useArmature(BOBJArmature armature)
    {
        if (this.active != null && this.active.customArmature != null)
        {
            return this.active.customArmature;
        }

        return armature;
    }

    /**
     * Apply currently running action pipeline onto given armature
     */
    @Override
    public void applyActions(BOBJArmature armature, float partialTicks)
    {
        if (this.active != null)
        {
            this.active.apply(armature, partialTicks);
        }

        if (this.lastActive != null && this.lastActive.isFading())
        {
            this.lastActive.applyInactive(armature, partialTicks, 1 - this.lastActive.getFadeFactor(partialTicks));
        }

        for (ActionPlayback action : this.actions)
        {
            if (action.isFading())
            {
                action.applyInactive(armature, partialTicks, 1 - action.getFadeFactor(partialTicks));
            }
            else
            {
                action.apply(armature, partialTicks);
            }
        }
    }
}