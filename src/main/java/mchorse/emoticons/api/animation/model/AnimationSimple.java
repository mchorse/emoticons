package mchorse.emoticons.api.animation.model;

import mchorse.emoticons.skin_n_bones.api.animation.Animation;
import mchorse.emoticons.skin_n_bones.api.animation.AnimationMesh;
import mchorse.emoticons.skin_n_bones.api.bobj.BOBJLoader;
import mchorse.mclib.utils.resources.RLUtils;

import java.util.Map;

public class AnimationSimple extends Animation
{
    public AnimationSimple(String name, BOBJLoader.BOBJData data)
    {
        super(name, data);
    }

    /**
     * Initialize the animation
     */
    @Override
    public void init()
    {
        Map<String, BOBJLoader.CompiledData> compiled = BOBJLoader.loadMeshes(this.data);

        for (Map.Entry<String, BOBJLoader.CompiledData> entry : compiled.entrySet())
        {
            String name = entry.getKey();
            BOBJLoader.CompiledData data = entry.getValue();
            AnimationMesh mesh = name.equals("body")
                ? new AnimationSimpleMesh(this, entry.getKey(), data)
                : new AnimationMesh(this, entry.getKey(), data);

            mesh.texture = RLUtils.create("s&b", this.name + "/textures/" + name + "/default.png");
            this.meshes.add(mesh);
        }

        this.data.dispose();
    }
}
