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
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;

public class Util {

	private static List<Movement> movements = new ArrayList<Movement>();
	private static List<MovementClient> clientsMovements = new ArrayList<MovementClient>();

	public static Configuration cfg;

	public static boolean obfuscation;

	public static SimpleNetworkWrapper channel;

	public static final String VERSION = "Alpha 0.2.0";

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

	public static void registerMovement(Movement target) {
		Property prop = cfg.get("movements", target.getName(), true);
		if (!prop.getBoolean(true)) {
			target.disable();
		}
		movements.add(target);
		if (!target.isMovementDisabled()) {
			for (Method m : target.getClass().getDeclaredMethods()) {
				if (m.isAnnotationPresent(SubscribeEvent.class)) {
					if (m.getParameterTypes()[0].getName().contains("cpw")) {
						FMLCommonHandler.instance().bus().register(target);
					} else if (m.getParameterTypes()[0].getName().contains(
							"minecraftforge")) {
						MinecraftForge.EVENT_BUS.register(target);
					}
				}
			}
		}
	}

	public static void registerClientMovement(MovementClient target) {
		Property prop = cfg.get("cmimovements", target.getName(), true);
		//		if (!prop.getBoolean(true)) {
		//			target.disable();
		//		}
		clientsMovements.add(target);
		//		if (!target.isMovementDisabled()) {
		for (Method m : target.getClass().getDeclaredMethods()) {
			if (m.isAnnotationPresent(SubscribeEvent.class)) {
				if (m.getParameterTypes()[0].getName().contains("cpw")) {
					FMLCommonHandler.instance().bus().register(target);
				} else if (m.getParameterTypes()[0].getName().contains(
						"minecraftforge")) {
					MinecraftForge.EVENT_BUS.register(target);
				}
			}
		}
		for (Field f : target.getClass().getDeclaredFields()) {
			if (f.getGenericType().toString()
					.contains(KeyBinding.class.getName())) {
				f.setAccessible(true);
				try {
					ClientRegistry.registerKeyBinding((KeyBinding)f.get(target));
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
				f.setAccessible(false);
			}
		}
		//		}
	}

	public static Object[] getMovements() {
		return movements.toArray();
	}
	
	public static Object[] getClientsMovements() {
		return clientsMovements.toArray();
	}

	public static boolean isCube(Block b){
		if(cubes.contains(b)){
			return true;
		}
		return false;
	}
}
