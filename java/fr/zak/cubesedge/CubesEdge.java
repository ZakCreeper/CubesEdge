package fr.zak.cubesedge;

import com.sun.xml.internal.stream.Entity;

import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import fr.zak.cubesedge.event.SpeedEvent;
import fr.zak.cubesedge.proxys.CommonProxy;

@Mod(modid = "cubesedge", name = "Cube's Edge", version = "Alpha 0.0.1")
public class CubesEdge{

	@Instance("cubesedge")
	public static CubesEdge cubesEdgeInstance;

	@SidedProxy(clientSide="fr.zak.cubesedge.proxys.ClientProxy", serverSide="fr.zak.cubesedge.proxys.CommonProxy")
	public static CommonProxy proxy;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event){
		proxy.registerRenderThings();
	}

	@EventHandler
	public void init(FMLInitializationEvent event){

		MinecraftForge.EVENT_BUS.register(new SpeedEvent());
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event){

	}
}
