package mchorse.emoticons.client;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;

import mchorse.emoticons.ClientProxy;
import mchorse.emoticons.capabilities.cosmetic.Cosmetic;
import mchorse.emoticons.capabilities.cosmetic.ICosmetic;
import mchorse.emoticons.client.gui.GuiEmotes;
import mchorse.emoticons.common.EmoteAPI;
import mchorse.emoticons.common.emotes.Emote;
import mchorse.emoticons.common.emotes.Emotes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Keyboard handler
 * 
 * This class is responsible for handling keyboard input (i.e. key 
 * presses) and storing keybindings associated with this mod.
 */
@SideOnly(Side.CLIENT)
public class KeyboardHandler
{
    public KeyBinding random;
    public KeyBinding emote1;
    public KeyBinding emote2;
    public KeyBinding emote3;
    public KeyBinding emote4;
    public KeyBinding emote5;
    public KeyBinding emote6;
    public KeyBinding emotes;
    public KeyBinding stopEmote;
    public KeyBinding reloadEmotes;

    public KeyboardHandler()
    {
        String pre = "emoticons.keys.";

        this.random = new KeyBinding(pre + "random", Keyboard.KEY_O, pre + "category");
        this.emote1 = new KeyBinding(pre + "emote1", Keyboard.KEY_NUMPAD1, pre + "category");
        this.emote2 = new KeyBinding(pre + "emote2", Keyboard.KEY_NUMPAD2, pre + "category");
        this.emote3 = new KeyBinding(pre + "emote3", Keyboard.KEY_NUMPAD3, pre + "category");
        this.emote4 = new KeyBinding(pre + "emote4", Keyboard.KEY_NUMPAD4, pre + "category");
        this.emote5 = new KeyBinding(pre + "emote5", Keyboard.KEY_NUMPAD5, pre + "category");
        this.emote6 = new KeyBinding(pre + "emote6", Keyboard.KEY_NUMPAD6, pre + "category");
        this.emotes = new KeyBinding(pre + "emotes", Keyboard.KEY_P, pre + "category");
        this.stopEmote = new KeyBinding(pre + "stop_emote", Keyboard.KEY_NONE, pre + "category");
        this.reloadEmotes = new KeyBinding(pre + "reload_emotes", Keyboard.KEY_NONE, pre + "category");

        ClientRegistry.registerKeyBinding(this.random);
        ClientRegistry.registerKeyBinding(this.emote1);
        ClientRegistry.registerKeyBinding(this.emote2);
        ClientRegistry.registerKeyBinding(this.emote3);
        ClientRegistry.registerKeyBinding(this.emote4);
        ClientRegistry.registerKeyBinding(this.emote5);
        ClientRegistry.registerKeyBinding(this.emote6);
        ClientRegistry.registerKeyBinding(this.emotes);
        ClientRegistry.registerKeyBinding(this.stopEmote);
        ClientRegistry.registerKeyBinding(this.reloadEmotes);
    }

    @SubscribeEvent
    public void onKeyPress(KeyInputEvent event)
    {
        EntityPlayer player = Minecraft.getMinecraft().player;
        ICosmetic cap = Cosmetic.get(player);

        if (cap != null)
        {
            this.processKeybind(player, cap);
        }
    }

    private void processKeybind(EntityPlayer player, ICosmetic cap)
    {
        Emote emote = cap.getEmote();

        String key = null;
        EmoteKeys emotes = ClientProxy.keys;

        if (this.random.isPressed())
        {
            List<String> keys = new ArrayList<>();
            keys.addAll(Emotes.EMOTES.keySet());
            key = keys.get((int) (keys.size() * Math.random()));
        }

        if (this.emote1.isPressed())
        {
            key = emotes.emotes.get(0);
        }

        if (this.emote2.isPressed())
        {
            key = emotes.emotes.get(1);
        }

        if (this.emote3.isPressed())
        {
            key = emotes.emotes.get(2);
        }

        if (this.emote4.isPressed())
        {
            key = emotes.emotes.get(3);
        }

        if (this.emote5.isPressed())
        {
            key = emotes.emotes.get(4);
        }

        if (this.emote6.isPressed())
        {
            key = emotes.emotes.get(5);
        }

        double dist = Math.abs(player.motionX + player.motionZ);

        /* Handle emotes only if the player is standing still */
        if (player.onGround && dist < 0.05 && key != null && !key.isEmpty())
        {
            EmoteAPI.setEmoteClient(key, player);
        }

        if (this.stopEmote.isPressed() && emote != null)
        {
            EmoteAPI.setEmoteClient("", player);
        }

        if (this.emotes.isPressed())
        {
            Minecraft.getMinecraft().displayGuiScreen(new GuiEmotes());
        }

        if (this.reloadEmotes.isPressed())
        {
            Emotes.register();
            ClientProxy.reloadActions();
        }
    }
}