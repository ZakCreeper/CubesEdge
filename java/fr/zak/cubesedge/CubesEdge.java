package fr.zak.cubesedge;

import java.util.Iterator;

import net.minecraft.block.Block;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.relauncher.Side;
import fr.zak.cubesedge.event.ConstructEvent;
import fr.zak.cubesedge.event.PlayerFall;
import fr.zak.cubesedge.event.PlayerJump;
import fr.zak.cubesedge.event.SpeedEvent;
import fr.zak.cubesedge.packet.MessageHandlerPacketPlayer;
import fr.zak.cubesedge.packet.PacketPlayer;
import fr.zak.cubesedge.proxys.CommonProxy;

/**
 * 
 * @author Zak (alex.ulysse@gmail.com)
 * 
 */
@Mod(modid = "cubesedge", name = "Cube's Edge", version = Util.VERSION)
public class CubesEdge {

	@Instance("cubesedge")
	public static CubesEdge cubesEdgeInstance;

	@SidedProxy(clientSide = "fr.zak.cubesedge.proxys.ClientProxy", serverSide = "fr.zak.cubesedge.proxys.CommonProxy")
	public static CommonProxy proxy;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		Util.detectObfuscation();
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		proxy.registerRenderThings();
		MinecraftForge.EVENT_BUS.register(new SpeedEvent());
		MinecraftForge.EVENT_BUS.register(new PlayerFall());
		MinecraftForge.EVENT_BUS.register(new PlayerJump());
		MinecraftForge.EVENT_BUS.register(new ConstructEvent());
		Util.channel = NetworkRegistry.INSTANCE.newSimpleChannel("cubesedge");
		Util.channel.registerMessage(MessageHandlerPacketPlayer.class, PacketPlayer.class, 0, Side.SERVER);
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		Iterator<Block> i = Block.blockRegistry.iterator();
		while(i.hasNext()){
			Block block = i.next();
			if(block.getBlockBoundsMaxX() == 1.0D && block.getBlockBoundsMaxY() == 1.0D && block.getBlockBoundsMaxZ() == 1.0D && block.getBlockBoundsMinX() == 0.0D && block.getBlockBoundsMinY() == 0.0D && block.getBlockBoundsMinZ() == 0.0D){
				Util.cubes.add(block);
			}
		}
	}
}
