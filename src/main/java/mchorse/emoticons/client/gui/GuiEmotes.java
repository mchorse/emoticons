package mchorse.emoticons.client.gui;

import mchorse.emoticons.ClientProxy;
import mchorse.emoticons.api.animation.model.AnimatorEmoticonsController;
import mchorse.emoticons.capabilities.cosmetic.Cosmetic;
import mchorse.emoticons.client.EmoteKeys;
import mchorse.emoticons.client.gui.GuiEmotesList.GuiSeachEmoteList;
import mchorse.emoticons.common.emotes.Emote;
import mchorse.emoticons.common.emotes.Emotes;
import mchorse.mclib.client.gui.framework.GuiBase;
import mchorse.mclib.client.gui.framework.elements.buttons.GuiButtonElement;
import mchorse.mclib.client.gui.framework.elements.utils.GuiDrawable;
import mchorse.mclib.client.gui.utils.keys.IKey;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class GuiEmotes extends GuiBase
{
    private EmoteKeys keys;

    private GuiSeachEmoteList emotes;
    private GuiEmoticonsModelRenderer model;
    private List<GuiButtonElement> buttons = new ArrayList<GuiButtonElement>();

    private String subtitle;
    private int index;

    public GuiEmotes()
    {
        Minecraft mc = Minecraft.getMinecraft();
        Cosmetic cap = (Cosmetic) Cosmetic.get(mc.player);

        if (cap.animator == null)
        {
            cap.setupAnimator(mc.player);
        }

        AnimatorEmoticonsController controller = new AnimatorEmoticonsController(cap.animator.animationName, cap.animator.userData);
        controller.fetchAnimation();
        controller.userConfig = cap.animator.userConfig;

        this.keys = ClientProxy.keys;

        this.emotes = new GuiSeachEmoteList(mc, (entry) -> this.setCurrentEmote(entry.get(0).key));
        this.emotes.label(IKey.lang("emoticons.gui.search"));
        this.model = new GuiEmoticonsModelRenderer(mc, controller);
        this.root.add(this.model, new GuiDrawable((v) ->
        {
            this.drawGradientRect(0, this.height - 90, this.width, this.height - 50, 0x00000000, 0x88000000);
            Gui.drawRect(0, this.height - 50, this.width, this.height, 0x88000000);

            Emote emote = Emotes.getDefault(this.keys.emotes.get(this.index));

            String label = emote.getTitle();
            String subtitle = emote.getDescription();
            int labelW = this.fontRenderer.getStringWidth(label);
            int subtitleW = this.fontRenderer.getStringWidth(subtitle);

            int x = this.viewport.mx();
            int y = this.viewport.ey() - 80;

            GlStateManager.pushMatrix();
            GlStateManager.translate(x - labelW, y, 0);
            GlStateManager.scale(2, 2, 2);
            this.fontRenderer.drawStringWithShadow(label, 0, 0, 0xffffff);
            GlStateManager.popMatrix();

            this.fontRenderer.drawStringWithShadow(subtitle, x - subtitleW / 2, y + 24, 0xaaaaaa);
        }));

        for (int i = 1; i <= 6; i ++)
        {
            final int index = i - 1;
            Emote emote = Emotes.getDefault(this.keys.emotes.get(index));

            GuiButtonElement button = new GuiButtonElement(mc, IKey.str(emote.getTitle()), (b) -> this.setCurrentEmote(index));

            button.flex().relative(this.viewport).set(0, 0, 0, 20).y(1, -30);

            this.root.add(button);
            this.buttons.add(button);
        }

        this.emotes.flex().set(10, 30, 100, 0).relative(this.viewport).h(1, -65);
        this.model.flex().set(0, 0, 0, 0).relative(this.viewport).w(1, 0).h(1, 0);

        this.root.add(this.emotes);
        this.setCurrentEmote(0);

        this.emotes.list.scroll.scrollSpeed = 20;
        this.subtitle = I18n.format("emoticons.gui.total_emotes", Emotes.EMOTES.size());
    }

    @Override
    public boolean doesGuiPauseGame()
    {
        return false;
    }

    @Override
    public void initGui()
    {
        int w = (this.width - 20 - 5 * 5) / 6;

        for (int i = 0; i < this.buttons.size(); i ++)
        {
            this.buttons.get(i).flex().x(10 + (w + 5) * i).w(w);
        }

        super.initGui();
    }

    private void setCurrentEmote(String key)
    {
        this.keys.emotes.set(this.index, key);

        AnimatorEmoticonsController anim = this.model.controller;
        Emote emote = Emotes.getDefault(this.keys.emotes.get(this.index));

        anim.setEmote(anim.animation.createAction(null, anim.userConfig.actions.getConfig("emote_" + key), true));
        this.buttons.get(this.index).label.set(emote.getTitle());
    }

    private void setCurrentEmote(int i)
    {
        this.index = i;

        GuiEmotesList.EmoteEntry entry = null;
        String emote = this.keys.emotes.get(i);

        for (GuiEmotesList.EmoteEntry element : this.emotes.list.getList())
        {
            if (element.key.endsWith(emote))
            {
                entry = element;

                break;
            }
        }

        if (entry != null)
        {
            this.emotes.list.setCurrentScroll(entry);
        }

        this.setCurrentEmote(emote);
    }

    @Override
    public void updateScreen()
    {
        this.model.controller.update(this.model.getEntity());
    }

    @Override
    protected void closeScreen()
    {
        super.closeScreen();

        EmoteKeys.toFile(ClientProxy.keys, new File(ClientProxy.configFolder, "keys.json"));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.drawDefaultBackground();
        this.drawGradientRect(0, 0, this.width, 40, 0x88000000, 0x00000000);

        super.drawScreen(mouseX, mouseY, partialTicks);

        String title = I18n.format("emoticons.gui.title") + " ";
        this.fontRenderer.drawStringWithShadow(title, 10, 12, 0xffffff);
        this.fontRenderer.drawStringWithShadow(this.subtitle, 10 + this.fontRenderer.getStringWidth(title), 12, 0xaaaaaa);
    }
}