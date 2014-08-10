package fr.zak.cubesedge.renderer;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.lwjgl.opengl.GL11;

import fr.zak.cubesedge.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.entity.Entity;

public class EntityRendererCustom extends EntityRenderer
{
	private final Minecraft mc;
	public static float offsetY = 0F; // just for testing, should be based on actual render size

	public EntityRendererCustom(Minecraft mc) {
		super(mc, mc.getResourceManager());
		this.mc = mc;
	}

	@Override
	public void updateCameraAndRender(float partialTick) {
		if (mc.thePlayer == null || mc.thePlayer.isPlayerSleeping()){
			super.updateCameraAndRender(partialTick);
			return;
		}
		// this is what changes the actual camera height
		// but also seems to affect the player model render position
		mc.thePlayer.yOffset -= offsetY;
		super.updateCameraAndRender(partialTick);
		mc.thePlayer.yOffset = 1.62F;
	}

	@Override
	public void getMouseOver(float partialTick) {
		if (mc.thePlayer == null || mc.thePlayer.isPlayerSleeping()){
			super.getMouseOver(partialTick);
			return;
		}
		// adjust the y position to get a mouseover at eye-level
		// not perfect, as the server posY does not match, meaning
		// that some block clicks do not process correctly
		// (distance check or something like that)
		
//		mc.thePlayer.eyeHeight = 1.12F;
		mc.thePlayer.posY += offsetY;
		mc.thePlayer.prevPosY += offsetY;
		mc.thePlayer.lastTickPosY += offsetY;
		super.getMouseOver(partialTick);
		mc.thePlayer.posY -= offsetY;
		mc.thePlayer.prevPosY -= offsetY;
		mc.thePlayer.lastTickPosY -= offsetY;
//		mc.thePlayer.eyeHeight = 0.12F;
	}
}