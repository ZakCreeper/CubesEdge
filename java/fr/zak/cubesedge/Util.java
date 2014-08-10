package fr.zak.cubesedge;

import java.lang.reflect.Field;

public class Util {
	public static boolean obfuscation;

	public static boolean prevRolling = false;
	public static int rollingTime = 0;
	public static boolean isRolling = false;

	public static boolean isGrabbing = false;
	public static boolean[] grabbingDirections = {false, false, false, false}; 

	public static boolean beginingRunning = false;
	public static float tickRunningLeft = 0;
	public static float tickRunningRight = 0;
	public static boolean animRunnig = false;
	public static boolean backLeft = false;
	public static boolean backRight = false;

	public static int temps = 0;
	public static boolean ralenti = false;

	public static boolean isOnWall = false;
	public static boolean wallJump = false;
	public static boolean animRight = false;
	public static boolean animLeft = false;
	
	public static boolean isSneaking = false;
	public static int sneakTime = 0; 
	public static boolean wasSprinting = false;

	public static void detectObfuscation() {
		obfuscation = true;
		try {
			Field[] fields = Class.forName("net.minecraft.world.World")
					.getDeclaredFields();
			for (Field f : fields) {
				f.setAccessible(true);
				if (f.getName().equalsIgnoreCase("loadedEntityList")) {
					obfuscation = false;
					return;
				}
			}
		} catch (Exception e) {
		}
	}
}
