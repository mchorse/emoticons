package mchorse.emoticons.common.emotes;

import mchorse.emoticons.Emoticons;
import mchorse.emoticons.utils.Time;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.util.HashMap;
import java.util.Map;

/**
 * Emotes registry
 * 
 * This class is responsible for registering and storing emote entries 
 * for usage with Emoticon's cosmetic API.
 */
public class Emotes
{
    public static final Map<String, Emote> EMOTES = new HashMap<String, Emote>();

    private static boolean REGISTERED;

    public static boolean has(String emote)
    {
        if (emote.contains(":"))
        {
            emote = emote.split(":")[0];
        }

        return EMOTES.containsKey(emote);
    }

    public static Emote get(String emote)
    {
        if (emote.contains(":"))
        {
            String[] splits = emote.split(":");
            Emote meme = EMOTES.get(splits[0]);

            return meme == null ? null : meme.getDynamicEmote(splits[1]);
        }

        Emote meme = EMOTES.get(emote);

        return meme == null ? null : meme.getDynamicEmote();
    }

    public static Emote getDefault(String emote)
    {
        Emote emo = get(emote);

        return emo == null ? get("default") : emo;
    }

    public static void register()
    {
        EMOTES.clear();

        SoundEvent bestMates = createSound("emoticons:best_mates");
        SoundEvent boneless = createSound("emoticons:boneless");
        SoundEvent defaultEmote = createSound("emoticons:default");
        SoundEvent discoFever = createSound("emoticons:disco_fever");
        SoundEvent electroShuffle = createSound("emoticons:electro_shuffle");
        SoundEvent floss = createSound("emoticons:floss");
        SoundEvent fresh = createSound("emoticons:fresh");
        SoundEvent gangnamStyle = createSound("emoticons:gangnam_style");
        SoundEvent hype = createSound("emoticons:hype");
        SoundEvent infiniteDab = createSound("emoticons:infinite_dab");
        SoundEvent orangeJustice = createSound("emoticons:orange_justice");
        SoundEvent skibidi = createSound("emoticons:skibidi");
        SoundEvent squatKick = createSound("emoticons:squat_kick");
        SoundEvent starPower = createSound("emoticons:star_power");
        SoundEvent takeTheL = createSound("emoticons:take_the_l");
        SoundEvent tidy = createSound("emoticons:tidy");
        SoundEvent freeFlow = createSound("emoticons:free_flow");
        SoundEvent shimmer = createSound("emoticons:shimmer");
        SoundEvent getFunky = createSound("emoticons:getFunky");

        /* Dance emotes */
        register(new Emote("best_mates", 11, true, bestMates));
        register(new Emote("boneless", 40, true, boneless));
        register(new Emote("default", 139, true, defaultEmote));
        register(new Emote("disco_fever", 175, true, discoFever));
        register(new Emote("electro_shuffle", 169, true, electroShuffle));
        register(new Emote("floss", 32, true, floss));
        register(new Emote("fresh", 101, true, fresh));
        register(new Emote("gangnam_style", 18, true, gangnamStyle));
        register(new Emote("hype", 68, true, hype));
        register(new Emote("infinite_dab", 19, true, infiniteDab));
        register(new Emote("orange_justice", 130, true, orangeJustice));
        register(new Emote("skibidi", 16, true, skibidi));
        register(new Emote("squat_kick", 232, true, squatKick));
        register(new StarPowerEmote("star_power", 160, true, starPower));
        register(new Emote("take_the_l", 16, true, takeTheL));
        register(new Emote("tidy", 104, true, tidy));
        register(new Emote("free_flow", 158, true, freeFlow));
        register(new Emote("shimmer", 156, true, shimmer));
        register(new Emote("get_funky", 172, true, getFunky));

        /* Just emotes */
        register(new Emote("boy", 29, false, null));
        register(new Emote("bow", 43, false, null));
        register(new Emote("calculated", 33, false, null));
        register(new Emote("chicken", 19, true, null));
        register(new Emote("clapping", 15, true, null));
        register(new Emote("club", 20, true, null));
        register(new Emote("confused", 140, false, null));
        register(new CryingEmote("crying", 27, true, null));
        register(new Emote("dab", 23, false, null));
        register(new Emote("facepalm", 104, false, null));
        register(new Emote("fist", 53, false, null));
        register(new Emote("laughing", 15, true, null));
        register(new Emote("no", 30, false, null));
        register(new Emote("pointing", 33, false, null));
        register(new PopcornEmote("popcorn", 102, true, null));
        register(new PureSaltEmote("pure_salt", 104, false, null));
        register(new RockPaperScissorsEmote("rock_paper_scissors", 60, false, null));
        register(new Emote("salute", 50, false, null));
        register(new Emote("shrug", 50, false, null));
        register(new Emote("t_pose", 80, true, null));
        register(new Emote("thinking", 100, true, null));
        register(new Emote("twerk", 14, true, null));
        register(new Emote("wave", 40, false, null));
        register(new Emote("yes", 23, false, null));

        /* Emotes 2020 */
        register(new Emote("bitchslap", Time.toTicks(100), false, null));
        register(new Emote("bongo_cat", Time.toTicks(238), false, null));
        register(new Emote("breathtaking", Time.toTicks(154), false, null));
        register(new DisgustedEmote("disgusted", Time.toTicks(200), false, null));
        register(new Emote("exhausted", Time.toTicks(330), true, null));
        register(new Emote("punch", Time.toTicks(58), false, null));
        register(new SneezeEmote("sneeze", Time.toTicks(200), false, null));
        register(new Emote("threatening", Time.toTicks(70), false, null));
        register(new Emote("woah", Time.toTicks(66), false, null));

        register(new Emote("stick_bug", Time.toTicks(25), true, null));
        register(new Emote("am_stuff", Time.toTicks(80), false, null));
        register(new Emote("slow_clap", Time.toTicks(200), false, null));
        register(new Emote("hell_yeah", Time.toTicks(70), false, null));
        register(new Emote("paranoid", Time.toTicks(315), false, null));
        register(new Emote("scared", Time.toTicks(50), true, null));

        register(new Emote("tada", Time.toTicks(90), false, null));
        register(new Emote("smug_dance", Time.toTicks(29), true, null));
        register(new Emote("nope", Time.toTicks(101), false, null));

        register(new Emote("ragdoll_1", Time.toTicks(135), false, null));
        register(new Emote("ragdoll_2", Time.toTicks(150), false, null));
        register(new Emote("ragdoll_3", Time.toTicks(120), false, null));

        REGISTERED = true;
    }

    public static void register(Emote emote)
    {
        EMOTES.put(emote.name, emote);

        if (emote.sound != null && !Emoticons.disableSoundEvents.get() && !REGISTERED)
        {
            ForgeRegistries.SOUND_EVENTS.register(emote.sound);
        }
        else
        {
            emote.sound = null;
        }
    }

    private static SoundEvent createSound(String path)
    {
        SoundEvent event = new SoundEvent(new ResourceLocation(path));
        event.setRegistryName(path);

        return event;
    }
}