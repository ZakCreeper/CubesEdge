package fr.zak.cubesedge.movement;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.MathHelper;
import net.minecraftforge.client.event.MouseEvent;
import cpw.mods.fml.common.ObfuscationReflectionHelper;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import fr.zak.cubesedge.Movement;
import fr.zak.cubesedge.MovementVar;
import fr.zak.cubesedge.entity.EntityPlayerCustom;

@Movement("Grab")
public class MovementGrab extends MovementVar {

	@Override
	public void control(EntityPlayerCustom playerCustom, EntityPlayer player) {
		int heading = MathHelper
				.floor_double((double) (player.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
		if (!playerCustom.isSneaking) {
			if (((((player.worldObj.getBlock(
					MathHelper.floor_double(player.posX),
					MathHelper.floor_double(player.posY),
					MathHelper.floor_double(player.posZ) - 1).isNormalCube() && heading == 2)
					|| (player.worldObj.getBlock(
							MathHelper.floor_double(player.posX),
							MathHelper.floor_double(player.posY),
							MathHelper.floor_double(player.posZ) + 1)
							.isNormalCube() && heading == 0)
					|| (player.worldObj.getBlock(
							MathHelper.floor_double(player.posX) - 1,
							MathHelper.floor_double(player.posY),
							MathHelper.floor_double(player.posZ))
							.isNormalCube() && heading == 1) || (player.worldObj
					.getBlock(MathHelper.floor_double(player.posX) + 1,
							MathHelper.floor_double(player.posY),
							MathHelper.floor_double(player.posZ))
					.isNormalCube() && heading == 3))
					&& ((!player.worldObj.getBlock(
							MathHelper.floor_double(player.posX),
							MathHelper.floor_double(player.posY) + 1,
							MathHelper.floor_double(player.posZ) - 1)
							.isNormalCube() && heading == 2)
							|| (!player.worldObj.getBlock(
									MathHelper.floor_double(player.posX),
									MathHelper.floor_double(player.posY) + 1,
									MathHelper.floor_double(player.posZ) + 1)
									.isNormalCube() && heading == 0)
							|| (!player.worldObj.getBlock(
									MathHelper.floor_double(player.posX) - 1,
									MathHelper.floor_double(player.posY) + 1,
									MathHelper.floor_double(player.posZ))
									.isNormalCube() && heading == 1) || (!player.worldObj
							.getBlock(MathHelper.floor_double(player.posX) + 1,
									MathHelper.floor_double(player.posY) + 1,
									MathHelper.floor_double(player.posZ))
							.isNormalCube() && heading == 3)) && (!player.worldObj
					.getBlock(MathHelper.floor_double(player.posX),
							MathHelper.floor_double(player.posY) - 2,
							MathHelper.floor_double(player.posZ))
					.isNormalCube())))
					&& player.worldObj.getBlock(
							MathHelper.floor_double(player.posX),
							MathHelper.floor_double(player.posY),
							MathHelper.floor_double(player.posZ)) != Blocks.ladder
					&& player.getCurrentEquippedItem() == null) {
				playerCustom.isGrabbing = true;
				playerCustom.rotationYaw = player.rotationYaw;
				playerCustom.rotationPitch = player.rotationPitch;
				playerCustom.prevRotationPitch = player.prevRotationPitch;
				playerCustom.prevRotationYaw = player.prevRotationYaw;
				playerCustom.grabbingDirections[heading] = true;
				if (heading == 0) {
					playerCustom.grabbingDirections[3] = true;
				} else {
					playerCustom.grabbingDirections[heading - 1] = true;
				}

				if (heading == 3) {
					playerCustom.grabbingDirections[0] = true;
				} else {
					playerCustom.grabbingDirections[heading + 1] = true;
				}
			} else {
				playerCustom.isGrabbing = false;
				playerCustom.rotationYaw = 0;
				if (!playerCustom.isRolling) {
					playerCustom.rotationPitch = 0;
				}
				playerCustom.prevRotationPitch = 0;
				playerCustom.prevRotationYaw = 0;
				playerCustom.grabbingDirections[0] = false;
				playerCustom.grabbingDirections[1] = false;
				playerCustom.grabbingDirections[2] = false;
				playerCustom.grabbingDirections[3] = false;
			}
			if (playerCustom.isGrabbing && !playerCustom.wasSneaking
					&& player.isSneaking()) {
				playerCustom.wasSneaking = true;
			}
			if (!playerCustom.isGrabbing) {
				playerCustom.wasSneaking = false;
			}
			if (playerCustom.wasSneaking) {
				return;
			}
			if (!player.isSneaking()
					&& !(Boolean) ObfuscationReflectionHelper.getPrivateValue(
							EntityLivingBase.class, (EntityLivingBase) player,
							41) && playerCustom.isGrabbing) {
				if (heading == 1 || heading == 3) {
					player.setPosition(
							MathHelper.floor_double(player.posX) + 0.5F,
							MathHelper.floor_double(player.posY) + 0.9F,
							player.posZ);
					player.motionX = 0;
				}
				if (heading == 2 || heading == 0) {
					player.setPosition(player.posX,
							MathHelper.floor_double(player.posY) + 0.9F,
							MathHelper.floor_double(player.posZ) + 0.5F);
					player.motionZ = 0;
				}
				player.motionY = 0.0;
				if (player.isSprinting()) {
					player.setSprinting(false);
				}
			} else if ((Boolean) ObfuscationReflectionHelper.getPrivateValue(
					EntityLivingBase.class, (EntityLivingBase) player, 41)
					&& playerCustom.isGrabbing) {
				player.motionY = 0.55D;
			}
		}
		// if(playerCustom.isGrabbing && !player.capabilities.isCreativeMode){
		// PotionEffect potioneffect =
		// player.getActivePotionEffect(Potion.jump);
		// float f1 = potioneffect != null ? (float)(potioneffect.getAmplifier()
		// + 1) : 0.0F;
		// int i = MathHelper.ceiling_float_int(player.fallDistance - 3.0F -
		// f1);
		// if(i > 0){
		// player.playSound(i > 4 ? "game.neutral.hurt.fall.big" :
		// "game.neutral.hurt.fall.small", 1.0F, 1.0F);
		// damageEntity(EntityLivingBase.class, player, DamageSource.fall,
		// (float)i);
		// }
		// player.fallDistance = 0;
		// }
	}

	@SubscribeEvent
	public void onClick(MouseEvent event) {
		if (((EntityPlayerCustom) Minecraft.getMinecraft().thePlayer
				.getExtendedProperties("Cube's Edge Player")).isGrabbing) {
			event.setCanceled(true);
		}
	}

	@Override
	public void renderTick(EntityPlayerCustom playerCustom) {

	}
}
