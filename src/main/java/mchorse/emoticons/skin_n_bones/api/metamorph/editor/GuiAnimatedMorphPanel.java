package mchorse.emoticons.skin_n_bones.api.metamorph.editor;

import mchorse.emoticons.skin_n_bones.api.animation.model.AnimatorPoseTransform;
import mchorse.emoticons.skin_n_bones.api.metamorph.AnimatedMorph;
import mchorse.emoticons.skin_n_bones.api.metamorph.AnimatedPose;
import mchorse.mclib.client.gui.framework.elements.GuiScrollElement;
import mchorse.mclib.client.gui.framework.elements.buttons.GuiButtonElement;
import mchorse.mclib.client.gui.framework.elements.buttons.GuiToggleElement;
import mchorse.mclib.client.gui.framework.elements.context.GuiContextMenu;
import mchorse.mclib.client.gui.framework.elements.context.GuiSimpleContextMenu;
import mchorse.mclib.client.gui.framework.elements.input.GuiTextElement;
import mchorse.mclib.client.gui.framework.elements.input.GuiTrackpadElement;
import mchorse.mclib.client.gui.framework.elements.input.GuiTransformations;
import mchorse.mclib.client.gui.framework.elements.list.GuiStringListElement;
import mchorse.mclib.client.gui.framework.elements.utils.GuiContext;
import mchorse.mclib.client.gui.utils.Elements;
import mchorse.mclib.client.gui.utils.Icons;
import mchorse.mclib.client.gui.utils.keys.IKey;
import mchorse.metamorph.client.gui.editor.GuiAnimation;
import mchorse.metamorph.client.gui.editor.GuiMorphPanel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTTagCompound;

import java.util.List;
import java.util.function.Consumer;

/**
 * General morph panel for animated morph editor
 */
public class GuiAnimatedMorphPanel extends GuiMorphPanel<AnimatedMorph, GuiAnimatedMorph> implements IBonePicker
{
    public GuiTextElement name;
    public GuiTrackpadElement scale;
    public GuiTrackpadElement scaleGui;
    public GuiTrackpadElement scaleItems;
    public GuiToggleElement renderHeldItems;
    public GuiTextElement head;

    public GuiButtonElement createPose;
    public GuiStringListElement bones;
    public GuiToggleElement fixed;
    public GuiToggleElement animated;
    public GuiPoseTransformations transforms;
    public GuiAnimation animation;

    private IKey createLabel = IKey.lang("emoticons.gui.morph.general.create_pose");
    private IKey resetLabel = IKey.lang("emoticons.gui.morph.general.reset_pose");

    private AnimatorPoseTransform transform;

    public static GuiContextMenu createCopyPasteMenu(Runnable copy, Consumer<AnimatedPose> paste)
    {
        GuiSimpleContextMenu menu = new GuiSimpleContextMenu(Minecraft.getMinecraft());
        AnimatedPose pose = null;

        try
        {
            NBTTagCompound tag = JsonToNBT.getTagFromJson(GuiScreen.getClipboardString());
            AnimatedPose loaded = new AnimatedPose();

            loaded.fromNBT(tag);

            pose = loaded;
        }
        catch (Exception e)
        {}

        menu.action(Icons.COPY, IKey.lang("emoticons.gui.editor.context.copy"), copy);

        if (pose != null)
        {
            final AnimatedPose innerPose = pose;

            menu.action(Icons.PASTE, IKey.lang("emoticons.gui.editor.context.paste"), () -> paste.accept(innerPose));
        }

        return menu;
    }

    public GuiAnimatedMorphPanel(Minecraft mc, GuiAnimatedMorph editor)
    {
        super(mc, editor);

        /* Pose editor */
        this.createPose = new GuiButtonElement(mc, this.createLabel, this::createResetPose);
        this.bones = new GuiStringListElement(mc, this::pickBone);
        this.bones.background().context(() -> createCopyPasteMenu(this::copyCurrentPose, this::pastePose));
        this.fixed = new GuiToggleElement(mc, IKey.lang("emoticons.gui.morph.general.fixed"), this::toggleFixed);
        this.animated = new GuiToggleElement(mc, IKey.lang("emoticons.gui.morph.general.animated"), this::toggleAnimated);
        this.transforms = new GuiPoseTransformations(mc);

        this.createPose.flex().relative(this).xy(10, 10).w(110).h(20);
        this.bones.flex().relative(this.createPose).y(1F, 5).w(1F).hTo(this.fixed.flex(), -5);
        this.animated.flex().relative(this).x(10).y(1F, -10).w(110).anchorY(1);
        this.fixed.flex().relative(this.animated).y(-1F, -5).w(1F);
        this.transforms.flex().relative(this).set(0, 0, 256, 70).x(0.5F, -128).y(1, -80);

        this.add(this.createPose, this.animated, this.fixed, this.bones, this.transforms);

        /* General */
        this.name = new GuiTextElement(mc, 120, (str) -> this.editor.userConfig.name = str);
        this.scale = new GuiTrackpadElement(mc, (value) ->
        {
            this.editor.userConfig.scale = value.floatValue();
            this.editor.updateMorph();
        });
        this.scale.tooltip(IKey.lang("emoticons.gui.morph.general.scale"));
        this.scaleGui = new GuiTrackpadElement(mc, (value) -> this.editor.userConfig.scaleGui = value.floatValue());
        this.scaleGui.tooltip(IKey.lang("emoticons.gui.morph.general.scale_gui"));
        this.scaleItems = new GuiTrackpadElement(mc, (value) -> this.editor.userConfig.scaleItems = value.floatValue());
        this.scaleItems.tooltip(IKey.lang("emoticons.gui.morph.general.scale_items"));
        this.renderHeldItems = new GuiToggleElement(mc, IKey.lang("emoticons.gui.morph.general.render_items"), true, (b) -> this.editor.userConfig.renderHeldItems = b.isToggled());
        this.head = new GuiTextElement(mc, 120, (str) ->
        {
            this.editor.userConfig.head = str;
            this.editor.updateMorph();
        });

        this.animation = new GuiAnimation(mc, false);
        this.animation.flex().column(5).padding(0);
        this.animation.interpolations.removeFromParent();

        GuiScrollElement element = new GuiScrollElement(mc);

        element.cancelScrollEdge();
        element.add(this.animation);
        element.add(Elements.label(IKey.lang("emoticons.gui.morph.general.name")), this.name);
        element.add(Elements.label(IKey.lang("emoticons.gui.morph.general.scale")), this.scale, this.scaleGui, this.scaleItems, this.renderHeldItems);
        element.add(Elements.label(IKey.lang("emoticons.gui.morph.general.head_bone")), this.head);

        element.flex().relative(this).x(1F).h(1F).w(130).anchorX(1F).column(5).vertical().stretch().scroll().padding(10);

        this.add(element, this.animation.interpolations);
    }

