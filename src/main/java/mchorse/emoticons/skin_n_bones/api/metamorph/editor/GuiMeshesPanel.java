package mchorse.emoticons.skin_n_bones.api.metamorph.editor;

import mchorse.emoticons.skin_n_bones.api.animation.AnimationMesh;
import mchorse.emoticons.skin_n_bones.api.animation.AnimationMeshConfig;
import mchorse.emoticons.skin_n_bones.api.metamorph.AnimatedMorph;
import mchorse.mclib.client.gui.framework.elements.GuiElement;
import mchorse.mclib.client.gui.framework.elements.buttons.GuiButtonElement;
import mchorse.mclib.client.gui.framework.elements.buttons.GuiToggleElement;
import mchorse.mclib.client.gui.framework.elements.input.GuiColorElement;
import mchorse.mclib.client.gui.framework.elements.input.GuiTextElement;
import mchorse.mclib.client.gui.framework.elements.input.GuiTexturePicker;
import mchorse.mclib.client.gui.framework.elements.list.GuiStringListElement;
import mchorse.mclib.client.gui.framework.elements.utils.GuiContext;
import mchorse.mclib.client.gui.utils.keys.IKey;
import mchorse.metamorph.client.gui.editor.GuiMorphPanel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class GuiMeshesPanel extends GuiMorphPanel<AnimatedMorph, GuiAnimatedMorph>
{
    public GuiStringListElement configs;

    public GuiElement fields;
    public GuiButtonElement texture;
    public GuiToggleElement filtering;
    public GuiToggleElement normals;
    public GuiToggleElement smooth;
    public GuiToggleElement visible;
    public GuiToggleElement lighting;
    public GuiColorElement color;

    public GuiTexturePicker picker;

    public AnimationMeshConfig config;

    public GuiMeshesPanel(Minecraft mc, GuiAnimatedMorph editor)
    {
        super(mc, editor);

        this.fields = new GuiElement(mc).noCulling();
        this.configs = new GuiStringListElement(mc, (str) -> this.selectConfig(str.get(0)));
        this.picker = new GuiTexturePicker(mc, (rl) ->
        {
            this.config.texture = rl;
            this.editor.updateMorph();
        });

        this.texture = new GuiButtonElement(mc, IKey.lang("emoticons.gui.morph.meshes.pick_texture"), (b) ->
        {
            this.picker.refresh();
            this.picker.fill(this.config.texture);

            this.add(this.picker);
            this.picker.resize();
        });

        this.filtering = new GuiToggleElement(mc, IKey.lang("emoticons.gui.morph.meshes.linear"), false, (b) ->
        {
            this.config.filtering = b.isToggled() ? GL11.GL_LINEAR : GL11.GL_NEAREST;
            this.editor.updateMorph();
        });

        this.normals = new GuiToggleElement(mc, IKey.lang("emoticons.gui.morph.meshes.normals"), false, (b) ->
        {
            this.config.normals = b.isToggled();
            this.editor.updateMorph();
        });

        this.smooth = new GuiToggleElement(mc, IKey.lang("emoticons.gui.morph.meshes.smooth"), false, (b) ->
        {
            this.config.smooth = b.isToggled();
            this.editor.updateMorph();
        });

        this.visible = new GuiToggleElement(mc, IKey.lang("emoticons.gui.morph.meshes.visible"), false, (b) ->
        {
            this.config.visible = b.isToggled();
            this.editor.updateMorph();
        });

        this.lighting = new GuiToggleElement(mc, IKey.lang("emoticons.gui.morph.meshes.lighting"), false, (b) ->
        {
            this.config.lighting = b.isToggled();
            this.editor.updateMorph();
        });

        this.color = new GuiColorElement(mc, (color) ->
        {
            this.config.color = color;
            this.editor.updateMorph();
        }).onTop();

        this.fields.add(this.texture, this.filtering, this.normals, this.smooth, this.visible, this.lighting, this.color);

        this.configs.flex().relative(this.area).set(10, 25, 105, 90).h(1, -35);
        this.picker.flex().relative(this.area).wh(1F, 1F);

        this.texture.flex().relative(this.area).set(0, 0, 105, 20).x(1, -115);
        this.filtering.flex().relative(this.texture.resizer()).set(0, 25, 105, 11);
        this.normals.flex().relative(this.filtering.resizer()).set(0, 16, 105, 11);
        this.smooth.flex().relative(this.normals.resizer()).set(0, 16, 105, 11);
        this.visible.flex().relative(this.smooth.resizer()).set(0, 16, 105, 11);
        this.lighting.flex().relative(this.visible.resizer()).set(0, 16, 105, 11);
        this.color.flex().relative(this.lighting.resizer()).set(0, 36, 105, 20);

        this.texture.flex().y(1, -(this.color.resizer().getY() + this.color.resizer().getH() + 10));

        this.add(this.configs, this.fields);
    }

    public void selectConfig(String name)
    {
        AnimationMeshConfig config = this.editor.userConfig.meshes.get(name);

        if (config == null)
        {
            config = this.morph.animator.userConfig.meshes.get(name);
            config = config == null ? new AnimationMeshConfig() : config.clone();

            this.editor.userConfig.meshes.put(name, config);
            this.editor.updateMorph();
        }

        this.config = config;
        this.fields.setVisible(true);
        this.fillFields(config);
    }

    @Override
    public void fillData(AnimatedMorph morph)
    {
        super.fillData(morph);

        this.fillFields(null);
        this.fields.setVisible(false);

        this.configs.clear();

        for (AnimationMesh mesh : morph.animator.animation.meshes)
        {
            this.configs.add(mesh.name);
        }

        this.configs.sort();
        this.configs.update();
    }

    private void fillFields(AnimationMeshConfig config)
    {
        if (config == null)
        {
            this.filtering.toggled(false);
            this.normals.toggled(false);
            this.smooth.toggled(false);
            this.visible.toggled(true);
            this.lighting.toggled(true);
            this.color.picker.setColor(0xffffff);
        }
        else
        {
            this.filtering.toggled(config.filtering == GL11.GL_NEAREST);
            this.normals.toggled(config.normals);
            this.smooth.toggled(config.smooth);
            this.visible.toggled(config.visible);
            this.lighting.toggled(config.lighting);
            this.color.picker.setColor(config.color);
        }
    }

    @Override
    public void finishEditing()
    {
        this.picker.close();
    }

    @Override
    public void draw(GuiContext context)
    {
        this.configs.area.draw(0x88000000);

        this.font.drawStringWithShadow(I18n.format("emoticons.gui.morph.meshes.title"), this.configs.area.x, this.configs.area.y - 12, 0xffffff);

        if (this.fields.isVisible())
        {
            this.font.drawStringWithShadow(I18n.format("emoticons.gui.morph.meshes.texture"), this.texture.area.x, this.texture.area.y - 12, 0xffffff);
            this.font.drawStringWithShadow(I18n.format("emoticons.gui.morph.meshes.color"), this.color.area.x, this.color.area.y - 12, 0xffffff);
        }

        super.draw(context);
    }
}