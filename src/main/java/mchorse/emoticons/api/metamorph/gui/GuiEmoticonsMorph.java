package mchorse.emoticons.api.metamorph.gui;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import mchorse.emoticons.skin_n_bones.api.metamorph.editor.GuiAnimatedMorph;
import mchorse.mclib.client.gui.framework.GuiBase;
import mchorse.mclib.client.gui.framework.elements.context.GuiContextMenu;
import mchorse.mclib.client.gui.framework.elements.context.GuiSimpleContextMenu;
import mchorse.mclib.client.gui.utils.Icons;
import mchorse.mclib.client.gui.utils.keys.IKey;
import mchorse.mclib.utils.JsonUtils;
import mchorse.metamorph.bodypart.BodyPart;
import mchorse.metamorph.bodypart.GuiBodyPartListElement;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Keyboard;

import java.util.List;
import java.util.function.Supplier;

public class GuiEmoticonsMorph extends GuiAnimatedMorph
{
    public GuiEmoticonsMorph(Minecraft mc)
    {
        super(mc);

        this.general = new GuiEmoticonsMorphPanel(mc, this);
        this.defaultPanel = this.general;
        this.panels.set(this.panels.size() - 1, this.general);
        this.buttons.elements.get(this.buttons.elements.size() - 1).callback = (b) -> this.setPanel(this.general);

        this.keys().register(IKey.lang("emoticons.gui.morph.general.keys.pick_skin"), Keyboard.KEY_P, () ->
        {
            this.setPanel(this.meshes);
            this.meshes.configs.setCurrent("body");
            this.meshes.selectConfig("body");
            this.meshes.texture.clickItself(GuiBase.getCurrent());
        }).held(Keyboard.KEY_LSHIFT);

        List<GuiBodyPartListElement> list = this.bodyPart.getChildren(GuiBodyPartListElement.class);

        if (!list.isEmpty())
        {
            GuiBodyPartListElement bodyParts = list.get(0);
            Supplier<GuiContextMenu> menuSupplier = bodyParts.contextMenu;

            bodyParts.context(() ->
            {
                GuiSimpleContextMenu menu = (GuiSimpleContextMenu) menuSupplier.get();

                if (!bodyParts.isDeselected())
                {
                    menu.action(Icons.COPY, IKey.lang("emoticons.gui.morph.general.context.copy_emote"), () ->
                    {
                        JsonObject object = new JsonObject();
                        BodyPart part = bodyParts.getCurrentFirst();

                        object.addProperty("morph", part.morph.toNBT().toString());
                        object.addProperty("bone", part.limb);
                        object.addProperty("start", 0);
                        object.addProperty("length", 100);
                        object.addProperty("target", part.useTarget);
                        object.addProperty("fade_in", 0);
                        object.addProperty("fade_out", 0);

                        JsonArray translate = new JsonArray();

                        translate.add(part.translate.x);
                        translate.add(part.translate.y);
                        translate.add(part.translate.z);

                        JsonArray scale = new JsonArray();

                        scale.add(part.scale.x);
                        scale.add(part.scale.y);
                        scale.add(part.scale.z);

                        JsonArray rotation = new JsonArray();

                        rotation.add(part.rotate.x);
                        rotation.add(part.rotate.y);
                        rotation.add(part.rotate.z);

                        object.add("translate", translate);
                        object.add("scale", scale);
                        object.add("rotation", rotation);

                        GuiScreen.setClipboardString(JsonUtils.jsonToPretty(object));
                    });
                }

                return menu;
            });
        }
    }
}