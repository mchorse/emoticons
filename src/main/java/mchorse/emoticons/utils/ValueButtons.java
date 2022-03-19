package mchorse.emoticons.utils;

import mchorse.emoticons.ClientProxy;
import mchorse.emoticons.common.emotes.Emotes;
import mchorse.mclib.client.gui.framework.elements.GuiElement;
import mchorse.mclib.client.gui.framework.elements.buttons.GuiButtonElement;
import mchorse.mclib.client.gui.utils.Elements;
import mchorse.mclib.client.gui.utils.GuiUtils;
import mchorse.mclib.client.gui.utils.keys.IKey;
import mchorse.mclib.config.gui.GuiConfigPanel;
import mchorse.mclib.config.values.ValueGUI;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class ValueButtons extends ValueGUI
{
    public ValueButtons(String id)
    {
        super(id);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public List<GuiElement> getFields(Minecraft mc, GuiConfigPanel config)
    {
        GuiButtonElement reload = new GuiButtonElement(mc, IKey.lang("emoticons.config.reload"), (button) ->
        {
            Emotes.register();
            ClientProxy.reloadActions();
        });
        GuiButtonElement open = new GuiButtonElement(mc, IKey.lang("emoticons.config.open"), (button) -> GuiUtils.openFolder(new File(ClientProxy.configFolder, "emotes").getAbsolutePath()));

        return Arrays.asList(Elements.row(mc, 5, 0, 20, reload, open));
    }
}