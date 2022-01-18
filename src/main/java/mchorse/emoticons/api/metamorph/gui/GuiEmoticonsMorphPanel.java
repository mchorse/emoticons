package mchorse.emoticons.api.metamorph.gui;

import mchorse.emoticons.api.metamorph.EmoticonsMorph;
import mchorse.emoticons.skin_n_bones.api.metamorph.AnimatedMorph;
import mchorse.emoticons.skin_n_bones.api.metamorph.editor.GuiAnimatedMorph;
import mchorse.emoticons.skin_n_bones.api.metamorph.editor.GuiAnimatedMorphPanel;
import mchorse.mclib.client.gui.utils.keys.IKey;
import mchorse.mclib.utils.Direction;
import mchorse.metamorph.api.MorphUtils;
import mchorse.metamorph.client.gui.creative.GuiNestedEdit;
import net.minecraft.client.Minecraft;

public class GuiEmoticonsMorphPanel extends GuiAnimatedMorphPanel
{
    public GuiNestedEdit edit;

    public GuiEmoticonsMorphPanel(Minecraft mc, GuiAnimatedMorph editor)
    {
        super(mc, editor);

        this.edit = new GuiNestedEdit(mc, false, (editing) ->
        {
            this.editor.morphs.nestEdit(((EmoticonsMorph) this.morph).placeholder.get(), editing, (morph) ->
            {
                ((EmoticonsMorph) this.morph).placeholder.setDirect(MorphUtils.copy(morph));
            });
        });
        this.edit.tooltip(IKey.lang("emoticons.gui.morph.general.placeholder_tooltip"), Direction.TOP);
        this.edit.flex().relative(this).anchorX(0.5F).wh(100, 20).x(0.5F).y(10);

        this.add(this.edit);
    }

    @Override
    public void fillData(AnimatedMorph morph)
    {
        super.fillData(morph);

        this.edit.setMorph(((EmoticonsMorph) morph).placeholder.get());
    }
}