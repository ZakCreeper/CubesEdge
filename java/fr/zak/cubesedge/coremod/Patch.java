package fr.zak.cubesedge.coremod;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.entity.Entity;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Project;

import cpw.mods.fml.common.ObfuscationReflectionHelper;
import fr.zak.cubesedge.Util;

public class Patch {

	public static void entitySetAnglesPatch(float par1, float par2, Entity entity){
		float f2 = entity.rotationPitch;
		float f3 = entity.rotationYaw;
		if(!Util.isRolling){
			entity.rotationYaw = (float)((double)entity.rotationYaw + (double)par1 * 0.15D);
			entity.rotationPitch = (float)((double)entity.rotationPitch - (double)par2 * 0.15D);

			if (entity.rotationPitch < -90.0F)
			{
				entity.rotationPitch = -90.0F;
			}

			if (entity.rotationPitch > 90.0F)
			{
				entity.rotationPitch = 90.0F;
			}

			entity.prevRotationPitch += entity.rotationPitch - f2;
			entity.prevRotationYaw += entity.rotationYaw - f3;
		}
	}

	public static void entityRendererRenderHandPatch(float par1, int par2, EntityRenderer renderer){
		if (renderer.debugViewDirection <= 0)
		{
			GL11.glMatrixMode(GL11.GL_PROJECTION);
			GL11.glLoadIdentity();
			float f1 = 0.07F;

			if (((Minecraft)ObfuscationReflectionHelper.getPrivateValue(EntityRenderer.class, renderer, 5)).gameSettings.anaglyph)
			{
				GL11.glTranslatef((float)(-(par2 * 2 - 1)) * f1, 0.0F, 0.0F);
			}

			if ((Double)ObfuscationReflectionHelper.getPrivateValue(EntityRenderer.class, renderer, 46) != 1.0D)
			{
				GL11.glTranslatef((Float)ObfuscationReflectionHelper.getPrivateValue(EntityRenderer.class, renderer, 47), (float)(-(Double)ObfuscationReflectionHelper.getPrivateValue(EntityRenderer.class, renderer, 48)), 0.0F);
				GL11.glScaled((Double)ObfuscationReflectionHelper.getPrivateValue(EntityRenderer.class, renderer, 46), (Double)ObfuscationReflectionHelper.getPrivateValue(EntityRenderer.class, renderer, 46), 1.0D);
			}

			Project.gluPerspective(getFOVModifier(EntityRenderer.class, renderer, par1, false), (float)((Minecraft)ObfuscationReflectionHelper.getPrivateValue(EntityRenderer.class, renderer, 5)).displayWidth / (float)((Minecraft)ObfuscationReflectionHelper.getPrivateValue(EntityRenderer.class, renderer, 5)).displayHeight, 0.05F, ((Float)ObfuscationReflectionHelper.getPrivateValue(EntityRenderer.class, renderer, 6)) * 2.0F);

			if (((Minecraft)ObfuscationReflectionHelper.getPrivateValue(EntityRenderer.class, renderer, 5)).playerController.enableEverythingIsScrewedUpMode())
			{
				float f2 = 0.6666667F;
				GL11.glScalef(1.0F, f2, 1.0F);
			}

			GL11.glMatrixMode(GL11.GL_MODELVIEW);
			GL11.glLoadIdentity();

			if (((Minecraft)ObfuscationReflectionHelper.getPrivateValue(EntityRenderer.class, renderer, 5)).gameSettings.anaglyph)
			{
				GL11.glTranslatef((float)(par2 * 2 - 1) * 0.1F, 0.0F, 0.0F);
			}

			GL11.glPushMatrix();
			hurtCameraEffect(EntityRenderer.class, renderer, par1);

			if (((Minecraft)ObfuscationReflectionHelper.getPrivateValue(EntityRenderer.class, renderer, 5)).gameSettings.viewBobbing)
			{
				setupViewBobbing(EntityRenderer.class, renderer, par1);
			}

			if (((Minecraft)ObfuscationReflectionHelper.getPrivateValue(EntityRenderer.class, renderer, 5)).gameSettings.thirdPersonView == 0 && !((Minecraft)ObfuscationReflectionHelper.getPrivateValue(EntityRenderer.class, renderer, 5)).renderViewEntity.isPlayerSleeping() && !((Minecraft)ObfuscationReflectionHelper.getPrivateValue(EntityRenderer.class, renderer, 5)).gameSettings.hideGUI && !((Minecraft)ObfuscationReflectionHelper.getPrivateValue(EntityRenderer.class, renderer, 5)).playerController.enableEverythingIsScrewedUpMode())
			{
				renderer.enableLightmap((double)par1);
				renderer.itemRenderer.renderItemInFirstPerson(par1);
				renderer.disableLightmap((double)par1);
			}

			GL11.glPopMatrix();

			if (((Minecraft)ObfuscationReflectionHelper.getPrivateValue(EntityRenderer.class, renderer, 5)).gameSettings.thirdPersonView == 0 && !((Minecraft)ObfuscationReflectionHelper.getPrivateValue(EntityRenderer.class, renderer, 5)).renderViewEntity.isPlayerSleeping())
			{
				renderer.itemRenderer.renderOverlays(par1);
				hurtCameraEffect(EntityRenderer.class, renderer, par1);
			}

			if (((Minecraft)ObfuscationReflectionHelper.getPrivateValue(EntityRenderer.class, renderer, 5)).gameSettings.viewBobbing)
			{
				setupViewBobbing(EntityRenderer.class, renderer, par1);
			}
		}
	}

	private static float getFOVModifier(Class c, EntityRenderer renderer, float par1, boolean par2){
		try {
			Method m = c.getDeclaredMethods()[3];
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
		}
	}
	
	private static void hurtCameraEffect(Class c, EntityRenderer renderer, float par1){
		try {
			Method m = c.getDeclaredMethods()[13];
			m.setAccessible(true);
			m.invoke(renderer, par1);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}
	
	private static void setupViewBobbing(Class c, EntityRenderer renderer, float par1){
		try {
			Method m = c.getDeclaredMethods()[12];
			m.setAccessible(true);
			m.invoke(renderer, par1);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}
}
