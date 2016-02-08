package fr.zak.cubesedge.movement;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import fr.zak.cubesedge.Movement;
import fr.zak.cubesedge.Util;
import fr.zak.cubesedge.entity.EntityPlayerCustom;

public class MovementJump extends Movement {

	@Override
	public void control(EntityPlayerCustom playerCustom, EntityPlayer player, Side side) {
		int x = MathHelper.floor_double(player.posX);
		int y = MathHelper.floor_double(player.posY);
		int z = MathHelper.floor_double(player.posZ);
		int heading = MathHelper
				.floor_double((double) (player.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
		if (!player.capabilities.isFlying && !playerCustom.isSneaking) {
			if ((Util.isCube(getBlock(player.worldObj,x,
					y - 1,
					z - 1)) && heading == 2)
					|| (Util.isCube(getBlock(player.worldObj,
							x,
							y - 1,
							z + 1)
							) && heading == 0)
					|| (Util.isCube(getBlock(player.worldObj,
							x - 1,
							y - 1,
							z)
							) && heading == 1)
					|| (Util.isCube(getBlock(player.worldObj,
							x + 1,
							y - 1,
							z)
							) && heading == 3)) {
				if ((!Util.isCube(getBlock(player.worldObj,
						x,
						y,
						z - 1)
						) && heading == 2)
						|| (!Util.isCube(getBlock(player.worldObj,
								x,
								y,
								z + 1)
								) && heading == 0)
						|| (!Util.isCube(getBlock(player.worldObj,
								x - 1,
								y,
								z)
								) && heading == 1)
						|| (!Util.isCube(getBlock(player.worldObj,
								x + 1,
								y,
								z)
								) && heading == 3)) {
					if (ObfuscationReflectionHelper.getPrivateValue(
							EntityLivingBase.class, (EntityLivingBase) player,
							39/*isJumping var*/)) {
						player.motionY = 0.41999998688697815D;
					}
				}
				if ((Util.isCube(getBlock(player.worldObj,
						x,
						y,
						z - 1)
						) && heading == 2)
						|| (Util.isCube(getBlock(player.worldObj,
								x,
								y,
								z + 1)
								) && heading == 0)
						|| (Util.isCube(getBlock(player.worldObj,
								x - 1,
								y,
								z)
								) && heading == 1)
						|| (Util.isCube(getBlock(player.worldObj,
								x + 1,
								y,
								z)
								) && heading == 3)) {
					playerCustom.isJumping = 3;
					if ((Util.isCube(getBlock(player.worldObj,
							x,
							y + 1,
							z - 1)
							) && heading == 2)
							|| (Util.isCube(getBlock(player.worldObj,
									x,
									y + 1,
									z + 1)
									) && heading == 0)
							|| (Util.isCube(getBlock(player.worldObj,
									x - 1,
									y + 1,
									z)
									) && heading == 1)
							|| (Util.isCube(getBlock(player.worldObj,
									x + 1,
									y + 1,
									z)
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
						EntityLivingBase.class, (EntityLivingBase) player, 39/*isJumping var*/)) {
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
	public String getName() {
		// TODO Auto-generated method stub
		return "Jump";
	}
}
