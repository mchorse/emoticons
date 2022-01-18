package mchorse.emoticons.utils;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import java.lang.reflect.Method;

/**
 * Little compatibility layer for PlayerRevive mod by CreativeMD, specifically
 * lying on the ground when the player is bleeding...
 *
 * TODO: make sure this code is getting called when updating S&B core code
 *
 * @author CreativeMD
 */
public class PlayerReviveUtils
{
    private static Method isPlayerBleedingMethod = loadPlayerReviveMethod();

    private static Method loadPlayerReviveMethod()
    {
        try
        {
            return ReflectionHelper.findMethod(Class.forName("com.creativemd.playerrevive.server.PlayerReviveServer"), "isPlayerBleeding", "isPlayerBleeding", EntityPlayer.class);
        }
        catch (ClassNotFoundException | ReflectionHelper.UnableToFindMethodException e)
        {
            return null;
        }
    }

    public static boolean isPlayerBleeding(EntityLivingBase target)
    {
        if (target instanceof EntityPlayer && isPlayerBleedingMethod != null)
        {
            try
            {
                return (boolean) isPlayerBleedingMethod.invoke(null, target);
            }
            catch (Exception e)
            {}
        }

        return false;
    }
}