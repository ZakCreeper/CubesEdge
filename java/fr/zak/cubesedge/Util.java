package fr.zak.cubesedge;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;

public class Util {
	
	public static boolean obfuscation;
	
	public static SimpleNetworkWrapper channel;
	
	public static final String VERSION = "Alpha 0.1.9";
	
	public static List<Block> cubes = new ArrayList<Block>();
	
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
