package mchorse.emoticons.api.metamorph.emote;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import mchorse.emoticons.api.animation.model.AnimatorEmoticonsController;
import mchorse.emoticons.skin_n_bones.api.animation.model.AnimatorHeldItemConfig;
import mchorse.emoticons.skin_n_bones.api.bobj.BOBJArmature;
import mchorse.emoticons.skin_n_bones.api.bobj.BOBJBone;
import mchorse.mclib.utils.DummyEntity;
import mchorse.mclib.utils.Interpolations;
import mchorse.metamorph.api.MorphManager;
import mchorse.metamorph.api.morphs.AbstractMorph;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.JsonToNBT;

public class MorphPart
{
    public AbstractMorph morph;
    public AnimatorHeldItemConfig transform = new AnimatorHeldItemConfig("");
    public String bone = "";
    public String morphNbt = "";
    public int start;
    public int length;
    public boolean target;
    public int fadeIn;
    public int fadeOut;

    private DummyEntity entity;

    public static MorphPart fromJson(JsonObject object)
    {
        MorphPart part = new MorphPart();

        if (object.has("bone"))
        {
            part.bone = object.get("bone").getAsString();
        }

        if (object.has("morph"))
        {
            part.morphNbt = object.get("morph").getAsString();
        }

        if (object.has("start"))
        {
            part.start = object.get("start").getAsInt();
        }

        if (object.has("length"))
        {
            part.length = object.get("length").getAsInt();
        }

        if (object.has("target"))
        {
            part.target = object.get("target").getAsBoolean();
        }

        if (object.has("fade_in"))
        {
            part.fadeIn = object.get("fade_in").getAsInt();
        }

        if (object.has("fade_out"))
        {
            part.fadeOut = object.get("fade_out").getAsInt();
        }

        if (object.has("translate") && object.get("translate").isJsonArray())
        {
            JsonArray array = object.get("translate").getAsJsonArray();

            if (array.size() >= 3)
            {
                part.transform.x = array.get(0).getAsFloat();
                part.transform.y = array.get(1).getAsFloat();
                part.transform.z = array.get(2).getAsFloat();
            }
        }

        if (object.has("scale") && object.get("scale").isJsonArray())
        {
            JsonArray array = object.get("scale").getAsJsonArray();

            if (array.size() >= 3)
            {
                part.transform.scaleX = array.get(0).getAsFloat();
                part.transform.scaleY = array.get(1).getAsFloat();
                part.transform.scaleZ = array.get(2).getAsFloat();
            }
        }

        if (object.has("rotation") && object.get("rotation").isJsonArray())
        {
            JsonArray array = object.get("rotation").getAsJsonArray();

            if (array.size() >= 3)
            {
                part.transform.rotateX = array.get(0).getAsFloat();
                part.transform.rotateY = array.get(1).getAsFloat();
                part.transform.rotateZ = array.get(2).getAsFloat();
            }
        }

        return part;
    }

    public void reset()
    {
        this.morph = null;
        this.entity = null;
    }

    public EntityLivingBase getEntity(EntityLivingBase target)
    {
        if (this.target)
        {
            return target;
        }

        if (this.entity == null)
        {
            this.entity = new DummyEntity(target.world);
        }

        return this.entity;
    }

    public void update(EntityLivingBase entity, int tick)
    {
        int end = this.start + this.length;

        if (tick >= this.start && tick < end && this.morph == null)
        {
            try
            {
                this.morph = MorphManager.INSTANCE.morphFromNBT(JsonToNBT.getTagFromJson(this.morphNbt));
            }
            catch (Exception e)
            {}
        }
        else if (tick >= end || tick < this.start)
        {
            this.reset();
        }

        if (this.morph != null)
        {
            this.morph.update(this.getEntity(entity));
        }
    }

    public void render(EntityLivingBase target, BOBJArmature armature, AnimatorEmoticonsController animator, int tick, float partial)
    {
        if (this.morph == null)
        {
            return;
        }

        BOBJBone bone = armature.bones.get(this.bone);

        if (bone != null)
        {
            int s = this.start;
            int e = this.start + this.length;
            float scale = Interpolations.envelope(tick + partial, s, s + this.fadeIn, e - this.fadeOut, e);

            GlStateManager.pushMatrix();

            animator.setupMatrix(bone);

            GlStateManager.translate(this.transform.x, this.transform.y, this.transform.z);
            GlStateManager.rotate(this.transform.rotateZ, 0, 0, 1);
            GlStateManager.rotate(this.transform.rotateY, 0, 1, 0);
            GlStateManager.rotate(this.transform.rotateX, 1, 0, 0);
            GlStateManager.scale(this.transform.scaleX * scale, this.transform.scaleY * scale, this.transform.scaleZ * scale);

            this.morph.render(this.getEntity(target), 0, 0, 0, 0, partial);

            GlStateManager.popMatrix();
        }
    }
}