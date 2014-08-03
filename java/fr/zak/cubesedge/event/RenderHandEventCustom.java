package fr.zak.cubesedge.event;

import java.lang.reflect.Method;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraftforge.client.event.RenderHandEvent;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Project;

import cpw.mods.fml.common.ObfuscationReflectionHelper;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class RenderHandEventCustom {

	@SubscribeEvent
	public void onHand(RenderHandEvent event){
		Minecraft mc = Minecraft.getMinecraft();
		GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        float f1 = 0.07F;
        float farPlaneDistance = ObfuscationReflectionHelper.getPrivateValue(EntityRenderer.class, mc.entityRenderer, 6);
        double cameraZoom = ObfuscationReflectionHelper.getPrivateValue(EntityRenderer.class, mc.entityRenderer, 46);
        double cameraYaw = ObfuscationReflectionHelper.getPrivateValue(EntityRenderer.class, mc.entityRenderer, 47);
        double cameraPitch = ObfuscationReflectionHelper.getPrivateValue(EntityRenderer.class, mc.entityRenderer, 48);

        if (mc.gameSettings.anaglyph)
        {
            GL11.glTranslatef((float)(-(event.renderPass * 2 - 1)) * f1, 0.0F, 0.0F);
        }

        if (cameraZoom != 1.0D)
        {
            GL11.glTranslatef((float)cameraYaw, (float)(-cameraPitch), 0.0F);
            GL11.glScaled(cameraZoom, cameraZoom, 1.0D);
        }

        Project.gluPerspective(this.forceGetFOVModifier(EntityRenderer.class, mc.entityRenderer, event.partialTicks, false), (float)mc.displayWidth / (float)mc.displayHeight, 0.05F, farPlaneDistance * 2.0F);

        if (mc.playerController.enableEverythingIsScrewedUpMode())
        {
            float f2 = 0.6666667F;
            GL11.glScalef(1.0F, f2, 1.0F);
        }

        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glLoadIdentity();

        if (mc.gameSettings.anaglyph)
        {
            GL11.glTranslatef((float)(event.renderPass * 2 - 1) * 0.1F, 0.0F, 0.0F);
        }

        GL11.glPushMatrix();
        this.forceHurtCameraEffect(EntityRenderer.class, mc.entityRenderer, event.partialTicks);

        if (mc.gameSettings.viewBobbing)
        {
            this.forceSetupViewBobbing(EntityRenderer.class, mc.entityRenderer, event.partialTicks);
        }

		if(mc.gameSettings.thirdPersonView == 0 && !mc.renderViewEntity.isPlayerSleeping() && !mc.gameSettings.hideGUI && !mc.playerController.enableEverythingIsScrewedUpMode()){
			mc.entityRenderer.enableLightmap((double)event.partialTicks);
			mc.getTextureManager().bindTexture(mc.thePlayer.getLocationSkin());
			new ModelBiped().bipedLeftArm.render(0.0625F);
			mc.entityRenderer.disableLightmap((double)event.partialTicks);
		}
		
		GL11.glPopMatrix();
		
		if (mc.gameSettings.thirdPersonView == 0 && !mc.renderViewEntity.isPlayerSleeping())
        {
            mc.entityRenderer.itemRenderer.renderOverlays(event.partialTicks);
            this.forceHurtCameraEffect(EntityRenderer.class, mc.entityRenderer, event.partialTicks);
        }

        if (mc.gameSettings.viewBobbing)
        {
            this.forceSetupViewBobbing(EntityRenderer.class, mc.entityRenderer, event.partialTicks);
        }
	}
	
	private float forceGetFOVModifier(Class c, EntityRenderer instance, float f, boolean b){
		try
		{
			// TODO : Mettre le nom obfusce
			Method m = c.getDeclaredMethod("getFOVModifier", float.class, boolean.class);
			m.setAccessible(true);
			return (Float)m.invoke(instance, f, b);
		}
		catch(NoSuchMethodException e)
		{
			return 0;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return 0;
		}
	}
	
	private void forceHurtCameraEffect(Class c, EntityRenderer instance, float f){
		try
		{
			// TODO : Mettre le nom obfusce
			Method m = c.getDeclaredMethod("hurtCameraEffect", float.class);
			m.setAccessible(true);
			m.invoke(instance, f);
		}
		catch(NoSuchMethodException e)
		{
			if(c != EntityRenderer.class)
			{
				forceHurtCameraEffect(c.getSuperclass(), instance, f);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private void forceSetupViewBobbing(Class c, EntityRenderer instance, float f){
		try
		{
			// TODO : Mettre le nom obfusce
			Method m = c.getDeclaredMethod("getFOVModifier", float.class);
			m.setAccessible(true);
			m.invoke(instance, f);
		}
		catch(NoSuchMethodException e)
		{
			if(c != EntityRenderer.class)
			{
				forceSetupViewBobbing(c.getSuperclass(), instance, f);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
