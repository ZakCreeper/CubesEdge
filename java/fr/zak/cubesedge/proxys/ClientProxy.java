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

	@Override
	public void registerRenderThings() {
		FMLCommonHandler.instance().bus().register(new ClientTickHandler());
		Util.registerMovement(new MovementTurn());
		Util.registerMovement(new MovementRoll());
		Util.registerMovement(new MovementGrab());
		Util.registerMovement(new MovementWallJump());
		Util.registerMovement(new MovementJump());
		Util.registerMovement(new MovementSlide());
		Util.registerMovement(new MovementSlow());
		Util.registerMovement(new MovementSprint());
	}
}
