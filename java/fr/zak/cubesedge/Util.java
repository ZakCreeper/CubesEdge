package fr.zak.cubesedge;

import net.minecraft.entity.Entity;

public class Util {
	public static boolean obfuscated = Entity.class.getDeclaredMethods()[3].getName().equals("setSize") ? false : true;
	public static boolean isRolling = false;
	public static boolean isGrabbing = false;
}
