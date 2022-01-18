package mchorse.emoticons.skin_n_bones.api.bobj;

import javax.vecmath.Matrix4f;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BOBJArmature
{
    /**
     * Name of this armature 
     */
    public String name;

    /**
     * Default action of this armature 
     */
    public String action = "";

    /**
     * Map of all bones in this armature 
     */
    public Map<String, BOBJBone> bones = new HashMap<String, BOBJBone>();

    /**
     * List of all bones stored in {@link #bones}, but ordered by index 
     */
    public List<BOBJBone> orderedBones = new ArrayList<BOBJBone>();

    /**
     * List of bones that have {@link BOBJBoneModifier}
     */
    public List<BOBJBone> ikBones = new ArrayList<BOBJBone>();

    /**
     * Array of matrices which are going to be used for transforming 
     * vertices.
     */
    public Matrix4f[] matrices;

    /**
     * Whether this armature was initialized already 
     */
    private boolean initialized;

    public BOBJArmature(String name)
    {
        this.name = name;
    }

    public void addBone(BOBJBone bone)
    {
        this.bones.put(bone.name, bone);
        this.orderedBones.add(bone);
    }

    /**
     * Initiate the armature. This method is responsible for connecting 
     * parent bones to their children and initializing matrix array. 
     * This method should be invoked only once.
     */
    public void initArmature()
    {
        if (!this.initialized)
        {
            List<BOBJBone> ikBones = new ArrayList<BOBJBone>();

            /* "Connect" parent bones to children bones */
            for (BOBJBone bone : this.bones.values())
            {
                if (bone.hasModifiers())
                {
                    ikBones.add(bone);
                }

                if (!bone.parent.isEmpty())
                {
                    bone.parentBone = this.bones.get(bone.parent);
                    bone.relBoneMat.set(bone.parentBone.boneMat);
                    bone.relBoneMat.invert();
                    bone.relBoneMat.mul(bone.boneMat);
                }
                else
                {
                    bone.relBoneMat.set(bone.boneMat);
                }
            }

            /* IK bones, McHorse 2022: it was never finished :( */
            if (!ikBones.isEmpty())
            {
                this.ikBones = ikBones;
            }

            /* Sort bones according to their index */
            Collections.sort(this.orderedBones, (o1, o2) -> o1.index - o2.index);

            this.matrices = new Matrix4f[this.orderedBones.size()];
            this.initialized = true;
        }
    }

    /**
     * Setup matrices  
     */
    public void setupMatrices()
    {
        for (BOBJBone bone : this.orderedBones)
        {
            this.matrices[bone.index] = bone.compute();
        }
    }

    public void copyOrder(BOBJArmature armature)
    {
        for (BOBJBone bone : armature.orderedBones)
        {
            BOBJBone thisBone = this.bones.get(bone.name);

            if (thisBone != null)
            {
                thisBone.index = bone.index;
            }
        }

        Collections.sort(this.orderedBones, (o1, o2) -> o1.index - o2.index);
    }
}