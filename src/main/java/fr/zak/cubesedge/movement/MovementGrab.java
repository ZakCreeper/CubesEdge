package fr.zak.cubesedge.movement;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraftforge.client.event.MouseEvent;
import cpw.mods.fml.common.ObfuscationReflectionHelper;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import fr.zak.cubesedge.Movement;
import fr.zak.cubesedge.Util;
import fr.zak.cubesedge.entity.EntityPlayerCustom;

public class MovementGrab extends Movement {

	@Override
	public void control(EntityPlayerCustom playerCustom, EntityPlayer player, Side side) {
		int x = MathHelper.floor_double(player.posX);
		int y = MathHelper.floor_double(player.posY);
		int z = MathHelper.floor_double(player.posZ);
		int heading = MathHelper
				.floor_double((double) (player.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
		if (!playerCustom.isSneaking) {
			if (((((Util.isCube(player.worldObj.getBlock(
					x,
					y,
					z - 1)) && heading == 2)
					|| (Util.isCube(player.worldObj.getBlock(
							x,
							y,
							z + 1)
							) && heading == 0)
							|| (Util.isCube(player.worldObj.getBlock(
									x - 1,
									y,
									z)
									) && heading == 1) || (Util.isCube(player.worldObj
											.getBlock(x + 1,
													y,
													z)
											) && heading == 3))
											&& ((!Util.isCube(player.worldObj.getBlock(
													x,
													y + 1,
													z - 1)
													) && heading == 2)
													|| (!Util.isCube(player.worldObj.getBlock(
															x,
															y + 1,
															z + 1)
															) && heading == 0)
															|| (!Util.isCube(player.worldObj.getBlock(
																	x - 1,
																	y + 1,
																	z)
																	) && heading == 1) || (!Util.isCube(player.worldObj
																			.getBlock(x + 1,
																					y + 1,
																					z)
																			) && heading == 3)) && (!Util.isCube(player.worldObj
																					.getBlock(x,
																							y - 2,
																							z)
																					))))
																					&& player.worldObj.getBlock(
																							x,
																							y,
																							z) != Blocks.ladder
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
							x + 0.5F,
							y + 0.9F,
							player.posZ);
					player.motionX = 0;
				}
				if (heading == 2 || heading == 0) {
					player.setPosition(player.posX,
							y + 0.9F,
							z + 0.5F);
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

		//		if(side == Side.CLIENT){
		////			Util.channel.sendToServer(new CPacketPlayerAction(playerCustom.isGrabbing ? 0 : 1));
		//		}
		//		
		//		if(side == Side.SERVER){
		//			System.out.println("1 : " + playerCustom.isGrabbing);
		//		}
		/*    CLIENT    */
		if(!player.worldObj.isRemote){
			if(playerCustom.isGrabbing && !player.capabilities.isCreativeMode){
				PotionEffect potioneffect =
						player.getActivePotionEffect(Potion.jump);
				float f1 = potioneffect != null ? (float)(potioneffect.getAmplifier()
						+ 1) : 0.0F;
				int i = MathHelper.ceiling_float_int(player.fallDistance - 3.0F -
						f1);
				if(i > 0){
					player.playSound(i > 4 ? "game.neutral.hurt.fall.big" :
						"game.neutral.hurt.fall.small", 1.0F, 1.0F);
					damageEntity(EntityLivingBase.class, player, DamageSource.fall,
							(float)i);
				}
				player.fallDistance = 0;
			}
		}
	}

	@SubscribeEvent
	public void onClick(MouseEvent event) {
		if (((EntityPlayerCustom)Minecraft.getMinecraft().thePlayer.getExtendedProperties("Cube's Edge Player")).isGrabbing) {
			event.setCanceled(true);
		}
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Grab";
	}

	@SuppressWarnings("unchecked")
	public static void damageEntity(Class clz, Entity ent, DamageSource dmg,
			float i) {
		try {
			Method m = clz.getDeclaredMethod(Util.obfuscation ? "func_70105_a"
					: "damageEntity", DamageSource.class, float.class);
			m.setAccessible(true);
			m.invoke(ent, dmg, i);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}
	}
}
