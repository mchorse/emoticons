package mchorse.emoticons.blockbuster;

import mchorse.blockbuster.client.gui.dashboard.panels.recording_editor.GuiRecordingEditorPanel;
import mchorse.blockbuster.client.gui.dashboard.panels.recording_editor.actions.GuiActionPanel;
import mchorse.mclib.client.gui.framework.elements.input.GuiTextElement;
import mchorse.mclib.client.gui.framework.elements.utils.GuiContext;
import net.minecraft.client.Minecraft;

public class GuiEmoteActionPanel extends GuiActionPanel<EmoteAction>
{
    public GuiTextElement emote;

    public GuiEmoteActionPanel(Minecraft mc, GuiRecordingEditorPanel panel)
    {
        super(mc, panel);

        this.emote = new GuiTextElement(mc, 10000, (str) -> this.action.emote = str);
        this.emote.flex().relative(this.area).set(10, 0, 0, 20).y(1, -30).w(1, -20);

        this.add(this.emote);
    }

    @Override
    public void fill(EmoteAction action)
    {
        super.fill(action);

        this.emote.setText(action.emote);
    }

    @Override
    public void draw(GuiContext context)
    {
        super.draw(context);
    }
}