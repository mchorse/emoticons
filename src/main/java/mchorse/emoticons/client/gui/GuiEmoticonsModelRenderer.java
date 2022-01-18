package mchorse.emoticons.client.gui;

import mchorse.emoticons.api.animation.model.AnimatorEmoticonsController;
import mchorse.mclib.client.gui.framework.elements.GuiModelRenderer;
import mchorse.mclib.client.gui.framework.elements.utils.GuiContext;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiEmoticonsModelRenderer extends GuiModelRenderer
{
    public AnimatorEmoticonsController controller;

    public GuiEmoticonsModelRenderer(Minecraft mc, AnimatorEmoticonsController controller)
    {
        super(mc);

        this.controller = controller;
    }

    @Override
    protected void drawUserModel(GuiContext guiContext)
    {
        this.controller.render(this.entity, 0, 0, 0, 0, Minecraft.getMinecraft().getRenderPartialTicks());
    }
}