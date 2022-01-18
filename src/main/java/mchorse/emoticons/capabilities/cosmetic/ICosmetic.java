package mchorse.emoticons.capabilities.cosmetic;

import mchorse.emoticons.common.emotes.Emote;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface ICosmetic
{
    /**
     * Set current emote 
     */
    public void setEmote(Emote emote, EntityLivingBase target);

    /**
     * Get current emote (check for null)
     */
    public Emote getEmote();

    /**
     * Update this cosmetics capability based on the provided entity
     */
    public void update(EntityLivingBase entity);

    /**
     * Render the entity with this cosmetics capability
     */
    @SideOnly(Side.CLIENT)
    public boolean render(EntityLivingBase entity, double x, double y, double z, float partialTicks);
}