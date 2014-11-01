package fr.zak.cubesedge.proxys;

import cpw.mods.fml.common.FMLCommonHandler;
import fr.zak.cubesedge.Util;
import fr.zak.cubesedge.movement.client.MovementRollClient;
import fr.zak.cubesedge.movement.client.MovementSlideClient;
import fr.zak.cubesedge.movement.client.MovementSlowClient;
import fr.zak.cubesedge.movement.client.MovementSprintClient;
import fr.zak.cubesedge.movement.client.MovementTurnClient;
import fr.zak.cubesedge.ticks.ClientTickHandler;

public class ClientProxy extends CommonProxy {

	@Override
	public void registerRenderThings() {
		FMLCommonHandler.instance().bus().register(new ClientTickHandler());
		Util.registerClientMovement(new MovementSlowClient());
		Util.registerClientMovement(new MovementRollClient());
		Util.registerClientMovement(new MovementSlideClient());
		Util.registerClientMovement(new MovementSprintClient());
		Util.registerClientMovement(new MovementTurnClient());
	}
}
