package fr.zak.cubesedge.proxys;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fr.zak.cubesedge.IMovement;
import fr.zak.cubesedge.Util;
import fr.zak.cubesedge.ticks.ClientTickHandler;

public class ClientProxy extends CommonProxy {

	@Override
	public void registerRenderThings() {
		FMLCommonHandler.instance().bus().register(new ClientTickHandler());
		for(IMovement target : Util.movements){
			if (!target.isMovementDisabled()) {
				for (Method m : target.getClass().getDeclaredMethods()) {
					if(m.isAnnotationPresent(SideOnly.class)){
						if(m.getAnnotation(SideOnly.class).value() == Side.CLIENT){
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
				for (Field f : target.getClass().getDeclaredFields()) {
					if (f.getGenericType().toString()
							.contains(KeyBinding.class.getName())) {
						f.setAccessible(true);
						try {
							ClientRegistry.registerKeyBinding((KeyBinding) f
									.get(target));
						} catch (IllegalArgumentException e) {
							e.printStackTrace();
						} catch (IllegalAccessException e) {
							e.printStackTrace();
						}
						f.setAccessible(false);
					}
				}
			}
		}
	}
}
