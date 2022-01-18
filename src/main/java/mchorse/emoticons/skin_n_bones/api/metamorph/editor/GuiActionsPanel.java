package mchorse.emoticons.skin_n_bones.api.metamorph.editor;

import mchorse.emoticons.skin_n_bones.api.animation.model.ActionConfig;
import mchorse.emoticons.skin_n_bones.api.bobj.BOBJAction;
import mchorse.emoticons.skin_n_bones.api.metamorph.AnimatedMorph;
import mchorse.mclib.client.gui.framework.elements.GuiElement;
import mchorse.mclib.client.gui.framework.elements.buttons.GuiToggleElement;
import mchorse.mclib.client.gui.framework.elements.input.GuiTrackpadElement;
import mchorse.mclib.client.gui.framework.elements.list.GuiStringListElement;
import mchorse.mclib.client.gui.framework.elements.list.GuiStringSearchListElement;
import mchorse.mclib.client.gui.framework.elements.utils.GuiContext;
import mchorse.mclib.client.gui.utils.keys.IKey;
import mchorse.metamorph.client.gui.editor.GuiMorphPanel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiActionsPanel extends GuiMorphPanel<AnimatedMorph, GuiAnimatedMorph>
{
    public static final String[] ACTIONS = new String[] {"Idle", "Running", "Sprinting", "Crouching", "CrouchingIdle", "Swimming", "SwimmingIdle", "Flying", "FlyingIdle", "Riding", "RidingIdle", "Dying", "Falling", "Sleeping", "Jump", "Swipe", "Hurt", "Land", "Shoot", "Consume"};

    public ActionConfig config;

    public GuiStringListElement configs;
    public GuiElement fields;
    public GuiStringSearchListElement action;
    public GuiToggleElement clamp;
    public GuiToggleElement reset;
    public GuiTrackpadElement speed;
    public GuiTrackpadElement fade;
    public GuiTrackpadElement tick;

    public GuiActionsPanel(Minecraft mc, GuiAnimatedMorph editor)
    {
        super(mc, editor);

        this.configs = new GuiStringListElement(mc, (str) -> this.selectAction(str.get(0)));

        for (String action : ACTIONS)
        {
            this.configs.add(action);
        }

        this.fields = new GuiElement(mc).noCulling();
        this.configs.sort();
        this.configs.flex().relative(this.area).set(10, 25, 105, 90).h(1, -35);

        this.action = new GuiStringSearchListElement(mc, (value) -> this.config.name = value.get(0));
        this.clamp = new GuiToggleElement(mc, IKey.lang("emoticons.gui.morph.actions.clamp"), false, (b) -> this.config.clamp = b.isToggled());
        this.reset = new GuiToggleElement(mc, IKey.lang("emoticons.gui.morph.actions.reset"), false, (b) -> this.config.reset = b.isToggled());
        this.speed = new GuiTrackpadElement(mc, (value) -> this.config.speed = value.floatValue());
        this.speed.tooltip(IKey.lang("emoticons.gui.morph.actions.speed"));
        this.fade = new GuiTrackpadElement(mc, (value) -> this.config.fade = value.intValue());
        this.fade.tooltip(IKey.lang("emoticons.gui.morph.actions.fade"));
        this.fade.limit(0, Integer.MAX_VALUE, true);
        this.tick = new GuiTrackpadElement(mc, (value) -> this.config.tick = value.intValue());
        this.tick.tooltip(IKey.lang("emoticons.gui.morph.actions.tick"));
        this.tick.limit(0, Integer.MAX_VALUE, true);

        GuiElement fields = new GuiElement(mc);

        fields.flex().relative(this).xy(1F, 1F).w(130).anchor(1, 1).column(5).vertical().stretch().padding(10);
        fields.add(this.clamp, this.reset, this.speed, this.fade, this.tick);

        this.action.flex().relative(this.area).x(1F, -10).y(22).w(110).hTo(fields.area, 5).anchorX(1F);

        this.fields.add(fields, this.action);
        this.fields.setVisible(false);

        this.add(this.configs, this.fields);
    }

    private void selectAction(String name)
    {
        name = this.editor.userConfig.actions.toKey(name);

        ActionConfig config = this.editor.userConfig.actions.actions.get(name);

        if (config == null)
        {
            config = this.morph.animator.userConfig.actions.actions.get(name);
            config = config == null ? new ActionConfig(name) : config.clone();

            this.editor.userConfig.actions.actions.put(name, config);
        }

        this.config = config;
        this.fields.setVisible(true);
        this.fillFields(config);
    }

    @Override
    public void fillData(AnimatedMorph morph)
    {
        super.fillData(morph);

        this.action.list.clear();

        for (BOBJAction action : morph.animator.animation.data.actions.values())
        {
            this.action.list.add(action.name);
        }

        this.action.list.sort();

        this.action.list.setCurrent("");
        this.fields.setVisible(false);
        this.configs.setCurrent("");
    }

    private void fillFields(ActionConfig config)
    {
        this.action.list.setCurrentScroll(config.name);
        this.clamp.toggled(config.clamp);
        this.reset.toggled(config.reset);
        this.speed.setValue(config.speed);
        this.fade.setValue((int) config.fade);
        this.tick.setValue(config.tick);
    }

    @Override
    public void draw(GuiContext context)
    {
        this.configs.area.draw(0x88000000);
        this.font.drawStringWithShadow(I18n.format("emoticons.gui.morph.actions.title"), this.configs.area.x, this.configs.area.y - 12, 0xffffff);

        if (this.fields.isVisible())
        {
            this.font.drawStringWithShadow(I18n.format("emoticons.gui.morph.actions.action"), this.action.area.x, this.action.area.y - 12, 0xffffff);
            this.action.area.draw(0x88000000);
        }

        super.draw(context);
    }
}