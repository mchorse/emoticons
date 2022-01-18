package mchorse.emoticons.api.animation.model;

import java.util.Map;

import com.google.common.collect.Maps;

import mchorse.emoticons.common.emotes.Emote;
import mchorse.emoticons.skin_n_bones.api.animation.AnimationMesh;
import mchorse.emoticons.skin_n_bones.api.animation.AnimationMeshConfig;
import mchorse.emoticons.skin_n_bones.api.animation.model.AnimatorController;
import mchorse.emoticons.skin_n_bones.api.animation.model.AnimatorHeldItemConfig;
import mchorse.emoticons.skin_n_bones.api.bobj.BOBJArmature;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


@SideOnly(Side.CLIENT)
public class AnimatorEmoticonsController extends AnimatorController
{
    private static final Map<String, ResourceLocation> ARMOR_TEXTURE_RES_MAP = Maps.<String, ResourceLocation>newHashMap();

    public ItemStack itemSlot = ItemStack.EMPTY;
    public float itemSlotScale = 0;

    public AnimatorEmoticonsController(String animationName, NBTTagCompound userData)
    {
        super(animationName, userData);
    }

    public void render(Emote emote, EntityLivingBase entity, double x, double y, double z, int i, float partialTicks)
    {
        this.e = emote;
        this.render(entity, x, y, z, i, partialTicks);
        this.e = null;
    }

    /**
     * Render current animation
     */
    @Override
    public void renderAnimation(EntityLivingBase entity, AnimationMesh mesh, float yaw, float partialTicks)
    {
        this.updateArmor(entity);

        super.renderAnimation(entity, mesh, yaw, partialTicks);

        if (this.e != null && this.emote != null)
        {
            BOBJArmature original = mesh.getArmature();
            BOBJArmature armature = this.animator.useArmature(original);

            this.e.renderEmote(entity, armature, this, (int) this.emote.getTick(0), partialTicks);
        }
    }

    /**
     * Render hand held items and skin layer
     */
    @Override
    protected void renderItems(EntityLivingBase entity, BOBJArmature armature)
    {
        if (!this.userConfig.renderHeldItems)
        {
            return;
        }

        float scale = this.userConfig.scaleItems;

        ItemStack mainItem = entity.getHeldItemMainhand();
        ItemStack offItem = entity.getHeldItemOffhand();

        if (!this.itemSlot.isEmpty())
        {
            if (this.itemSlotScale > 0)
            {
                for (AnimatorHeldItemConfig itemConfig : this.userConfig.rightHands.values())
                {
                    this.renderItem(entity, this.itemSlot, armature, itemConfig, TransformType.THIRD_PERSON_RIGHT_HAND, scale * this.itemSlotScale);
                }
            }
        }
        else if (!mainItem.isEmpty() && this.userConfig.rightHands != null)
        {
            for (AnimatorHeldItemConfig itemConfig : this.userConfig.rightHands.values())
            {
                this.renderItem(entity, mainItem, armature, itemConfig, TransformType.THIRD_PERSON_RIGHT_HAND, scale);
            }
        }

        if (!offItem.isEmpty() && this.userConfig.leftHands != null)
        {
            for (AnimatorHeldItemConfig itemConfig : this.userConfig.leftHands.values())
            {
                this.renderItem(entity, offItem, armature, itemConfig, TransformType.THIRD_PERSON_LEFT_HAND, scale);
            }
        }
    }

    /**
     * Update armor slots
     */
    private void updateArmor(EntityLivingBase entity)
    {
        this.updateArmorSlot("armor_helmet", entity, EntityEquipmentSlot.HEAD);
        this.updateArmorSlot("armor_chest", entity, EntityEquipmentSlot.CHEST);
        this.updateArmorSlot("armor_leggings", entity, EntityEquipmentSlot.LEGS);
        this.updateArmorSlot("armor_feet", entity, EntityEquipmentSlot.FEET);
    }

    /**
     * Update separate armor slots
     */
    private void updateArmorSlot(String id, EntityLivingBase entity, EntityEquipmentSlot slot)
    {
        AnimationMeshConfig config = this.userConfig.meshes.get(id);

        if (config == null)
        {
            return;
        }

        if (this.userData.hasKey("Meshes", Constants.NBT.TAG_COMPOUND) && this.userData.getCompoundTag("Meshes").hasKey(id))
        {
            return;
        }

        ItemStack stack = entity.getItemStackFromSlot(slot);

        if (stack.getItem() instanceof ItemArmor)
        {
            ItemArmor item = (ItemArmor) stack.getItem();

            config.visible = true;
            config.texture = this.getArmorResource(entity, stack, slot, null);
            config.color = 0xffffffff;

            if (item.hasOverlay(stack))
            {
                config.color = item.getColor(stack);
            }
        }
        else
        {
            config.visible = false;
            config.color = 0xffffffff;
        }
    }

    /**
     * More generic ForgeHook version of the above function, it allows for Items to have more control over what texture they provide.
     *
     * @param entity Entity wearing the armor
     * @param stack ItemStack for the armor
     * @param slot Slot ID that the item is in
     * @param type Subtype, can be null or "overlay"
     * @return ResourceLocation pointing at the armor's texture
     */
    private ResourceLocation getArmorResource(net.minecraft.entity.Entity entity, ItemStack stack, EntityEquipmentSlot slot, String type)
    {
        ItemArmor item = (ItemArmor) stack.getItem();
        String texture = item.getArmorMaterial().getName();
        String domain = "minecraft";
        int idx = texture.indexOf(':');
        if (idx != -1)
        {
            domain = texture.substring(0, idx);
            texture = texture.substring(idx + 1);
        }
        String s1 = String.format("%s:textures/models/armor/%s_layer_%d%s.png", domain, texture, (isLegSlot(slot) ? 2 : 1), type == null ? "" : String.format("_%s", type));

        s1 = net.minecraftforge.client.ForgeHooksClient.getArmorTexture(entity, stack, s1, slot, type);
        ResourceLocation resourcelocation = ARMOR_TEXTURE_RES_MAP.get(s1);

        if (resourcelocation == null)
        {
            resourcelocation = new ResourceLocation(s1);
            ARMOR_TEXTURE_RES_MAP.put(s1, resourcelocation);
        }

        return resourcelocation;
    }

    private boolean isLegSlot(EntityEquipmentSlot slotIn)
    {
        return slotIn == EntityEquipmentSlot.LEGS;
    }
}