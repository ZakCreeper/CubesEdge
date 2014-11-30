package fr.zak.cubesedge.coremod;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.MathHelper;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.opengl.GL11;

import fr.zak.cubesedge.Util;
import fr.zak.cubesedge.WorldUtil;
import fr.zak.cubesedge.entity.EntityPlayerCustom;

public class Patch {

	public static void entitySetAnglesPatch(float par1, float par2,
			Entity entity) {
	}

	@SideOnly(Side.CLIENT)
	public static void entityRendererRenderHandPatch(float par1, int par2,
			EntityRenderer renderer) {
		
	}

	private static float getFOVModifier(Class c, EntityRenderer renderer,
			float par1, boolean par2) {
		try {
			Method m = c.getDeclaredMethod(Util.obfuscation ? "func_78481_a"
					: "getFOVModifier", float.class, boolean.class);
			m.setAccessible(true);
			return (Float) m.invoke(renderer, par1, par2);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			return 0;
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			return 0;
		} catch (InvocationTargetException e) {
			e.printStackTrace();
			return 0;
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			return 0;
		} catch (SecurityException e) {
			e.printStackTrace();
			return 0;
		}
	}

	private static void hurtCameraEffect(Class c, EntityRenderer renderer,
			float par1) {
		try {
			Method m = c.getDeclaredMethod(Util.obfuscation ? "func_78482_e"
					: "hurtCameraEffect", float.class);
			m.setAccessible(true);
			m.invoke(renderer, par1);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}
	}

	private static void setupViewBobbing(EntityRenderer renderer, float par1) {
		if (((Minecraft) ObfuscationReflectionHelper.getPrivateValue(
				EntityRenderer.class, renderer, 5)).getRenderViewEntity() instanceof EntityPlayer) {
			EntityPlayer entityplayer = (EntityPlayer) ((Minecraft) ObfuscationReflectionHelper
					.getPrivateValue(EntityRenderer.class, renderer, 5)).getRenderViewEntity();
			float f1 = entityplayer.distanceWalkedModified
					- entityplayer.prevDistanceWalkedModified;
			float f2 = -(entityplayer.distanceWalkedModified + f1 * par1);
			float f3 = entityplayer.prevCameraYaw
					+ (entityplayer.cameraYaw - entityplayer.prevCameraYaw)
					* par1;
			float f4 = entityplayer.prevCameraPitch
					+ (entityplayer.cameraPitch - entityplayer.prevCameraPitch)
					* par1;
			if (!entityplayer.isSprinting()) {
				GL11.glTranslatef(MathHelper.sin(f2 * (float) Math.PI) * f3
						* 0.5F,
						-Math.abs(MathHelper.cos(f2 * (float) Math.PI) * f3),
						0.0F);
				GL11.glRotatef(
						MathHelper.sin(f2 * (float) Math.PI) * f3 * 3.0F, 0.0F,
						0.0F, 1.0F);
				GL11.glRotatef(
						Math.abs(MathHelper.cos(f2 * (float) Math.PI - 0.2F)
								* f3) * 5.0F, 1.0F, 0.0F, 0.0F);
				GL11.glRotatef(f4, 1.0F, 0.0F, 0.0F);
			}
		}
	}

	@SideOnly(Side.CLIENT)
	private static void renderItemInFirstPerson(ItemRenderer renderer,
			float par1) {
	}

	public static void processPlayerPatch(NetHandlerPlayServer net,
			C03PacketPlayer packet) {
	}

	public static boolean isEntityInsideOpaqueBlockPatch(Entity ent) {
		for (int i = 0; i < 8; ++i) {
			float f = ((float) ((i >> 0) % 2) - 0.5F) * ent.width * 0.8F;
			float f1 = ((float) ((i >> 1) % 2) - 0.5F) * 0.1F;
			float f2 = ((float) ((i >> 2) % 2) - 0.5F) * ent.width * 0.8F;
			int j = MathHelper.floor_double(ent.posX + (double) f);
			int k = 0;
			int l = MathHelper.floor_double(ent.posZ + (double) f2);
			if (!(ent instanceof EntityPlayer)) {
				k = MathHelper.floor_double(ent.posY
						+ (double) ent.getEyeHeight() + (double) f1);
			} else {
				if (((EntityPlayerCustom) ent
						.getExtendedProperties("Cube's Edge Player")).isSneaking
						|| ((EntityPlayerCustom) ent
								.getExtendedProperties("Cube's Edge Player")).isRolling
						|| (Util.isCube(WorldUtil.getBlock(ent.worldObj,
								MathHelper.floor_double(ent.posX),
								MathHelper.floor_double(ent.posY),
								MathHelper.floor_double(ent.posZ))) && (((EntityPlayerCustom) ent
								.getExtendedProperties("Cube's Edge Player")).wasSliding || ((EntityPlayerCustom) ent
								.getExtendedProperties("Cube's Edge Player")).wasRolling))) {
					k = MathHelper.floor_double(ent.posY
							+ (double) ent.getEyeHeight() + (double) f1) - 1;
				} else {
					k = MathHelper.floor_double(ent.posY
							+ (double) ent.getEyeHeight() + (double) f1);
				}
			}
			if (Util.isCube(WorldUtil.getBlock(ent.worldObj, j, k, l))) {
				return true;
			}
		}

		return false;
	}

	@SideOnly(Side.CLIENT)
	public static void orientCameraPatch(float par1, EntityRenderer renderer) {

	}
}
