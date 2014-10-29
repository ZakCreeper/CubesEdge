package fr.zak.cubesedge;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;

public class Util {

	public static List<IMovement> movements = new ArrayList<IMovement>();

	public static Configuration cfg;

	public static boolean obfuscation;

	public static SimpleNetworkWrapper channel;

	public static final String VERSION = "Alpha 0.1.18";

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

	public static void forceSetSize(Class clz, Entity ent, float width,
			float height) {
		try {
			Method m = clz.getDeclaredMethod(Util.obfuscation ? "func_70105_a"
					: "setSize", float.class, float.class);
			m.setAccessible(true);
			m.invoke(ent, width, height);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}
	}

	public static void registerMovement(IMovement target) {
			Property prop = cfg.get("movements", target.getName(), true);
			if (!prop.getBoolean(true)) {
				target.disable();
			}
			movements.add(target);
	}

	public static Object[] getMovements() {
		return movements.toArray();
	}
	
	public static boolean isCube(Block b){
		if(cubes.contains(b)){
			return true;
		}
		return false;
	}
}
