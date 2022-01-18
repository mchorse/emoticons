package mchorse.emoticons.skin_n_bones.api.bobj;

import java.util.ArrayList;
import java.util.List;

public class BOBJGroup
{
    public String name;
    public List<BOBJChannel> channels = new ArrayList<BOBJChannel>();

    public BOBJGroup(String name)
    {
        this.name = name;
    }

    public void apply(BOBJBone bone, float frame)
    {
        for (BOBJChannel channel : this.channels)
        {
            channel.apply(bone, frame);
        }
    }

    public void applyInterpolate(BOBJBone bone, float frame, float x)
    {
        for (BOBJChannel channel : this.channels)
        {
            channel.applyInterpolate(bone, frame, x);
        }
    }

    public int getDuration()
    {
        int max = 0;

        for (BOBJChannel channel : this.channels)
        {
            int size = channel.keyframes.size();

            if (size > 0)
            {
                max = Math.max(max, (int) channel.keyframes.get(size - 1).frame);
            }
        }

        return max;
    }
}