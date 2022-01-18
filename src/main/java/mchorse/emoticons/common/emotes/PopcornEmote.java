package mchorse.emoticons.common.emotes;

import javax.vecmath.Vector4f;

import mchorse.emoticons.api.animation.model.AnimatorEmoticonsController;
import mchorse.emoticons.client.particles.PopcornParticle;
import mchorse.emoticons.skin_n_bones.api.bobj.BOBJArmature;
import mchorse.emoticons.skin_n_bones.api.bobj.BOBJBone;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PopcornEmote extends Emote
{
    public PopcornEmote(String name, int duration, boolean looping, SoundEvent sound)
    {
        super(name, duration, looping, sound);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void progressAnimation(EntityLivingBase entity, BOBJArmature armature, AnimatorEmoticonsController animator, int tick, float partial)
    {
        if (tick == 8 || tick == 32 || tick == 56 || tick == 86)
        {
            BOBJBone hand = armature.bones.get("low_right_arm.end");
            Vector4f result = animator.calcPosition(entity, hand, 0, 0.15F, 0, partial);

            for (int i = 0, c = 15; i < c; i++)
            {
                PopcornParticle salt = new PopcornParticle(entity.world, result.x, result.y, result.z, 0.1);

                Minecraft.getMinecraft().effectRenderer.addEffect(salt);
            }
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void startAnimation(AnimatorEmoticonsController animator)
    {
        animator.userConfig.meshes.get("popcorn").visible = true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void stopAnimation(AnimatorEmoticonsController animator)
    {
        animator.userConfig.meshes.get("popcorn").visible = false;
    }
}