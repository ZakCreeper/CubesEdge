package fr.zak.cubesedge.movement;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.MathHelper;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent;
import cpw.mods.fml.common.ObfuscationReflectionHelper;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import fr.zak.cubesedge.Movement;
import fr.zak.cubesedge.MovementVar;
import fr.zak.cubesedge.Util;
import fr.zak.cubesedge.entity.EntityPlayerCustom;
import fr.zak.cubesedge.packet.PacketPlayer;
import fr.zak.cubesedge.renderer.EntityRendererCustom;

@Movement("Roll")
public class MovementRoll extends MovementVar {

	private EntityRenderer renderer, prevRenderer;
	
	public void control(EntityPlayerCustom playerCustom, EntityPlayer player, int heading){
		if(!player.capabilities.isFlying && !playerCustom.isSneaking){
			if(player.fallDistance > 3.0F && player.fallDistance < 15F){
				if (player.worldObj.getBlock(MathHelper.floor_double(player.posX), MathHelper.floor_double(player.posY) - 3, MathHelper.floor_double(player.posZ)).isNormalCube() || player.worldObj.getBlock(MathHelper.floor_double(player.posX), MathHelper.floor_double(player.posY) - 4, MathHelper.floor_double(player.posZ)).isNormalCube()){
					if(player.isSneaking()){
						playerCustom.prevRolling = true;
					}
				}
			}
			if(playerCustom.prevRolling && player.onGround){
				playerCustom.isRolling = true;
				Util.channel.sendToServer(new PacketPlayer.CPacketPlayerRoll(true));
			}
			if(playerCustom.isRolling){
				player.setSprinting(false);
				KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindForward.getKeyCode(), true);
				KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindLeft.getKeyCode(), false);
				KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindRight.getKeyCode(), false);
				KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindBack.getKeyCode(), false);
				KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindSneak.getKeyCode(), false);
				player.motionZ *= 0.9;
				player.motionX *= 0.9;
				float f2 = player.rotationPitch;
				player.rotationPitch = (float)((double)player.rotationPitch + 30);
				player.prevRotationPitch += player.rotationPitch - f2;
				if(player.rotationPitch >= 360){
					playerCustom.prevRolling = false;
					playerCustom.isRolling = false;
					Util.channel.sendToServer(new PacketPlayer.CPacketPlayerRoll(false));
					KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindForward.getKeyCode(), false);
				}
			}
		}
	}
	
	public void renderTick(EntityPlayerCustom playerCustom){
		if(playerCustom.isRolling || (Minecraft.getMinecraft().theWorld.getBlock(MathHelper.floor_double(Minecraft.getMinecraft().thePlayer.posX), MathHelper.floor_double(Minecraft.getMinecraft().thePlayer.posY), MathHelper.floor_double(Minecraft.getMinecraft().thePlayer.posZ)).isNormalCube() && playerCustom.wasRolling)){
			int x1 = MathHelper.floor_double(Minecraft.getMinecraft().thePlayer.posX);
			int y1 = MathHelper.floor_double(Minecraft.getMinecraft().thePlayer.posY);
			int z1 = MathHelper.floor_double(Minecraft.getMinecraft().thePlayer.posZ);
			ExtendedBlockStorage ebs = ((ExtendedBlockStorage[])ObfuscationReflectionHelper.getPrivateValue(Chunk.class, Minecraft.getMinecraft().thePlayer.worldObj.getChunkFromBlockCoords(x1, z1), 2))[y1 >> 4];
			if(ebs.getExtSkylightValue((x1 & 15), y1 & 15, (z1 & 15)) == 0){
				ebs.setExtSkylightValue((x1 & 15), y1 & 15, (z1 & 15), playerCustom.lastLightValue);
			}
			playerCustom.lastLightValue = (byte) ebs.getExtSkylightValue((x1 & 15), y1 & 15, (z1 & 15));
			KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindSneak.getKeyCode(), true);
			if (renderer == null) {
				renderer = new EntityRendererCustom(Minecraft.getMinecraft());
			}
			System.out.println("nlk");
			if (Minecraft.getMinecraft().entityRenderer != renderer) {
				// be sure to store the previous renderer
				prevRenderer = Minecraft.getMinecraft().entityRenderer;
				Minecraft.getMinecraft().entityRenderer = renderer;
			}
		} else if (prevRenderer != null && Minecraft.getMinecraft().entityRenderer != prevRenderer && Minecraft.getMinecraft().theWorld.getBlock(MathHelper.floor_double(Minecraft.getMinecraft().thePlayer.posX), MathHelper.floor_double(Minecraft.getMinecraft().thePlayer.posY), MathHelper.floor_double(Minecraft.getMinecraft().thePlayer.posZ)) == Blocks.air) {
			System.out.println("fesf");
			// reset the renderer
			KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindSneak.getKeyCode(), false);
			Minecraft.getMinecraft().entityRenderer = prevRenderer;
			playerCustom.wasRolling = false;
			Util.forceSetSize(Entity.class, Minecraft.getMinecraft().thePlayer, 0.6F, 1.8F);
		}
		if(!playerCustom.wasRolling){
			playerCustom.wasRolling = playerCustom.isRolling;
		}
	}
	
	@SubscribeEvent
	public void onFall(LivingFallEvent event){
		if(event.entityLiving instanceof EntityPlayer && ((EntityPlayerCustom)event.entityLiving.getExtendedProperties("Cube's Edge Player")).isRolling){
			event.distance = 0;
		}
	}
	
	@SubscribeEvent
	public void jump(LivingJumpEvent event){
		if(event.entityLiving instanceof EntityPlayer && ((EntityPlayerCustom)event.entityLiving.getExtendedProperties("Cube's Edge Player")).isRolling){
			event.entityLiving.motionY = 0;
		}
	}
	
	@SubscribeEvent
	public void onClick(MouseEvent event){
		if(((EntityPlayerCustom)Minecraft.getMinecraft().thePlayer.getExtendedProperties("Cube's Edge Player")).isRolling){
			event.setCanceled(true);
		}
	}
}
