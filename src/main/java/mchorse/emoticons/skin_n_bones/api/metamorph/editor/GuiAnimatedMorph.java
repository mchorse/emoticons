package mchorse.emoticons.skin_n_bones.api.metamorph.editor;

import mchorse.emoticons.skin_n_bones.api.animation.AnimationManager;
import mchorse.emoticons.skin_n_bones.api.animation.model.AnimatorConfig;
import mchorse.emoticons.skin_n_bones.api.metamorph.AnimatedMorph;
import mchorse.mclib.client.gui.framework.elements.GuiModelRenderer;
import mchorse.mclib.client.gui.framework.elements.utils.GuiDrawable;
import mchorse.mclib.client.gui.utils.Icons;
import mchorse.mclib.client.gui.utils.keys.IKey;
import mchorse.metamorph.api.morphs.AbstractMorph;
import mchorse.metamorph.bodypart.GuiBodyPartEditor;
import mchorse.metamorph.client.gui.editor.GuiAbstractMorph;
import mchorse.metamorph.client.gui.editor.GuiMorphPanel;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiAnimatedMorph extends GuiAbstractMorph<AnimatedMorph>
{
    public static final ResourceLocation GUI_ICONS = new ResourceLocation("skin_n_bones:textures/gui/icons.png");

    public AnimatorConfig userConfig;

    public GuiAnimatedMorphPanel general;
    public GuiMeshesPanel meshes;
    public GuiActionsPanel actions;
    public GuiBodyPartEditor bodyPart;

    public GuiAnimatedModelRenderer model;

    public GuiAnimatedMorph(Minecraft mc)
    {
        super(mc);

        /* Animated morph panels */
        this.general = new GuiAnimatedMorphPanel(mc, this);
        this.meshes = new GuiMeshesPanel(mc, this);
        this.actions = new GuiActionsPanel(mc, this);
        this.bodyPart = new GuiAnimatedBodyPartEditor(mc, this);
        this.defaultPanel = this.general;

        this.registerPanel(this.bodyPart, IKey.lang("metamorph.gui.body_parts.parts"), Icons.LIMB);
        this.registerPanel(this.meshes, IKey.lang("emoticons.gui.morph.meshes.title"), Icons.MATERIAL);
        this.registerPanel(this.actions, IKey.lang("emoticons.gui.morph.actions.title"), Icons.MORE);
        this.registerPanel(this.general, IKey.lang("emoticons.gui.morph.general.title"), Icons.POSE);

        /* Miscellaneous */
        this.prepend(new GuiDrawable((n) ->
        {
            this.drawGradientRect(0, this.area.ey() - 30, this.area.w, this.area.ey(), 0x00000000, 0x88000000);
        }));
    }

    @Override
    public void setPanel(GuiMorphPanel panel)
    {
        this.model.bone = null;

        super.setPanel(panel);
    }

    @Override
    protected GuiModelRenderer createMorphRenderer(Minecraft mc)
    {
        this.model = new GuiAnimatedModelRenderer(mc);
        this.model.looking = false;
        this.model.picker((bone) ->
        {
            if (this.view.delegate instanceof IBonePicker)
            {
                ((IBonePicker) this.view.delegate).pickBone(bone);
            }
        });

        return this.model;
    }

    @Override
    public boolean canEdit(AbstractMorph morph)
    {
        if (morph instanceof AnimatedMorph)
        {
            AnimatedMorph animated = (AnimatedMorph) morph;

            return AnimationManager.INSTANCE.animations.containsKey(animated.animationName);
        }

        return false;
    }

    @Override
    public void startEdit(AnimatedMorph morph)
    {
        morph.parts.initBodyParts();
        morph.initiateAnimator();
        this.userConfig = new AnimatorConfig();
        this.bodyPart.setLimbs(morph.animator.animation.meshes.get(0).getArmature().bones.keySet());
        this.model.controller = morph.animator;

        if (morph.userConfigData != null && !morph.userConfigData.hasNoTags())
        {
            this.userConfig.fromNBT(morph.userConfigData);
        }

        super.startEdit(morph);
    }

    @Override
    public void finishEdit()
    {
        this.updateMorph();
        super.finishEdit();
    }

    public void updateMorph()
    {
        this.morph.userConfigData = this.userConfig.toNBT(null);
        this.morph.userConfigChanged = true;
        this.morph.updateAnimator();
    }

}