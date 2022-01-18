package mchorse.emoticons.common.emotes;

import mchorse.emoticons.api.animation.model.AnimatorEmoticonsController;
import mchorse.emoticons.skin_n_bones.api.bobj.BOBJArmature;
import mchorse.emoticons.utils.Time;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundEvent;

import javax.vecmath.Vector4f;

public class DisgustedEmote extends Emote
{
    public DisgustedEmote(String name, int duration, boolean looping, SoundEvent sound)
    {
        super(name, duration, looping, sound);
    }

    @Override
    public void progressAnimation(EntityLivingBase entity, BOBJArmature armature, AnimatorEmoticonsController animator, int tick, float partial)
    {
        super.progressAnimation(entity, armature, animator, tick, partial);

        if (tick >= Time.toTicks(117) && tick < Time.toTicks(140))
        {
            for (int i = 0; i < 10; i ++)
            {
                Vector4f result = animator.calcPosition(entity, armature.bones.get("head"), 0, 0.125F, 0.25F, partial);

                entity.world.spawnParticle(EnumParticleTypes.ITEM_CRACK, result.x + this.rand(0.1F), result.y, result.z + this.rand(0.1F), this.rand(0.05F), -0.125F, this.rand(0.05F), 351, 2);
            }
        }
    }
}