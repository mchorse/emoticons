package mchorse.emoticons.skin_n_bones.api.bobj;

import java.util.ArrayList;
import java.util.List;

public class BOBJChannel
{
    public String path;
    public int index;
    public List<BOBJKeyframe> keyframes = new ArrayList<BOBJKeyframe>();

    public BOBJChannel(String path, int index)
    {
        this.path = path;
        this.index = index;
    }

    public float calculate(float frame)
    {
        int c = this.keyframes.size();

        if (c <= 0)
        {
            return 0;
        }

        if (c == 1)
        {
            return this.keyframes.get(0).value;
        }

        BOBJKeyframe keyframe = this.keyframes.get(0);

        if (keyframe.frame > frame)
        {
            return keyframe.value;
        }

        for (int i = 0; i < c; i++)
        {
            keyframe = this.keyframes.get(i);

            if (keyframe.frame > frame && i != 0)
            {
                BOBJKeyframe prev = this.keyframes.get(i - 1);

                float x = (frame - prev.frame) / (keyframe.frame - prev.frame);

                return prev.interpolate(x, keyframe);
            }
        }

        return keyframe.value;
    }

    public BOBJKeyframe get(float frame, boolean next)
    {
        int c = this.keyframes.size();

        if (c == 0)
        {
            return null;
        }

        if (c == 1)
        {
            return this.keyframes.get(0);
        }

        BOBJKeyframe keyframe = null;

        for (int i = 0; i < c; i++)
        {
            keyframe = this.keyframes.get(i);

            if (keyframe.frame > frame && i != 0)
            {
                return next ? keyframe : this.keyframes.get(i - 1);
            }
        }

        return keyframe;
    }

    public void apply(BOBJBone bone, float frame)
    {
        if (this.path.equals("location"))
        {
            if (this.index == 0) bone.x = this.calculate(frame);
            else if (this.index == 1) bone.y = this.calculate(frame);
            else if (this.index == 2) bone.z = this.calculate(frame);
        }
        else if (this.path.equals("rotation"))
        {
            if (this.index == 0) bone.rotateX = this.calculate(frame);
            else if (this.index == 1) bone.rotateY = this.calculate(frame);
            else if (this.index == 2) bone.rotateZ = this.calculate(frame);
        }
        else if (this.path.equals("scale"))
        {
            if (this.index == 0) bone.scaleX = this.calculate(frame);
            else if (this.index == 1) bone.scaleY = this.calculate(frame);
            else if (this.index == 2) bone.scaleZ = this.calculate(frame);
        }
    }

    public void applyInterpolate(BOBJBone bone, float frame, float x)
    {
        float value = this.calculate(frame);

        if (this.path.equals("location"))
        {
            if (this.index == 0) bone.x = value + (bone.x - value) * x;
            else if (this.index == 1) bone.y = value + (bone.y - value) * x;
            else if (this.index == 2) bone.z = value + (bone.z - value) * x;
        }
        else if (this.path.equals("rotation"))
        {
            if (this.index == 0) bone.rotateX = value + (bone.rotateX - value) * x;
            else if (this.index == 1) bone.rotateY = value + (bone.rotateY - value) * x;
            else if (this.index == 2) bone.rotateZ = value + (bone.rotateZ - value) * x;
        }
        else if (this.path.equals("scale"))
        {
            if (this.index == 0) bone.scaleX = value + (bone.scaleX - value) * x;
            else if (this.index == 1) bone.scaleY = value + (bone.scaleY - value) * x;
            else if (this.index == 2) bone.scaleZ = value + (bone.scaleZ - value) * x;
        }
    }
}