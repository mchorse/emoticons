package mchorse.emoticons.common.emotes;

import mchorse.emoticons.api.animation.model.AnimatorEmoticonsController;
import mchorse.emoticons.skin_n_bones.api.bobj.BOBJArmature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class RockPaperScissorsEmote extends Emote
{
    public String suffix = "";

    public RockPaperScissorsEmote(String name, int duration, boolean looping, SoundEvent sound)
    {
        super(name, duration, looping, sound);
    }

    public RockPaperScissorsEmote(String name, int duration, boolean looping, SoundEvent sound, String suffix)
    {
        super(name, duration, looping, sound);
        this.suffix = suffix;
    }

    @Override
    public Emote getDynamicEmote()
    {
        int rand = this.rand.nextInt(30);
        String suffix = "";

        if (rand <= 10) suffix = "rock";
        else if (rand <= 20) suffix = "paper";
        else if (rand <= 30) suffix = "scissors";

        return this.getDynamicEmote(suffix);
    }

    @Override
    public Emote getDynamicEmote(String suffix)
    {
        return new RockPaperScissorsEmote(this.name, this.duration, this.looping, this.sound, suffix);
    }

    @Override
    public String getKey()
    {
        return this.name + (this.suffix.isEmpty() ? "" : ":" + this.suffix);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void startAnimation(AnimatorEmoticonsController animator)
    {
        if (this.suffix.equals("rock")) animator.itemSlot = new ItemStack(Blocks.STONE, 1);
        else if (this.suffix.equals("paper")) animator.itemSlot = new ItemStack(Items.PAPER, 1);
        else if (this.suffix.equals("scissors")) animator.itemSlot = new ItemStack(Items.SHEARS, 1);

        animator.itemSlotScale = 0;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void progressAnimation(EntityLivingBase entity, BOBJArmature armature, AnimatorEmoticonsController animator, int tick, float partial)
    {
        if (tick > 25 && tick < 55)
        {
            if (tick < 30) animator.itemSlotScale = (tick - 25 + partial) / 5;
            else if (tick >= 50) animator.itemSlotScale = 1 - (tick - 50 + partial) / 5;
            else animator.itemSlotScale = 1;
        }
        else
        {
            animator.itemSlotScale = 0;
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void stopAnimation(AnimatorEmoticonsController animator)
    {
        animator.itemSlot = ItemStack.EMPTY;
        animator.itemSlotScale = 0;
    }
}