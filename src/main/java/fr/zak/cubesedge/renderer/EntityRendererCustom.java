package fr.zak.cubesedge.renderer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;

public class EntityRendererCustom extends EntityRenderer {
	private final Minecraft mc;
	private float offsetY = -1F; // just for testing, should be based on actual
									// render size

	public EntityRendererCustom(Minecraft mc) {
		super(mc, mc.getResourceManager());
		this.mc = mc;
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
}