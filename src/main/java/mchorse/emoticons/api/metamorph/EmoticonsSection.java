package mchorse.emoticons.api.metamorph;

import mchorse.emoticons.skin_n_bones.api.animation.AnimationManager;
import mchorse.metamorph.api.creative.categories.MorphCategory;
import mchorse.metamorph.api.creative.sections.MorphSection;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class EmoticonsSection extends MorphSection
{
    public MorphCategory category;

    public EmoticonsSection(String title)
    {
        super(title);

        this.category = new MorphCategory(this, "emoticons");
    }

    @Override
    public void update(World world)
    {
        this.category.clear();

        for (AnimationManager.AnimationEntry entry : AnimationManager.INSTANCE.animations.values())
        {
            EmoticonsMorph morph = new EmoticonsMorph();
            NBTTagCompound tag = new NBTTagCompound();

            tag.setString("Name", "emoticons." + entry.animation.name);
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