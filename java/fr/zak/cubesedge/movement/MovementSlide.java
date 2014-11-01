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
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent;
import cpw.mods.fml.common.ObfuscationReflectionHelper;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import fr.zak.cubesedge.IMovement;
import fr.zak.cubesedge.Util;
import fr.zak.cubesedge.entity.EntityPlayerCustom;
import fr.zak.cubesedge.packet.PacketPlayer.CPacketPlayerAction;
import fr.zak.cubesedge.renderer.EntityRendererCustom;

public class MovementSlide extends IMovement {

	@Override
	public void control(EntityPlayerCustom playerCustom, EntityPlayer player) {
		if (!player.capabilities.isFlying) {
			if (!player.isSprinting() && playerCustom.wasSprinting) {
				if (player.isSneaking() && player.onGround
						&& !playerCustom.isRolling) {
					playerCustom.isSneaking = true;
				}
			}
			if (playerCustom.isSneaking && player.isSneaking()) {
				if (player.isCollidedHorizontally) {
					playerCustom.sneakTime = 16;
				}
				if (playerCustom.sneakTime < 16 && player.onGround) {
					player.motionX *= (0.98F * 0.91F) + 1;
					player.motionZ *= (0.98F * 0.91F) + 1;
					playerCustom.sneakTime++;
				}
			}
			if (playerCustom.isSneaking && !player.isSneaking()) {
				playerCustom.isSneaking = false;
				playerCustom.sneakTime = 0;
			}
			playerCustom.wasSprinting = player.isSprinting();
		}
		if(playerCustom.isSneaking){
			Util.forceSetSize(Entity.class, player,
					0.6F, 0.6F);
		}
		else {
			Util.forceSetSize(Entity.class, player,
					0.6F, 1.8F);
		}
	}

	@SubscribeEvent
	public void jump(LivingJumpEvent event) {
		if (event.entityLiving instanceof EntityPlayer
				&& ((EntityPlayerCustom) event.entityLiving
						.getExtendedProperties("Cube's Edge Player")).isSneaking) {
			event.entityLiving.motionY = 0;
		}
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Slide";
	}
}
