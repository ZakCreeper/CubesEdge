package fr.zak.cubesedge;

import java.lang.reflect.Field;

public class Util {
	public static boolean obfuscation;
	public static boolean isRolling = false;
	public static boolean isGrabbing = false;
	public static boolean b = false;
	
	public static void detectObfuscation()
    {
        obfuscation = true;
        try
        {
            Field[] fields = Class.forName("net.minecraft.world.World").getDeclaredFields();
            for(Field f : fields)
            {
            	f.setAccessible(true);
            	if(f.getName().equalsIgnoreCase("loadedEntityList"))
            	{
            		obfuscation = false;
            		return;
            	}
            }
        }
        catch (Exception e)
        {
        }
    }
}
