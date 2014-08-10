package fr.zak.cubesedge.coremod;

import static net.minecraftforge.client.IItemRenderer.ItemRenderType.EQUIPPED_FIRST_PERSON;
import static net.minecraftforge.client.IItemRenderer.ItemRenderType.FIRST_PERSON_MAP;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemCloth;
import net.minecraft.item.ItemMap;
import net.minecraft.item.ItemStack;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.MapData;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.MinecraftForgeClient;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.util.glu.Project;

import cpw.mods.fml.common.ObfuscationReflectionHelper;
import fr.zak.cubesedge.Util;

public class Patch {

	public static void entitySetAnglesPatch(float par1, float par2, Entity entity){
		float f2 = entity.rotationPitch;
		float f3 = entity.rotationYaw;
		if(!Util.isRolling && !Util.isGrabbing){
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
		if(Util.isGrabbing){
			entity.rotationYaw = (float)((double)entity.rotationYaw + (double)par1 * 0.15D);
			entity.rotationPitch = (float)((double)entity.rotationPitch - (double)par2 * 0.15D);
			int heading = MathHelper.floor_double((double)(Minecraft.getMinecraft().thePlayer.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;

			if(Util.grabbingDirections[0] && Util.grabbingDirections[3] && Util.grabbingDirections[1]){
				if(MathHelper.wrapAngleTo180_float(entity.rotationYaw) > 44){
					entity.rotationYaw = 44;
				}
				if(MathHelper.wrapAngleTo180_float(entity.rotationYaw) < -44){
					entity.rotationYaw = -44;
				}
			}
			else if(Util.grabbingDirections[1] && Util.grabbingDirections[0] && Util.grabbingDirections[2]){
				if(MathHelper.wrapAngleTo180_float(entity.rotationYaw) < 46){
					entity.rotationYaw = 46;
				}
				if(MathHelper.wrapAngleTo180_float(entity.rotationYaw) > 134){
					entity.rotationYaw = 134;
				}
			}
			else if(Util.grabbingDirections[2] && Util.grabbingDirections[1] && Util.grabbingDirections[3]){
				if(MathHelper.wrapAngleTo180_float(entity.rotationYaw) < 136 && MathHelper.wrapAngleTo180_float(entity.rotationYaw) > 1){
					entity.rotationYaw = 136;
				}
				if(MathHelper.wrapAngleTo180_float(entity.rotationYaw) > -136 && MathHelper.wrapAngleTo180_float(entity.rotationYaw) < -1){
					entity.rotationYaw = -136;
				}
			}
			else if(Util.grabbingDirections[3] && Util.grabbingDirections[2] && Util.grabbingDirections[0]){
				if(MathHelper.wrapAngleTo180_float(entity.rotationYaw) > -46){
					entity.rotationYaw = -46;
				}
				if(MathHelper.wrapAngleTo180_float(entity.rotationYaw) < -134){
					entity.rotationYaw = -134;
				}
			}

			if (entity.rotationPitch > 0.0F)
			{
				entity.rotationPitch = 0.0F;
			}

			if (entity.rotationPitch < -20.0F)
			{
				entity.rotationPitch = -20.0F;
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
				GL11.glTranslatef(((Double)ObfuscationReflectionHelper.getPrivateValue(EntityRenderer.class, renderer, 47)).floatValue(), (float)(-(Double)ObfuscationReflectionHelper.getPrivateValue(EntityRenderer.class, renderer, 48)), 0.0F);
				GL11.glScaled((Double)ObfuscationReflectionHelper.getPrivateValue(EntityRenderer.class, renderer, 46), (Double)ObfuscationReflectionHelper.getPrivateValue(EntityRenderer.class, renderer, 46), 1.0D);
			}

			Project.gluPerspective(getFOVModifier(EntityRenderer.class, renderer, par1, false), (float)((Minecraft)ObfuscationReflectionHelper.getPrivateValue(EntityRenderer.class, renderer, 5)).displayWidth / (float)((Minecraft)ObfuscationReflectionHelper.getPrivateValue(EntityRenderer.class, renderer, 5)).displayHeight, 0.05F, ((Float)ObfuscationReflectionHelper.getPrivateValue(EntityRenderer.class, renderer, 6)) * 2.0F);

			if (((Minecraft)ObfuscationReflectionHelper.getPrivateValue(EntityRenderer.class, renderer, 5)).playerController.enableEverythingIsScrewedUpMode())
			{
				float f2 = 0.6666667F;
				GL11.glScalef(1.0F, f2, 1.0F);
			}

			GL11.glMatrixMode(GL11.GL_MODELVIEW);
			if(!Util.isGrabbing){
				GL11.glLoadIdentity();
			}
			int heading = MathHelper.floor_double((double)(Minecraft.getMinecraft().thePlayer.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
			if(Util.isGrabbing && heading != 2){
				if(heading != 0){
					GL11.glRotatef(90 * heading, 0, 1, 0);
				}
				else{
					GL11.glRotatef(180, 0, 1, 0);
				}
			}

			if (((Minecraft)ObfuscationReflectionHelper.getPrivateValue(EntityRenderer.class, renderer, 5)).gameSettings.anaglyph)
			{
				GL11.glTranslatef((float)(par2 * 2 - 1) * 0.1F, 0.0F, 0.0F);
			}

			GL11.glPushMatrix();
			hurtCameraEffect(EntityRenderer.class, renderer, par1);

			if (((Minecraft)ObfuscationReflectionHelper.getPrivateValue(EntityRenderer.class, renderer, 5)).gameSettings.viewBobbing)
			{
				setupViewBobbing(renderer, par1);
			}

			if (((Minecraft)ObfuscationReflectionHelper.getPrivateValue(EntityRenderer.class, renderer, 5)).gameSettings.thirdPersonView == 0 && !((Minecraft)ObfuscationReflectionHelper.getPrivateValue(EntityRenderer.class, renderer, 5)).renderViewEntity.isPlayerSleeping() && !((Minecraft)ObfuscationReflectionHelper.getPrivateValue(EntityRenderer.class, renderer, 5)).gameSettings.hideGUI && !((Minecraft)ObfuscationReflectionHelper.getPrivateValue(EntityRenderer.class, renderer, 5)).playerController.enableEverythingIsScrewedUpMode())
			{
				renderer.enableLightmap((double)par1);
				renderItemInFirstPerson(renderer.itemRenderer, par1);
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
				setupViewBobbing(renderer, par1);
			}
		}
	}

	private static float getFOVModifier(Class c, EntityRenderer renderer, float par1, boolean par2){
		try {
			Method m = c.getDeclaredMethod(CubesEdgeFMLLoadingPlugin.obfuscation ? "func_78481_a" : "getFOVModifier", float.class, boolean.class);
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

	private static void hurtCameraEffect(Class c, EntityRenderer renderer, float par1){
		try {
			Method m = c.getDeclaredMethod(CubesEdgeFMLLoadingPlugin.obfuscation ? "func_78482_e" : "hurtCameraEffect", float.class);
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

	private static void setupViewBobbing(EntityRenderer renderer, float par1)
	{
		if (((Minecraft)ObfuscationReflectionHelper.getPrivateValue(EntityRenderer.class, renderer, 5)).renderViewEntity instanceof EntityPlayer)
		{
			EntityPlayer entityplayer = (EntityPlayer)((Minecraft)ObfuscationReflectionHelper.getPrivateValue(EntityRenderer.class, renderer, 5)).renderViewEntity;
			float f1 = entityplayer.distanceWalkedModified - entityplayer.prevDistanceWalkedModified;
			float f2 = -(entityplayer.distanceWalkedModified + f1 * par1);
			float f3 = entityplayer.prevCameraYaw + (entityplayer.cameraYaw - entityplayer.prevCameraYaw) * par1;
			float f4 = entityplayer.prevCameraPitch + (entityplayer.cameraPitch - entityplayer.prevCameraPitch) * par1;
			if(!entityplayer.isSprinting()){
				GL11.glTranslatef(MathHelper.sin(f2 * (float)Math.PI) * f3 * 0.5F, -Math.abs(MathHelper.cos(f2 * (float)Math.PI) * f3), 0.0F);
				GL11.glRotatef(MathHelper.sin(f2 * (float)Math.PI) * f3 * 3.0F, 0.0F, 0.0F, 1.0F);
				GL11.glRotatef(Math.abs(MathHelper.cos(f2 * (float)Math.PI - 0.2F) * f3) * 5.0F, 1.0F, 0.0F, 0.0F);
				GL11.glRotatef(f4, 1.0F, 0.0F, 0.0F);
			}
		}
	}

	private static void renderItemInFirstPerson(ItemRenderer renderer, float par1){
		float f1 = (Float)ObfuscationReflectionHelper.getPrivateValue(ItemRenderer.class, renderer, 6) + ((Float)ObfuscationReflectionHelper.getPrivateValue(ItemRenderer.class, renderer, 5) - (Float)ObfuscationReflectionHelper.getPrivateValue(ItemRenderer.class, renderer, 6)) * par1;
		EntityClientPlayerMP entityclientplayermp = ((Minecraft)ObfuscationReflectionHelper.getPrivateValue(ItemRenderer.class, renderer, 3)).thePlayer;
		float f2 = entityclientplayermp.prevRotationPitch + (entityclientplayermp.rotationPitch - entityclientplayermp.prevRotationPitch) * par1;
		if(!Util.isGrabbing){
				GL11.glPushMatrix();
				GL11.glRotatef(f2, 1.0F, 0.0F, 0.0F);
				GL11.glRotatef(entityclientplayermp.prevRotationYaw + (entityclientplayermp.rotationYaw - entityclientplayermp.prevRotationYaw) * par1, 0.0F, 1.0F, 0.0F);
				RenderHelper.enableStandardItemLighting();
				GL11.glPopMatrix();
		}
		EntityPlayerSP entityplayersp = (EntityPlayerSP)entityclientplayermp;
		float f3 = entityplayersp.prevRenderArmPitch + (entityplayersp.renderArmPitch - entityplayersp.prevRenderArmPitch) * par1;
		float f4 = entityplayersp.prevRenderArmYaw + (entityplayersp.renderArmYaw - entityplayersp.prevRenderArmYaw) * par1;
		if(!Util.isGrabbing){
				GL11.glRotatef((entityclientplayermp.rotationPitch - f3) * 0.1F, 1.0F, 0.0F, 0.0F);
				GL11.glRotatef((entityclientplayermp.rotationYaw - f4) * 0.1F, 0.0F, 1.0F, 0.0F);
		}
		ItemStack itemstack = (ItemStack)ObfuscationReflectionHelper.getPrivateValue(ItemRenderer.class, renderer, 4);

		if (itemstack != null && itemstack.getItem() instanceof ItemCloth)
		{
			GL11.glEnable(GL11.GL_BLEND);
			OpenGlHelper.glBlendFunc(770, 771, 1, 0);
		}

		int i = ((Minecraft)ObfuscationReflectionHelper.getPrivateValue(ItemRenderer.class, renderer, 3)).theWorld.getLightBrightnessForSkyBlocks(MathHelper.floor_double(entityclientplayermp.posX), MathHelper.floor_double(entityclientplayermp.posY), MathHelper.floor_double(entityclientplayermp.posZ), 0);
		int j = i % 65536;
		int k = i / 65536;
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)j / 1.0F, (float)k / 1.0F);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		float f5;
		float f6;
		float f7;

		if (itemstack != null)
		{
			int l = itemstack.getItem().getColorFromItemStack(itemstack, 0);
			f5 = (float)(l >> 16 & 255) / 255.0F;
			f6 = (float)(l >> 8 & 255) / 255.0F;
			f7 = (float)(l & 255) / 255.0F;
			GL11.glColor4f(f5, f6, f7, 1.0F);
		}
		else
		{
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		}

		float f8;
		float f9;
		float f10;
		float f13;
		Render render;
		RenderPlayer renderplayer;

		if (itemstack != null && itemstack.getItem() instanceof ItemMap)
		{
			GL11.glPushMatrix();
			if(Minecraft.getMinecraft().gameSettings.viewBobbing){
				if (((Minecraft)ObfuscationReflectionHelper.getPrivateValue(ItemRenderer.class, renderer, 3)).renderViewEntity instanceof EntityPlayer)
				{
					EntityPlayer entityplayer = (EntityPlayer)((Minecraft)ObfuscationReflectionHelper.getPrivateValue(ItemRenderer.class, renderer, 3)).renderViewEntity;
					float bf1 = entityplayer.distanceWalkedModified - entityplayer.prevDistanceWalkedModified;
					float bf2 = -(entityplayer.distanceWalkedModified + bf1 * par1);
					float bf3 = entityplayer.prevCameraYaw + (entityplayer.cameraYaw - entityplayer.prevCameraYaw) * par1;
					float bf4 = entityplayer.prevCameraPitch + (entityplayer.cameraPitch - entityplayer.prevCameraPitch) * par1;
					if(entityplayer.isSprinting()){
						GL11.glTranslatef(MathHelper.sin(bf2 * (float)Math.PI) * bf3 * 0.5F, -Math.abs(MathHelper.cos(bf2 * (float)Math.PI) * bf3), 0.0F);
						GL11.glRotatef(MathHelper.sin(bf2 * (float)Math.PI) * bf3 * 3.0F, 0.0F, 0.0F, 1.0F);
						GL11.glRotatef(Math.abs(MathHelper.cos(bf2 * (float)Math.PI - 0.2F) * bf3) * 5.0F, 1.0F, 0.0F, 0.0F);
						GL11.glRotatef(bf4, 1.0F, 0.0F, 0.0F);
					}
				}
			}
			f13 = 0.8F;
			f5 = entityclientplayermp.getSwingProgress(par1);
			f6 = MathHelper.sin(f5 * (float)Math.PI);
			f7 = MathHelper.sin(MathHelper.sqrt_float(f5) * (float)Math.PI);
			GL11.glTranslatef(-f7 * 0.4F, MathHelper.sin(MathHelper.sqrt_float(f5) * (float)Math.PI * 2.0F) * 0.2F, -f6 * 0.2F);
			f5 = 1.0F - f2 / 45.0F + 0.1F;

			if (f5 < 0.0F)
			{
				f5 = 0.0F;
			}

			if (f5 > 1.0F)
			{
				f5 = 1.0F;
			}

			f5 = -MathHelper.cos(f5 * (float)Math.PI) * 0.5F + 0.5F;
			GL11.glTranslatef(0.0F, 0.0F * f13 - (1.0F - f1) * 1.2F - f5 * 0.5F + 0.04F, -0.9F * f13);
			GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
			GL11.glRotatef(f5 * -85.0F, 0.0F, 0.0F, 1.0F);
			GL11.glEnable(GL12.GL_RESCALE_NORMAL);
			((Minecraft)ObfuscationReflectionHelper.getPrivateValue(ItemRenderer.class, renderer, 3)).getTextureManager().bindTexture(entityclientplayermp.getLocationSkin());

			for (int i1 = 0; i1 < 2; ++i1)
			{
				int j1 = i1 * 2 - 1;
				GL11.glPushMatrix();
				GL11.glTranslatef(-0.0F, -0.6F, 1.1F * (float)j1);
				GL11.glRotatef((float)(-45 * j1), 1.0F, 0.0F, 0.0F);
				GL11.glRotatef(-90.0F, 0.0F, 0.0F, 1.0F);
				GL11.glRotatef(59.0F, 0.0F, 0.0F, 1.0F);
				GL11.glRotatef((float)(-65 * j1), 0.0F, 1.0F, 0.0F);
				render = RenderManager.instance.getEntityRenderObject(((Minecraft)ObfuscationReflectionHelper.getPrivateValue(ItemRenderer.class, renderer, 3)).thePlayer);
				renderplayer = (RenderPlayer)render;
				f10 = 1.0F;
				GL11.glScalef(f10, f10, f10);
				renderplayer.renderFirstPersonArm(((Minecraft)ObfuscationReflectionHelper.getPrivateValue(ItemRenderer.class, renderer, 3)).thePlayer);
				GL11.glPopMatrix();
			}

			f6 = entityclientplayermp.getSwingProgress(par1);
			f7 = MathHelper.sin(f6 * f6 * (float)Math.PI);
			f8 = MathHelper.sin(MathHelper.sqrt_float(f6) * (float)Math.PI);
			GL11.glRotatef(-f7 * 20.0F, 0.0F, 1.0F, 0.0F);
			GL11.glRotatef(-f8 * 20.0F, 0.0F, 0.0F, 1.0F);
			GL11.glRotatef(-f8 * 80.0F, 1.0F, 0.0F, 0.0F);
			f9 = 0.38F;
			GL11.glScalef(f9, f9, f9);
			GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
			GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
			GL11.glTranslatef(-1.0F, -1.0F, 0.0F);
			f10 = 0.015625F;
			GL11.glScalef(f10, f10, f10);
			((Minecraft)ObfuscationReflectionHelper.getPrivateValue(ItemRenderer.class, renderer, 3)).getTextureManager().bindTexture((ResourceLocation)ObfuscationReflectionHelper.getPrivateValue(ItemRenderer.class, renderer, 1));
			Tessellator tessellator = Tessellator.instance;
			GL11.glNormal3f(0.0F, 0.0F, -1.0F);
			tessellator.startDrawingQuads();
			byte b0 = 7;
			tessellator.addVertexWithUV((double)(0 - b0), (double)(128 + b0), 0.0D, 0.0D, 1.0D);
			tessellator.addVertexWithUV((double)(128 + b0), (double)(128 + b0), 0.0D, 1.0D, 1.0D);
			tessellator.addVertexWithUV((double)(128 + b0), (double)(0 - b0), 0.0D, 1.0D, 0.0D);
			tessellator.addVertexWithUV((double)(0 - b0), (double)(0 - b0), 0.0D, 0.0D, 0.0D);
			tessellator.draw();

			IItemRenderer custom = MinecraftForgeClient.getItemRenderer(itemstack, FIRST_PERSON_MAP);
			MapData mapdata = ((ItemMap)itemstack.getItem()).getMapData(itemstack, ((Minecraft)ObfuscationReflectionHelper.getPrivateValue(ItemRenderer.class, renderer, 3)).theWorld);

			if (custom == null)
			{
				if (mapdata != null)
				{
					((Minecraft)ObfuscationReflectionHelper.getPrivateValue(ItemRenderer.class, renderer, 3)).entityRenderer.getMapItemRenderer().func_148250_a(mapdata, false);
				}
			}
			else
			{
				custom.renderItem(FIRST_PERSON_MAP, itemstack, ((Minecraft)ObfuscationReflectionHelper.getPrivateValue(ItemRenderer.class, renderer, 3)).thePlayer, ((Minecraft)ObfuscationReflectionHelper.getPrivateValue(ItemRenderer.class, renderer, 3)).getTextureManager(), mapdata);
			}

			GL11.glPopMatrix();
		}
		else if (itemstack != null)
		{
			GL11.glPushMatrix();
			if(Minecraft.getMinecraft().gameSettings.viewBobbing){
				if (((Minecraft)ObfuscationReflectionHelper.getPrivateValue(ItemRenderer.class, renderer, 3)).renderViewEntity instanceof EntityPlayer)
				{
					EntityPlayer entityplayer = (EntityPlayer)((Minecraft)ObfuscationReflectionHelper.getPrivateValue(ItemRenderer.class, renderer, 3)).renderViewEntity;
					float bf1 = entityplayer.distanceWalkedModified - entityplayer.prevDistanceWalkedModified;
					float bf2 = -(entityplayer.distanceWalkedModified + bf1 * par1);
					float bf3 = entityplayer.prevCameraYaw + (entityplayer.cameraYaw - entityplayer.prevCameraYaw) * par1;
					float bf4 = entityplayer.prevCameraPitch + (entityplayer.cameraPitch - entityplayer.prevCameraPitch) * par1;
					if(entityplayer.isSprinting()){
						GL11.glTranslatef(MathHelper.sin(bf2 * (float)Math.PI) * bf3 * 0.5F, -Math.abs(MathHelper.cos(bf2 * (float)Math.PI) * bf3), 0.0F);
						GL11.glRotatef(MathHelper.sin(bf2 * (float)Math.PI) * bf3 * 3.0F, 0.0F, 0.0F, 1.0F);
						GL11.glRotatef(Math.abs(MathHelper.cos(bf2 * (float)Math.PI - 0.2F) * bf3) * 5.0F, 1.0F, 0.0F, 0.0F);
						GL11.glRotatef(bf4, 1.0F, 0.0F, 0.0F);
					}
				}
			}
			f13 = 0.8F;

			if (entityclientplayermp.getItemInUseCount() > 0)
			{
				EnumAction enumaction = itemstack.getItemUseAction();

				if (enumaction == EnumAction.eat || enumaction == EnumAction.drink)
				{
					f6 = (float)entityclientplayermp.getItemInUseCount() - par1 + 1.0F;
					f7 = 1.0F - f6 / (float)itemstack.getMaxItemUseDuration();
					f8 = 1.0F - f7;
					f8 = f8 * f8 * f8;
					f8 = f8 * f8 * f8;
					f8 = f8 * f8 * f8;
					f9 = 1.0F - f8;
					GL11.glTranslatef(0.0F, MathHelper.abs(MathHelper.cos(f6 / 4.0F * (float)Math.PI) * 0.1F) * (float)((double)f7 > 0.2D ? 1 : 0), 0.0F);
					GL11.glTranslatef(f9 * 0.6F, -f9 * 0.5F, 0.0F);
					GL11.glRotatef(f9 * 90.0F, 0.0F, 1.0F, 0.0F);
					GL11.glRotatef(f9 * 10.0F, 1.0F, 0.0F, 0.0F);
					GL11.glRotatef(f9 * 30.0F, 0.0F, 0.0F, 1.0F);
				}
			}
			else
			{
				f5 = entityclientplayermp.getSwingProgress(par1);
				f6 = MathHelper.sin(f5 * (float)Math.PI);
				f7 = MathHelper.sin(MathHelper.sqrt_float(f5) * (float)Math.PI);
				GL11.glTranslatef(-f7 * 0.4F, MathHelper.sin(MathHelper.sqrt_float(f5) * (float)Math.PI * 2.0F) * 0.2F, -f6 * 0.2F);
			}

			GL11.glTranslatef(0.7F * f13, -0.65F * f13 - (1.0F - f1) * 0.6F, -0.9F * f13);
			GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
			GL11.glEnable(GL12.GL_RESCALE_NORMAL);
			f5 = entityclientplayermp.getSwingProgress(par1);
			f6 = MathHelper.sin(f5 * f5 * (float)Math.PI);
			f7 = MathHelper.sin(MathHelper.sqrt_float(f5) * (float)Math.PI);
			GL11.glRotatef(-f6 * 20.0F, 0.0F, 1.0F, 0.0F);
			GL11.glRotatef(-f7 * 20.0F, 0.0F, 0.0F, 1.0F);
			GL11.glRotatef(-f7 * 80.0F, 1.0F, 0.0F, 0.0F);
			f8 = 0.4F;
			GL11.glScalef(f8, f8, f8);
			float f11;
			float f12;

			if (entityclientplayermp.getItemInUseCount() > 0)
			{
				EnumAction enumaction1 = itemstack.getItemUseAction();

				if (enumaction1 == EnumAction.block)
				{
					GL11.glTranslatef(-0.5F, 0.2F, 0.0F);
					GL11.glRotatef(30.0F, 0.0F, 1.0F, 0.0F);
					GL11.glRotatef(-80.0F, 1.0F, 0.0F, 0.0F);
					GL11.glRotatef(60.0F, 0.0F, 1.0F, 0.0F);
				}
				else if (enumaction1 == EnumAction.bow)
				{
					GL11.glRotatef(-18.0F, 0.0F, 0.0F, 1.0F);
					GL11.glRotatef(-12.0F, 0.0F, 1.0F, 0.0F);
					GL11.glRotatef(-8.0F, 1.0F, 0.0F, 0.0F);
					GL11.glTranslatef(-0.9F, 0.2F, 0.0F);
					f10 = (float)itemstack.getMaxItemUseDuration() - ((float)entityclientplayermp.getItemInUseCount() - par1 + 1.0F);
					f11 = f10 / 20.0F;
					f11 = (f11 * f11 + f11 * 2.0F) / 3.0F;

					if (f11 > 1.0F)
					{
						f11 = 1.0F;
					}

					if (f11 > 0.1F)
					{
						GL11.glTranslatef(0.0F, MathHelper.sin((f10 - 0.1F) * 1.3F) * 0.01F * (f11 - 0.1F), 0.0F);
					}

					GL11.glTranslatef(0.0F, 0.0F, f11 * 0.1F);
					GL11.glRotatef(-335.0F, 0.0F, 0.0F, 1.0F);
					GL11.glRotatef(-50.0F, 0.0F, 1.0F, 0.0F);
					GL11.glTranslatef(0.0F, 0.5F, 0.0F);
					f12 = 1.0F + f11 * 0.2F;
					GL11.glScalef(1.0F, 1.0F, f12);
					GL11.glTranslatef(0.0F, -0.5F, 0.0F);
					GL11.glRotatef(50.0F, 0.0F, 1.0F, 0.0F);
					GL11.glRotatef(335.0F, 0.0F, 0.0F, 1.0F);
				}
			}

			if (itemstack.getItem().shouldRotateAroundWhenRendering())
			{
				GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
			}

			if (itemstack.getItem().requiresMultipleRenderPasses())
			{
				renderer.renderItem(entityclientplayermp, itemstack, 0, EQUIPPED_FIRST_PERSON);
				for (int x = 1; x < itemstack.getItem().getRenderPasses(itemstack.getItemDamage()); x++)
				{
					int k1 = itemstack.getItem().getColorFromItemStack(itemstack, 1);
					f10 = (float)(k1 >> 16 & 255) / 255.0F;
					f11 = (float)(k1 >> 8 & 255) / 255.0F;
					f12 = (float)(k1 & 255) / 255.0F;
					GL11.glColor4f(1.0F * f10, 1.0F * f11, 1.0F * f12, 1.0F);
					renderer.renderItem(entityclientplayermp, itemstack, x, EQUIPPED_FIRST_PERSON);
				}
			}
			else
			{
				renderer.renderItem(entityclientplayermp, itemstack, 0, EQUIPPED_FIRST_PERSON);
			}

			GL11.glPopMatrix();
		}
		else if (!entityclientplayermp.isInvisible())
		{
			GL11.glPushMatrix();
			if(Minecraft.getMinecraft().gameSettings.viewBobbing){
				EntityPlayer entityplayer = (EntityPlayer)((Minecraft)ObfuscationReflectionHelper.getPrivateValue(ItemRenderer.class, renderer, 3)).renderViewEntity;
				float bf4 = entityplayer.prevCameraPitch + (entityplayer.cameraPitch - entityplayer.prevCameraPitch) * par1;
				if (((Minecraft)ObfuscationReflectionHelper.getPrivateValue(ItemRenderer.class, renderer, 3)).renderViewEntity instanceof EntityPlayer)
				{
					if(entityplayer.isSprinting()){
						GL11.glRotatef(bf4, 1.0F, 0.0F, 0.0F);
					}
				}
			}
			f13 = 0.8F;
			f5 = entityclientplayermp.getSwingProgress(par1);
			f6 = MathHelper.sin(f5 * (float)Math.PI);
			f7 = MathHelper.sin(MathHelper.sqrt_float(f5) * (float)Math.PI);
			GL11.glTranslatef(-f7 * 0.3F, MathHelper.sin(MathHelper.sqrt_float(f5) * (float)Math.PI * 2.0F) * 0.4F, -f6 * 0.4F);
			GL11.glTranslatef(0.8F * f13, -0.75F * f13 - (1.0F - f1) * 0.6F, -0.9F * f13);
			GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
			GL11.glEnable(GL12.GL_RESCALE_NORMAL);
			f5 = entityclientplayermp.getSwingProgress(par1);
			f6 = MathHelper.sin(f5 * f5 * (float)Math.PI);
			f7 = MathHelper.sin(MathHelper.sqrt_float(f5) * (float)Math.PI);
			GL11.glRotatef(f7 * 70.0F, 0.0F, 1.0F, 0.0F);
			GL11.glRotatef(-f6 * 20.0F, 0.0F, 0.0F, 1.0F);
			((Minecraft)ObfuscationReflectionHelper.getPrivateValue(ItemRenderer.class, renderer, 3)).getTextureManager().bindTexture(entityclientplayermp.getLocationSkin());
			GL11.glTranslatef(-1.0F, 3.6F, 3.5F);
			GL11.glRotatef(120.0F, 0.0F, 0.0F, 1.0F);
			GL11.glRotatef(200.0F, 1.0F, 0.0F, 0.0F);
			GL11.glRotatef(-135.0F, 0.0F, 1.0F, 0.0F);
			GL11.glScalef(1.0F, 1.0F, 1.0F);
			GL11.glTranslatef(5.6F, 0.0F, 0.0F);
			render = RenderManager.instance.getEntityRenderObject(((Minecraft)ObfuscationReflectionHelper.getPrivateValue(ItemRenderer.class, renderer, 3)).thePlayer);
			renderplayer = (RenderPlayer)render;
			f10 = 1.0F;
			GL11.glScalef(f10, f10, f10);
			if(Minecraft.getMinecraft().gameSettings.viewBobbing){
				EntityPlayer entityplayer = (EntityPlayer)((Minecraft)ObfuscationReflectionHelper.getPrivateValue(ItemRenderer.class, renderer, 3)).renderViewEntity;
				if (((Minecraft)ObfuscationReflectionHelper.getPrivateValue(ItemRenderer.class, renderer, 3)).renderViewEntity instanceof EntityPlayer)
				{
					if(entityplayer.isSprinting()){
						GL11.glTranslatef(0.F, Util.tickRunningRight * 0.6F, 0);
						GL11.glRotatef(-Util.tickRunningRight * 20F, 0, 0, 0.4F);
					}
				}
			}
			if(Util.isGrabbing){
				GL11.glRotatef(-20, 1, 0, 1);
			}
			renderplayer.renderFirstPersonArm(((Minecraft)ObfuscationReflectionHelper.getPrivateValue(ItemRenderer.class, renderer, 3)).thePlayer);
			GL11.glPopMatrix();
		}

		if (itemstack != null && itemstack.getItem() instanceof ItemCloth)
		{
			GL11.glDisable(GL11.GL_BLEND);
		}

		if(!entityclientplayermp.isInvisible() && ((itemstack != null && !(itemstack.getItem() instanceof ItemMap) || itemstack == null))){
			GL11.glPushMatrix();
			if(Minecraft.getMinecraft().gameSettings.viewBobbing){
				EntityPlayer entityplayer = (EntityPlayer)((Minecraft)ObfuscationReflectionHelper.getPrivateValue(ItemRenderer.class, renderer, 3)).renderViewEntity;
				float bf4 = entityplayer.prevCameraPitch + (entityplayer.cameraPitch - entityplayer.prevCameraPitch) * par1;
				if (((Minecraft)ObfuscationReflectionHelper.getPrivateValue(ItemRenderer.class, renderer, 3)).renderViewEntity instanceof EntityPlayer)
				{
					if(entityplayer.isSprinting()){
						GL11.glRotatef(bf4, 1.0F, 0.0F, 0.0F);
					}
				}
			}
			GL11.glRotatef(75, 0, 1, 0);
			f13 = 0.8F;
			GL11.glTranslatef(0.8F * f13, -0.75F * f13 - (1.0F - f1) * 0.6F, -0.9F * f13);
			GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
			GL11.glEnable(GL12.GL_RESCALE_NORMAL);
			((Minecraft)ObfuscationReflectionHelper.getPrivateValue(ItemRenderer.class, renderer, 3)).getTextureManager().bindTexture(entityclientplayermp.getLocationSkin());
			GL11.glTranslatef(-1.0F, 3.6F, 3.5F);
			GL11.glRotatef(120.0F, 0.0F, 0.0F, 1.0F);
			GL11.glRotatef(200.0F, 1.0F, 0.0F, 0.0F);
			GL11.glRotatef(-135.0F, 0.0F, 1.0F, 0.0F);
			GL11.glScalef(1.0F, 1.0F, 1.0F);
			GL11.glTranslatef(5.6F, 0.0F, 0.0F);
			render = RenderManager.instance.getEntityRenderObject(((Minecraft)ObfuscationReflectionHelper.getPrivateValue(ItemRenderer.class, renderer, 3)).thePlayer);
			renderplayer = (RenderPlayer)render;
			f10 = 1.0F;
			GL11.glScalef(f10, f10, f10);
			GL11.glRotatef(25, 0, 0, 1);
			GL11.glRotatef(10, 1, 0, 0);
			GL11.glTranslatef(0.2F, 0, -0.15F);
			GL11.glRotatef(5, 0, 1, 0);
			GL11.glTranslatef(0, 0.08F, 0);
			if(Minecraft.getMinecraft().gameSettings.viewBobbing){
				EntityPlayer entityplayer = (EntityPlayer)((Minecraft)ObfuscationReflectionHelper.getPrivateValue(ItemRenderer.class, renderer, 3)).renderViewEntity;
				if (((Minecraft)ObfuscationReflectionHelper.getPrivateValue(ItemRenderer.class, renderer, 3)).renderViewEntity instanceof EntityPlayer)
				{
					if(entityplayer.isSprinting()){
						GL11.glTranslatef(0.F, Util.tickRunningLeft * 0.8F, 0);
						GL11.glRotatef(Util.tickRunningLeft * 20F, 0, 0, 0.4F);
					}
				}
			}
			if(Util.isGrabbing){
				GL11.glRotatef(15, 1, 0, 1);
				GL11.glTranslatef(0.1F, 0.22F, 0);
			}
			renderplayer.renderFirstPersonArm(((Minecraft)ObfuscationReflectionHelper.getPrivateValue(ItemRenderer.class, renderer, 3)).thePlayer);
			GL11.glPopMatrix();
		}

		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		RenderHelper.disableStandardItemLighting();
	}
	
	public static void processPlayerPatch(NetHandlerPlayServer net, float f1, float f2){
		System.out.println(net + " : " + f1 + " : " + f2);
	}
}
