package mchorse.emoticons.skin_n_bones.api.metamorph;

import mchorse.emoticons.skin_n_bones.api.animation.AnimationManager;
import mchorse.metamorph.api.creative.categories.MorphCategory;
import mchorse.metamorph.api.creative.sections.MorphSection;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class AnimatedSection extends MorphSection
{
    public MorphCategory category;

    public AnimatedSection(String title)
    {
        super(title);

        this.category = new MorphCategory(this, "skin_n_bones");
    }

    @Override
    public void update(World world)
    {
        this.category.clear();

        for (AnimationManager.AnimationEntry entry : AnimationManager.INSTANCE.animations.values())
        {
            AnimatedMorph morph = new AnimatedMorph();
            NBTTagCompound tag = new NBTTagCompound();

            tag.setString("Name", "skin_n_bones." + entry.animation.name);
            tag.setString("Animation", entry.animation.name);

            morph.fromNBT(tag);

            this.category.add(morph);
        }

        this.categories.clear();
        this.categories.add(this.category);
    }

    @Override
    public void reset()
    {
        this.categories.clear();
    }
}