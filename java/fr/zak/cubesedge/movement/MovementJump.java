package fr.zak.cubesedge.movement;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent;
import cpw.mods.fml.common.ObfuscationReflectionHelper;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import fr.zak.cubesedge.Movement;
import fr.zak.cubesedge.MovementVar;
import fr.zak.cubesedge.Util;
import fr.zak.cubesedge.entity.EntityPlayerCustom;

@Movement("Jump")
public class MovementJump extends MovementVar {

	@Override
	public void control(EntityPlayerCustom playerCustom, EntityPlayer player) {
		int heading = MathHelper
				.floor_double((double) (player.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
		if (!player.capabilities.isFlying && !playerCustom.isSneaking) {
			if ((Util.isCube(player.worldObj.getBlock(MathHelper.floor_double(player.posX),
					MathHelper.floor_double(player.posY) - 1,
					MathHelper.floor_double(player.posZ) - 1)) && heading == 2)
					|| (Util.isCube(player.worldObj.getBlock(
							MathHelper.floor_double(player.posX),
							MathHelper.floor_double(player.posY) - 1,
							MathHelper.floor_double(player.posZ) + 1)
							) && heading == 0)
					|| (Util.isCube(player.worldObj.getBlock(
							MathHelper.floor_double(player.posX) - 1,
							MathHelper.floor_double(player.posY) - 1,
							MathHelper.floor_double(player.posZ))
							) && heading == 1)
					|| (Util.isCube(player.worldObj.getBlock(
							MathHelper.floor_double(player.posX) + 1,
							MathHelper.floor_double(player.posY) - 1,
							MathHelper.floor_double(player.posZ))
							) && heading == 3)) {
				if ((!Util.isCube(player.worldObj.getBlock(
						MathHelper.floor_double(player.posX),
						MathHelper.floor_double(player.posY),
						MathHelper.floor_double(player.posZ) - 1)
						) && heading == 2)
						|| (!Util.isCube(player.worldObj.getBlock(
								MathHelper.floor_double(player.posX),
								MathHelper.floor_double(player.posY),
								MathHelper.floor_double(player.posZ) + 1)
								) && heading == 0)
						|| (!Util.isCube(player.worldObj.getBlock(
								MathHelper.floor_double(player.posX) - 1,
								MathHelper.floor_double(player.posY),
								MathHelper.floor_double(player.posZ))
								) && heading == 1)
						|| (!Util.isCube(player.worldObj.getBlock(
								MathHelper.floor_double(player.posX) + 1,
								MathHelper.floor_double(player.posY),
								MathHelper.floor_double(player.posZ))
								) && heading == 3)) {
					if (ObfuscationReflectionHelper.getPrivateValue(
							EntityLivingBase.class, (EntityLivingBase) player,
							41)) {
						player.motionY = 0.41999998688697815D;
					}
				}
				if ((Util.isCube(player.worldObj.getBlock(
						MathHelper.floor_double(player.posX),
						MathHelper.floor_double(player.posY),
						MathHelper.floor_double(player.posZ) - 1)
						) && heading == 2)
						|| (Util.isCube(player.worldObj.getBlock(
								MathHelper.floor_double(player.posX),
								MathHelper.floor_double(player.posY),
								MathHelper.floor_double(player.posZ) + 1)
								) && heading == 0)
						|| (Util.isCube(player.worldObj.getBlock(
								MathHelper.floor_double(player.posX) - 1,
								MathHelper.floor_double(player.posY),
								MathHelper.floor_double(player.posZ))
								) && heading == 1)
						|| (Util.isCube(player.worldObj.getBlock(
								MathHelper.floor_double(player.posX) + 1,
								MathHelper.floor_double(player.posY),
								MathHelper.floor_double(player.posZ))
								) && heading == 3)) {
					playerCustom.isJumping = 3;
					if ((Util.isCube(player.worldObj.getBlock(
							MathHelper.floor_double(player.posX),
							MathHelper.floor_double(player.posY) + 1,
							MathHelper.floor_double(player.posZ) - 1)
							) && heading == 2)
							|| (Util.isCube(player.worldObj.getBlock(
									MathHelper.floor_double(player.posX),
									MathHelper.floor_double(player.posY) + 1,
									MathHelper.floor_double(player.posZ) + 1)
									) && heading == 0)
							|| (Util.isCube(player.worldObj.getBlock(
									MathHelper.floor_double(player.posX) - 1,
									MathHelper.floor_double(player.posY) + 1,
									MathHelper.floor_double(player.posZ))
									) && heading == 1)
							|| (Util.isCube(player.worldObj.getBlock(
									MathHelper.floor_double(player.posX) + 1,
									MathHelper.floor_double(player.posY) + 1,
									MathHelper.floor_double(player.posZ))
									) && heading == 3)) {
						playerCustom.isJumping = 4;
					}
				}
			}

			if (player.fallDistance == 0 && !player.onGround) {
				if (playerCustom.isJumping == 3) {
					player.motionY *= 0.75D;
				}
				if (playerCustom.isJumping == 4) {
					if (playerCustom.jumpTime < 1) {
						player.motionY *= 1.25D;
						playerCustom.jumpTime++;
					}
				}
			}
			if (player.onGround || playerCustom.isTurning
					|| playerCustom.isGrabbing) {
				if (!(Boolean) ObfuscationReflectionHelper.getPrivateValue(
						EntityLivingBase.class, (EntityLivingBase) player, 41)) {
					playerCustom.isJumping = 0;
				}
				playerCustom.jumpTime = 0;
			}
		}
	}

	@SubscribeEvent
	public void jump(LivingJumpEvent event) {
		// if(event.entityLiving instanceof EntityPlayer &&
		// ((EntityPlayerCustom)event.entityLiving.getExtendedProperties("Cube's Edge Player")).isJumping
		// != 0){
		// event.entityLiving.motionY = 0;
		// }
	}

	@Override
	public void renderTick(EntityPlayerCustom playerCustom) {

	}
}
