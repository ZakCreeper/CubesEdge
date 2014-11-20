package fr.zak.cubesedge.proxys;

import cpw.mods.fml.common.FMLCommonHandler;
import fr.zak.cubesedge.Util;
import fr.zak.cubesedge.movement.client.MovementRollClient;
import fr.zak.cubesedge.movement.client.MovementSlideClient;
import fr.zak.cubesedge.movement.client.MovementSlowClient;
import fr.zak.cubesedge.movement.client.MovementSprintClient;
import fr.zak.cubesedge.movement.client.MovementTurnClient;
import fr.zak.cubesedge.ticks.RenderTickHandler;

public class ClientProxy extends CommonProxy {

	@Override
	public void registerRenderThings() {
		FMLCommonHandler.instance().bus().register(new RenderTickHandler());
		Util.registerClientMovement(new MovementSlowClient());
		Util.registerClientMovement(new MovementRollClient());
		Util.registerClientMovement(new MovementSlideClient());
		Util.registerClientMovement(new MovementSprintClient());
		Util.registerClientMovement(new MovementTurnClient());
	}
}
