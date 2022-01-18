package mchorse.emoticons.skin_n_bones.api.metamorph;

import mchorse.emoticons.api.animation.model.AnimatorEmoticonsController;
import mchorse.emoticons.skin_n_bones.api.animation.AnimationMesh;
import mchorse.emoticons.skin_n_bones.api.animation.model.AnimatorPoseTransform;
import mchorse.emoticons.skin_n_bones.api.bobj.BOBJArmature;
import mchorse.emoticons.skin_n_bones.api.bobj.BOBJBone;
import mchorse.mclib.utils.Interpolations;
import mchorse.metamorph.bodypart.BodyPart;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import java.util.Map;

@SideOnly(Side.CLIENT)
public class AnimatorMorphController extends AnimatorEmoticonsController
{
    public AnimatedMorph morph;

    public AnimatorMorphController(String animationName, NBTTagCompound userData, AnimatedMorph morph)
    {
        super(animationName, userData);

        this.morph = morph;
    }

    @Override
    public void renderAnimation(EntityLivingBase entity, AnimationMesh mesh, float yaw, float partialTicks)
    {
        super.renderAnimation(entity, mesh, yaw, partialTicks);

        this.renderBodyParts(entity, mesh, yaw, partialTicks);
    }

    protected void renderBodyParts(EntityLivingBase entity, AnimationMesh mesh, float yaw, float partialTicks)
    {
        /* Render body part */
        BOBJArmature armature = mesh.getCurrentArmature();

        for (BodyPart part : this.morph.parts.parts)
        {
            BOBJBone bone = armature.bones.get(part.limb);

            if (bone != null)
            {
                GL11.glPushMatrix();
                this.setupMatrix(bone);
                part.render(this.morph, entity, partialTicks);
                GL11.glPopMatrix();
            }

            /* I hope this won't affect performance too much, but it's 
             * necessary because it restores animation data if the body 
             * part is an animated morph that uses the same animation */
            this.setupBoneMatrices(entity, armature, yaw, partialTicks);
        }
    }

    @Override
    protected void setupBoneTransformations(EntityLivingBase entity, BOBJArmature armature, float yaw, float partialTicks)
    {
        AnimatedPose pose = this.morph.pose;
        boolean inProgress = this.morph.animation.isInProgress();

        if (inProgress)
        {
            pose = this.morph.animation.calculatePose(pose, armature, partialTicks);
        }

        super.setupBoneTransformations(entity, armature, yaw, partialTicks);

        if (pose == null)
        {
            return;
        }

        for (Map.Entry<String, AnimatorPoseTransform> entry : pose.bones.entrySet())
        {
            BOBJBone bone = armature.bones.get(entry.getKey());
            AnimatorPoseTransform transform = entry.getValue();
            float factor = MathHelper.clamp(transform.fixed, 0, 1);

            if (!this.morph.animated)
            {
                factor = AnimatorPoseTransform.FIXED;
            }

            bone.x = Interpolations.lerp(transform.x, bone.x + transform.x, factor);
            bone.y = Interpolations.lerp(transform.y, bone.y + transform.y, factor);
            bone.z = Interpolations.lerp(transform.z, bone.z + transform.z, factor);
            bone.scaleX = Interpolations.lerp(transform.scaleX, bone.scaleX * transform.scaleX, factor);
            bone.scaleY = Interpolations.lerp(transform.scaleY, bone.scaleY * transform.scaleY, factor);
            bone.scaleZ = Interpolations.lerp(transform.scaleZ, bone.scaleZ * transform.scaleZ, factor);
            bone.rotateX = Interpolations.lerp(transform.rotateX, bone.rotateX + transform.rotateX, factor);
            bone.rotateY = Interpolations.lerp(transform.rotateY, bone.rotateY + transform.rotateY, factor);
            bone.rotateZ = Interpolations.lerp(transform.rotateZ, bone.rotateZ + transform.rotateZ, factor);
        }
    }
}