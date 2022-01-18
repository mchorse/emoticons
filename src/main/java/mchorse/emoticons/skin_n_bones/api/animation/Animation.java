package mchorse.emoticons.skin_n_bones.api.animation;

import mchorse.emoticons.skin_n_bones.api.animation.model.ActionConfig;
import mchorse.emoticons.skin_n_bones.api.animation.model.ActionPlayback;
import mchorse.emoticons.skin_n_bones.api.bobj.BOBJAction;
import mchorse.emoticons.skin_n_bones.api.bobj.BOBJLoader;
import mchorse.mclib.utils.resources.RLUtils;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL15;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Animation class
 * 
 * This class is responsible for managing the playback of an animation
 */
@SideOnly(Side.CLIENT)
public class Animation
{
    /**
     * This animation's name 
     */
    public String name;

    /**
     * Blockbuster OBJ data
     */
    public BOBJLoader.BOBJData data;

    /**
     * List of animated meshes that were constructed from given 
     */
    public List<AnimationMesh> meshes;

    /**
     * Local Minecraft reference 
     */
    public Minecraft mc;

    public Animation(String name, BOBJLoader.BOBJData data)
    {
        this.name = name;
        this.data = data;

        this.mc = Minecraft.getMinecraft();
        this.meshes = new ArrayList<AnimationMesh>();
    }

    /**
     * Reload this animation 
     */
    public void reload(BOBJLoader.BOBJData data)
    {
        this.data = data;
        this.delete();
        this.init();
    }

    /**
     * Create an action with default priority 
     */
    public ActionPlayback createAction(ActionPlayback old, ActionConfig config, boolean looping)
    {
        return this.createAction(old, config, looping, 1);
    }

    /**
     * Create an action playback based on given arguments. This method 
     * is used for creating actions so it was easier to tell which 
     * actions are missing. Beside that, you can pass an old action so 
     * in morph merging situation it wouldn't interrupt animation.
     */
    public ActionPlayback createAction(ActionPlayback old, ActionConfig config, boolean looping, int priority)
    {
        BOBJAction action = this.data.actions.get(config.name);

        /* If given action is missing, then omit creation of ActionPlayback */
        if (action == null)
        {
            return null;
        }

        /* If old is the same, then there is no point creating a new one */
        if (old != null && old.action == action)
        {
            old.config = config;
            old.setSpeed(1);

            return old;
        }

        ActionPlayback playback = new ActionPlayback(action, config, looping, priority);

        if (action.name.contains("ragdoll"))
        {
            playback.customArmature = this.data.armatures.get("ArmatureRagdoll");
        }

        return playback;
    }

    /**
     * Initialize the animation 
     */
    public void init()
    {
        Map<String, BOBJLoader.CompiledData> compiled = BOBJLoader.loadMeshes(this.data);

        for (Map.Entry<String, BOBJLoader.CompiledData> entry : compiled.entrySet())
        {
            String name = entry.getKey();
            BOBJLoader.CompiledData data = entry.getValue();
            AnimationMesh mesh = new AnimationMesh(this, entry.getKey(), data);

            mesh.texture = RLUtils.create("s&b", this.name + "/textures/" + name + "/default.png");
            this.meshes.add(mesh);
        }

        this.data.dispose();
    }

    /**
     * Delete resources
     */
    public void delete()
    {
        for (AnimationMesh mesh : this.meshes)
        {
            mesh.delete();
        }

        this.meshes.clear();
    }

    /**
     * Render the animation 
     */
    public void render(Map<String, AnimationMeshConfig> configs)
    {
        for (AnimationMesh mesh : this.meshes)
        {
            mesh.render(this.mc, configs == null ? null : configs.get(mesh.name));
        }

        /* Unbind the buffer. REQUIRED to avoid OpenGL crash */
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
    }
}