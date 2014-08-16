package fr.zak.cubesedge;

import java.io.File;
import java.util.HashSet;
import java.util.Iterator;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.ObfuscationReflectionHelper;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.relauncher.Side;
import fr.zak.cubesedge.event.ConstructEvent;
import fr.zak.cubesedge.movement.MovementGrab;
import fr.zak.cubesedge.movement.MovementJump;
import fr.zak.cubesedge.movement.MovementRoll;
import fr.zak.cubesedge.movement.MovementSlide;
import fr.zak.cubesedge.movement.MovementSlow;
import fr.zak.cubesedge.movement.MovementSprint;
import fr.zak.cubesedge.movement.MovementTurn;
import fr.zak.cubesedge.movement.MovementWallJump;
import fr.zak.cubesedge.packet.PacketPlayer;
import fr.zak.cubesedge.proxys.CommonProxy;

/**
 * 
 * @author Zak (alex.ulysse@gmail.com)
 * 
 */
@Mod(modid = "cubesedge", name = "Cube's Edge", version = Util.VERSION, guiFactory = "fr.zak.cubesedge.GuiFactory")
public class CubesEdge {

	public boolean isMovementDisabled = false;
	
	@SidedProxy(clientSide = "fr.zak.cubesedge.proxys.ClientProxy", serverSide = "fr.zak.cubesedge.proxys.CommonProxy")
	public static CommonProxy proxy;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		Util.cfg = new Configuration(new File(Loader.instance().getConfigDir(), "cube's edge.cfg"));
		Util.cfg.load();
		Util.detectObfuscation();
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		Util.registerMovement(new MovementTurn());
		Util.registerMovement(new MovementRoll());
		Util.registerMovement(new MovementGrab());
		Util.registerMovement(new MovementWallJump());
		Util.registerMovement(new MovementJump());
		Util.registerMovement(new MovementSlide());
		Util.registerMovement(new MovementSlow());
		Util.registerMovement(new MovementSprint());
		proxy.registerRenderThings();
		MinecraftForge.EVENT_BUS.register(new ConstructEvent());
		Util.channel = NetworkRegistry.INSTANCE.newSimpleChannel("cubesedge");
		Util.channel.registerMessage(PacketPlayer.CPacketPlayerSneak.Handler.class, PacketPlayer.CPacketPlayerSneak.class, 0, Side.SERVER);
		Util.channel.registerMessage(PacketPlayer.CPacketPlayerRoll.Handler.class, PacketPlayer.CPacketPlayerRoll.class, 1, Side.SERVER);
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
