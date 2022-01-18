package mchorse.emoticons.skin_n_bones.api.bobj;

import javax.vecmath.Matrix4f;
import javax.vecmath.Vector3f;
import javax.vecmath.Vector4f;

/**
 * Bone IK modifier
 */
public class BOBJBoneModifier
{
    /**
     * Target bone which is used for tracking its position 
     */
    public BOBJBone target;

    /**
     * How many bones should it affect in the chain? 
     */
    public int chain = 0;

    /**
     * Should bone move to the position the target (i.e. stick)
     */
    public boolean stick;

    private Vector4f global = new Vector4f();
    private Vector4f local = new Vector4f();
    private Matrix4f inverse = new Matrix4f();

    /**
     * Construct modifier out of bone and chain length  
     */
    public BOBJBoneModifier(BOBJBone target, int chain, boolean stick)
    {
        this.target = target;
        this.chain = chain;
        this.stick = stick;
    }

    /**
     * Apply IK modifier on given bone. This method REQUIRES both given 
     * bone and target bone to have matrices already computed!
     * 
     * TODO: Fix the special case with twist 180 roll twist
     * TODO: Allow chains of more than 1 elements
     */
    public void apply(BOBJBone bone)
    {
        if (this.chain == 0 || this.target == null)
        {
            return;
        }

        /* Calculate global position of the target */
        this.global.set(0, 0, 0, 1);
        this.target.mat.transform(this.global);

        this.local.set(0, 0, 0, 1);
        bone.mat.transform(this.local);

        this.local.sub(this.global);
        float distance = this.local.length();

        /* Calculate local vector */
        this.inverse.set(bone.mat);
        this.inverse.invert();
        this.local.set(this.global);
        this.inverse.transform(this.local);

        /* Attempt doing look at */
        Vector3f forward = new Vector3f(this.local.x, this.local.y, this.local.z);
        forward.normalize();

        this.local.set(0, 0, 1, 1);
        this.target.mat.transform(this.local);

        Vector3f right = new Vector3f(0, 1, 0);
        right.normalize();
        right.cross(forward, right);
        right.normalize();
        Vector3f up = new Vector3f(0, 0, 0);
        up.cross(right, forward);
        up.normalize();

        /* Orient */
        this.inverse.setIdentity();
        this.inverse.m00 = right.x;
        this.inverse.m10 = right.y;
        this.inverse.m20 = right.z;
        this.inverse.m01 = forward.x;
        this.inverse.m11 = forward.y;
        this.inverse.m21 = forward.z;
        this.inverse.m02 = up.x;
        this.inverse.m12 = up.y;
        this.inverse.m22 = up.z;

        /* Move the bone exactly length away from the target bone */
        if (this.stick)
        {
            this.local.set(0, distance - bone.length, 0, 1);
            this.inverse.transform(this.local);
            this.inverse.m03 = this.local.x;
            this.inverse.m13 = this.local.y;
            this.inverse.m23 = this.local.z;
        }

        Matrix4f m = new Matrix4f();
        bone.mat.set(bone.relBoneMat);
        bone.applyTransformations();
        bone.mat.mul(this.inverse);

        if (bone.parentBone != null)
        {
            m = new Matrix4f(bone.parentBone.mat);
        }

        m.mul(bone.mat);
        bone.mat.set(m);
    }
}