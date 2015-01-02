package fr.zak.cubesedge.renderer;

import org.lwjgl.opengl.GL11;

import fr.zak.cubesedge.Util;
import fr.zak.cubesedge.entity.EntityPlayerCustom;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.util.MathHelper;

public class EntityRendererCustom extends EntityRenderer {
	private final Minecraft mc;
	private float offsetY = -1F; // just for testing, should be based on actual
									// render size

	public EntityRendererCustom(Minecraft mc) {
		super(mc, mc.getResourceManager());
		this.mc = mc;
		System.out.println("fefw");
	}

	@Override
	public void getMouseOver(float partialTick) {
		if (mc.thePlayer == null || mc.thePlayer.isPlayerSleeping()) {
			super.getMouseOver(partialTick);
			return;
		}

		// adjust the y position to get a mouseover at eye-level
		// not perfect, as the server posY does not match, meaning
		// that some block clicks do not process correctly
		// (distance check or something like that)
		mc.thePlayer.posY += offsetY;
		mc.thePlayer.prevPosY += offsetY;
		mc.thePlayer.lastTickPosY += offsetY;
		super.getMouseOver(partialTick);
		mc.thePlayer.posY -= offsetY;
		mc.thePlayer.prevPosY -= offsetY;
		mc.thePlayer.lastTickPosY -= offsetY;
	}
	
	@Override
	public void orientCamera(float f){
		System.out.println("cdefqq");
		super.orientCamera(f);

		if (((EntityPlayerCustom) Minecraft.getMinecraft().thePlayer
				.getExtendedProperties("Cube's Edge Player")).isSneaking
				|| ((EntityPlayerCustom) Minecraft.getMinecraft().thePlayer
						.getExtendedProperties("Cube's Edge Player")).isRolling
				|| (Util.isCube(Minecraft.getMinecraft().theWorld.getBlock(
						MathHelper
								.floor_double(Minecraft.getMinecraft().thePlayer.posX),
						MathHelper.floor_double(Minecraft.getMinecraft().thePlayer.posY),
						MathHelper.floor_double(Minecraft.getMinecraft().thePlayer.posZ))) && (((EntityPlayerCustom) Minecraft
						.getMinecraft().thePlayer
						.getExtendedProperties("Cube's Edge Player")).wasSliding || ((EntityPlayerCustom) Minecraft
						.getMinecraft().thePlayer
						.getExtendedProperties("Cube's Edge Player")).wasRolling))) {
			GL11.glTranslatef(0, 1, 0);
		}
	}
}