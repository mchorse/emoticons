package mchorse.emoticons.utils;

public class Time
{
    public static int toTicks(int frames30)
    {
        return (int) Math.floor(frames30 / 30F * 20F);
    }
}