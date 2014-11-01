package fr.zak.cubesedge.proxys;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fr.zak.cubesedge.IMovement;
import fr.zak.cubesedge.Util;
import fr.zak.cubesedge.movement.MovementGrab;
import fr.zak.cubesedge.movement.MovementJump;
import fr.zak.cubesedge.movement.MovementRoll;
import fr.zak.cubesedge.movement.MovementSlide;
import fr.zak.cubesedge.movement.MovementSlow;
import fr.zak.cubesedge.movement.MovementSprint;
import fr.zak.cubesedge.movement.MovementTurn;
import fr.zak.cubesedge.movement.MovementWallJump;
import fr.zak.cubesedge.ticks.ClientTickHandler;

public class ClientProxy extends CommonProxy {

	public static KeyBinding turn = new KeyBinding("Turn", Keyboard.KEY_APOSTROPHE,
			"Cube's Edge");
	
	@Override
	public void registerRenderThings() {
		FMLCommonHandler.instance().bus().register(new ClientTickHandler());
//		for(Object o : Util.getMovements()){
//			for (Field f : o.getClass().getDeclaredFields()) {
//				if (f.getGenericType().toString()
//						.contains(KeyBinding.class.getName())) {
//					f.setAccessible(true);
//					try {
//						ClientRegistry.registerKeyBinding((KeyBinding)f.get(o));
//					} catch (IllegalArgumentException e) {
//						e.printStackTrace();
//					} catch (IllegalAccessException e) {
//						e.printStackTrace();
//					}
//					f.setAccessible(false);
//				}
//			}
//		}
		ClientRegistry.registerKeyBinding(turn);
		for(Object o : Util.clientEvFML){
			FMLCommonHandler.instance().bus().register(o);
		}
		for(Object o : Util.clientEvMF){
			MinecraftForge.EVENT_BUS.register(o);
		}
	}
}
