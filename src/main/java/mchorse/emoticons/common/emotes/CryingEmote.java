package mchorse.emoticons.common.emotes;

import javax.vecmath.Vector4f;

import mchorse.emoticons.api.animation.model.AnimatorEmoticonsController;
import mchorse.emoticons.skin_n_bones.api.bobj.BOBJArmature;
import mchorse.emoticons.skin_n_bones.api.bobj.BOBJBone;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundEvent;

public class CryingEmote extends Emote
{
    public CryingEmote(String name, int duration, boolean looping, SoundEvent sound)
    {
        super(name, duration, looping, sound);
    }

    @Override
    public void progressAnimation(EntityLivingBase entity, BOBJArmature armature, AnimatorEmoticonsController animator, int tick, float partial)
    {
        if (tick % 2 == 0)
        {
            BOBJBone hand = armature.bones.get("head");
            Vector4f result = animator.calcPosition(entity, hand, 0, 0.5F, 0.35F, partial);

            entity.world.spawnParticle(EnumParticleTypes.WATER_DROP, result.x, result.y, result.z, 1, -1, 1);
        }
    }
}