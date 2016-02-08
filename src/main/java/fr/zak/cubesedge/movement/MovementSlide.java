package fr.zak.cubesedge.movement;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import fr.zak.cubesedge.Movement;
import fr.zak.cubesedge.Util;
import fr.zak.cubesedge.entity.EntityPlayerCustom;

public class MovementSlide extends Movement {

	@Override
	public void control(EntityPlayerCustom playerCustom, EntityPlayer player, Side side) {
		int x = MathHelper.floor_double(player.posX);
		int y = MathHelper.floor_double(player.posY);
		int z = MathHelper.floor_double(player.posZ);
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
		if(playerCustom.isSneaking || (Util.isCube(getBlock(player.worldObj,
						x, y, z)) && playerCustom.wasSliding)){
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
				&& ((EntityPlayerCustom)event.entityLiving.getExtendedProperties("Cube's Edge Player")).isSneaking) {
			event.entityLiving.motionY = 0;
		}
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Slide";
	}
}
