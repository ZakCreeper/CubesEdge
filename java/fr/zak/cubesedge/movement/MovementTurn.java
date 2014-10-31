package fr.zak.cubesedge.movement;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;

import org.lwjgl.input.Keyboard;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent.KeyInputEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fr.zak.cubesedge.IMovement;
import fr.zak.cubesedge.Util;
import fr.zak.cubesedge.entity.EntityPlayerCustom;

public class MovementTurn extends IMovement {

	@Override
	public void control(EntityPlayerCustom playerCustom, EntityPlayer player, Side side) {
		int heading = MathHelper
				.floor_double((double) (player.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
		if (!player.capabilities.isFlying && !playerCustom.isSneaking) {
			if (playerCustom.isTurning) {
				if (!playerCustom.isOnWall) {
					float yaw = MathHelper
							.wrapAngleTo180_float(player.rotationYaw);
					player.rotationYaw = yaw - 180;
					playerCustom.isTurning = false;
					if (playerCustom.isJumping != 0
							&& playerCustom.turningTime == 0
							&& !playerCustom.isTurningOnWall) {
						playerCustom.isTurningOnWall = true;
					}
				} else {
					if (playerCustom.turningTime == 0
							&& !playerCustom.isTurningOnWall) {
						float yaw = MathHelper
								.wrapAngleTo180_float(player.rotationYaw);
						if ((Util.isCube(player.worldObj.getBlock(
								MathHelper.floor_double(player.posX) + 1,
								MathHelper.floor_double(player.posY),
								MathHelper.floor_double(player.posZ))
								) && heading == 0)
								|| (Util.isCube(player.worldObj
										.getBlock(
												MathHelper
														.floor_double(player.posX),
												MathHelper
														.floor_double(player.posY),
												MathHelper
														.floor_double(player.posZ) + 1)
										) && heading == 1)
								|| (Util.isCube(player.worldObj
										.getBlock(
												MathHelper
														.floor_double(player.posX) - 1,
												MathHelper
														.floor_double(player.posY),
												MathHelper
														.floor_double(player.posZ))
										) && heading == 2)
								|| (Util.isCube(player.worldObj
										.getBlock(
												MathHelper
														.floor_double(player.posX),
												MathHelper
														.floor_double(player.posY),
												MathHelper
														.floor_double(player.posZ) - 1)
										) && heading == 3)) {
							player.rotationYaw = yaw - 90;
						} else if ((Util.isCube(player.worldObj.getBlock(
								MathHelper.floor_double(player.posX) - 1,
								MathHelper.floor_double(player.posY),
								MathHelper.floor_double(player.posZ))
								) && heading == 0)
								|| (Util.isCube(player.worldObj
										.getBlock(
												MathHelper
														.floor_double(player.posX),
												MathHelper
														.floor_double(player.posY),
												MathHelper
														.floor_double(player.posZ) - 1)
										) && heading == 1)
								|| (Util.isCube(player.worldObj
										.getBlock(
												MathHelper
														.floor_double(player.posX) + 1,
												MathHelper
														.floor_double(player.posY),
												MathHelper
														.floor_double(player.posZ))
										) && heading == 2)
								|| (Util.isCube(player.worldObj
										.getBlock(
												MathHelper
														.floor_double(player.posX),
												MathHelper
														.floor_double(player.posY),
												MathHelper
														.floor_double(player.posZ) + 1)
										) && heading == 3)) {
							player.rotationYaw = yaw + 90;
						}
						playerCustom.isTurningOnWall = true;
					}
				}
			}
			if (playerCustom.isTurningOnWall) {
				if (playerCustom.turningTime < 10) {
					playerCustom.turningTime++;
				}
				if (playerCustom.turningTime == 10) {
					playerCustom.isTurning = false;
					playerCustom.turningTime = 0;
					playerCustom.isTurningOnWall = false;
				}
				if ((Util.isCube(player.worldObj.getBlock(
						MathHelper.floor_double(player.posX),
						MathHelper.floor_double(player.posY),
						MathHelper.floor_double(player.posZ) - 1)
						) && heading == 0)
						|| (Util.isCube(player.worldObj.getBlock(
								MathHelper.floor_double(player.posX) + 1,
								MathHelper.floor_double(player.posY),
								MathHelper.floor_double(player.posZ))
								) && heading == 1)
						|| (Util.isCube(player.worldObj.getBlock(
								MathHelper.floor_double(player.posX),
								MathHelper.floor_double(player.posY),
								MathHelper.floor_double(player.posZ) + 1)
								) && heading == 2)
						|| (Util.isCube(player.worldObj.getBlock(
								MathHelper.floor_double(player.posX) - 1,
								MathHelper.floor_double(player.posY),
								MathHelper.floor_double(player.posZ))
								) && heading == 3)) {
					player.motionZ *= 0.95D;
					player.motionX *= 0.95D;
					player.motionY *= 0.75D;
					if (player instanceof EntityPlayerSP) {
						if (((EntityPlayerSP) player).movementInput.jump
								&& !playerCustom.isJumpingOnWall) {
							if (heading == 0) {
								player.motionZ = 0.7F;
							}
							if (heading == 1) {
								player.motionX = -0.7F;
							}
							if (heading == 2) {
								player.motionZ = -0.7F;
							}
							if (heading == 3) {
								player.motionX = 0.7F;
							}
							player.motionY = 0.7D;
							playerCustom.isJumpingOnWall = true;
						}
					}
				}
			}
		}
	}

	private KeyBinding turn = new KeyBinding("Turn", Keyboard.KEY_APOSTROPHE,
			"Cube's Edge");

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void key(KeyInputEvent event) {
		if (turn.isPressed()
				&& !((EntityPlayerCustom) Minecraft.getMinecraft().thePlayer
						.getExtendedProperties("Cube's Edge Player")).isTurning) {
			((EntityPlayerCustom) Minecraft.getMinecraft().thePlayer
					.getExtendedProperties("Cube's Edge Player")).isTurning = true;
		}
	}

	@Override
	public void renderTick(EntityPlayerCustom playerCustom) {

	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Turn";
	}
}
