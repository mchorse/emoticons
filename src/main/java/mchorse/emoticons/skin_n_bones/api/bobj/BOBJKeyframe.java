package mchorse.emoticons.skin_n_bones.api.bobj;

import mchorse.mclib.utils.Interpolations;
import net.minecraft.util.math.MathHelper;

/**
 * BOBJ keyframe. This class is responsible for holding data about a 
 * keyframe. 
 */
public class BOBJKeyframe
{
    public float frame;
    public float value;
    public Interpolation interpolation = Interpolation.LINEAR;

    /* For bezier interpolation */
    public float leftX;
    public float leftY;

    public float rightX;
    public float rightY;

    /**
     * Parse a keyframe from BOBJ line of tokens
     */
    public static BOBJKeyframe parse(String[] tokens)
    {
        if (tokens.length == 8)
        {
            float leftX = Float.parseFloat(tokens[4]);
            float leftY = Float.parseFloat(tokens[5]);

            float rightX = Float.parseFloat(tokens[6]);
            float rightY = Float.parseFloat(tokens[7]);

            return new BOBJKeyframe(Float.parseFloat(tokens[1]), Float.parseFloat(tokens[2]), tokens[3], leftX, leftY, rightX, rightY);
        }
        else if (tokens.length == 4)
        {
            return new BOBJKeyframe(Float.parseFloat(tokens[1]), Float.parseFloat(tokens[2]), tokens[3]);
        }
        else if (tokens.length == 3)
        {
            return new BOBJKeyframe(Float.parseFloat(tokens[1]), Float.parseFloat(tokens[2]));
        }

        return null;
    }

    /**
     * Get interpolation from string 
     */
    public static Interpolation interpolationFromString(String interp)
    {
        if (interp.equals("CONSTANT"))
        {
            return Interpolation.CONSTANT;
        }
        else if (interp.equals("BEZIER"))
        {
            return Interpolation.BEZIER;
        }

        return Interpolation.LINEAR;
    }

    public BOBJKeyframe(float frame, float value)
    {
        this.frame = frame;
        this.value = value;
    }

    public BOBJKeyframe(float frame, float value, String interp)
    {
        this(frame, value);

        this.interpolation = interpolationFromString(interp);
    }

    public BOBJKeyframe(float frame, float value, String interp, float leftX, float leftY, float rightX, float rightY)
    {
        this(frame, value, interp);

        this.leftX = leftX;
        this.leftY = leftY;
        this.rightX = rightX;
        this.rightY = rightY;
    }

    public float interpolate(float x, BOBJKeyframe next)
    {
        return this.interpolation.interpolate(this, x, next);
    }

    /**
     * Interpolations. These enums provide different interpolation types.
     */
    public static enum Interpolation
    {
        CONSTANT
        {
            @Override
            public float interpolate(BOBJKeyframe keyframe, float x, BOBJKeyframe next)
            {
                return keyframe.value;
            }
        },
        LINEAR
        {
            @Override
            public float interpolate(BOBJKeyframe keyframe, float x, BOBJKeyframe next)
            {
                return Interpolations.lerp(keyframe.value, next.value, x);
            }
        },
        BEZIER
        {
            @Override
            public float interpolate(BOBJKeyframe keyframe, float x, BOBJKeyframe next)
            {
                if (x <= 0) return keyframe.value;
                if (x >= 1) return next.value;

                /* Transform input to 0..1 */
                float w = next.frame - keyframe.frame;
                float h = next.value - keyframe.value;

                /* In case if there is no slope whatsoever */
                if (h == 0) h = 0.00001F;

                float x1 = (keyframe.rightX - keyframe.frame) / w;
                float y1 = (keyframe.rightY - keyframe.value) / h;
                float x2 = (next.leftX - keyframe.frame) / w;
                float y2 = (next.leftY - keyframe.value) / h;
                float e = 0.0005F;

                e = h == 0 ? e : Math.max(Math.min(e, 1 / h * e), 0.00001F);
                x1 = MathHelper.clamp(x1, 0, 1);
                x2 = MathHelper.clamp(x2, 0, 1);

                return Interpolations.bezier(0, y1, y2, 1, Interpolations.bezierX(x1, x2, x, e)) * h + keyframe.value;
            }
        };

        public abstract float interpolate(BOBJKeyframe keyframe, float x, BOBJKeyframe next);
    }
}