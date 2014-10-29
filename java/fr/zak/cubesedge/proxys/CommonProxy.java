package fr.zak.cubesedge.proxys;

import java.lang.reflect.Method;

import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fr.zak.cubesedge.IMovement;
import fr.zak.cubesedge.Util;

public class CommonProxy {

	public void registerRenderThings() {
		for(IMovement target : Util.movements){
			if (!target.isMovementDisabled()) {
				for (Method m : target.getClass().getDeclaredMethods()) {
					if(!m.isAnnotationPresent(SideOnly.class)){
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
		}
	}
}
