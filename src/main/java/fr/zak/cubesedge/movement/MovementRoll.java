package fr.zak.cubesedge.movement;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import fr.zak.cubesedge.Movement;
import fr.zak.cubesedge.Util;
import fr.zak.cubesedge.entity.EntityPlayerCustom;

public class MovementRoll extends Movement {

	@Override
	public void control(EntityPlayerCustom playerCustom, EntityPlayer player, Side side) {
//		int x = MathHelper.floor_double(player.posX);
//		int y = MathHelper.floor_double(player.posY);
//		int z = MathHelper.floor_double(player.posZ);
		if (!player.capabilities.isFlying && !playerCustom.isSneaking) {
			if (playerCustom.prevRolling && player.onGround) {
				playerCustom.isRolling = true;
			}
			if (playerCustom.isRolling) {
				player.setSprinting(false);
				KeyBinding.setKeyBindState(
						Minecraft.getMinecraft().gameSettings.keyBindForward
								.getKeyCode(), true);
				KeyBinding.setKeyBindState(
						Minecraft.getMinecraft().gameSettings.keyBindLeft
								.getKeyCode(), false);
				KeyBinding.setKeyBindState(
						Minecraft.getMinecraft().gameSettings.keyBindRight
								.getKeyCode(), false);
				KeyBinding.setKeyBindState(
						Minecraft.getMinecraft().gameSettings.keyBindBack
								.getKeyCode(), false);
				KeyBinding.setKeyBindState(
						Minecraft.getMinecraft().gameSettings.keyBindSneak
								.getKeyCode(), false);
				player.motionZ *= 1.5;
				player.motionX *= 1.5;
				if (playerCustom.rotationPitch == 0) {
					playerCustom.rotationPitch = player.rotationPitch;
				}
				playerCustom.rotationPitch += 30;
				float f2 = player.rotationPitch;
				if (Minecraft.getMinecraft().gameSettings.thirdPersonView == 0) {
					player.rotationPitch = (float) ((double) playerCustom.rotationPitch);
					player.prevRotationPitch += playerCustom.rotationPitch - f2;
				}
				if (playerCustom.rotationPitch >= 360) {
					playerCustom.prevRolling = false;
					playerCustom.isRolling = false;
					playerCustom.rotationPitch = 0;
					KeyBinding
							.setKeyBindState(
									Minecraft.getMinecraft().gameSettings.keyBindForward
											.getKeyCode(), false);
				}
			}
		}
		if(playerCustom.isRolling){
			Util.forceSetSize(Entity.class, player,
					0.6F, 0.6F);
		}
		else {
			Util.forceSetSize(Entity.class, player,
					0.6F, 1.8F);
		}
	}

	//TODO : Experimental code == bullshit but it work xD
	boolean roll;
	byte ticks;
	
	@SubscribeEvent
	public void onFall(LivingFallEvent event) {
		
		if(event.entityLiving instanceof EntityPlayer && ((EntityPlayerCustom)event.entityLiving.getExtendedProperties("Cube's Edge Player")).prevRolling){
			roll = true;
		}
		if (event.entityLiving instanceof EntityPlayer && roll) {
			ticks++;
			event.distance = 0;
		}
		if(ticks >= 2){
			ticks = 0;
			roll = false;
		}
	}

	@SubscribeEvent
	public void jump(LivingJumpEvent event) {
		if (event.entityLiving instanceof EntityPlayer
				&& ((EntityPlayerCustom)event.entityLiving.getExtendedProperties("Cube's Edge Player")).isRolling) {
			event.entityLiving.motionY = 0;
		}
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Roll";
	}
}
