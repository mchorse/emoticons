package mchorse.emoticons.skin_n_bones.api.metamorph.editor;

import mchorse.emoticons.skin_n_bones.api.animation.model.AnimatorController;
import mchorse.emoticons.skin_n_bones.api.animation.model.AnimatorHeldItemConfig;
import mchorse.emoticons.skin_n_bones.api.bobj.BOBJArmature;
import mchorse.emoticons.skin_n_bones.api.bobj.BOBJBone;
import mchorse.emoticons.skin_n_bones.api.metamorph.AnimatorMorphController;
import mchorse.mclib.client.Draw;
import mchorse.mclib.client.gui.framework.elements.GuiModelRenderer;
import mchorse.mclib.client.gui.framework.elements.input.GuiTransformations;
import mchorse.mclib.client.gui.framework.elements.utils.GuiContext;
import mchorse.mclib.utils.DummyEntity;
import mchorse.mclib.utils.MatrixUtils;
import mchorse.mclib.utils.RenderingUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

import javax.vecmath.Matrix4f;
import javax.vecmath.Vector3d;
import java.util.List;

public class GuiAnimatedModelRenderer extends GuiModelRenderer
{
    public AnimatorController controller;

    public boolean items;
    public boolean looking = true;
    public String bone;

    public GuiAnimatedModelRenderer(Minecraft mc)
    {
        super(mc);
    }

    public void toggleItems()
    {
        this.items = !this.items;
        ((DummyEntity) this.entity).toggleItems(this.items);
    }

    @Override
    protected void drawUserModel(GuiContext context)
    {
        float headYaw = this.yaw - (this.customEntity ? this.entityYawBody : 0);
        float headPitch = this.pitch;

        if (!this.looking)
        {
            headYaw = this.customEntity ? this.entityYawHead : 0;
            headPitch = this.customEntity ? this.entityPitch : 0;
        }

        if (this.controller != null)
        {
            this.entity.rotationYaw = this.entity.prevRotationYaw = this.customEntity ? this.entityYawBody : headYaw;
            this.entity.rotationYawHead = this.entity.prevRotationYawHead = headYaw;
            this.entity.rotationPitch = this.entity.prevRotationPitch = headPitch;

            this.controller.render(this.entity, 0, 0, 0, 0, context.partialTicks);
            this.tryPicking(context);

            BOBJArmature armature = this.getArmature();

            if (armature != null)
            {
                float scale = this.controller.userConfig.scale;

                GlStateManager.enableAlpha();
                GlStateManager.enableBlend();
                GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);

                GlStateManager.disableDepth();
                GlStateManager.disableTexture2D();
                GlStateManager.disableLighting();

                GlStateManager.pushMatrix();
                GlStateManager.scale(scale, scale, scale);
                GlStateManager.rotate(this.customEntity ? -this.entityYawBody : 0, 0, 1, 0);

                for (BOBJBone bone : armature.orderedBones)
                {
                    this.drawBoneHighlight(bone);
                }

                GlStateManager.popMatrix();
                GlStateManager.enableDepth();
                GlStateManager.enableTexture2D();
                GlStateManager.enableLighting();
            }
        }
    }

    @Override
    protected void drawForStencil(GuiContext context)
    {
        BOBJArmature armature = this.getArmature();

        if (armature == null)
        {
            return;
        }

        float scale = this.controller.userConfig.scale;

        GlStateManager.disableTexture2D();
        GlStateManager.disableDepth();
        GlStateManager.pushMatrix();
        GlStateManager.scale(scale, scale, scale);
        GlStateManager.rotate(this.customEntity ? -this.entityYawBody : 0, 0, 1, 0);

        List<BOBJBone> bones = armature.orderedBones;

        for (int i = 0; i < bones.size(); i ++)
        {
            BOBJBone bone = bones.get(i);

            GL11.glStencilFunc(GL11.GL_ALWAYS, i + 1, -1);
            this.drawBoneHighlight(bone);
        }

        GlStateManager.popMatrix();
        GlStateManager.enableTexture2D();
        GlStateManager.enableDepth();
    }

    private void drawBoneHighlight(BOBJBone bone)
    {
        final float size = 0.05F;

        GlStateManager.pushMatrix();

        this.controller.setupMatrix(bone);

        if (bone.name.equals(this.bone))
        {
            Draw.cube(-size, -size, -size, size, size, size, 0F, 1F, 0F, 0.5F);

            GlStateManager.pushMatrix();

            if (this.controller instanceof AnimatorMorphController)
            {
                AnimatorMorphController morphController = (AnimatorMorphController) this.controller;

                if (morphController.morph != null && morphController.morph.pose != null
                    && morphController.morph.pose.bones.get(bone.name) != null)
                {
                    AnimatorHeldItemConfig transform = morphController.morph.pose.bones.get(bone.name);

                    if (GuiTransformations.GuiStaticTransformOrientation.getOrientation() == GuiTransformations.TransformOrientation.GLOBAL)
                    {
                        RenderingUtils.glRevertRotationScale(new Vector3d(transform.rotateX, transform.rotateY, transform.rotateZ),
                                                             new Vector3d(transform.scaleX, transform.scaleY, transform.scaleZ),
                                                             MatrixUtils.RotationOrder.XYZ);
                    }
                }
            }

            Draw.axis(0.1F);
            GlStateManager.popMatrix();
        }
        else
        {
            Draw.cube(-size, -size, -size, size, size, size, 0F, 0.5F, 1F, 0.5F);
        }

        GlStateManager.popMatrix();
    }

    private BOBJArmature getArmature()
    {
        if (this.controller != null && this.controller.animation != null)
        {
            return this.controller.animation.meshes.get(0).getCurrentArmature();
        }

        return null;
    }

    @Override
    protected String getStencilValue(int value)
    {
        BOBJArmature armature = this.getArmature();

        if (armature != null)
        {
            return armature.orderedBones.get(value - 1).name;
        }

        return super.getStencilValue(value);
    }
}