package fr.zak.cubesedge.renderer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;

import org.lwjgl.opengl.GL11;

import fr.zak.cubesedge.Util;
import fr.zak.cubesedge.entity.EntityPlayerCustom;

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

		if (((EntityPlayerCustom) Minecraft.getMinecraft().thePlayer
				.getExtendedProperties("Cube's Edge Player")).isSneaking
				|| ((EntityPlayerCustom) Minecraft.getMinecraft().thePlayer
						.getExtendedProperties("Cube's Edge Player")).isRolling
						|| (Util.isCube(Minecraft.getMinecraft().theWorld.getBlockState(new BlockPos(
								MathHelper
								.floor_double(Minecraft.getMinecraft().thePlayer.posX),
								MathHelper.floor_double(Minecraft.getMinecraft().thePlayer.posY),
								MathHelper.floor_double(Minecraft.getMinecraft().thePlayer.posZ))).getBlock()) && (((EntityPlayerCustom) Minecraft
										.getMinecraft().thePlayer
										.getExtendedProperties("Cube's Edge Player")).wasSliding || ((EntityPlayerCustom) Minecraft
												.getMinecraft().thePlayer
												.getExtendedProperties("Cube's Edge Player")).wasRolling))) {
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
		else {
			if(((EntityPlayerCustom) Minecraft.getMinecraft().thePlayer.getExtendedProperties("Cube's Edge Player")).wasSliding || ((EntityPlayerCustom) Minecraft.getMinecraft().thePlayer.getExtendedProperties("Cube's Edge Player")).wasRolling){
				KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindSneak.getKeyCode(), false);
			}
			super.getMouseOver(partialTick);
		}
	}

	@Override
	public void orientCamera(float f){
		super.orientCamera(f);

		if (((EntityPlayerCustom) Minecraft.getMinecraft().thePlayer
				.getExtendedProperties("Cube's Edge Player")).isSneaking
				|| ((EntityPlayerCustom) Minecraft.getMinecraft().thePlayer
						.getExtendedProperties("Cube's Edge Player")).isRolling
						|| (Util.isCube(Minecraft.getMinecraft().theWorld.getBlockState(new BlockPos(
								MathHelper
								.floor_double(Minecraft.getMinecraft().thePlayer.posX),
								MathHelper.floor_double(Minecraft.getMinecraft().thePlayer.posY),
								MathHelper.floor_double(Minecraft.getMinecraft().thePlayer.posZ))).getBlock()) && (((EntityPlayerCustom) Minecraft
										.getMinecraft().thePlayer
										.getExtendedProperties("Cube's Edge Player")).wasSliding || ((EntityPlayerCustom) Minecraft
												.getMinecraft().thePlayer
												.getExtendedProperties("Cube's Edge Player")).wasRolling))) {
			GL11.glTranslatef(0, 1, 0);
		}
	}
	
//	@Override
//	public void renderHand(float f, int i){
//		GL11.glMatrixMode(GL11.GL_MODELVIEW);
//		if (!((EntityPlayerCustom) Minecraft.getMinecraft().thePlayer
//				.getExtendedProperties("Cube's Edge Player")).isGrabbing) {
//			if (!((EntityPlayerCustom) Minecraft.getMinecraft().thePlayer
//					.getExtendedProperties("Cube's Edge Player")).animLeft
//					&& !((EntityPlayerCustom) Minecraft.getMinecraft().thePlayer
//							.getExtendedProperties("Cube's Edge Player")).animRight) {
//				GL11.glLoadIdentity();
//			}
//		}
//		int heading = MathHelper
//				.floor_double((double) (Minecraft.getMinecraft().thePlayer.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
//		if (((EntityPlayerCustom) Minecraft.getMinecraft().thePlayer
//				.getExtendedProperties("Cube's Edge Player")).isGrabbing
//				&& heading != 2) {
//			if (heading != 0) {
//				GL11.glRotatef(90 * heading, 0, 1, 0);
//			} else {
//				GL11.glRotatef(180, 0, 1, 0);
//			}
//		}
//		heading -= 1;
//		if (((EntityPlayerCustom) Minecraft.getMinecraft().thePlayer
//				.getExtendedProperties("Cube's Edge Player")).animLeft
//				&& heading != 2) {
//			if (heading != 0) {
//				GL11.glRotatef(90 * heading, 0, 1, 0);
//			} else {
//				GL11.glRotatef(180, 0, 1, 0);
//			}
//		}
//		if (((EntityPlayerCustom) Minecraft.getMinecraft().thePlayer
//				.getExtendedProperties("Cube's Edge Player")).animRight) {
//			GL11.glRotatef(-90 * heading, 0, 1, 0);
//		}
//
//		if (mc.gameSettings.anaglyph) {
//			GL11.glTranslatef((float) (i * 2 - 1) * 0.1F, 0.0F, 0.0F);
//		}
//		super.renderHand(f, i);
//	}
}