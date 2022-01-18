package mchorse.emoticons.skin_n_bones.api.animation.model;

import mchorse.emoticons.common.emotes.Emote;
import mchorse.emoticons.skin_n_bones.api.animation.Animation;
import mchorse.emoticons.skin_n_bones.api.animation.AnimationManager;
import mchorse.emoticons.skin_n_bones.api.animation.AnimationMesh;
import mchorse.emoticons.skin_n_bones.api.bobj.BOBJArmature;
import mchorse.emoticons.skin_n_bones.api.bobj.BOBJBone;
import mchorse.mclib.client.render.RenderLightmap;
import mchorse.mclib.utils.MatrixUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import javax.vecmath.Matrix4f;
import javax.vecmath.Vector4f;
import java.nio.FloatBuffer;

@SideOnly(Side.CLIENT)
public class AnimatorController
{
    /**
     * Matrix buffer. Used for copying bone's matrix to buffer for item 
     * rendering. 
     */
    public static final FloatBuffer matrix = BufferUtils.createFloatBuffer(16);

    /**
     * Matrix buffer for {@link Matrix4f}. 
     */
    public static final float[] buffer = new float[16];

    /**
     * Default animator factory 
     */
    public static final IAnimatorFactory DEFAULT_FACTORY = (controller) -> new Animator(controller);

    /* Animation */
    public IAnimatorFactory factory = DEFAULT_FACTORY;
    public Animation animation;
    public IAnimator animator;
    public ActionPlayback emote;
    public Emote e;

    /* Configuration */
    public AnimatorConfig.AnimatorConfigEntry config;
    public AnimatorConfig userConfig = new AnimatorConfig();
    public long lastModified;
    public int checkConfig;

    /* NBT config */
    public String animationName;
    public NBTTagCompound userData;

    private Minecraft mc;
    private Vector4f result = new Vector4f();
    private Matrix4f rotate = new Matrix4f();

    public AnimatorController(String animationName, NBTTagCompound userData)
    {
        this.refresh(animationName, userData);

        this.mc = Minecraft.getMinecraft();
    }

    public Vector4f calcPosition(EntityLivingBase entity, BOBJBone bone, float x, float y, float z, float partial)
    {
        final float pi = (float) Math.PI;

        this.result.set(x, y, z, 1);
        bone.mat.transform(this.result);

        this.rotate.setIdentity();
        this.rotate.rotY((180F - entity.renderYawOffset + 180F) / 180F * pi);
        this.rotate.transform(this.result);
        this.result.scale(0.9375F);

        float ex = (float) (entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * partial);
        float ey = (float) (entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * partial);
        float ez = (float) (entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * partial);

        this.result.x += ex;
        this.result.y += ey;
        this.result.z += ez;

        return this.result;
    }

    public void setEmote(ActionPlayback emote)
    {
        this.emote = emote;

        if (this.animator != null)
        {
            this.animator.setEmote(emote);
        }
    }

    public void refresh(String animationName, NBTTagCompound userData)
    {
        this.animation = null;
        this.animator = null;

        this.animationName = animationName;
        this.userData = userData;
    }

    public void renderOnScreen(EntityPlayer player, int x, int y, float scale, float alpha)
    {
        this.fetchAnimation();

        if (this.animation != null && this.animation.meshes.size() > 0)
        {
            RenderHelper.enableGUIStandardItemLighting();
            GlStateManager.enableRescaleNormal();

            GlStateManager.enableAlpha();
            GlStateManager.enableDepth();
            GlStateManager.color(1, 1, 1, 1);

            float mult = this.userConfig.scaleGui;

            GL11.glPushMatrix();
            GL11.glTranslatef(x, y, 0);
            /* Because orthographic matrix's positive Y is leans toward 
             * bottom */
            GL11.glScalef(scale * mult, -scale * mult, scale * mult);
            GL11.glRotatef(45, 1, 0, 0);
            GL11.glRotatef(45, 0, 1, 0);

            /* Head looks ugly without resetting those fields */
            float yaw = player.rotationYawHead;
            float prevYaw = player.prevRotationYawHead;
            float pitch = player.rotationPitch;
            float prevPitch = player.prevRotationPitch;

            player.rotationYawHead = player.prevRotationYawHead = 0;
            player.rotationPitch = player.prevRotationPitch = 0;

            this.renderAnimation(player, this.animation.meshes.get(0), 0, 0);

            player.rotationYawHead = yaw;
            player.prevRotationYawHead = prevYaw;
            player.rotationPitch = pitch;
            player.prevRotationPitch = prevPitch;

            GL11.glPopMatrix();

            GlStateManager.disableDepth();
            GlStateManager.disableAlpha();

            RenderHelper.disableStandardItemLighting();
            GlStateManager.disableRescaleNormal();
        }
    }

