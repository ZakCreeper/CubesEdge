package fr.zak.cubesedge.proxys;

import cpw.mods.fml.common.FMLCommonHandler;
import fr.zak.cubesedge.ticks.ClientTickHandler;

public class ClientProxy extends CommonProxy{

	@Override
	public void registerRenderThings()
	{
		FMLCommonHandler.instance().bus().register(new ClientTickHandler());
	}
}
