package mchorse.emoticons.skin_n_bones.api.animation.model;

import mchorse.emoticons.skin_n_bones.api.bobj.BOBJAction;
import mchorse.emoticons.skin_n_bones.api.bobj.BOBJArmature;
import mchorse.emoticons.skin_n_bones.api.bobj.BOBJBone;
import mchorse.emoticons.skin_n_bones.api.bobj.BOBJGroup;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ActionPlayback
{
    public BOBJAction action;
    public ActionConfig config;

    private int fade;
    private float ticks;
    private int duration;
    private float speed = 1;

    private boolean looping = false;
    private boolean fading = false;
    public boolean playing = true;
    public int priority;

    public BOBJArmature customArmature;

    public ActionPlayback(BOBJAction action, ActionConfig config)
    {
        this(action, config, true);
    }

    public ActionPlayback(BOBJAction action, ActionConfig config, boolean looping)
    {
        this.action = action;
        this.config = config;
        this.duration = action.getDuration();
        this.looping = looping;
        this.setSpeed(1);
    }

    public ActionPlayback(BOBJAction action, ActionConfig config, boolean looping, int priority)
    {
        this(action, config, looping);
        this.priority = priority;
    }

    /* Action playback control methods */

    /**
     * Resets the animation (if config allows) 
     */
    public void reset()
    {
        if (this.config.reset)
        {
            this.ticks = Math.copySign(1, this.speed) < 0 ? this.duration : 0;
        }

        this.unfade();
    }

    /**
     * Whether this action playback finished fading 
     */
    public boolean finishedFading()
    {
        return this.fading == true && this.fade <= 0;
    }

    /**
     * Whether this action playback is fading 
     */
    public boolean isFading()
    {
        return this.fading == true && this.fade > 0;
    }

    /**
     * Start fading 
     */
    public void fade()
    {
        this.fade = (int) this.config.fade;
        this.fading = true;
    }

    /**
     * Reset fading
     */
    public void unfade()
    {
        this.fade = 0;
        this.fading = false;
    }

    /**
     * Calculate fade factor with given partial ticks
     * 
     * Closer to 1 means started fading, meanwhile closer to 0 is almost 
     * finished fading.
     */
    public float getFadeFactor(float partialTicks)
    {
        return (this.fade - partialTicks) / this.config.fade;
    }

    /**
     * Set speed of an action playback 
     */
    public void setSpeed(float speed)
    {
        this.speed = speed * this.config.speed;
    }

    /* Update methods */

    public void update()
    {
        if (this.fading && this.fade > 0)
        {
            this.fade--;

            return;
        }

        if (!this.playing) return;

        this.ticks += this.speed;

        if (!this.looping && !this.fading && this.ticks >= this.duration)
        {
            this.fade();
        }

        if (this.looping)
        {
            if (this.ticks >= this.duration && this.speed > 0 && this.config.clamp)
            {
                this.ticks -= this.duration;
                this.ticks += this.config.tick;
            }
            else if (this.ticks < 0 && this.speed < 0 && this.config.clamp)
            {
                this.ticks = this.duration + this.ticks;
                this.ticks -= this.config.tick;
            }
        }
    }

    public float getTick(float partialTick)
    {
        float ticks = this.ticks + partialTick * this.speed;

        if (this.looping)
        {
            if (ticks >= this.duration && this.speed > 0 && this.config.clamp)
            {
                ticks -= this.duration;
            }
            else if (this.ticks < 0 && this.speed < 0 && this.config.clamp)
            {
                ticks = this.duration + ticks;
            }
        }

        return ticks;
    }

    public void apply(BOBJArmature armature, float partialTick)
    {
        for (BOBJGroup group : this.action.groups.values())
        {
            BOBJBone bone = armature.bones.get(group.name);

            if (bone != null)
            {
                group.apply(bone, this.getTick(partialTick));
            }
        }
    }

    public void applyInactive(BOBJArmature armature, float partialTick, float x)
    {
        for (BOBJGroup group : this.action.groups.values())
        {
            BOBJBone bone = armature.bones.get(group.name);

            if (bone != null)
            {
                group.applyInterpolate(bone, this.ticks, x);
            }
        }
    }
}