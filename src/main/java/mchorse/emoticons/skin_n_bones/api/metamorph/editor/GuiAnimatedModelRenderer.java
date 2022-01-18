package mchorse.emoticons.skin_n_bones.api.metamorph.editor;

import mchorse.emoticons.skin_n_bones.api.animation.model.AnimatorController;
import mchorse.emoticons.skin_n_bones.api.bobj.BOBJArmature;
import mchorse.emoticons.skin_n_bones.api.bobj.BOBJBone;
import mchorse.mclib.client.Draw;
import mchorse.mclib.client.gui.framework.elements.GuiModelRenderer;
import mchorse.mclib.client.gui.framework.elements.utils.GuiContext;
import mchorse.mclib.utils.DummyEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

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
        float headYaw = this.yaw;
        float headPitch = this.pitch;

        if (!this.looking)
        {
            headYaw = headPitch = 0;
        }

        if (this.controller != null)
        {
            this.entity.rotationYaw = this.entity.prevRotationYaw = headYaw;
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
            Draw.axis(0.1F);
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