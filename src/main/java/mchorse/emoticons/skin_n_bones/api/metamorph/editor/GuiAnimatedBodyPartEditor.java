package mchorse.emoticons.skin_n_bones.api.metamorph.editor;

import mchorse.metamorph.bodypart.BodyPart;
import mchorse.metamorph.bodypart.GuiBodyPartEditor;
import mchorse.metamorph.client.gui.editor.GuiAbstractMorph;
import net.minecraft.client.Minecraft;

public class GuiAnimatedBodyPartEditor extends GuiBodyPartEditor implements IBonePicker
{
    public GuiAnimatedBodyPartEditor(Minecraft mc, GuiAbstractMorph editor)
    {
        super(mc, editor);
    }

    @Override
    protected void setPart(BodyPart part)
    {
        super.setPart(part);

        if (part != null)
        {
            GuiAnimatedMorph parent = (GuiAnimatedMorph) this.editor;

            parent.model.bone = part.limb;
        }
    }

    @Override
    protected void setupNewBodyPart(BodyPart part)
    {
        super.setupNewBodyPart(part);

        part.rotate.x = 0;
    }

    @Override
    protected void pickLimb(String limbName)
    {
        GuiAnimatedMorph parent = (GuiAnimatedMorph) this.editor;

        super.pickLimb(limbName);
        parent.model.bone = limbName;
    }

    @Override
    public void pickBone(String limb)
    {
        try
        {
            this.pickLimb(limb);
            this.limbs.setCurrent(limb);
        }
        catch (Exception e) {}
    }
}