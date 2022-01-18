package mchorse.emoticons.api.metamorph;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import mchorse.emoticons.api.metamorph.emote.MorphEmote;
import mchorse.emoticons.api.metamorph.emote.MorphPart;
import mchorse.emoticons.common.emotes.Emote;
import mchorse.metamorph.Metamorph;
import mchorse.metamorph.api.MorphManager;
import mchorse.metamorph.api.models.IMorphProvider;
import mchorse.metamorph.api.morphs.AbstractMorph;
import mchorse.metamorph.capabilities.morphing.Morphing;
import mchorse.metamorph.capabilities.render.IModelRenderer;
import mchorse.metamorph.capabilities.render.ModelRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Optional.Method;

/**
 * Metamorph handler. Used solely to isolate registration of 
 * {@link EmoticonsFactory} for optional Metamorph integration.
 */
public class MetamorphHandler
{
    public static boolean isLoaded()
    {
        return Loader.isModLoaded(Metamorph.MOD_ID);
    }

    /**
     * Registers components of this optional integrations 
     */
    @Method(modid = Metamorph.MOD_ID)
    public static void register()
    {
        EmoticonsFactory factory = new EmoticonsFactory();

        MorphManager.INSTANCE.factories.add(factory);
        MinecraftForge.EVENT_BUS.register(factory);
    }

    /**
     * Set the damn emote oof 
     */
    @Method(modid = Metamorph.MOD_ID)
    public static void setEmote(Emote emote, EntityLivingBase entity)
    {
        AbstractMorph morph = null;

        if (entity instanceof EntityPlayer)
        {
            morph = Morphing.get((EntityPlayer) entity).getCurrentMorph();
        }
        else if (entity instanceof IMorphProvider)
        {
            morph = ((IMorphProvider) entity).getMorph();
        }

        /* This should allow using emotes within Blockbuster's sequencer morph */
        if (morph instanceof IMorphProvider)
        {
            morph = ((IMorphProvider) morph).getMorph();
        }

        if (morph instanceof EmoticonsMorph)
        {
            ((EmoticonsMorph) morph).setEmote(emote, entity);
        }
    }

    public static boolean hasSelector(EntityLivingBase entity)
    {
        if (isLoaded())
        {
            return hasEntitySelector(entity);
        }

        return false;
    }

    @Method(modid = Metamorph.MOD_ID)
    private static boolean hasEntitySelector(EntityLivingBase entity)
    {
        IModelRenderer renderer = ModelRenderer.get(entity);

        return renderer != null && renderer.canRender();
    }

    public static Emote createEmote(String key, int length, boolean looping, JsonObject object)
    {
        if (isLoaded())
        {
            return createMorphEmote(key, length, looping, object);
        }

        return new Emote(key, length, looping, null);
    }

    @Method(modid = Metamorph.MOD_ID)
    private static Emote createMorphEmote(String key, int length, boolean looping, JsonObject object)
    {
        MorphEmote emote = new MorphEmote(key, length, looping, null);

        if (object.has("morphs") && object.get("morphs").isJsonArray())
        {
            for (JsonElement element : object.get("morphs").getAsJsonArray())
            {
                if (element.isJsonObject())
                {
                    emote.parts.add(MorphPart.fromJson(element.getAsJsonObject()));
                }
            }
        }

        return emote;
    }
}