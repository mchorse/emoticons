package mchorse.emoticons.api.metamorph.emote;

import mchorse.emoticons.api.animation.model.AnimatorEmoticonsController;
import mchorse.emoticons.common.emotes.Emote;
import mchorse.emoticons.skin_n_bones.api.bobj.BOBJArmature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

public class MorphEmote extends Emote
{
    public List<MorphPart> parts = new ArrayList<MorphPart>();

    public MorphEmote(String name, int duration, boolean looping, SoundEvent sound)
    {
        super(name, duration, looping, sound);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void updateEmote(EntityLivingBase entity, AnimatorEmoticonsController animator, int tick)
    {
        super.updateEmote(entity, animator, tick);

        for (MorphPart part : this.parts)
        {
            part.update(entity, tick);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void renderEmote(EntityLivingBase entity, BOBJArmature armature, AnimatorEmoticonsController animator, int tick, float partial)
    {
        super.renderEmote(entity, armature, animator, tick, partial);

        for (MorphPart part : this.parts)
        {
            part.render(entity, armature, animator, tick, partial);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void startAnimation(AnimatorEmoticonsController animator)
    {
        super.startAnimation(animator);

        for (MorphPart part : this.parts)
        {
            part.reset();
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void stopAnimation(AnimatorEmoticonsController animator)
    {
        super.startAnimation(animator);

        for (MorphPart part : this.parts)
        {
            part.reset();
        }
    }
}