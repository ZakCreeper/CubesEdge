package fr.zak.cubesedge.proxys;

import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.FMLCommonHandler;
import fr.zak.cubesedge.event.GuiInGameEvent;
import fr.zak.cubesedge.event.KeyHandler;
import fr.zak.cubesedge.event.RenderHandEventCustom;
import fr.zak.cubesedge.event.RenderPlayerEventCustom;
import fr.zak.cubesedge.ticks.ClientTickHandler;

public class ClientProxy extends CommonProxy{

	@Override
	public void registerRenderThings()
	{
		MinecraftForge.EVENT_BUS.register(new GuiInGameEvent());
		FMLCommonHandler.instance().bus().register(new ClientTickHandler());
		MinecraftForge.EVENT_BUS.register(new RenderPlayerEventCustom());
		FMLCommonHandler.instance().bus().register(new KeyHandler());
		MinecraftForge.EVENT_BUS.register(new RenderHandEventCustom());
	}
}