    private void copyCurrentPose()
    {
        GuiScreen.setClipboardString(this.morph.pose.toNBT().toString());
    }

    private void pastePose(AnimatedPose pose)
    {
        this.morph.pose.copy(pose);
        this.transforms.set(this.transforms.trans);
    }

    private void createResetPose(GuiButtonElement button)
    {
        if (this.morph.pose == null)
        {
            AnimatedPose pose = new AnimatedPose();

            for (String bone : this.morph.animator.animation.meshes.get(0).getArmature().bones.keySet())
            {
                pose.bones.put(bone, new AnimatorPoseTransform(bone));
            }

            this.morph.pose = pose;
        }
        else
        {
            this.morph.pose = null;
            this.editor.model.bone = "";
        }

        this.setPoseEditorVisible();
    }

    private void pickBone(List<String> bone)
    {
        this.pickBone(bone.get(0));
    }

    @Override
    public void pickBone(String bone)
    {
        if (this.morph.pose == null)
        {
            return;
        }

        this.transform = this.morph.pose.bones.get(bone);

        if (this.transform == null)
        {
            this.transform = new AnimatorPoseTransform(bone);
            this.morph.pose.bones.put(bone, this.transform);
        }

        this.bones.setCurrentScroll(bone);
        this.animated.toggled(this.morph.animated);
        this.fixed.toggled(this.transform.fixed == AnimatorPoseTransform.FIXED);
        this.transforms.set(this.transform);
        this.editor.model.bone = bone;
    }

    private void toggleFixed(GuiToggleElement toggle)
    {
        this.transform.fixed = toggle.isToggled() ? AnimatorPoseTransform.FIXED : AnimatorPoseTransform.ANIMATED;
    }

    private void toggleAnimated(GuiToggleElement toggle)
    {
        this.morph.animated = toggle.isToggled();
    }

    @Override
    public void fillData(AnimatedMorph morph)
    {
        super.fillData(morph);

        this.setPoseEditorVisible();

        this.name.setText(this.editor.userConfig.name);
        this.scale.setValue(this.editor.userConfig.scale);
        this.scaleGui.setValue(this.editor.userConfig.scaleGui);
        this.scaleItems.setValue(this.editor.userConfig.scaleItems);
        this.renderHeldItems.toggled(this.editor.userConfig.renderHeldItems);
        this.head.setText(this.editor.userConfig.head);

        this.animation.fill(morph.animation);
    }

    private void setPoseEditorVisible()
    {
        AnimatedPose pose = this.morph.pose;

        this.createPose.label = pose == null ? this.createLabel : this.resetLabel;
        this.bones.setVisible(pose != null);
        this.fixed.setVisible(pose != null);
        this.animated.setVisible(pose != null);
        this.transforms.setVisible(pose != null);

        this.bones.clear();
        this.bones.add(this.morph.animator.animation.meshes.get(0).getArmature().bones.keySet());
        this.bones.sort();

        if (pose != null)
        {
            this.pickBone(this.bones.getList().get(0));
        }
    }

    @Override
    public void draw(GuiContext context)
    {
        super.draw(context);
    }

    public static class GuiPoseTransformations extends GuiTransformations
    {
        public AnimatorPoseTransform trans;

        public GuiPoseTransformations(Minecraft mc)
        {
            super(mc);
        }

        public void set(AnimatorPoseTransform trans)
        {
            this.trans = trans;

            if (trans != null)
            {
                this.fillT(trans.x, trans.y, trans.z);
                this.fillS(trans.scaleX, trans.scaleY, trans.scaleZ);
                this.fillR(trans.rotateX / (float) Math.PI * 180, trans.rotateY / (float) Math.PI * 180, trans.rotateZ / (float) Math.PI * 180);
            }
        }

        @Override
        public void localTranslate(double x, double y, double z)
        {
            this.trans.addTranslation(x, y, z, GuiStaticTransformOrientation.getOrientation());

            this.fillT(this.trans.x, this.trans.y, this.trans.z);
        }

        @Override
        public void setT(double x, double y, double z)
        {
            this.trans.x = (float) x;
            this.trans.y = (float) y;
            this.trans.z = (float) z;
        }

        @Override
        public void setS(double x, double y, double z)
        {
            this.trans.scaleX = (float) x;
            this.trans.scaleY = (float) y;
            this.trans.scaleZ = (float) z;
        }

        @Override
        public void setR(double x, double y, double z)
        {
            this.trans.rotateX = (float) (x / 180F * (float) Math.PI);
            this.trans.rotateY = (float) (y / 180F * (float) Math.PI);
            this.trans.rotateZ = (float) (z / 180F * (float) Math.PI);
        }
    }
}