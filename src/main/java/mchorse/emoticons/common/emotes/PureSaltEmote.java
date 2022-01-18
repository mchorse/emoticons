package mchorse.emoticons.common.emotes;

import javax.vecmath.Vector4f;

import mchorse.emoticons.api.animation.model.AnimatorEmoticonsController;
import mchorse.emoticons.client.particles.SaltParticle;
import mchorse.emoticons.skin_n_bones.api.bobj.BOBJArmature;
import mchorse.emoticons.skin_n_bones.api.bobj.BOBJBone;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PureSaltEmote extends Emote
{
    public PureSaltEmote(String name, int duration, boolean looping, SoundEvent sound)
    {
        super(name, duration, looping, sound);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void progressAnimation(EntityLivingBase entity, BOBJArmature armature, AnimatorEmoticonsController animator, int tick, float partial)
    {
        if (tick > 18 && tick <= 78 && tick % 2 == 0)
        {
            BOBJBone hand = armature.bones.get("low_right_arm.end");
            Vector4f result = animator.calcPosition(entity, hand, 0, 0.15F, 0, partial);

            for (int i = 0, c = tick == 78 ? 12 : 1; i < c; i++)
            {
                SaltParticle salt = new SaltParticle(entity.world, result.x, result.y, result.z, 0);

                Minecraft.getMinecraft().effectRenderer.addEffect(salt);
            }
        }
    }
}