    protected void renderOnScreen(EntityPlayer player, AnimationMesh mesh, int x, int y, float scale, float alpha)
    {
        this.animation.render(this.userConfig.meshes);
    }

    public void render(EntityLivingBase entity, double x, double y, double z, float entityYaw, float partialTicks)
    {
        if (this.animation != null && this.animation.meshes.size() > 0)
        {
            GlStateManager.disableCull();
            GlStateManager.enableAlpha();
            GlStateManager.enableBlend();
            GlStateManager.enableRescaleNormal();
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);

            float yaw = this.interpolate(entity.prevRenderYawOffset, entity.renderYawOffset, partialTicks);

            /* Fix the yaw when riding */
            if (entity.isRiding())
            {
                Entity vehicle = entity.getRidingEntity();

                /* For some reason, Minecart's always look to the left
                 * instead of forward, so we must rotate 90 degrees in 
                 * order to orient character forward */
                if (vehicle instanceof EntityMinecart)
                {
                    yaw = this.interpolate(vehicle.prevRotationYaw , vehicle.rotationYaw, partialTicks);
                    yaw += 90;
                }
            }

            float scale = this.userConfig.scale;

            GL11.glPushMatrix();
            GL11.glTranslated(x, y, z);

            boolean captured = MatrixUtils.captureMatrix();

            GL11.glScalef(scale, scale, scale);

            if (entity.isPlayerSleeping())
            {
                EntityPlayer player = (EntityPlayer) entity;

                GlStateManager.rotate(player.getBedOrientationInDegrees(), 0.0F, 1.0F, 0.0F);
                GlStateManager.rotate(270.0F, 0.0F, 1.0F, 0.0F);
            }
            else
            {
                GL11.glRotatef(180 - (yaw - 180), 0.0F, 1.0F, 0.0F);
            }

            this.renderAnimation(entity, this.animation.meshes.get(0), yaw, partialTicks);

            if (captured) MatrixUtils.releaseMatrix();

            GL11.glPopMatrix();
            GlStateManager.enableCull();
            GlStateManager.disableRescaleNormal();
        }
    }

    private float interpolate(float prev, float yaw, float partialTicks)
    {
        float result;

        for (result = yaw - prev; result < -180.0F; result += 360.0F)
        {}

        while (result >= 180.0F)
        {
            result -= 360.0F;
        }

        return prev + partialTicks * result;
    }

    /**
     * Render current animation
     */
    public void renderAnimation(EntityLivingBase entity, AnimationMesh mesh, float yaw, float partialTicks)
    {
        BOBJArmature original = mesh.getArmature();
        BOBJArmature armature = this.animator.useArmature(original);
        float alpha = 1;

        if (entity.isInvisible())
        {
            alpha = !entity.isInvisibleToPlayer(Minecraft.getMinecraft().player) ? 0.15F : 0;
        }

        this.setupBoneMatrices(entity, armature, yaw, partialTicks);

        for (AnimationMesh part : this.animation.meshes)
        {
            part.setCurrentArmature(armature);
            part.alpha = alpha;
            part.updateMesh();
        }

        /* Hurt colors */
        GlStateManager.enableRescaleNormal();
        boolean flag = RenderLightmap.set(entity, partialTicks);

        this.animation.render(this.userConfig.meshes);

        if (flag) RenderLightmap.unset();
        GlStateManager.disableRescaleNormal();

        this.renderItems(entity, armature);
        this.renderHead(entity, armature.bones.get(this.userConfig.head));
    }

    public void setupBoneMatrices(EntityLivingBase entity, BOBJArmature armature, float yaw, float partialTicks)
    {
        for (BOBJBone bone : armature.orderedBones)
        {
            bone.reset();
        }

        this.setupBoneTransformations(entity, armature, yaw, partialTicks);

        for (BOBJBone bone : armature.orderedBones)
        {
            armature.matrices[bone.index] = bone.compute();
        }
    }

    protected void setupBoneTransformations(EntityLivingBase entity, BOBJArmature armature, float yaw, float partialTicks)
    {
        BOBJBone head = armature.bones.get(this.userConfig.head);

        if (head != null)
        {
            float yawHead = entity.prevRotationYawHead + (entity.rotationYawHead - entity.prevRotationYawHead) * partialTicks;
            float pitch = entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks;

            yawHead = (yaw - yawHead) / 180F * (float) Math.PI;
            pitch = pitch / 180F * (float) Math.PI;

            head.rotateX = pitch;
            head.rotateY = yawHead;
        }

        if (this.animator != null)
        {
            this.animator.applyActions(armature, partialTicks);
        }
    }

    /**
     * Render head block thing
     */
    protected void renderHead(EntityLivingBase entity, BOBJBone head)
    {
        ItemStack stack = entity.getItemStackFromSlot(EntityEquipmentSlot.HEAD);

        if (!stack.isEmpty() && head != null)
        {
            Item item = stack.getItem();

            if (!(item instanceof ItemArmor) || ((ItemArmor) item).getEquipmentSlot() != EntityEquipmentSlot.HEAD)
            {
                GlStateManager.pushMatrix();
                this.setupMatrix(head);

                GlStateManager.translate(0.0F, 0.25F, 0.0F);
                GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
                GlStateManager.scale(0.625F, 0.625F, 0.625F);

                this.mc.getItemRenderer().renderItem(entity, stack, TransformType.HEAD);
                GlStateManager.popMatrix();
            }
        }
    }

    /**
     * Render hand held items and skin layer
     */
    protected void renderItems(EntityLivingBase entity, BOBJArmature armature)
    {
        if (!this.userConfig.renderHeldItems)
        {
            return;
        }

        float scale = this.userConfig.scaleItems;

        ItemStack mainItem = entity.getHeldItemMainhand();
        ItemStack offItem = entity.getHeldItemOffhand();

        if (!mainItem.isEmpty() && this.userConfig.rightHands != null)
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
     * Render an item in the hand bone 
     */
    public void renderItem(EntityLivingBase entity, ItemStack stack, BOBJArmature armature, AnimatorHeldItemConfig itemConfig, TransformType type, float scale)
    {
        boolean left = type == TransformType.THIRD_PERSON_LEFT_HAND;
        BOBJBone bone = armature.bones.get(itemConfig.boneName);

        if (bone != null)
        {
            GlStateManager.pushMatrix();

            this.setupMatrix(bone);
            GlStateManager.translate(itemConfig.x, itemConfig.y, itemConfig.z);
            GlStateManager.scale((left ? -scale : scale) * itemConfig.scaleX, scale * itemConfig.scaleY, scale * itemConfig.scaleZ);
            GlStateManager.rotate(itemConfig.rotateX, 1, 0, 0);
            GlStateManager.rotate(itemConfig.rotateY, 0, 1, 0);
            GlStateManager.rotate(itemConfig.rotateZ, 0, 0, 1);

            this.mc.getItemRenderer().renderItemSide(entity, stack, TransformType.THIRD_PERSON_RIGHT_HAND, false);
            GlStateManager.popMatrix();
        }
    }

    /**
     * Setup bone matrix 
     */
    public void setupMatrix(BOBJBone bone)
    {
        this.setupMatrix(bone.mat);
    }

    /**
     * Setup matrix 
     */
    public void setupMatrix(Matrix4f m)
    {
        buffer[0] = m.m00;
        buffer[1] = m.m10;
        buffer[2] = m.m20;
        buffer[3] = m.m30;
        buffer[4] = m.m01;
        buffer[5] = m.m11;
        buffer[6] = m.m21;
        buffer[7] = m.m31;
        buffer[8] = m.m02;
        buffer[9] = m.m12;
        buffer[10] = m.m22;
        buffer[11] = m.m32;
        buffer[12] = m.m03;
        buffer[13] = m.m13;
        buffer[14] = m.m23;
        buffer[15] = m.m33;

        matrix.clear();
        matrix.put(buffer);
        matrix.flip();

        GL11.glMultMatrix(matrix);
    }

    /**
     * Update this controller 
     */
    public void update(EntityLivingBase target)
    {
        this.fetchAnimation();

        if (this.animator != null)
        {
            this.watchConfig();
            this.animator.update(target);
        }
    }

    /**
     * This method is responsible for checking whether the config 
     * changed, and if it did changed, reload animator.
     */
    protected void watchConfig()
    {
        this.checkConfig++;

        if (this.checkConfig > 10)
        {
            this.checkConfig = 0;

            if (this.lastModified < this.config.lastModified)
            {
                this.animation = null;
                this.fetchAnimation();
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public void fetchAnimation()
    {
        if (this.animation != null)
        {
            return;
        }

        Animation animation = AnimationManager.INSTANCE.getAnimation(this.animationName);

        if (animation != null)
        {
            this.animation = animation;
            this.config = AnimationManager.INSTANCE.getConfig(animation.name);

            this.userConfig.copy(this.config.config);
            this.userConfig.fromNBT(this.userData);

            this.animator = this.factory.createAnimator(this);
            this.animator.setEmote(this.emote);
            this.lastModified = this.config.lastModified;
        }
    }
}